package com.meatchop.tmcdeliverypartner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;



    public class Tracking_DeliveryPartnerLocation_Service extends Service implements LocationListener {

        public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
        public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
        private static final String TAG = "Tag";
        boolean isGPSEnable = false;


        boolean isNetworkEnable = false;
        double latitude, longitude;
        static String Latitude;
        static String Longitude;
        LocationManager locationManager;
        Location location;
        Context context;
        private Handler mHandler = new Handler();
        private Timer mTimer = null;
        long notify_interval = 10000;
        private boolean stopService = false;
       // public static String str_receiver = "com.meatchop.tmcdeliverypartner";
      //  Intent intent;
        String locationStatus , OrderTrackingTableKey;

        public Tracking_DeliveryPartnerLocation_Service() {

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            context = this;
            Log.d("TrackingServiceTag", "My foreground service.");



        }

        @Override
        public int onStartCommand(Intent inten, int flags, int startId) {
            super.onStartCommand(inten, flags, startId);


            if (inten != null) {
                OrderTrackingTableKey = inten.getExtras().getString("orderTrackingDetailskey");

                String action = inten.getAction();

                if (action != null) {
                    switch (action) {
                        case ACTION_START_FOREGROUND_SERVICE:
                            StartForeground();

                            mTimer = new Timer();
                            mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);

                            break;
                        case ACTION_STOP_FOREGROUND_SERVICE:

                            Log.e(TAG, "Service Stopped");
                            stopService = true;
                            if (locationManager != null) {
                                locationManager.removeUpdates(this);
                            }

                            Toast.makeText(getApplicationContext(), "stopped", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            return START_STICKY;

        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            Log.e(TAG, "Service Stopped");
            stopService = true;
            if (locationManager != null) {
                locationManager.removeUpdates(this);

            }
        }

        private void StartForeground() {
            Intent intent = new Intent(context, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

            String CHANNEL_ID = "channel_location";
            String CHANNEL_NAME = "channel_location";

            NotificationCompat.Builder builder = null;
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                builder.setChannelId(CHANNEL_ID);
                builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
            } else {
                builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            }

            builder.setContentTitle("Delivery Partner App");
            builder.setContentText("You are now Sharing Your Location");
            Uri notificationSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(notificationSound);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            startForeground(101, notification);
        }




        @SuppressLint("LongLogTag")
        @Override
        public void onLocationChanged(Location location) {
            Log.e("TrackingServiceGps ", "Changed" + "");

            /*Toast.makeText(getBaseContext(), "Got Location From GPS",
                    Toast.LENGTH_SHORT).show();

             */
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("LocationchangedLattitude", location.getLatitude() + "");
            Log.e("LocationChangedlongitude", location.getLongitude() + "");

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

        @SuppressLint("LongLogTag")
        private void fn_getlocation() {
            if (!stopService) {
                locationStatus = null;
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnable = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
                Log.e("TrackingServiceGps ", isGPSEnable + "");

                isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                Log.e("TrackingServiceNetwork ", isNetworkEnable + "");

                if (!isGPSEnable && !isNetworkEnable) {

                } else {


                    if (isGPSEnable && !stopService) {
                        Log.e("TrackingServiceGps ", "trueeee" + "");

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
                        Log.e("TrackingServiceGps ", "LocationManager" + "");

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, this);
                        Log.e("Gps ", "AfterLocationManager" + "");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.e("TrackingServiceGps ", "getLocationfromLocationManager" + "");

                            if (location != null) {
                                Log.e("TrackingServicelatitude", location.getLatitude() + "");
                               /*
                                Toast.makeText(getBaseContext(), "Got Location From GPS",
                                        Toast.LENGTH_SHORT).show();

                                */
                                Log.e("TrackingServicelongitude", location.getLongitude() + "");
                                locationStatus = "got Location from GPS";
                                latitude = location.getLatitude();
                                Latitude = String.valueOf(latitude);
                                longitude = location.getLongitude();
                                Longitude = String.valueOf(longitude);
                             //   fn_update(Latitude, Longitude);
                                updateLatitudeAndLongitudetoDB(Latitude,Longitude);

                            }
                        }


                    }


                    Handler handler1 = new Handler();    // android.os.Handler
                    Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            if (locationStatus == null) {

                                Toast.makeText(getBaseContext(), "If you stay Indoor , You May not get Accurate Location , Wait for 30 Seconds",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    };
                    handler1.postDelayed(runnable1, 30000);

                    if (locationStatus == null) {
                        Handler handler = new Handler();    // android.os.Handler
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (isNetworkEnable && !stopService) {
                                    Log.v("TrackingServiceTAG", "mapReady8 removed");


                                    getLocationfromNetwork();

                                }

                            }
                        };
                        handler.postDelayed(runnable, 30000);


                    }
                }

            }
        }

        @SuppressLint("LongLogTag")
        private void getLocationfromNetwork() {
            if(locationStatus == null) {
                locationManager.removeUpdates(Tracking_DeliveryPartnerLocation_Service.this);
                Log.v("TrackingServiceTAG", "mapReady8 removed");

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
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("TrackingServiceNetwork latitude", location.getLatitude() + "");
                        Log.e("TrackingServiceNetwork longitude", location.getLongitude() + "");
                        locationStatus = "got Location from NETWORK";
/*
                        Toast.makeText(getBaseContext(), "Got Location From NETWORK",
                                Toast.LENGTH_SHORT).show();

 */
                        latitude = location.getLatitude();
                        Latitude = String.valueOf(latitude);
                        longitude = location.getLongitude();
                        Longitude = String.valueOf(longitude);
                     //   fn_update(Latitude, Longitude);
                        Log.v("TAG", "mapReady8.2 getting LOCO from network");
                            updateLatitudeAndLongitudetoDB(Latitude,Longitude);
                      //  fn_update(location);
                    }
                }


            }
        }

        private void updateLatitudeAndLongitudetoDB(String latitude, String longitude) {
            Log.d(TAG, " uploaduserDatatoDB.");
            JSONObject  jsonObject = new JSONObject();
            try {
                jsonObject.put("deliveryuserlat", latitude);
                jsonObject.put("key", OrderTrackingTableKey);
                jsonObject.put("deliveryuserlong", longitude);



            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateTrackingOrderTable,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                   // Intent i = new Intent(Tracking_DeliveryPartnerLocation_Service.this, Dashboard.class);
                   // startActivity(i);
                    Log.d(Constants.TAG, "Response:  " + response);

                    Log.d(Constants.TAG, "Response: locationStatus" + locationStatus);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Error: " + error.toString());

                    error.printStackTrace();
                }
            }) {
                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(this).add(jsonObjectRequest);



        }

      /*  private void fn_update(String latitude, String longitude) {
            intent.putExtra(Latitude, latitude);
            intent.putExtra(Longitude, longitude);
            sendBroadcast(intent);
        }
           private void fn_update(Location location){
            String lat = String.valueOf(location.getLatitude());
            String lon = String .valueOf(location.getLongitude());
            intent.putExtra(Latitude,lat);
            intent.putExtra(Longitude,lon);
            sendBroadcast(intent);
        }
       */

        private class TimerTaskToGetLocation extends TimerTask {
            @Override
            public void run() {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fn_getlocation();
                    }
                });

            }
        }




    }








//   wakeLock.release();


/*      //   wakeLock.release();
                       //     Log.e(TAG, "wakelock " + wakeLock);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (!stopService) {
                        //Perform your task here

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!stopService) {
                        handler.postDelayed(this, TimeUnit.SECONDS.toMillis(10));
                    }
                }
            }
        };
        handler.postDelayed(runnable, 2000);
*/



  /*    StartForeground();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);


     */
     /*   PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        if (powerManager != null)
            Log.e(TAG, "powermanager");

        wakeLock = Objects.requireNonNull(powerManager).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "deliverypartnerdemo:Keep-awake");

        if (wakeLock != null)
            Log.e(TAG, "wakelock");

        Objects.requireNonNull(wakeLock).acquire(TimeUnit.HOURS.toMillis(120000));
        Log.e(TAG, "TrackingServicewakelock "+wakeLock);
*/


/*

    private void updatingIndatabase(String latitude, String longitude) {
        //String latitude = String.valueOf(DeliveryPartnerLatitude);
        // String longitude = String.valueOf(DeliveryPartnerLongitude);
        HashMap<String, Object> addUser = new HashMap<>();
        addUser.put("Latitude", latitude);
        addUser.put("Longitude", longitude);
        Toast.makeText(this, "Updating Location ",

                Toast.LENGTH_SHORT).show();

        DeliveryPartnerTableRef.child(phonenumber).updateChildren(addUser);
        Log.i("Tag","Lat"+latitude+"   lon   "+longitude);

    }


 */


  /* PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

                        if (powerManager != null)
                            Log.e(TAG, "powermanager");

                        wakeLock = Objects.requireNonNull(powerManager).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "deliverypartnerdemo:Keep-awake");

                        if (wakeLock != null)
                            Log.e(TAG, "wakelock");

                        Objects.requireNonNull(wakeLock).acquire(TimeUnit.HOURS.toMillis(12000000));
                        Log.e(TAG, "wakelock "+wakeLock);
*/
