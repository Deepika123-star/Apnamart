package com.smartwebarts.apnamart.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import com.google.gson.Gson;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.AddressModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.MyGlide;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class ProfileActivity extends AppCompatActivity {

    private AppSharedPreferences appSharedPreferences;
    private String apiKey;

    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        appSharedPreferences = new AppSharedPreferences(getApplication());
        apiKey = getString(R.string.location_api_key_id);
        Toolbar_Set.INSTANCE.setBottomNav(this);
        Toolbar_Set.INSTANCE.getCartList(this);

        ImageView userProfilePic = findViewById(R.id.userProfilePic);
        TextView username = findViewById(R.id.Username);
        TextView usermobile = findViewById(R.id.UserMobile);
        TextView useremail = findViewById(R.id.userEmail);
        TextView address = findViewById(R.id.address);

        MyGlide.Profile(this,appSharedPreferences.getLoginUserProfilePic(),userProfilePic);
        username.setText(appSharedPreferences.getLoginUserName());
        usermobile.setText(appSharedPreferences.getLoginMobile());
        useremail.setText(appSharedPreferences.getLoginEmail());


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        getAddress(address);
//        address.setText(appSharedPreferences.getLoginAddress());
    }

    private void getAddress(TextView address) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.getaddress(this, "1", appSharedPreferences.getLoginUserLoginId()
                    , new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {
                            AddressModel addressModel = new Gson().fromJson(message, AddressModel.class);
                            try {
                                if (addressModel.getAddress()!=null) {
                                    address.setText(addressModel.getAddress()
                                            +", "+addressModel.getCity()
                                            +", "+addressModel.getPincode());
                                } else {
                                    address.setText("Add an address");
                                }
                            } catch (Exception ignored) {
                                address.setText("Add an address");
                            }

                        }

                        @Override
                        public void fail(String from) {
                            address.setText("Add an address");
                        }
                    });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void logout(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);

        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                preferences.logout(ProfileActivity.this);
                dialog.dismiss();
            }
        });
    }


    public void OpenDialogFwd(View v1) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialogaddress, null);

        TextInputEditText txtArea = view.findViewById(R.id.txtArea);
        TextInputEditText txtCity = view.findViewById(R.id.txtCity);
        TextInputEditText txtHouse = view.findViewById(R.id.txtHouse);
        TextInputEditText txtPin = view.findViewById(R.id.txtPincode);
        Button FwdokButton = (Button) view.findViewById(R.id.okButton);
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        PlacesAutocompleteTextView placesAutocomplete = view.findViewById(R.id.places_autocomplete);

        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(PlaceDetails details) {
                                Log.d("test", "details " + details);
//                                mStreet.setText(details.name);
                                for (AddressComponent component : details.address_components) {
                                    for (AddressComponentType type : component.types) {
                                        switch (type) {
                                            case STREET_NUMBER:
                                                break;
                                            case ROUTE:
                                                break;
                                            case NEIGHBORHOOD:
                                                break;
                                            case SUBLOCALITY_LEVEL_1:
                                                break;
                                            case SUBLOCALITY:
                                                txtArea.append(" "+component.long_name);
                                                break;
                                            case LOCALITY:
                                                txtCity.setText(component.long_name);
                                                break;
                                            case ADMINISTRATIVE_AREA_LEVEL_1:
//                                                txtArea.setText(component.short_name);
                                                break;
                                            case ADMINISTRATIVE_AREA_LEVEL_2:
                                                break;
                                            case COUNTRY:
                                                break;
                                            case POSTAL_CODE:
                                                txtPin.setText(component.long_name);
                                                break;
                                            case POLITICAL:
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );
        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FwdokButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!validate()) {
                    return;
                }

                if (UtilMethods.INSTANCE.isNetworkAvialable(ProfileActivity.this)) {

                    UtilMethods.INSTANCE.setAddress(ProfileActivity.this,
                            appSharedPreferences.getLoginUserLoginId(),
                            txtArea.getText().toString().trim(),
                            txtCity.getText().toString().trim(),
                            txtHouse.getText().toString().trim(),
                            txtPin.getText().toString().trim(),
                            /*placesAutocomplete.getText().toString().trim()*/"",
                            new mCallBackResponse() {
                                @Override
                                public void success(String from, String message) {
                                    getAddress((TextView) v1);
                                }

                                @Override
                                public void fail(String from) {

                                }
                            });

                } else {
                    UtilMethods.INSTANCE.internetNotAvailableMessage(ProfileActivity.this);
                }
                dialog.dismiss();
            }
            private boolean validate() {

                if (txtArea.getText().toString().isEmpty()){
                    txtArea.setError("Field Required");
                    return false;
                }
                if (txtPin.getText().toString().isEmpty()){
                    txtPin.setError("Field Required");
                    return false;
                }
                if (txtCity.getText().toString().isEmpty()){
                    txtCity.setError("Field Required");
                    return false;
                }
                if (txtHouse.getText().toString().isEmpty()){
                    txtHouse.setError("Field Required");
                    return false;
                }
//                if (placesAutocomplete.getText().toString().isEmpty()){
//                    placesAutocomplete.setError("Field Required");
//                    return false;
//                }
                return true;
            }
        });

        dialog.show();
    }

}