package com.smartwebarts.apnamart.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderListModel {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("razorpay_payment_id")
    @Expose
    private String razorpayPaymentId;
    @SerializedName("deladd")
    @Expose
    private String deladd;
    @SerializedName("del_time")
    @Expose
    private String delTime;
    @SerializedName("dat")
    @Expose
    private String dat;
    @SerializedName("del_amt")
    @Expose
    private String delAmt;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unit_in")
    @Expose
    private String unitIn;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paymentmethod")
    @Expose
    private String paymentmethod;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("status_updatedate")
    @Expose
    private String statusUpdatedate;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("userdate")
    @Expose
    private String userdate;
    @SerializedName("usertime")
    @Expose
    private String usertime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getDeladd() {
        return deladd;
    }

    public void setDeladd(String deladd) {
        this.deladd = deladd;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getDelAmt() {
        return delAmt;
    }

    public void setDelAmt(String delAmt) {
        this.delAmt = delAmt;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitIn() {
        return unitIn;
    }

    public void setUnitIn(String unitIn) {
        this.unitIn = unitIn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getStatusUpdatedate() {
        return statusUpdatedate;
    }

    public void setStatusUpdatedate(String statusUpdatedate) {
        this.statusUpdatedate = statusUpdatedate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getUserdate() {
        return userdate;
    }

    public void setUserdate(String userdate) {
        this.userdate = userdate;
    }

    public String getUsertime() {
        return usertime;
    }

    public void setUsertime(String usertime) {
        this.usertime = usertime;
    }
}
