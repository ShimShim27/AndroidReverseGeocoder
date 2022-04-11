package shim.shim.reversegeocoderapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import shim.shim.androidreversegeocoder.ReverseGeocoder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reverseGeocoder = ReverseGeocoder(nThreads = 1, nCoordinatesCached = 50)

        val coordinates = listOf(Pair(-55.45134, 153.48612), Pair(65.57829, -116.16070), Pair(-25.15858, -117.67318), Pair(-55.45134, 153.48612))

        for (coords in coordinates){
            reverseGeocoder.reverse(
                context = this,
                lat = coords.first,
                long = coords.second,
                onAddressResult = {addressName:String->
                    print("The address is $addressName")
                })
        }

    }
}