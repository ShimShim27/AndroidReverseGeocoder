# 📍 AndroidReverseGeocoder
A library for reverse geocoding coordinates in Android. The library is built on top of android's [Geocoder](https://developer.android.com/reference/android/location/Geocoder) library.
AndroidReverseGeocoder implements results caching mechanism to prevent repetitive processing of an already reverse geocoded coordinates. AndroidReverseGeocoder
also incorporates Java's [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) for simultaneous reverse geocoding of coordinates. AndroidReverseGeocoder also handles errors that
may be thrown by the original [Geocoder](https://developer.android.com/reference/android/location/Geocoder) library.
<br/>

## Add AndroidReverseGeocoder library 📙
Add in [build.gradle file](https://github.com/ShimShim27/AndroidReverseGeocoder/blob/master/build.gradle)
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add in [build.gradle](https://github.com/ShimShim27/AndroidReverseGeocoder/blob/master/app/build.gradle) (app level) file
```
dependencies {
  implementation 'com.github.ShimShim27:AndroidReverseGeocoder:v1.0'
}
```

## Sample Usage 🧞
1. Show a toast of the name of place associated with coordinates 10.68532, -146.69885
```
val reverseGeocoder = ReverseGeocoder(nThreads = 1, nCoordinatesCached = 50)

reverseGeocoder.reverse(
  context = this,
  lat = 10.68532,
  long = -146.69885,
  onAddressResult = {addressName:String->
      runOnUiThread {
          Toast.makeText(this,"The address is $addressName", Toast.LENGTH_SHORT).show()
      }
  }
```
2. Reverse geocoding two coordinates at a time

```
val reverseGeocoder = ReverseGeocoder(nThreads = 2, nCoordinatesCached = 50)
        
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
```

## Note 🤫
Always call shutdownAll() in ReverseGeocoder instance to cancel all reverse geocoding processes (especially when the current activity is destroyed). 
After shutdownAll() is called, function reverse() will not work properly if called again from the same instance of ReverseGeocoder. 
The function createNew() should be called in this case to re-enable reverse() function or create a new instance of ReverseGeocoder function.

