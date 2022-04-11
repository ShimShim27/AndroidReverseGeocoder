# AndroidReverseGeocoder
Library for reverse geocoding coordinates in Android. <br/>

## Add  androidreversegeocoder library
Add in build.gradle file
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add in build.gradle (app level) file
```
dependencies {
  implementation 'com.github.ShimShim27:AndroidReverseGeocoder:v1.0'
}
```

## Sample Usage
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
2. Reverse geocoding multiple string

```
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
```

## Note
Always call shutdownAll() in ReverseGeocoder instance to cancel all reverse geocoding processes (especially when the current activity is destroyed). 
After shutdownAll() is called, function reverse() will not work properly if called again from the same instance of ReverseGeocoder. 
The function createNew() should be called in this case to re-enable reverse() function or create a new instance of ReverseGeocoder function.

