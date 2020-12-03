package com.smartwebarts.apnamart.shared_preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SharedLinkPrefs extends AndroidViewModel {

    private static final String PREF_NAME = "sharelink";
    private static final String SHARED_LINK_USER_ID = "sharedid";
    private Context context;
    private SharedPreferences preferences;

    public SharedLinkPrefs(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public String getSharedLinkUserId() {
        return getPreferences().getString(SHARED_LINK_USER_ID, "");
    }

    public void setSharedLinkUserId(String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(SHARED_LINK_USER_ID, value);
        editor.apply();
    }
}
