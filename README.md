# AndroidReverseGeocoder
For reverse geocoding coordinates in Android. <br/>

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
Show a toast of the name of place associated with coordinates 10.68532, -146.69885
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
  })

```

