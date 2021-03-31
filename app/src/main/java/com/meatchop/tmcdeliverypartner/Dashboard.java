package com.meatchop.tmcdeliverypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Dashboard extends AppCompatActivity implements LocationListener {
String vendorKey,deliveryUserMobileNumber,deliveryUserkey,CurrentDate;
Button button;
ListView assignedOrders_ListView;
String orderTrackingDetailskey,userLatitude="0",userLongitude="0",deliverydistance ="0",Status,vendorLatitude,vendorLongitude,deliveryUserMobileNo;
List<Modal_AssignedOrders> assignedOrdersList;
List<Modal_AssignedOrders> sortedassignedOrdersList;
LinearLayout loadingpanelmask, loadingPanel,dashboard_layout,settings_layout,newOrdersSync_Layout;
TextView deliveryUserStatus;



    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    public static String str_receiverr = "com.meatchop.tmcdeliverypartner";
    String locationStatus = null;

    boolean isTurnOnLiveTrackingService = false;
    boolean GpsStatus ;
    String distanceinString ="";

    private static final String TAG = "LocationOnOff";

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    Intent serviceIntent;
    boolean isnewOrderSyncClicked = false ;
    String deliveryuserLatitude="";
    String deliveryuserLongitude ="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        assignedOrders_ListView =findViewById(R.id.assignedOrders_ListView);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        settings_layout =findViewById(R.id.settings_layout);
        dashboard_layout =findViewById(R.id.dashboard_layout);
        deliveryUserStatus  = findViewById(R.id.deliveryUserStatus);
        button = findViewById(R.id.button);
        assignedOrdersList = new ArrayList<>();
        sortedassignedOrdersList = new ArrayList<>();
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Adjusting_Widgets_Visibility(true);
        getDataFromAppData();
        CurrentDate = getDate_and_time();
        SharedPreferences shared = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));
        vendorLatitude = (shared.getString("vendorLatitude", "12.9406"));
        vendorLongitude = (shared.getString("VendorLongitute", "80.1496"));

        deliveryUserMobileNumber = (shared.getString("deliveryUserMobileNumber", ""));
        deliveryUserkey = (shared.getString("deliveryUserkey", ""));
        Status = (shared.getString("deliveryUserStatus", ""));
        deliveryUserStatus.setText(Status);
        checkLocationPermissionforOurApp();
      //  SwitchOnGps(Dashboard.this,"","","");


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard.this,"Your Orders Has been Loading . Please Wait !!!",Toast.LENGTH_LONG).show();

            }
        });

        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isnewOrderSyncClicked) {
                    Adjusting_Widgets_Visibility(true);

                    isnewOrderSyncClicked =true;
                    isTurnOnLiveTrackingService = false;
                  //  checkLocationPermissionforOurApp();

                    CurrentDate = getDate_and_time();
                    SharedPreferences shared = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
                    vendorKey = (shared.getString("VendorKey", "vendor_1"));
                    deliveryUserMobileNumber = (shared.getString("deliveryUserMobileNumber", ""));
                    deliveryUserkey = (shared.getString("deliveryUserkey", ""));
                    Status = (shared.getString("deliveryUserStatus", "AVAILABLE"));
                    deliveryUserStatus.setText(Status);
                    assignedOrdersList.clear();
                  //  SwitchOnGps(Dashboard.this,"","","");

                    getAssingedOrders(vendorKey,deliveryUserMobileNumber, CurrentDate);
                }
                else{
                    Toast.makeText(Dashboard.this,"Your Orders Has" +
                            " been Loading . Please Wait !!!",Toast.LENGTH_LONG).show();
                }

            }
        });

        settings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Dashboard.this, com.meatchop.tmcdeliverypartner.Settings.class);
                startActivity(i);
                overridePendingTransition(0, 0);

                finish();
            }
        });





/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Latitude = "12.9809";
                String Longitude = "80.1642";

                Uri MapUri = Uri.parse("google.navigation:q="+ Latitude + ","  + Longitude);
                Intent MapIntent = new Intent(Intent.ACTION_VIEW,MapUri);
                MapIntent.setPackage("com.google.android.apps.maps");

                try {
                    if((MapIntent.resolveActivity(getPackageManager()))!=null){
                        startActivity(MapIntent);
                    }
                }catch (NullPointerException nu){
                    Log.e(Constants.TAG, "NullPointerException", nu.getCause());

                    Toast.makeText(Dashboard.this,"Can't Open Map",Toast.LENGTH_LONG).show();
                }
            }
        });


 */
    }

    private void getDataFromAppData() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMobileAppData,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    String orderdeliveredtextmsg  = String.valueOf(json.get("orderdeliveredtextmsg"));
                                    Log.d(Constants.TAG, "orderdeliveredtextmsg: " + orderdeliveredtextmsg);
                                    String orderpickeduptextmsg = String.valueOf(json.get("orderpickeduptextmsg"));
                                    Log.d(Constants.TAG, "orderpickeduptextmsg: " + orderpickeduptextmsg);

                                    Constants.orderdeliveredtextmsg=orderdeliveredtextmsg;
                                    Constants.orderpickeduptextmsg=orderpickeduptextmsg;
                                    getAssingedOrders(vendorKey,deliveryUserMobileNumber, CurrentDate);

                                } catch (JSONException e) {
                                    getAssingedOrders(vendorKey,deliveryUserMobileNumber, CurrentDate);

                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }

                            }
                        } catch (JSONException e) {
                            getAssingedOrders(vendorKey,deliveryUserMobileNumber, CurrentDate);

                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                getAssingedOrders(vendorKey,deliveryUserMobileNumber, CurrentDate);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Mobile");
                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }


    @Override
    protected void onResume() {
        super.onResume();
       // checkLocationPermissionforOurApp();
       // getDataFromAppData();
    }


    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }

    }


    private void getAssingedOrders(String vendorKey,String deliveryUserMobileNumber,String Currentdate) {
        assignedOrdersList.clear();
        sortedassignedOrdersList.clear();
        String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_assignedorderdetailsfordeliveryuser+"?deliveryusermobileno="+deliveryUserMobileNumberEncoded+"&date="+Currentdate+"&vendorkey="+vendorKey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1 = 0;
                            int arrayLength = result.length();
                            Log.d(Constants.TAG, "Response: " + arrayLength);


                            for (; i1 <= (arrayLength - 1); i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);
                                    Modal_AssignedOrders assignedOrders = new Modal_AssignedOrders();
                                    if (json.has("itemdesp")) {
                                        assignedOrders.itemdesp = (json.getJSONArray("itemdesp"));

                                    } else {
                                        JSONArray array = new JSONArray();
                                        assignedOrders.itemdesp = array;

                                    }


                                    if (json.has("coupondiscount")) {
                                        assignedOrders.discountamount = String.valueOf(json.get("coupondiscount"));

                                    } else {
                                        assignedOrders.discountamount = "";

                                    }


                                    if (json.has("orderstatus")) {
                                        assignedOrders.orderstatus = String.valueOf(json.get("orderstatus"));

                                    } else {
                                        assignedOrders.orderstatus = "no value";

                                    }

                                    if (json.has("deliverytype")) {
                                        assignedOrders.deliverytype = String.valueOf(json.get("deliverytype"));

                                    } else {
                                        assignedOrders.deliverytype = "no value";

                                    }

                                    if (json.has("deliveryamount")) {
                                        assignedOrders.deliveryamount = String.valueOf(json.get("deliveryamount"));

                                    } else {
                                        assignedOrders.deliveryamount = "no value";

                                    }
                                    if (json.has("orderDetailskey")) {
                                        assignedOrders.orderDetailskey = String.valueOf(json.get("orderDetailskey"));

                                    } else {
                                        assignedOrders.orderDetailskey = "no value";

                                    }
                                    if (json.has("orderplaceddate")) {
                                        assignedOrders.orderplaceddate = String.valueOf(json.get("orderplaceddate"));

                                    } else {
                                        assignedOrders.orderplaceddate = "no value";

                                    }
                                    if (json.has("payableamount")) {
                                        assignedOrders.payableamount = String.valueOf(json.get("payableamount"));

                                    } else {
                                        assignedOrders.payableamount = "no value";

                                    }
                                    if (json.has("paymentmode")) {
                                        assignedOrders.paymentmode = String.valueOf(json.get("paymentmode"));

                                    } else {
                                        assignedOrders.paymentmode = "no value";

                                    }
                                    if (json.has("slottimerange")) {
                                        assignedOrders.slottimerange = String.valueOf(json.get("slottimerange"));

                                    } else {
                                        assignedOrders.slottimerange = "no value";

                                    }
                                    if (json.has("tokenno")) {
                                        assignedOrders.tokenno = String.valueOf(json.get("tokenno"));

                                    } else {
                                        assignedOrders.tokenno = "no value";

                                    }
                                    if (json.has("userkey")) {
                                        assignedOrders.userkey = String.valueOf(json.get("userkey"));

                                    } else {
                                        assignedOrders.userkey = "no value";

                                    }

                                    if (json.has("slotdate")) {
                                        assignedOrders.slotdate = String.valueOf(json.get("slotdate"));

                                    } else {
                                        assignedOrders.slotdate = "no value";

                                    }
                                    if (json.has("slotname")) {
                                        assignedOrders.slotname = String.valueOf(json.get("slotname"));

                                    } else {
                                        assignedOrders.slotname = "no value";

                                    }
                                    if (json.has("usermobile")) {
                                        assignedOrders.usermobile = String.valueOf(json.get("usermobile"));

                                    } else {
                                        assignedOrders.usermobile = "no value";

                                    }
                                    if (json.has("vendorkey")) {
                                        assignedOrders.vendorkey = String.valueOf(json.get("vendorkey"));

                                    } else {
                                        assignedOrders.vendorkey = "no value";

                                    }
                                    if (json.has("vendorname")) {
                                        assignedOrders.vendorname = String.valueOf(json.get("vendorname"));

                                    } else {
                                        assignedOrders.vendorname = "no value";

                                    }
                                    if (json.has("orderTrackingDetailskey")) {

                                        assignedOrders.orderTrackingDetailskey = String.valueOf(json.get("orderTrackingDetailskey"));

                                    } else {
                                        assignedOrders.orderTrackingDetailskey = "no value";

                                    }
                                    if (json.has("orderid")) {
                                        assignedOrders.orderid = String.valueOf(json.get("orderid"));

                                    } else {
                                        assignedOrders.orderid = "no value";

                                    }
                                    if (json.has("orderreadytime")) {
                                        assignedOrders.orderreadytime = String.valueOf(json.get("orderreadytime"));

                                    } else {
                                        assignedOrders.orderreadytime = "no value";

                                    }
                                    if (json.has("orderplacedtime")) {
                                        assignedOrders.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                                    } else {
                                        assignedOrders.orderplacedtime = "no value";

                                    }

                                    if (json.has("orderconfirmedtime")) {
                                        assignedOrders.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                                    } else {
                                        assignedOrders.orderconfirmedtime = "no value";

                                    }
                                    if (json.has("orderpickeduptime")) {
                                        assignedOrders.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                                    } else {
                                        assignedOrders.orderpickeduptime = "no value";

                                    }

                                    if (json.has("useraddresslat")) {
                                        assignedOrders.useraddresslat = String.valueOf(json.get("useraddresslat"));

                                    } else {
                                        assignedOrders.useraddresslat = "no value";

                                    }

                                    if (json.has("deliverydistance")) {
                                        assignedOrders.distancebetweenpartner_Customer = String.valueOf(json.get("deliverydistance"));
                                        deliverydistance = String.valueOf(json.get("deliverydistance"));
                                    } else {
                                        assignedOrders.distancebetweenpartner_Customer = "0";
                                        deliverydistance = "0";
                                    }


                                    if (json.has("useraddresslong")) {
                                        assignedOrders.useraddresslong = String.valueOf(json.get("useraddresslong"));

                                    } else {
                                        assignedOrders.useraddresslong = "no value";

                                    }

                                    if (json.has("addressline1")) {
                                        assignedOrders.useraddressline1 = String.valueOf(json.get("addressline1"));

                                    } else {
                                        assignedOrders.useraddressline1 = "no value";

                                    }
                                    if (json.has("addressline2")) {
                                        assignedOrders.useraddressline2 = String.valueOf(json.get("addressline2"));

                                    } else {
                                        assignedOrders.useraddressline2 = "no value";

                                    }
                                    if (json.has("addresstype")) {
                                        assignedOrders.useraddresstype = String.valueOf(json.get("addresstype"));

                                    } else {
                                        assignedOrders.useraddresstype = "no value";

                                    }
                                    if (json.has("addresskey")) {

                                        assignedOrders.useraddresskey = String.valueOf(json.get("addresskey"));

                                    } else {
                                        assignedOrders.useraddresskey = "no value";

                                    }

                                    if (json.has("landmark")) {
                                        assignedOrders.useraddresslandmark = String.valueOf(json.get("landmark"));

                                    } else {
                                        assignedOrders.useraddresslandmark = "no value";

                                    }
                                    if (json.has("locationlat")) {
                                        assignedOrders.locationlat = String.valueOf(json.get("locationlat"));

                                    } else {
                                        assignedOrders.locationlat = "no value";

                                    }

                                    if (json.has("locationlong")) {
                                        assignedOrders.locationlong = String.valueOf(json.get("locationlong"));

                                    } else {
                                        assignedOrders.locationlong = "no value";

                                    }
                                    if (json.has("pincode")) {
                                        assignedOrders.pincode = String.valueOf(json.get("pincode"));

                                    } else {
                                        assignedOrders.pincode = "no value";

                                    }

                                    String OrderStatus = assignedOrders.getOrderstatus().toUpperCase();

                                    if ((OrderStatus.equals(Constants.READYFORPICKUP)) || (OrderStatus.equals(Constants.PICKEDUP))) {


                                        assignedOrdersList.add(assignedOrders);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    isnewOrderSyncClicked = false;

                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }

                            }
                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);
                            isnewOrderSyncClicked = false;
                            Toast.makeText(getBaseContext(), "No Orders Has been Assigned For you", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                        Adjusting_Widgets_Visibility(false);
                       /* try {
                            CalculateDistanceviaApi(assignedOrdersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                       if (deliverydistance.equals("0")) {
                            try {
                                CalculateDistanceviaApi(assignedOrdersList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else{
                            callAdapter(assignedOrdersList);

                        }

                        */
                        callAdapter(assignedOrdersList);


                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);
                isnewOrderSyncClicked =false;

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }
    private void CalculateDistanceviaApi(List<Modal_AssignedOrders> assignedOrders) throws JSONException {
        Log.i("Tag", "Latlangcal");
        int i = 0;
        for (; i < assignedOrders.size(); i++) {
            Modal_AssignedOrders modal_assignedOrders = assignedOrders.get(i);

            String user_Latitude = String.valueOf(modal_assignedOrders.getUseraddresslat());
            String user_Longitude = String.valueOf(modal_assignedOrders.getUseraddresslong());


            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + user_Latitude + "," + user_Longitude + "&destinations=" + deliveryuserLatitude + "," + deliveryuserLongitude + "&mode=driving&language=en-EN&sensor=false&key=AIzaSyDYkZGOckF609Cjt6mnyNX9QhTY9-kAqGY";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try {

                        Log.d(Constants.TAG, "response " + response.toString());

                        JSONArray rowsArray = (JSONArray) response.get("rows");
                        Log.d(Constants.TAG, "rows" + rowsArray.toString());
                        JSONObject elements = rowsArray.getJSONObject(0);
                        Log.d(Constants.TAG, "elements" + elements.toString());

                        JSONArray elementsArray = (JSONArray) elements.get("elements");
                        Log.d(Constants.TAG, "elementsArray" + elementsArray.toString());

                        JSONObject distance = elementsArray.getJSONObject(0);
                        Log.d(Constants.TAG, "distance" + distance.toString());
                        JSONObject jsondistance = distance.getJSONObject("distance");
                        Log.d(Constants.TAG, "jsondistance :" + jsondistance);

                        distanceinString = jsondistance.getString("text");
                        Log.d(Constants.TAG, "distanceinString :" + distanceinString);
                        modal_assignedOrders.setDistancebetweenpartner_Customer(distanceinString);
                        sortedassignedOrdersList.add(modal_assignedOrders);
                        if (sortedassignedOrdersList.size() == assignedOrders.size()) {


                            callAdapter(sortedassignedOrdersList);

                        }
                        //assignedOrders.add(modal_assignedOrders);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "Error2: " + error.getMessage());
                    Log.d(Constants.TAG, "Error3: " + error.toString());

                    error.printStackTrace();
                }
            });


            // Make the request
            Volley.newRequestQueue(Dashboard.this).add(jsonObjectRequest);



        }
    }

    private void callAdapter(List<Modal_AssignedOrders> assignedOrders) {
            Collections.sort(assignedOrders, new Comparator<Modal_AssignedOrders>() {
                public int compare(final Modal_AssignedOrders object1, final Modal_AssignedOrders object2) {
                    return object1.getDistancebetweenpartner_Customer().compareTo(object2.getDistancebetweenpartner_Customer());
                }
            });



        Adapter_AssignedOrders_ListView adapter_assignedOrders_listView = new Adapter_AssignedOrders_ListView(Dashboard.this,assignedOrders,deliveryUserMobileNumber,Dashboard.this);
        assignedOrders_ListView.setAdapter(adapter_assignedOrders_listView);
        isnewOrderSyncClicked =false;

    }


    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
       String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);



         String formattedDate = CurrentDay+", "+CurrentDate;
        return formattedDate;
    }



    public boolean checkLocationPermissionforOurApp() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION))&&(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION))) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;

        } else {


            Toast.makeText(this, "permission Granted", Toast.LENGTH_LONG).show();

          //  SwitchOnGps(Dashboard.this);
            return true;

        }


    }

    void closeService() {

        serviceIntent = new Intent(Dashboard.this, Tracking_DeliveryPartnerLocation_Service.class);
        serviceIntent.setAction(Tracking_DeliveryPartnerLocation_Service.ACTION_STOP_FOREGROUND_SERVICE);
        stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show();

                 //   SwitchOnGps(Dashboard.this);


                    // permission was granted. Do the
                    // contacts-related task you need to do.


                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }









     @SuppressLint("QueryPermissionsNeeded")
     void OpenGoogleMapAndDrawRoute(String userLatitude, String userLongitude) {
        Uri MapUri = Uri.parse("google.navigation:q="+ userLatitude + ","  + userLongitude);
        Intent MapIntent = new Intent(Intent.ACTION_VIEW,MapUri);
         MapIntent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));

        try {
            if((MapIntent.resolveActivity(getPackageManager()))!=null){
                startActivity(MapIntent);
            }
        }catch (NullPointerException nu){
            Log.e(Constants.TAG, "NullPointerException", nu.getCause());

            Toast.makeText(Dashboard.this,"Can't Open Map",Toast.LENGTH_LONG).show();
        }
    }


    void SwitchOnGps(final Context context, String userlatitude, String userlongitude, String OrderTrackingDetailskey) {

        userLatitude =userlatitude;
        userLongitude = userlongitude;
        orderTrackingDetailskey = OrderTrackingDetailskey;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()

                .addLocationRequest(locationRequest);


        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());




        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                  //  registerReceiver(receiver, new IntentFilter(CurrentLocationService.str_receiverr));
                   if(isTurnOnLiveTrackingService) {
                       serviceIntent = new Intent(Dashboard.this, Tracking_DeliveryPartnerLocation_Service.class);
                       serviceIntent.putExtra("orderTrackingDetailskey", orderTrackingDetailskey);

                       serviceIntent.setAction(Tracking_DeliveryPartnerLocation_Service.ACTION_START_FOREGROUND_SERVICE);
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                           startForegroundService(serviceIntent);
                       } else {
                           startService(serviceIntent);

                       }
                       // OpenGoogleMapAndDrawRoute(userLatitude,userLongitutde);
                   }
                   else{
                      // registerReceiver(receiver, new IntentFilter(CurrentLocationService.str_receiverr));
                      // startService(new Intent(Dashboard.this, CurrentLocationService.class));
                       getCurrentLocation();


                   }
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult((Activity) context,LocationRequest.PRIORITY_HIGH_ACCURACY);
                            }
                            catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                       // registerReceiver(receiver, new IntentFilter(CurrentLocationService.str_receiverr));
                       // startService(new Intent(Dashboard.this, Tracking_DeliveryPartnerLocation_Service.class));
                        if(isTurnOnLiveTrackingService) {
                            serviceIntent = new Intent(Dashboard.this, Tracking_DeliveryPartnerLocation_Service.class);
                            serviceIntent.putExtra("orderTrackingDetailskey", orderTrackingDetailskey);
                            serviceIntent.setAction(Tracking_DeliveryPartnerLocation_Service.ACTION_START_FOREGROUND_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(serviceIntent);
                            } else {
                                startService(serviceIntent);

                            }
                        }

                        else{
                           // registerReceiver(receiver, new IntentFilter(CurrentLocationService.str_receiverr));
                           // startService(new Intent(Dashboard.this, CurrentLocationService.class));
                            getCurrentLocation();

                        }

                     //   OpenGoogleMapAndDrawRoute(Latitude,Longitude);
                        Log.i(TAG, "onActivityResult: GPS Enabled by user");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.i(TAG, "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void getCurrentLocation() {



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
        locationManager.removeUpdates(Dashboard.this);
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




    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.v(TAG, "BroadCastSucess");

                deliveryuserLatitude = bundle.getString(CurrentLocationService.Latitude);
                deliveryuserLongitude = bundle.getString(CurrentLocationService.Longitude);
                if (deliveryuserLatitude != null&&deliveryuserLongitude!=null) {
                    stopService(new Intent(Dashboard.this, CurrentLocationService.class));
                    Log.v(TAG, "BroadCastSucess"+deliveryuserLongitude +"   "+deliveryuserLatitude);
                    unregisterReceiver(receiver);
                    //assignedOrdersList.clear();
                  //  sortedassignedOrdersList.clear();
               //     getAssingedOrders(deliveryUserMobileNumber,CurrentDate);



                } else {
                    Toast.makeText(Dashboard.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    Log.v(TAG, "BroadCast Failed");

                }
            }
        }
    };


    @Override
    public void onLocationChanged(@NonNull Location loc) {
        Log.e("TAG", "mapReady4");
        locationStatus = "got Location from GPS";


        deliveryuserLongitude = String.valueOf(loc.getLongitude());
        Log.v("TAG", deliveryuserLongitude);

        deliveryuserLatitude = String.valueOf(loc.getLatitude());
        Log.v("TAG", deliveryuserLatitude);


       // assignedOrdersList.clear();
      //  sortedassignedOrdersList.clear();
     //   getAssingedOrders(deliveryUserMobileNumber,CurrentDate);

    }
}