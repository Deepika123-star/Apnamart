package com.smartwebarts.apnamart.models;

import com.google.gson.annotations.SerializedName;

public class AmountModel {

    @SerializedName("wallet_amt")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
