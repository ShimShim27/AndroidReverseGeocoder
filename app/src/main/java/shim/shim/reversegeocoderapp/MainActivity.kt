package shim.shim.reversegeocoderapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import shim.shim.androidreversegeocoder.ReverseGeocoder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reverseGeocoder = ReverseGeocoder(nThreads = 1, nCoordinatesCached = 50)
        reverseGeocoder.reverse(
            context = this,
            lat = 10.68532,
            long = -146.69885,
            onAddressResult = {addressName:String->
                runOnUiThread {
                    Toast.makeText(this,"The address is $addressName", Toast.LENGTH_SHORT).show()
                }
            })

    }
}