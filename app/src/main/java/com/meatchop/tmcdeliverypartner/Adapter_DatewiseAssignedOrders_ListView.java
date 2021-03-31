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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcdeliverypartner.AssignedOrdersDetails;
import com.meatchop.tmcdeliverypartner.Constants;
import com.meatchop.tmcdeliverypartner.Dashboard;
import com.meatchop.tmcdeliverypartner.DeliveredOrdersDatewise;
import com.meatchop.tmcdeliverypartner.Modal_AssignedOrders;
import com.meatchop.tmcdeliverypartner.R;

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

public class Adapter_DatewiseAssignedOrders_ListView extends ArrayAdapter<Modal_AssignedOrders> {
    Context mContext;
    String user_Latitude,user_Longitude,orderTrackingDetailskey,orderDetailskey;
    List<Modal_AssignedOrders> ordersList;
    LocationManager locationManager ;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay;
    boolean isPaymentModeSelected = false;
    String paymentModefromArray = "",paymentModeString="";
    boolean GpsStatus ;
    DeliveredOrdersDatewise deliveredOrdersDatewise;

    public Adapter_DatewiseAssignedOrders_ListView(Context mContext, List<Modal_AssignedOrders> ordersList, DeliveredOrdersDatewise dashboard) {
        super(mContext, R.layout.datewise_assigned_orders, ordersList);
        this.deliveredOrdersDatewise = dashboard;
        this.mContext = mContext;
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
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.datewise_assigned_orders, (ViewGroup) view, false);
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
        if(paymentModefromArray.equals("CASH")){
            paymentModefromArray="CASH ON DELIVERY";
        }
        paymentMode_text_widget.setText(String.valueOf(paymentModefromArray));


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
                if(json.has("marinadeitemdesp")){
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");

                    String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));

                    itemDesp = String.format(marinadeitemName + "  with ");

                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    itemName = itemName + " Marinade Box ";
                    if (i > 0) {

                        itemDesp = String.format("%s  ,  %s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", itemDesp, itemName, quantity);

                    }


                }
                else {
                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    if (i > 0) {

                        itemDesp = String.format("%s  ,\n %s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", itemDesp, itemName, quantity);

                    }
                }
                orderItemDetails_text_widget.setText(String.format(itemDesp));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        openGoogleMap_Button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   saveOrderTrackingKey(orderTrackingDetailskey);
                user_Latitude =   String.valueOf(modal_assignedOrders.getUseraddresslat());
                user_Longitude =   String.valueOf(modal_assignedOrders.getUseraddresslong());

                deliveredOrdersDatewise.OpenGoogleMapAndDrawRoute(user_Latitude,user_Longitude);

                // dashboard.SwitchOnGps(mContext,user_Latitude,user_Longitude,orderTrackingDetailskey);
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




        return listViewItem;


    }





}
