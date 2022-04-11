package shim.shim.androidreversegeocoder

import android.content.Context
import android.location.Geocoder
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException


/**
 * This class is built on top of android's built in library called Geocoder.
 * This class also enables an easy reverse geocoding of coordinates in android. The class
 * implemented address caching mechanisms to prevent reverse geocoding again coordinates that
 * has already been processed. The class also implemented error handling to prevent crashes
 * well known in using android's Geocoder built in library.
 *
 * @param nThreads - determines the amount of thread to be used to do simultaneous reverse geocoding.
 * It is recommended to assign this to only 1 to prevent same coordinates to be reverse geocoded
 * multiple times.
 *
 * @param nCoordinatesCached -  determines how many coordinates' addresses to be cached. Assigning this
 * to 100 means that only 100 coordinates' address will be cached and the oldest ones will be deleted.
 * Assigning this to null means infinite caching which is not recommended since it can produce Memory Error
 * if it exceeds memory capacity.
 */
class ReverseGeocoder(private val nThreads: Int, private val nCoordinatesCached: Int?) {
    private lateinit var service: ExecutorService

    /**
     * The pair here contains
     * the address name and
     * whether it is loading or not.
     *
     * The boolean prevents from reverse
     * coding same coordinates.
     */
    private val reverseGeocodedMap = mutableMapOf<String, Pair<String, Boolean>>()

    init {
        createNew()
    }


    /**
     * This function will reverse geocode the coordinates provided. Addresses obtained from
     * reverse geocoding are cached to prevent reverse geocoding same coordinates again.
     *
     * @param context the context of the application
     *
     * @param lat latitude of the coordinates to be reversed
     *
     * @param long longitude of the coordinates to be reversed
     *
     * @param onAddressResult a lambda will be called after
     * reverse geocoding is successful. This lambda are also called not in UI thread,
     * so it you will be needing to handle the UI processes that
     * will be inside this parameter. The name string parameter referenced to the address resulted
     * by reverse geocoding. Empty address means that reverse geocoding failed or no address available
     */
    fun reverse(
        context: Context,
        lat: Double,
        long: Double,
        onAddressResult: (name: String) -> Unit
    ) {


        try {
            service.submit {
                val coordinatesKey = "%s %s".format(lat, long)
                try {
                    val existingAddress = reverseGeocodedMap[coordinatesKey]
                    if (existingAddress != null) {
                        onAddressResult(if (existingAddress.second) "" else existingAddress.first)
                    } else {
                        reverseGeocodedMap[coordinatesKey] = Pair("", true)

                        val geo = Geocoder(context, Locale.getDefault())
                        val addresses = geo.getFromLocation(lat, long, 1)
                        var locationName: String? = null
                        if (addresses.isNotEmpty()) {
                            locationName = addresses[0].getAddressLine(0)
                        }
                        val finalLocationName = locationName ?: ""
                        reverseGeocodedMap[coordinatesKey] = Pair(finalLocationName, false)

                        /**
                         * Ensures the number of cached
                         * coordinates is in the limit
                         */
                        if (nCoordinatesCached != null && reverseGeocodedMap.size > nCoordinatesCached) {
                            reverseGeocodedMap.remove(reverseGeocodedMap.keys.toTypedArray()[0])
                        }

                        onAddressResult(finalLocationName)
                    }


                } catch (e: Exception) {
                    reverseGeocodedMap[coordinatesKey] = Pair("", false)
                    onAddressResult("")
                }


            }

        } catch (rejected: RejectedExecutionException) {
            /**
             * Service already
             * shutdown.
             */
        }

    }


    /**
     * Call this function first when you need to use the reverse function
     * of the same instance of this class that has already called shutDownAll function.
     */
    fun createNew() {
        service = Executors.newFixedThreadPool(nThreads)
    }


    /**
     * Stop all the pending reverse geocoding processes. Don't forget to call this
     * especially when an activity or service is destroyed since this will work on background.
     * Calling the reverse function after calling this function won't work.
     * To call the reverse function, createNew function should be called first
     * or create a new instance of this class.
     */
    fun shutDownAll() {
        service.shutdownNow()
    }

}