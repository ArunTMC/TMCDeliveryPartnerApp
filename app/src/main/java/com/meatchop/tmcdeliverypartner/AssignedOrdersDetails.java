package com.meatchop.tmcdeliverypartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AssignedOrdersDetails extends AppCompatActivity {
    TextView orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,orderReadyTimeText_widget,orderPickedupTimeText_widget
            ,orderPlacedTimeText_widget,customerMobileNotext_widget,addressDetails_text_widget,landMarktext_widget;
    TextView discounttext_widget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;

    Adapter_forItemwiseDetails_Listview adapter_forOrderDetails_listview;
    List<Modal_AssignedOrders> OrderdItems_desp;
    ListView itemDesp_listview;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String discount="0";
    double screenInches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_orders_details_activity);
        orderIdtext_widget = findViewById(R.id.orderIdtext_widget);
        orderStatustext_widget = findViewById(R.id.orderStatustext_widget);
        itemDesp_listview = findViewById(R.id.itemDesp_listview);
        paymentTypetext_widget = findViewById(R.id.paymentTypetext_widget);
        orderPlacedTimeText_widget = findViewById(R.id.orderPlacedTimeText_widget);

        orderReadyTimeText_widget = findViewById(R.id.orderReadyTimeText_widget);
        orderPickedupTimeText_widget = findViewById(R.id.orderPickedupTimeText_widget);
        customerMobileNotext_widget = findViewById(R.id.customerMobileNotext_widget);

        addressDetails_text_widget = findViewById(R.id.addressDetails_text_widget);
        landMarktext_widget = findViewById(R.id.landMarktext_widget);
        discounttext_widget = findViewById(R.id.discounttext_widget);
        total_item_Rs_text_widget = findViewById(R.id.total_amount_text_widget);
        total_Rs_to_Pay_text_widget = findViewById(R.id.total_Rs_to_Pay_text_widget);
        taxes_and_Charges_rs_text_widget = findViewById(R.id.taxes_and_Charges_rs_text_widget);

        OrderdItems_desp = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);

        Bundle bundle = getIntent().getExtras();
        Modal_AssignedOrders modal_manageOrders_pojo_class = bundle.getParcelable("data");
        String userAddressline1 = String.valueOf(modal_manageOrders_pojo_class.getUseraddressline1());
        String userAddressline2 = String.valueOf(modal_manageOrders_pojo_class.getUseraddressline2());
        String pincode = String.valueOf(modal_manageOrders_pojo_class.getPincode());
         discount = String.valueOf(modal_manageOrders_pojo_class.getDiscountamount());
        discounttext_widget.setText(discount);

        if(!(userAddressline1.equals(""))&&!(userAddressline2.equals(""))&&!(pincode.equals(""))){
            addressDetails_text_widget.setText(String.format("%s\n%s-%s", userAddressline1, userAddressline2, pincode));

        }
        else
        {
            addressDetails_text_widget.setText(String.format("Address is not  Available for this Order"));

        }

        orderIdtext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderid()));
        orderStatustext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()));
        paymentTypetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getPaymentmode()));
        orderPlacedTimeText_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderplacedtime()));
        orderReadyTimeText_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderreadytime()));
        orderPickedupTimeText_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderpickeduptime()));
        customerMobileNotext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getUsermobile()));
        landMarktext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getUseraddresslandmark()));
        String itemDespString = modal_manageOrders_pojo_class.getItemDesp_string();
        try {
            JSONArray jsonArray = new JSONArray(itemDespString);
            for(int i=0; i < jsonArray.length(); i++) {

                JSONObject json = jsonArray.getJSONObject(i);
                Modal_AssignedOrders manageOrders_pojo_class = new Modal_AssignedOrders();


                if(json.has("marinadeitemdesp")){
                    try {
                        Modal_AssignedOrders marinademanageOrders_pojo_class = new Modal_AssignedOrders();

                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        if(json.has("grossweight")){
                            marinademanageOrders_pojo_class.ItemFinalWeight = String.valueOf(marinadesObject.get("grossweight"));

                        }
                        else if(json.has("netweight")){
                            marinademanageOrders_pojo_class.ItemFinalWeight = String.valueOf(marinadesObject.get("netweight"));

                        }
                        else if(json.has("portionsize")){
                            marinademanageOrders_pojo_class.ItemFinalWeight = "No value";

                        }

                        double itemPrice = Double.parseDouble(String.valueOf(marinadesObject.get("tmcprice")));
                        double quantity = Double.parseDouble(String.valueOf(json.get("quantity")));
                        double itemFinalPrice = itemPrice*quantity;
                        int itemFinalPriceInt = (int) Math.ceil(itemFinalPrice);

                        marinademanageOrders_pojo_class.itemName = String.valueOf(marinadesObject.get("itemname"));
                        marinademanageOrders_pojo_class.ItemFinalPrice= String.valueOf(itemFinalPriceInt);
                        marinademanageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                        marinademanageOrders_pojo_class.GstAmount = String.valueOf(marinadesObject.get("gstamount"));
                        OrderdItems_desp.add(marinademanageOrders_pojo_class);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(json.has("grossweight")){
                    manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("grossweight"));

                }
                else if(json.has("netweight")){
                    manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                }
                else if(json.has("portionsize")){
                    manageOrders_pojo_class.ItemFinalWeight = "No value";

                }

                double itemPrice = Double.parseDouble(String.valueOf(json.get("tmcprice")));
                double quantity = Double.parseDouble(String.valueOf(json.get("quantity")));
                double itemFinalPrice = itemPrice*quantity;
                int itemFinalPriceInt = (int) Math.ceil(itemFinalPrice);

                manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));
                manageOrders_pojo_class.ItemFinalPrice= String.valueOf(itemFinalPriceInt);
                manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));
                OrderdItems_desp.add(manageOrders_pojo_class);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





        add_amount_ForBillDetails(OrderdItems_desp);
        adapter_forOrderDetails_listview = new Adapter_forItemwiseDetails_Listview(AssignedOrdersDetails.this, OrderdItems_desp);


        itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);
        Helper.getListViewSize(itemDesp_listview, screenInches);
    }

    public void add_amount_ForBillDetails(List<Modal_AssignedOrders> orderdItems_desp) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double discountAmountDouble =0;

        for(int i =0; i<orderdItems_desp.size();i++){
            Modal_AssignedOrders getOrderAmountDetails = orderdItems_desp.get(i);

            //find total amount with out GST
            double new_total_amountfromArray = Double.parseDouble(getOrderAmountDetails.getItemFinalPrice());
            Log.i(Constants.TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

            new_total_amount = new_total_amountfromArray;
            old_total_Amount = old_total_Amount + new_total_amount;
            Log.i(Constants.TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
            Log.i(Constants.TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);
            getOrderAmountDetails.setTotalAmountWithoutGst(String.valueOf(decimalFormat.format(old_total_Amount)));





            //find total GST amount
            double taxes_and_chargesfromArray = Double.parseDouble(getOrderAmountDetails.getGstAmount());
            Log.i(Constants.TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);
            //taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);



            Log.i(Constants.TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
            Log.i(Constants.TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
            Log.i(Constants.TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
            new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
            old_taxes_and_charges_Amount=old_taxes_and_charges_Amount+new_taxes_and_charges_Amount;
            getOrderAmountDetails.setTotalGstAmount(String.valueOf(decimalFormat.format(old_taxes_and_charges_Amount)));




            new_to_pay_Amount =  (old_total_Amount + old_taxes_and_charges_Amount);
            getOrderAmountDetails.setTotalAmountWithGst(String.valueOf(decimalFormat.format(new_to_pay_Amount)));

        }


        try{
            discountAmountDouble = Double.parseDouble(discount);

        }catch (Exception e){
            discountAmountDouble = 0;
        }

        new_to_pay_Amount  = new_to_pay_Amount-discountAmountDouble;
//find total payable Amount

        total_item_Rs_text_widget.setText(decimalFormat.format(old_total_Amount));
        taxes_and_Charges_rs_text_widget.setText(decimalFormat.format(old_taxes_and_charges_Amount));
        int new_totalAmount_withGst = (int) Math.ceil(new_to_pay_Amount);

        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }
}


