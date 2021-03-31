package com.meatchop.tmcdeliverypartner;

public class Constants {
    public static final String TAG ="Tag";
    public static final String api_getListofVendors = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list";
    public static final String api_getDeliveryUserforMobileNo ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/deliveryuserformobileno";
    public static final String api_addDeliveryUserDetails ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/deliveryuser?modulename=DeliveryExecutive";

    public static final String CASH_ON_DELIVERY ="CASH ON DELIVERY";
    public static final String PHONEPE ="PHONEPE";
    public static final String RAZORPAY ="RAZORPAY";
    public static final String PAYTM ="PAYTM";
    public static String orderdeliveredtextmsg ="Your order was delivered. Keep ordering from Meat Chop.";
    public static  String orderpickeduptextmsg ="Your order has been picked and out for delivery. Delivery Partner Mobile : ";

    public static final String READYFORPICKUP ="READY FOR PICKUP";
    public static final String PICKEDUP ="PICKED UP";
    public static final String DELIVERED ="DELIVERED";

    public static final String api_updateOrderDetailsTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/orderdetails?modulename=PlaceOrder";

    public static final String api_updateDeliveryUserDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryuser?modulename=DeliveryExecutive";
    public static final String api_assignedorderdetailsfordeliveryuser ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/assignedorderdetailsfordeliveryusernew";

 //   public static final String api_assignedorderdetailsfordeliveryuser ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/assignedorderdetailsfordeliveryuser";
    public static final String api_updateTrackingOrderTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/trackingorderdetails?modulename=TrackOrder";
    public static final String api_ToSendTextMsgtoUser ="https://alerts.sinfini.com/api/web2sms.php?workingkey=Ae428c4a11138a24e41b45a0fce3576fb&sender=TMCHOP";
    public static final String api_GetTrackingOrderDetailsforDate_Vendorkey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdertailsfordate";
    public static final  String api_GetMobileAppData = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list?modulename=Mobile";
    public static final String api_addOrderDetailsInOrderDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/placeorder?modulename=PlaceOrder";
    public static final String api_addOrderDetailsInOrderItemDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/orderitem?modulename=OrderItem";
    public static final String api_addOrderDetailsInOrderTrackingDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/ordertracking?modulename=TrackOrder";
    public static final String api_addOrderDetailsInPaymentDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/payment?modulename=Payment";
    public static final  String api_getdeliveryPartnerName = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/key";

}