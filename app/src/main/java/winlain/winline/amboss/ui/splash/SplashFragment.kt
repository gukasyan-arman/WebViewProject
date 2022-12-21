package winlain.winline.amboss.ui.splash

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.testapp.R
import com.example.testapp.databinding.FragmentSplashBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.*


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private var isInternetConnection = false
    private var isSimCardInside = true
    private var isConditionsSuccess = false
    private lateinit var url: String
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference: SharedPreferences = requireActivity()
            .getSharedPreferences("URL_SHARED_PREF", Context.MODE_PRIVATE)
        url = sharedPreference.getString("url", "")!!
        val editor = sharedPreference.edit()

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireContext())
        }
        isSIMInserted(requireContext())

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) {task ->
            if (task.isSuccessful) {
                if (url == "") {
                    CoroutineScope(Dispatchers.Main).launch {
                        getUrlFromRemoteConfig()
                        editor.putString("url", url)
                        editor.apply()
                        goToFragment(url)
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        editor.putString("url", url)
                        editor.apply()
                        goToFragment(url)
                    }
                }
                Log.d("remoteConfig", "Remote Config parameters updated")
            } else {
                Log.d("remoteConfig","Failed to update Remote Config parameters")
            }
        }
        if (url == "" && !isInternetConnection) {
            binding.splashErrorText.visibility = View.VISIBLE
        }
        Log.d("remoteConfigUrl", "remoteConfUrl = $url")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isInternetConnection = true
                Log.i("checkNetwork", "NetworkCapabilities.TRANSPORT_WIFI")
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                isInternetConnection = true
                Log.i("checkNetwork", "NetworkCapabilities.TRANSPORT_ETHERNET")
                true
            } else {
                false
            }
        } else {
            isInternetConnection = false
            Log.i("checkNetwork", "network none")
        }
        return false
    }

    private fun isSIMInserted(context: Context){
        isSimCardInside = TelephonyManager.SIM_STATE_ABSENT != (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simState
    }

    private fun splashDelay(actionId: Int, duration: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            delay(duration)
            findNavController().navigate(actionId)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private suspend fun getUrlFromRemoteConfig() {
        url = Firebase.remoteConfig.getString("url")
    }

    private suspend fun goToFragment(url: String) {
        if (url == "" && !isInternetConnection) {
            binding.splashErrorText.visibility = View.VISIBLE
        } else {
            binding.splashErrorText.visibility = View.GONE
            if (url.isEmpty() || Build.BRAND == "google" || !isInternetConnection
                || !isSimCardInside || Build.HARDWARE.contains("goldfish")) {
                isConditionsSuccess = true
                splashDelay(R.id.action_splashFragment_to_mainFragment, 1500)
            } else {
                isConditionsSuccess = false
                splashDelay(R.id.action_splashFragment_to_webViewFragment, 0)
            }
        }
    }
}