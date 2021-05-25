package hlc.realtime.location.support

import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import timber.log.Timber
import java.util.*


object GPSHelper  {
    private lateinit var context: Application

    /** 舊式寫法 Android location api */
    private lateinit var mLocationManager : LocationManager
    private lateinit var locationListener : LocationListener

    /** 新式寫法 Google’s location service api */
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var mLocationCallback : LocationCallback
    private lateinit var locationRequest : LocationRequest

    private var vertical :Double = .0
    private var horizontal :Double = .0

    fun setInit(context: Application) {
        this.context = context
        locationListener = LocationListener {
            this.vertical = it.longitude // 經度
            this.horizontal = it.latitude // 緯度
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GPSHelper.context)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Timber.tag("hlcDebug").d("onLocationResult : ${location.latitude} / ${location.longitude}")
                }
            }
        }

        locationRequest = LocationRequest.create()
        locationRequest.interval = 10000 // 時間間隔
        locationRequest.fastestInterval = 5000 //可以處理得最快時間間隔

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    fun getLocation()  {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context,"請打開權限",Toast.LENGTH_SHORT).show()
            return
        }
        /** 舊式寫法 Android location api */
//        mLocationManager = this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, locationListener)

//        val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
//        location?.let {
//            this.vertical = it.longitude
//            this.horizontal = it.latitude
//            Timber.tag("hlcDebug").d("location : $vertical / $horizontal")
//            mLocationManager.removeUpdates(locationListener)
//            setCurrentAddress()
//        }

        /** 新式寫法 Google’s location service api */
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                Timber.tag("hlcDebug").d("location : $it")
                it?.let {
                    this.vertical = it.longitude // 經度
                    this.horizontal = it.latitude // 緯度
                    setCurrentAddress()
                }
                if (it==null) Toast.makeText(context, "找不到GPS",Toast.LENGTH_SHORT).show()
                fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
            }
            .addOnFailureListener {
                Timber.tag("hlcDebug").d("addOnFailureListener : $it")
                fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
            }

        return
    }

    private lateinit var geocoder : Geocoder
    val addressList = mutableListOf<Address>()

    fun setCurrentAddress(){
        geocoder = Geocoder(context, Locale.TAIWAN)
        addressList.clear()
        addressList.addAll( geocoder.getFromLocation(horizontal, vertical,3) )

        addressList?.let {
            val address: String = addressList[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            val city: String = addressList[0].locality
            val state: String = addressList[0].adminArea
            val country: String = addressList[0].countryName
            val postalCode: String = addressList[0].postalCode
            val knownName: String = addressList[0].featureName
        }
    }

    fun getCurrentAddress() = addressList

    fun getLongitude() = vertical
    fun getLatitude() = horizontal
}