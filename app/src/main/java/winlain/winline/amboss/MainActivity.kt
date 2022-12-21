package winlain.winline.amboss


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.databinding.ActivityMainBinding
import winlain.winline.amboss.ui.splash.ConnectionLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData
    private var isInternetConnection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {isNetwork ->
                if (isNetwork) {
                    isInternetConnection = true
                    Toast.makeText(this, "connection success", Toast.LENGTH_SHORT).show()
                }else {
                    isInternetConnection = false
                    Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}