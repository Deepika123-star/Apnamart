package com.smartwebarts.apnamart.address;

import java.util.List;

public class DeliveryDetails {

    private String userid;
    private String mobile;
    private String orderid;
    private String checkout;
    private String paymentmethod;
    private String address;
    private String landmark;
    private String pincode;
    private String deliverydate;
    private String time;
    private List<DeliveryProductDetails> products;

    public DeliveryDetails(String userid, String mobile, String orderid, String paymentmethod, String address, String landmark, String pincode, String deliverydate, String time, List<DeliveryProductDetails> products) {
        this.userid = userid;
        this.mobile = mobile;
        this.orderid = orderid;
        this.checkout = "1";
        this.paymentmethod = paymentmethod;
        this.address = address;
        this.landmark = landmark;
        this.pincode = pincode;
        this.deliverydate = deliverydate;
        this.time = time;
        this.products = products;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DeliveryProductDetails> getProducts() {
        return products;
    }

    public void setProducts(List<DeliveryProductDetails> products) {
        this.products = products;
    }
}
