package com.meatchop.tmcdeliverypartner;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class Modal_AssignedOrders implements Parcelable {
    String deliverydistance,orderdeliverytime,distancebetweenpartner_Customer,discountamount,totalGstAmount,totalAmountWithoutGst,totalAmountWithGst,itemDesp_string,itemName,orderplaceddate,payableamount,paymentmode,slotdate,slotname,slottimerange,userkey,usermobile,vendorkey,vendorname,orderTrackingDetailskey,
            orderid,orderreadytime,orderpickeduptime,orderplacedtime,orderconfirmedtime,useraddresslat,useraddresslong, useraddresskey,useraddresstype,
            landmark,quantity,GstAmount,ItemFinalPrice,ItemFinalWeight,locationlat,locationlong,useraddresslandmark,deliverytype,orderDetailskey,deliveryamount,orderstatus,tokenno,usermobileno,useraddressline1,useraddressline2,pincode;
    JSONArray itemdesp;

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public String getItemName() {
        return itemName;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getOrderdeliverytime() {
        return orderdeliverytime;
    }

    public void setOrderdeliverytime(String orderdeliverytime) {
        this.orderdeliverytime = orderdeliverytime;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDistancebetweenpartner_Customer() {
        return distancebetweenpartner_Customer;
    }

    public void setDistancebetweenpartner_Customer(String distancebetweenpartner_Customer) {
        this.distancebetweenpartner_Customer = distancebetweenpartner_Customer;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public String getTotalGstAmount() {
        return totalGstAmount;
    }

    public String getOrderpickeduptime() {
        return orderpickeduptime;
    }

    public void setOrderpickeduptime(String orderpickeduptime) {
        this.orderpickeduptime = orderpickeduptime;
    }

    public void setTotalGstAmount(String totalGstAmount) {
        this.totalGstAmount = totalGstAmount;
    }

    public String getTotalAmountWithoutGst() {
        return totalAmountWithoutGst;
    }

    public void setTotalAmountWithoutGst(String totalAmountWithoutGst) {
        this.totalAmountWithoutGst = totalAmountWithoutGst;
    }

    public String getTotalAmountWithGst() {
        return totalAmountWithGst;
    }

    public void setTotalAmountWithGst(String totalAmountWithGst) {
        this.totalAmountWithGst = totalAmountWithGst;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGstAmount() {
        return GstAmount;
    }

    public void setGstAmount(String gstAmount) {
        GstAmount = gstAmount;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemFinalPrice() {
        return ItemFinalPrice;
    }

    public void setItemFinalPrice(String itemFinalPrice) {
        ItemFinalPrice = itemFinalPrice;
    }

    public String getItemFinalWeight() {
        return ItemFinalWeight;
    }

    public void setItemFinalWeight(String itemFinalWeight) {
        ItemFinalWeight = itemFinalWeight;
    }

    public Modal_AssignedOrders() {

    }

    public String getItemDesp_string() {
        return itemDesp_string;
    }

    public void setItemDesp_string(String itemDesp_string) {
        this.itemDesp_string = itemDesp_string;
    }

    protected Modal_AssignedOrders(Parcel in) {
        orderplaceddate = in.readString();
        payableamount = in.readString();
        paymentmode = in.readString();
        slotdate = in.readString();
        slotname = in.readString();
        slottimerange = in.readString();
        userkey = in.readString();
        usermobile = in.readString();
        vendorkey = in.readString();
        vendorname = in.readString();
        orderTrackingDetailskey = in.readString();
        orderid = in.readString();
        orderreadytime = in.readString();
        orderplacedtime = in.readString();
        orderconfirmedtime = in.readString();
        useraddresslat = in.readString();
        useraddresslong = in.readString();
        useraddresskey = in.readString();
        useraddresstype = in.readString();
        locationlat = in.readString();
        locationlong = in.readString();
        useraddresslandmark = in.readString();
        deliverytype = in.readString();
        orderDetailskey = in.readString();
        deliveryamount = in.readString();
        orderstatus = in.readString();
        tokenno = in.readString();
        usermobileno = in.readString();
        useraddressline1 = in.readString();
        useraddressline2 = in.readString();
        pincode = in.readString();
        itemDesp_string = in.readString();
        orderpickeduptime = in.readString();
        discountamount = in.readString();
        deliverydistance = in.readString();

    }

    public static final Creator<Modal_AssignedOrders> CREATOR = new Creator<Modal_AssignedOrders>() {
        @Override
        public Modal_AssignedOrders createFromParcel(Parcel in) {
            return new Modal_AssignedOrders(in);
        }

        @Override
        public Modal_AssignedOrders[] newArray(int size) {
            return new Modal_AssignedOrders[size];
        }
    };

    public String getUseraddresstype() {
        return useraddresstype;
    }

    public void setUseraddresstype(String useraddresstype) {
        this.useraddresstype = useraddresstype;
    }

    public String getLocationlat() {
        return locationlat;
    }

    public void setLocationlat(String locationlat) {
        this.locationlat = locationlat;
    }

    public String getLocationlong() {
        return locationlong;
    }

    public void setLocationlong(String locationlong) {
        this.locationlong = locationlong;
    }

    public String getUseraddresslandmark() {
        return useraddresslandmark;
    }

    public void setUseraddresslandmark(String useraddresslandmark) {
        this.useraddresslandmark = useraddresslandmark;
    }

    public String getUseraddresskey() {
        return useraddresskey;
    }

    public void setUseraddresskey(String useraddresskey) {
        this.useraddresskey = useraddresskey;
    }

    public String getOrderplaceddate() {
        return orderplaceddate;
    }

    public void setOrderplaceddate(String orderplaceddate) {
        this.orderplaceddate = orderplaceddate;
    }

    public String getPayableamount() {
        return payableamount;
    }

    public void setPayableamount(String payableamount) {
        this.payableamount = payableamount;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getSlotdate() {
        return slotdate;
    }

    public void setSlotdate(String slotdate) {
        this.slotdate = slotdate;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public String getSlottimerange() {
        return slottimerange;
    }

    public void setSlottimerange(String slottimerange) {
        this.slottimerange = slottimerange;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getOrderTrackingDetailskey() {
        return orderTrackingDetailskey;
    }

    public void setOrderTrackingDetailskey(String orderTrackingDetailskey) {
        this.orderTrackingDetailskey = orderTrackingDetailskey;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderreadytime() {
        return orderreadytime;
    }

    public void setOrderreadytime(String orderreadytime) {
        this.orderreadytime = orderreadytime;
    }

    public String getOrderplacedtime() {
        return orderplacedtime;
    }

    public void setOrderplacedtime(String orderplacedtime) {
        this.orderplacedtime = orderplacedtime;
    }

    public String getOrderconfirmedtime() {
        return orderconfirmedtime;
    }

    public void setOrderconfirmedtime(String orderconfirmedtime) {
        this.orderconfirmedtime = orderconfirmedtime;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getOrderDetailskey() {
        return orderDetailskey;
    }

    public void setOrderDetailskey(String orderDetailskey) {
        this.orderDetailskey = orderDetailskey;
    }

    public void setItemdesp(JSONArray itemdesp) {
        this.itemdesp = itemdesp;
    }

    public String getDeliveryamount() {
        return deliveryamount;
    }

    public void setDeliveryamount(String deliveryamount) {
        this.deliveryamount = deliveryamount;
    }

    public JSONArray getItemdesp() {
        return itemdesp;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }


    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getUseraddressline1() {
        return useraddressline1;
    }

    public void setUseraddressline1(String useraddressline1) {
        this.useraddressline1 = useraddressline1;
    }

    public String getUseraddressline2() {
        return useraddressline2;
    }

    public void setUseraddressline2(String useraddressline2) {
        this.useraddressline2 = useraddressline2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUseraddresslat() {
        return useraddresslat;
    }

    public void setUseraddresslat(String useraddresslat) {
        this.useraddresslat = useraddresslat;
    }

    public String getUseraddresslong() {
        return useraddresslong;
    }

    public void setUseraddresslong(String useraddresslong) {
        this.useraddresslong = useraddresslong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderplaceddate);
        parcel.writeString(payableamount);
        parcel.writeString(paymentmode);
        parcel.writeString(slotdate);
        parcel.writeString(slotname);
        parcel.writeString(slottimerange);
        parcel.writeString(userkey);
        parcel.writeString(usermobile);
        parcel.writeString(vendorkey);
        parcel.writeString(vendorname);
        parcel.writeString(orderTrackingDetailskey);
        parcel.writeString(orderid);
        parcel.writeString(orderreadytime);
        parcel.writeString(orderplacedtime);
        parcel.writeString(orderconfirmedtime);
        parcel.writeString(useraddresslat);
        parcel.writeString(useraddresslong);
        parcel.writeString(useraddresskey);
        parcel.writeString(useraddresstype);
        parcel.writeString(locationlat);
        parcel.writeString(locationlong);
        parcel.writeString(useraddresslandmark);
        parcel.writeString(deliverytype);
        parcel.writeString(orderDetailskey);
        parcel.writeString(deliveryamount);
        parcel.writeString(orderstatus);
        parcel.writeString(tokenno);
        parcel.writeString(usermobileno);
        parcel.writeString(useraddressline1);
        parcel.writeString(useraddressline2);
        parcel.writeString(pincode);
        parcel.writeString(itemDesp_string);
        parcel.writeString(orderpickeduptime);
        parcel.writeString(discountamount);
        parcel.writeString(deliverydistance);

    }
}
