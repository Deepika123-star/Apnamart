package com.smartwebarts.apnamart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import com.smartwebarts.apnamart.cart.CartActivity;
import com.smartwebarts.apnamart.dashboard.DashboardActivity;
import com.smartwebarts.apnamart.models.RegisterSocialModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.shared_preference.LoginData;
import com.smartwebarts.apnamart.shared_preference.SharedLinkPrefs;

public class RegisterSocial extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String IMAGE = "image";
    public String name, email, image;
    TextInputEditText txtName, txtEmail, txtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_social);

        name = getIntent().getExtras().getString(NAME, "");
        email = getIntent().getExtras().getString(EMAIL, "");
        image = getIntent().getExtras().getString(IMAGE, "");

        txtName = findViewById(R.id.fullName);
        txtEmail = findViewById(R.id.email);
        txtMobile = findViewById(R.id.mobile);

        if (!name.isEmpty()){
            txtName.setText(name);
            txtName.setEnabled(false);

        }
        if (!email.isEmpty()){
            txtEmail.setText(email);
            txtEmail.setEnabled(false);
        }

    }

    public void socialRegister(View view) {

        if (txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Enter Email ID");
            return;
        }

        if (txtName.getText().toString().trim().isEmpty()){
            txtName.setError("Enter User Name");
            return;
        }

        if (txtMobile.getText().length()<10){
            txtMobile.setError("Enter a valid 10 digit number");
            return;
        }

        SharedLinkPrefs prefs = new SharedLinkPrefs(getApplication());
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.signupsocialuser(this, email, txtMobile.getText().toString(), name, prefs.getSharedLinkUserId(), new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    RegisterSocialModel model = new Gson().fromJson(message, RegisterSocialModel.class);
                    if (model.getMessage()!=null && model.getMessage().equalsIgnoreCase("success")) {
                        saveUser(model.getId(),
                                model.getUsername(),
                                model.getEmail(),
                                image,
                                model.getContact(),
                                "");

                        prefs.setSharedLinkUserId("");
                    }
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(RegisterSocial.this, ""+from, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    private void saveUser(String id, String name, String email, String photourl, String phone, String accessToken) {

        LoginData data = new LoginData(id, "", "", "", name, "", email, "",
                phone, "User", "", "", "", "", "", "", "",
                "", "", photourl,photourl,photourl, "", "", "");
        String strdata = new Gson().toJson(data);
        AppSharedPreferences preferences = new AppSharedPreferences(this.getApplication());
        preferences.setLoginDetails(strdata);

        if (getIntent().getBooleanExtra("cart", false)) {
            Intent intent = new Intent(RegisterSocial.this, CartActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(RegisterSocial.this, DashboardActivity.class);
            startActivity(intent);
        }
        finishAffinity();
    }
}