package com.smartwebarts.apnamart.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import com.smartwebarts.apnamart.models.ProductModel;

@Entity
public class WishItem implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    @ColumnInfo(name = "unitIn")
    private String unitIn;

    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "currentprice")
    private String currentprice;

    @ColumnInfo(name = "brand")
    private String brand;

    @ColumnInfo(name = "productType")
    private String productType;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "vendorId")
    private String vendorId;

    @ColumnInfo(name = "quantity")
    private String quantity;

    public WishItem() {
    }

    public WishItem(ProductModel product) {
        this.unitIn = product.getUnitIn();
//        this.unit = product.getUnit();
        this.currentprice = product.getCurrentprice();
        this.id = product.getId();
        this.brand = product.getBrand();
        this.productType = product.getProductType();
        this.name = product.getName();
        this.type = product.getType();
        this.thumbnail = product.getThumbnail();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.vendorId = product.getVendorId();
        this.quantity = quantity;
    }

    public String getUnitIn() {
        return unitIn;
    }

    public void setUnitIn(String unitIn) {
        this.unitIn = unitIn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
