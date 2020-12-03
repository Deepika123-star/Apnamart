package com.smartwebarts.apnamart.shared_preference;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;

import com.smartwebarts.apnamart.LoginActivity;

public class AppSharedPreferences extends AndroidViewModel {

//    INSTANCE;

    private static final String PREF_NAME = "SMS";
    private static final String loginDetails = "loginDetails";
    private Context context;

    SharedPreferences preferences;

    public AppSharedPreferences(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public String getLoginDetails() {
        return getPreferences().getString(loginDetails, "");
    }

    public void setLoginDetails( String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(loginDetails, value);
        editor.apply();
    }

    public String getLoginUserName(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null  && data.getName()!=null){
                return data.getName();
            }
        }
        return "";
    }

    public String getLoginEmail(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null  && data.getEmail()!=null){
                return data.getEmail();
            }
        }
        return "";
    }

    public String getLoginAddress(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null  && data.getAddress()!=null){
                return data.getAddress();
            }
        }
        return "";
    }

    public String getLoginMobile(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null  && data.getContact()!=null){
                return data.getContact();
            }
        }
        return "";
    }

    public String getLoginUserProfilePic(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null && data.getProfilePic()!=null){
                return data.getProfilePic();
            }
        }
        return "";
    }


    public String getLoginUserLoginId(){
        String temp = getLoginDetails();
        if (!temp.isEmpty())
        {
            LoginData data = new Gson().fromJson(getLoginDetails(), LoginData.class);
            if (data != null  && data.getId()!=null){
                return data.getId();
            }
        }
        return "";
    }

    public void logout(Context context) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public Context getContext() {
        return context;
    }
}
