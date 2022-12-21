package winlain.winline.amboss.ui.webview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebViewClient
import com.example.testapp.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("URL_SHARED_PREF", Context.MODE_PRIVATE)
        val url = sharedPreferences.getString("url", "")!!

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url)

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        val mWebSettings = binding.webView.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.databaseEnabled = true
        mWebSettings.setSupportZoom(false)
        mWebSettings.allowFileAccess = true
        mWebSettings.allowContentAccess = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true


        binding.webView.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action === KeyEvent.ACTION_DOWN) {
                if (keyCode === KeyEvent.KEYCODE_BACK) {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        requireActivity().onBackPressed()
                    }

                }
            }
            allowReturnTransitionOverlap
        }
    }
}
