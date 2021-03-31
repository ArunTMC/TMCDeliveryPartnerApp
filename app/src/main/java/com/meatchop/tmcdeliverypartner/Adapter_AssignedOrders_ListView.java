package com.meatchop.tmcdeliverypartner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class
Adapter_AssignedOrders_ListView extends ArrayAdapter<Modal_AssignedOrders> {
    Context mContext;
    String orderstatus, user_Latitude,user_Longitude,orderTrackingDetailskey,orderDetailskey,usermobileNo,deliveryUserMobileNo="";
    List<Modal_AssignedOrders> ordersList;
    LocationManager locationManager ;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay;
    boolean isPaymentModeSelected = false;
    String paymentModefromArray = "",paymentModeString="";
    boolean GpsStatus ;
    Dashboard dashboard;

    public Adapter_AssignedOrders_ListView(Context mContext, List<Modal_AssignedOrders> ordersList, String deliveryUserMobileNumber, Dashboard dashboard) {
        super(mContext, R.layout.assigned_orders_listitem, ordersList);
        this.dashboard = dashboard;
        this.mContext = mContext;
        this.deliveryUserMobileNo=deliveryUserMobileNumber;
        this.ordersList = ordersList;


    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_AssignedOrders getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_AssignedOrders item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.assigned_orders_listitem, (ViewGroup) view, false);
        final TextView orderItemDetails_text_widget = listViewItem.findViewById(R.id.orderItemDetails_text_widget);
        final TextView orderStatus_text_widget = listViewItem.findViewById(R.id.orderStatus_text_widget);
        final TextView paymentMode_text_widget = listViewItem.findViewById(R.id.paymentMode_text_widget);
        final TextView addressDetails_text_widget = listViewItem.findViewById(R.id.addressDetails_text_widget);
        final TextView moblieNo_text_widget = listViewItem.findViewById(R.id.moblieNo_text_widget);
        final TextView tokenNo_text_widget = listViewItem.findViewById(R.id.tokenNo_text_widget);

        final TextView orderId_text_widget = listViewItem.findViewById(R.id.orderId_text_widget);
        final LinearLayout callLayout = listViewItem.findViewById(R.id.callLayout);
        final CardView assignedItemLayout = listViewItem.findViewById(R.id.assignedItemLayout);
        final Button pickup_Order_button_widget = listViewItem.findViewById(R.id.pickup_Order_button_widget);
        final Button delivered_button_widget = listViewItem.findViewById(R.id.delivered_button_widget);
        final Button openGoogleMap_Button_widget = listViewItem.findViewById(R.id.openGoogleMap_Button_widget);
        final Button changePaymentMode_widget = listViewItem.findViewById(R.id.changePaymentMode_widget);

        final TextView addressType_text_widget = listViewItem.findViewById(R.id.addressType_text_widget);
        final TextView km_text_widget = listViewItem.findViewById(R.id.km_text_widget);



        Modal_AssignedOrders modal_assignedOrders = ordersList.get(pos);
        orderStatus_text_widget.setText(String.valueOf(modal_assignedOrders.getOrderstatus()));
        String userAddressline1 = String.valueOf(modal_assignedOrders.getUseraddressline1());
        String userAddressline2 = String.valueOf(modal_assignedOrders.getUseraddressline2());
        String pincode = String.valueOf(modal_assignedOrders.getPincode());



        paymentModefromArray = String.valueOf(modal_assignedOrders.getPaymentmode()).toUpperCase();

        paymentMode_text_widget.setText(String.valueOf(paymentModefromArray));
      try {
          orderstatus = String.valueOf(modal_assignedOrders.getOrderstatus()).toUpperCase();
          if (orderstatus.equals(Constants.PICKEDUP)) {
              pickup_Order_button_widget.setVisibility(View.GONE);
              delivered_button_widget.setVisibility(View.VISIBLE);
          }

      }
      catch (Exception e ){
          e.printStackTrace();
      }
        if(!(userAddressline1.equals(""))&&!(userAddressline2.equals(""))&&!(pincode.equals(""))){
            addressDetails_text_widget.setText(String.format("%s\n%s-%s", userAddressline1, userAddressline2, pincode));

        }
        else
        {
            addressDetails_text_widget.setText(String.format("Address is not  Available for this Order"));

        }
      try {
          String addressType = String.valueOf(modal_assignedOrders.getUseraddresstype());
          addressType_text_widget.setText(addressType);
          orderId_text_widget.setText(String.valueOf(modal_assignedOrders.getOrderid()));
          moblieNo_text_widget.setText(String.valueOf(modal_assignedOrders.getUsermobile()));
          tokenNo_text_widget.setText(String.valueOf(modal_assignedOrders.getTokenno()));



          orderTrackingDetailskey = String.valueOf(modal_assignedOrders.getOrderTrackingDetailskey());
          orderDetailskey = String.valueOf(modal_assignedOrders.getOrderDetailskey());
          km_text_widget.setText(String.valueOf(modal_assignedOrders.getDistancebetweenpartner_Customer()));
      }
      catch (Exception e){
          e.printStackTrace();
      }

        try {
            JSONArray array  = modal_assignedOrders.getItemdesp();
            Log.i("tag","array.length()"+ array.length());
            String b= array.toString();
            modal_assignedOrders.setItemDesp_string(b);
            String itemDesp="";

            for(int i=0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);


                Log.i("tag","array.lengrh(i"+ json.length());
                if (json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");

                    String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));



                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(marinadesObject.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    itemName = itemName + " Marinade Box ";
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                    }

                    //     orderDetails_text_widget.setText(String.format(itemDesp));

                } else {

                    Log.i("tag", "array.lengrh(i" + json.length());

                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    if (itemDesp.length() > 0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

                    }
                }

            }
            orderItemDetails_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        pickup_Order_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Change_Status_to_PickedUp,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                dashboard.Adjusting_Widgets_Visibility(true);
                                Modal_AssignedOrders modal_assignedOrders = ordersList.get(pos);

                                String Status = "PICKED UP";
                                orderTrackingDetailskey   = String.valueOf(modal_assignedOrders.getOrderTrackingDetailskey());
                                Currenttime = getDate_and_time();
                                user_Latitude =   String.valueOf(modal_assignedOrders.getUseraddresslat());
                                user_Longitude =   String.valueOf(modal_assignedOrders.getUseraddresslong());
                                usermobileNo = String.valueOf(modal_assignedOrders.getUsermobile());
                                ChangeStatusOftheOrder(Status,orderTrackingDetailskey,Currenttime,usermobileNo);
                                pickup_Order_button_widget.setVisibility(View.GONE);
                                delivered_button_widget.setVisibility(View.VISIBLE);

                                dashboard.isTurnOnLiveTrackingService=true;
                                dashboard.SwitchOnGps(mContext,user_Latitude,user_Longitude,orderTrackingDetailskey);

}

                            @Override
                            public void onNo() {

                            }
                        });

            }
        });


        assignedItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AssignedOrdersDetails.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", modal_assignedOrders);
                intent.putExtras(bundle);

                mContext.startActivity(intent);

            }
        });



        delivered_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modal_AssignedOrders modal_assignedOrders = ordersList.get(pos);

                paymentModefromArray = String.valueOf(modal_assignedOrders.getPaymentmode()).toUpperCase();
                orderTrackingDetailskey   = String.valueOf(modal_assignedOrders.getOrderTrackingDetailskey());
                orderDetailskey = String.valueOf(modal_assignedOrders.getOrderDetailskey());
                usermobileNo = String.valueOf(modal_assignedOrders.getUsermobile());

                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Change_Status_to_Delivered,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                if(((paymentModefromArray.equals(Constants.PAYTM)))||(paymentModefromArray.equals(Constants.RAZORPAY))){

                                    String Status = "DELIVERED";
                                    Currenttime = getDate_and_time();

                                    ChangeStatusOftheOrder(Status,orderTrackingDetailskey,Currenttime, usermobileNo);
                                    dashboard.closeService();
                                }
                                else{

                                    openDialogtoChangePaymentMode(pos, orderTrackingDetailskey,orderDetailskey);

                                }



                            }

                            @Override
                            public void onNo() {

                            }
                        });





            }
        });


        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = moblieNo_text_widget.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +mobileNumber ));// Initiates the Intent
               mContext.startActivity(intent);
            }
        });

        openGoogleMap_Button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   saveOrderTrackingKey(orderTrackingDetailskey);
                user_Latitude =   String.valueOf(modal_assignedOrders.getUseraddresslat());
                user_Longitude =   String.valueOf(modal_assignedOrders.getUseraddresslong());

                dashboard.OpenGoogleMapAndDrawRoute(user_Latitude,user_Longitude);

                // dashboard.SwitchOnGps(mContext,user_Latitude,user_Longitude,orderTrackingDetailskey);
            }
        });



        changePaymentMode_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  openDialogtoChangePaymentMode(pos, orderTrackingDetailskey);
            }
        });



        return listViewItem;


    }






    private void openDialogtoChangePaymentMode(int pos, String orderTrackingDetailskey, String orderDetailskey) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.select_payment_mode_layout);
                    dialog.setTitle("Select the Payment Mode ");
                    dialog.setCanceledOnTouchOutside(false);

                    Button dialog_changePaymentMode = (Button) dialog.findViewById(R.id.dialog_changePaymentMode);
                    RadioGroup dialog_paymentRadioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_paymentRadioGroup);

                    Currenttime = getDate_and_time();

                    long sTime = System.currentTimeMillis();
                    Log.i(Constants.TAG, "date and time " + sTime);

                    Log.d(Constants.TAG, "Currenttime: " + Currenttime);


                    dialog_paymentRadioGroup.setSelected(false);

                    dialog_paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                            switch(checkedId){
                                case R.id.cashOnDelivery_radioButton:
                                    isPaymentModeSelected =true;
                                     paymentModeString = Constants.CASH_ON_DELIVERY;
                                     break;
                                case R.id.upi_radioButton:
                                    isPaymentModeSelected =true;

                                    paymentModeString = Constants.PHONEPE;
                                    break;

                            }
                        }
                    });

                    dialog_changePaymentMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isPaymentModeSelected&&(!paymentModeString.equals(""))) {

                                String Status = "DELIVERED";
                                Currenttime = getDate_and_time();

                                changePaymentModeinDB(paymentModeString,pos, orderDetailskey);

                                dashboard.closeService();
                                ChangeStatusOftheOrder(Status,orderTrackingDetailskey,Currenttime, usermobileNo);

                                dialog.cancel();
                                isPaymentModeSelected=false;
                            }
                            else{
                                Toast.makeText(mContext,"Select  one Payment Option",Toast.LENGTH_LONG).show();
                            }

                        }
                    });



                    dialog.show();
                }
                catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void changePaymentModeinDB(String paymentModeString, int pos, String orderDetailskey) {

        JSONObject  jsonObject = new JSONObject();
        try {
                jsonObject.put("key", orderDetailskey);
                jsonObject.put("paymentmode", paymentModeString);
                Log.i("tag","listenertoken"+ "");






        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
    }


    private void ChangeStatusOftheOrder(String changestatusto, String OrderKey, String currenttime, String usermobileNo) {
        JSONObject  jsonObject = new JSONObject();
        try {
            if(changestatusto.equals("PICKED UP")){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderconfirmedtime", "");
                jsonObject.put("ordercancelledtime", "");
                jsonObject.put("orderreadytime", "");
                jsonObject.put("orderpickeduptime", Currenttime);
                jsonObject.put("orderdeliverytime", "");
                jsonObject.put("deliveryuserlat", "");
                jsonObject.put("deliveryuserlong", "");
                Log.i("tag","listenertoken"+ "");
            }
            if(changestatusto.equals("DELIVERED")){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderconfirmedtime", "");
                jsonObject.put("ordercancelledtime", "");
                jsonObject.put("orderreadytime", "");
                jsonObject.put("orderpickeduptime", "");
                jsonObject.put("orderdeliverytime", Currenttime);
                jsonObject.put("deliveryuserlat", "");
                jsonObject.put("deliveryuserlong", "");
                Log.i("tag","listenertoken"+ "");
            }




        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i=0;i<ordersList.size();i++){
                    Modal_AssignedOrders modal_assignedOrders= ordersList.get(i);
                    String OrderId = modal_assignedOrders.getOrderTrackingDetailskey();
                    if(OrderKey.equals(OrderId)){
                        if(changestatusto.equals("PICKED UP")) {
                            if (deliveryUserMobileNo.startsWith("+91")) {
                                deliveryUserMobileNo = deliveryUserMobileNo.substring(3);
                            }
                            modal_assignedOrders.setOrderpickeduptime(currenttime);
                            dashboard.Adjusting_Widgets_Visibility(false);

                            SendTextMessagetoUserUsingApi(usermobileNo,Constants.orderpickeduptextmsg+" "+deliveryUserMobileNo);

                        }

                        if(changestatusto.equals("DELIVERED")){
                            SendTextMessagetoUserUsingApi(usermobileNo, Constants.orderdeliveredtextmsg);
                            dashboard.Adjusting_Widgets_Visibility(false);

                            ordersList.remove(i);

                        }
                        modal_assignedOrders.setOrderstatus(changestatusto);
                        notifyDataSetChanged();
                        dashboard.Adjusting_Widgets_Visibility(false);

                    }
                }
                Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                dashboard.Adjusting_Widgets_Visibility(false);

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }

    private void SendTextMessagetoUserUsingApi(String usermobileNo, String message) {
        if (usermobileNo.length() == 10) {

        } else {
            if (usermobileNo.startsWith("+91")) {
                usermobileNo = usermobileNo.substring(3);
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.api_ToSendTextMsgtoUser + "&to=" +usermobileNo+"&message="+message, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Rest response",response);
                    dashboard.Adjusting_Widgets_Visibility(false);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Rest response",error.toString());
                    dashboard.Adjusting_Widgets_Visibility(false);

                }
            }){

                @Override
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("content-type","application/fesf");
                    return params;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(mContext).add(stringRequest);

          /*  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_ToSendTextMsgtoUser + "&to=" +usermobileNo+"&message="+message, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            Log.d(Constants.TAG, "Response: " + response);

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
                    final Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");

                    return header;
                }
            };

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);


           */

        }


    }}