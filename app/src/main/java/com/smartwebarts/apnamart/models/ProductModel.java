package com.smartwebarts.apnamart.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

    @SerializedName("unit_in")
    @Expose
    private String unitIn;
    @SerializedName("units")
    @Expose
    private List<UnitModel> units;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("currentprice")
    @Expose
    private String currentprice;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("wishlist")
    @Expose
    private boolean wishlist;

    public ProductModel() {
    }

    public ProductModel(ProductDetailModel model) {
        this.unitIn = model.getUnitIn();
        this.units = model.getUnits();
        this.currentprice = model.getCurrentprice();
        this.id = model.getId();
        this.brand = model.getBrand();
        this.productType = model.getProductType();
        this.name = model.getName();
        this.type = model.getType();
        this.thumbnail = model.getThumbnail();
        this.price = model.getPrice();
        this.description = model.getDescription();
        this.vendorId = model.getVendorId();
    }

    public ProductModel(HomeProductsModel model) {
        this.unitIn = "";
        this.units = null;
        this.currentprice = "";
        this.id = model.getId();
        this.brand = model.getBrand();
        this.productType = model.getProductType();
        this.name = model.getName();
        this.type = "";
        this.thumbnail = model.getThumbnail();
        this.price = model.getPrice();
        this.description = model.getDescription();
        this.vendorId = model.getVendorId();
    }

    public String getUnitIn() {
        return unitIn;
    }

    public void setUnitIn(String unitIn) {
        this.unitIn = unitIn;
    }

    public List<UnitModel> getUnits() {
        return units;
    }

    public void setUnits(List<UnitModel> units) {
        this.units = units;
    }

    public String getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(String currentprice) {
        this.currentprice = currentprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
