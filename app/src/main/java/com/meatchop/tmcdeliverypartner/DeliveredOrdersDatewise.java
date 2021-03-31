package com.meatchop.tmcdeliverypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

public class DeliveredOrdersDatewise extends AppCompatActivity {
    TextView deliveryUserMobileno_textWidget,dateSelector_text;
    LinearLayout dateSelectorLayout,getdeliveredOrders_Layout,loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    String DateString,vendorKey,deliveryUserMobileNumber;
    List<Modal_AssignedOrders> assignedOrdersList;
    ListView assignedOrdersListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered_orders_datewise_activity);
        deliveryUserMobileno_textWidget  = findViewById(R.id.deliveryUserMobileno_textWidget);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        assignedOrdersList = new ArrayList<>();
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        assignedOrdersListview = findViewById(R.id.assignedOrdersListview);
        Adjusting_Widgets_Visibility(true);

        getdeliveredOrders_Layout = findViewById(R.id.getdeliveredOrders_Layout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", "vendor_1"));
        deliveryUserMobileNumber = (shared.getString("deliveryUserMobileNumber", ""));
        deliveryUserMobileno_textWidget.setText(deliveryUserMobileNumber);
            try{
                DateString =getDate_and_time();

                dateSelector_text.setText(DateString);
                Adjusting_Widgets_Visibility(true);

                getOrderForSelectedDate(DateString, vendorKey,dateSelector_text);


            }catch (Exception e){
                e.printStackTrace();
            }

        dateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    openDatePicker();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        getdeliveredOrders_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderForSelectedDate(DateString, vendorKey,dateSelector_text);

            }
        });

    }





    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat dayname = new SimpleDateFormat("EEE");
        String Currentday = dayname.format(c);
        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);

        String date = CurrentDate;


        return date;
    }





    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        datepicker = new DatePickerDialog(DeliveredOrdersDatewise.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            String month_in_String = getMonthString(monthOfYear);
                            DateString = (dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(dayOfMonth + " " + month_in_String + " " + year);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }

    private void getOrderForSelectedDate(String dateString, String vendorKey, TextView dateSelector_text) {
        Adjusting_Widgets_Visibility(true);

        assignedOrdersList.clear();
        String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_assignedorderdetailsfordeliveryuser+"?deliveryusermobileno="+deliveryUserMobileNumberEncoded+"&date="+dateString+"&vendorkey="+vendorKey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            Log.d(Constants.TAG, "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);
                                    Modal_AssignedOrders assignedOrders = new Modal_AssignedOrders();
                                    if(json.has("itemdesp")){
                                        assignedOrders.itemdesp = (json.getJSONArray("itemdesp"));

                                    }
                                    else{
                                        JSONArray array = new JSONArray();
                                        assignedOrders.itemdesp = array;

                                    }


                                    if(json.has("coupondiscount")){
                                        assignedOrders.discountamount = String.valueOf(json.get("coupondiscount"));

                                    }
                                    else{
                                        assignedOrders.discountamount = "";

                                    }



                                    if(json.has("orderstatus")){
                                        assignedOrders.orderstatus = String.valueOf(json.get("orderstatus"));

                                    }
                                    else{
                                        assignedOrders.orderstatus = "no value";

                                    }

                                    if(json.has("deliverytype")){
                                        assignedOrders.deliverytype =String.valueOf(json.get("deliverytype"));

                                    }
                                    else{
                                        assignedOrders.deliverytype ="no value";

                                    }

                                    if(json.has("deliveryamount")){
                                        assignedOrders.deliveryamount =String.valueOf(json.get("deliveryamount"));

                                    }
                                    else{
                                        assignedOrders.deliveryamount ="no value";

                                    }
                                    if(json.has("orderDetailskey")){
                                        assignedOrders.orderDetailskey =String.valueOf(json.get("orderDetailskey"));

                                    }
                                    else{
                                        assignedOrders.orderDetailskey ="no value";

                                    }
                                    if(json.has("orderplaceddate")){
                                        assignedOrders.orderplaceddate =String.valueOf(json.get("orderplaceddate"));

                                    }
                                    else{
                                        assignedOrders.orderplaceddate ="no value";

                                    }
                                    if(json.has("payableamount")){
                                        assignedOrders.payableamount =String.valueOf(json.get("payableamount"));

                                    }
                                    else{
                                        assignedOrders.payableamount ="no value";

                                    }
                                    if(json.has("paymentmode")){
                                        assignedOrders.paymentmode =String.valueOf(json.get("paymentmode"));

                                    }
                                    else{
                                        assignedOrders.paymentmode ="no value";

                                    }
                                    if(json.has("slottimerange")){
                                        assignedOrders.slottimerange =String.valueOf(json.get("slottimerange"));

                                    }
                                    else{
                                        assignedOrders.slottimerange ="no value";

                                    }
                                    if(json.has("tokenno")){
                                        assignedOrders.tokenno =String.valueOf(json.get("tokenno"));

                                    }
                                    else{
                                        assignedOrders.tokenno ="no value";

                                    }
                                    if(json.has("userkey")){
                                        assignedOrders.userkey =String.valueOf(json.get("userkey"));

                                    }
                                    else{
                                        assignedOrders.userkey ="no value";

                                    }

                                    if(json.has("slotdate")){
                                        assignedOrders.slotdate =String.valueOf(json.get("slotdate"));

                                    }
                                    else{
                                        assignedOrders.slotdate ="no value";

                                    }
                                    if(json.has("slotname")){
                                        assignedOrders.slotname =String.valueOf(json.get("slotname"));

                                    }
                                    else{
                                        assignedOrders.slotname ="no value";

                                    }
                                    if(json.has("usermobile")){
                                        assignedOrders.usermobile =String.valueOf(json.get("usermobile"));

                                    }
                                    else{
                                        assignedOrders.usermobile ="no value";

                                    }
                                    if(json.has("vendorkey")){
                                        assignedOrders.vendorkey =String.valueOf(json.get("vendorkey"));

                                    }
                                    else{
                                        assignedOrders.vendorkey ="no value";

                                    }
                                    if(json.has("vendorname")){
                                        assignedOrders.vendorname =String.valueOf(json.get("vendorname"));

                                    }
                                    else{
                                        assignedOrders.vendorname ="no value";

                                    }
                                    if(json.has("orderTrackingDetailskey")){

                                        assignedOrders.orderTrackingDetailskey =String.valueOf(json.get("orderTrackingDetailskey"));

                                    }
                                    else{
                                        assignedOrders.orderTrackingDetailskey ="no value";

                                    }
                                    if(json.has("orderid")){
                                        assignedOrders.orderid =String.valueOf(json.get("orderid"));

                                    }
                                    else{
                                        assignedOrders.orderid ="no value";

                                    }
                                    if(json.has("orderreadytime")){
                                        assignedOrders.orderreadytime =String.valueOf(json.get("orderreadytime"));

                                    }
                                    else{
                                        assignedOrders.orderreadytime ="no value";

                                    }
                                    if(json.has("orderplacedtime")){
                                        assignedOrders.orderplacedtime =String.valueOf(json.get("orderplacedtime"));

                                    }
                                    else{
                                        assignedOrders.orderplacedtime ="no value";

                                    }

                                    if(json.has("orderconfirmedtime")){
                                        assignedOrders.orderconfirmedtime =String.valueOf(json.get("orderconfirmedtime"));

                                    }
                                    else{
                                        assignedOrders.orderconfirmedtime ="no value";

                                    }
                                    if(json.has("orderpickeduptime")){
                                        assignedOrders.orderpickeduptime =String.valueOf(json.get("orderpickeduptime"));

                                    }
                                    else{
                                        assignedOrders.orderpickeduptime ="no value";

                                    }


                                    if(json.has("orderdeliverytime")){
                                        assignedOrders.orderdeliverytime =String.valueOf(json.get("orderdeliverytime"));

                                    }
                                    else{
                                        assignedOrders.orderdeliverytime ="no value";

                                    }
                                    if(json.has("useraddresslat")){
                                        assignedOrders.useraddresslat =String.valueOf(json.get("useraddresslat"));

                                    }
                                    else{
                                        assignedOrders.useraddresslat ="no value";

                                    }
                                    if(json.has("deliverydistance")){
                                        assignedOrders.deliverydistance =String.valueOf(json.get("deliverydistance"));

                                    }
                                    else{
                                        assignedOrders.deliverydistance ="0";

                                    }
                                    if(json.has("useraddresslong")){
                                        assignedOrders.useraddresslong =String.valueOf(json.get("useraddresslong"));

                                    }
                                    else{
                                        assignedOrders.useraddresslong ="no value";

                                    }

                                    if(json.has("addressline1")){
                                        assignedOrders.useraddressline1 =String.valueOf(json.get("addressline1"));

                                    }
                                    else{
                                        assignedOrders.useraddressline1 ="no value";

                                    }
                                    if(json.has("addressline2")){
                                        assignedOrders.useraddressline2 =String.valueOf(json.get("addressline2"));

                                    }
                                    else{
                                        assignedOrders.useraddressline2 ="no value";

                                    }
                                    if(json.has("addresstype")){
                                        assignedOrders.useraddresstype =String.valueOf(json.get("addresstype"));

                                    }
                                    else{
                                        assignedOrders.useraddresstype ="no value";

                                    }
                                    if(json.has("addresskey")){

                                        assignedOrders.useraddresskey =String.valueOf(json.get("addresskey"));

                                    }
                                    else{
                                        assignedOrders.useraddresskey ="no value";

                                    }

                                    if(json.has("landmark")){
                                        assignedOrders.useraddresslandmark =String.valueOf(json.get("landmark"));

                                    }
                                    else{
                                        assignedOrders.useraddresslandmark ="no value";

                                    }
                                    if(json.has("locationlat")){
                                        assignedOrders.locationlat =String.valueOf(json.get("locationlat"));

                                    }
                                    else{
                                        assignedOrders.locationlat ="no value";

                                    }

                                    if(json.has("locationlong")){
                                        assignedOrders.locationlong =String.valueOf(json.get("locationlong"));

                                    }
                                    else{
                                        assignedOrders.locationlong ="no value";

                                    }
                                    if(json.has("pincode")){
                                        assignedOrders.pincode =String.valueOf(json.get("pincode"));

                                    }
                                    else{
                                        assignedOrders.pincode ="no value";

                                    }




                                        assignedOrdersList.add(assignedOrders);




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);

                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getBaseContext(), "No Orders Has been Assigned For you",  Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);

                        }
                        callAdapter(assignedOrdersList);


                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);

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






    private void callAdapter(List<Modal_AssignedOrders> assignedOrders) {
        Collections.sort(assignedOrders, new Comparator<Modal_AssignedOrders>() {
            public int compare(final Modal_AssignedOrders object1, final Modal_AssignedOrders object2) {
                return object1.getOrderdeliverytime().compareTo(object2.getOrderdeliverytime());
            }
        });



        Adapter_DatewiseAssignedOrders_ListView adapter_datewiseAssignedOrders_listView = new Adapter_DatewiseAssignedOrders_ListView(DeliveredOrdersDatewise.this,assignedOrders,DeliveredOrdersDatewise.this);
        assignedOrdersListview.setAdapter(adapter_datewiseAssignedOrders_listView);
        Adjusting_Widgets_Visibility(false);

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

            Toast.makeText(DeliveredOrdersDatewise.this,"Can't Open Map",Toast.LENGTH_LONG).show();
        }
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

    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value == 2) {
            return "Mar";
        } else if (value == 3) {
            return "Apr";
        } else if (value == 4) {
            return "May";
        } else if (value == 5) {
            return "Jun";
        } else if (value == 6) {
            return "Jul";
        } else if (value == 7) {
            return "Aug";
        } else if (value == 8) {
            return "Sep";
        } else if (value == 9) {
            return "Oct";
        } else if (value == 10) {
            return "Nov";
        } else if (value == 11) {
            return "Dec";
        }
        return "";
    }




}