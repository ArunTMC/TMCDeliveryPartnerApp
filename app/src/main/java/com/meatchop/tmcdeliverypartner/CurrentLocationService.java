package com.meatchop.tmcdeliverypartner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import java.util.Objects;


public class CurrentLocationService extends Service implements LocationListener {
    Intent intent;
    static String Latitude;
    static String Longitude;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    public static String str_receiverr = "com.meatchop.tmcdeliverypartner";
    String locationStatus = null;
    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(str_receiverr);
        getLocation();
    }

    private void getLocation() {

        //1st Method-GettingLocation from Google Play services
     /*   fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Latitude = String.valueOf(location.getLatitude());
                        Longitude = String.valueOf(location.getLongitude());

                        Log.e("getloc", Latitude + "");
                        Log.e("getloc", Longitude + "");
                        Log.v("TAG", "mapReady4");
                        sendtoActivty(Latitude, Longitude);

                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);


      */




        locationStatus = null;
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("Gps ", isGPSEnable + "");

        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e("Network ", isNetworkEnable + "");

        if (!isGPSEnable && !isNetworkEnable) {

        } else {


            if (isGPSEnable) {

                GetLocationFromGPS();

            }


            Handler handler1 = new Handler();    // android.os.Handler
            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    if (locationStatus == null) {
                        Toast.makeText(getBaseContext(), "If you stay Indoor , You May not get Accurate Location , Wait for 30 Seconds",  Toast.LENGTH_SHORT).show();

                    }
                }
            };
            handler1.postDelayed(runnable1, 1500);
            if (locationStatus == null) {
                Handler handler = new Handler();    // android.os.Handler
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkEnable) {


                            getLocationfromNetwork();

                        }

                    }
                };
                handler.postDelayed(runnable, 3000);


            }
        }
    }
//2nd Method-Get Location by doing live tracking


    private void GetLocationFromGPS() {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);

    }

    private void getLocationfromNetwork() {
        locationManager.removeUpdates(CurrentLocationService.this);
        Log.v("TAG", "mapReady8 removed");

        if (locationStatus == null) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            Log.v("TAG", "mapReady8.2 getting LOCOTION from network");

        }


    }














    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(Location loc) {
        Log.e("LocationchangedLattitude", loc.getLatitude() + "");
        Log.e("LocationChangedlongitude", loc.getLongitude() + "");
        Log.e("TAG", "mapReady4");
        locationStatus = "got Location from GPS";


        Longitude = String.valueOf(loc.getLongitude());
        Log.v("TAG", Longitude);

        Latitude = String.valueOf(loc.getLatitude());
        Log.v("TAG", Latitude);
        sendtoActivty(Latitude, Longitude);

    }

    private void sendtoActivty(String latitude, String longitude) {
        intent.putExtra(Latitude, latitude);
        intent.putExtra(Longitude, longitude);
        sendBroadcast(intent);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

//2nd Method-Get Location by doing live tracking

/*
    private void GetLocationFromGPS() {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);

    }

    private void getLocationfromNetwork() {
        locationManager.removeUpdates(CurrentLocationService.this);
        Log.v("TAG", "mapReady8 removed");

        if (locationStatus == null) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            Log.v("TAG", "mapReady8.2 getting LOCOTION from network");

        }
    }




}


 */








//3rd Method -Get  Location from the Google Play Services
//GPS

   /*
       private void GetLocationFromGPS() {
if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.e("latitude", location.getLatitude() + "");
                Log.e("longitude", location.getLongitude() + "");
                Toast.makeText(getBaseContext(), "Got Location From GPS",
                        Toast.LENGTH_SHORT).show();

                locationStatus = "got Location from GPS";
                latitude = location.getLatitude();
                Latitude = String.valueOf(latitude);
                longitude = location.getLongitude();
                Longitude = String.valueOf(longitude);
                sendtoActivty(Latitude, Longitude);
            }
        }
        }
*/


   //Network

  /*
       private void getLocationfromNetwork() {
if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Toast.makeText(getBaseContext(), "Got Location From Network",
                        Toast.LENGTH_SHORT).show();

                Log.e("Network latitude", location.getLatitude() + "");
                Log.e("Network longitude", location.getLongitude() + "");

                latitude = location.getLatitude();
                Latitude = String.valueOf(latitude);
                longitude = location.getLongitude();
                Longitude = String.valueOf(longitude);
                sendtoActivty(Latitude,Longitude);

            }
        }
        }
*/
