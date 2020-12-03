package com.smartwebarts.apnamart.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.smartwebarts.apnamart.BuildConfig;
import com.smartwebarts.apnamart.ContactUsActivity;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.AddressModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shopbycategory.ShopByCategoryActivity;
import com.smartwebarts.apnamart.SignInActivity;
import com.smartwebarts.apnamart.WebViewActivity;
import com.smartwebarts.apnamart.history.MyHistoryActivity;
import com.smartwebarts.apnamart.profile.ProfileActivity;
import com.smartwebarts.apnamart.utils.Toolbar_Set;
import com.smartwebarts.apnamart.vendors.VendorActivity;
import com.smartwebarts.apnamart.wallet.WalletActivity;
import com.smartwebarts.apnamart.wishlist.WishListActivity;
import com.smartwebarts.apnamart.cart.CartActivity;
import com.smartwebarts.apnamart.database.DatabaseClient;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.ApplicationConstants;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_LOCATION = 202;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar_Set.INSTANCE.setBottomNav(this);
        setSupportActionBar(toolbar);
        getCartList();
        checkPermission();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setNavigationDrawer();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        UtilMethods.INSTANCE.version(this, null);
    }

    private void turnongps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
//            Toast.makeText(getApplicationContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
            //getActivity().finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(this)) {
//            Toast.makeText(getApplicationContext(),"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            // Log.e("Neha","Gps already enabled");
//            Toast.makeText(getApplicationContext(),"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            // Log.e("Neha","Gps already enabled");
//            Toast.makeText(getApplicationContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(7 * 1000);  //30 * 1000
            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(DashboardActivity.this, REQUEST_LOCATION);

                                // getActivity().finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private void getAddress(TextView address) {

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.getaddress2(this, "1", preferences.getLoginUserLoginId()
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

                if (UtilMethods.INSTANCE.isNetworkAvialable(DashboardActivity.this)) {

                    AppSharedPreferences preferences = new AppSharedPreferences(getApplication());

                    UtilMethods.INSTANCE.setAddress(DashboardActivity.this,
                            preferences.getLoginUserLoginId(),
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
                    UtilMethods.INSTANCE.internetNotAvailableMessage(DashboardActivity.this);
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

    private void setNavigationDrawer() {

        View headerLayout = navigationView.getHeaderView(0);
        Menu nav_Menu = navigationView.getMenu();
        TextView tvUser = headerLayout.findViewById(R.id.tvName);
        TextView tvEmail = headerLayout.findViewById(R.id.tvEmail);
        TextView tvAddress = headerLayout.findViewById(R.id.tvAddress);
        TextView tvAppPages = headerLayout.findViewById(R.id.tvAppPages);
        TextView tvTerms = headerLayout.findViewById(R.id.tv_terms);
        TextView tvPrivacy = headerLayout.findViewById(R.id.tv_privacy);
        TextView tvRefund = headerLayout.findViewById(R.id.tv_refund);
        LinearLayout ll_apppages = headerLayout.findViewById(R.id.ll_apppages);


        tvAppPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_apppages.getVisibility() == View.GONE) {
                    ll_apppages.setVisibility(View.VISIBLE);
                    tvAppPages.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_terms, 0, R.drawable.up_icon, 0);
                } else {
                    ll_apppages.setVisibility(View.GONE);
                    tvAppPages.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_terms, 0, R.drawable.down_icon, 0);
                }
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.TERMS_CONDITION);
                intent.putExtra(WebViewActivity.TITLE, "Terms & Conditions");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.PRIVACY_POLICY);
                intent.putExtra(WebViewActivity.TITLE, "Privacy Policy");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        tvRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.REFUND_POLICY);
                intent.putExtra(WebViewActivity.TITLE, "Refund Policy");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());

        if (preferences.getLoginUserName() != null && !preferences.getLoginUserName().isEmpty()) {
            String[] s = preferences.getLoginUserName().trim().split("\\s+");
            tvUser.setText(String.format("Welcome %s", s[0]));
            findViewById(R.id.move).setVisibility(View.VISIBLE);
            getAddress(tvAddress);
            tvAddress.setVisibility(View.VISIBLE);
        } else {
//            nav_Menu.findItem(R.id.nav_gallery).setVisible(false);
//            nav_Menu.findItem(R.id.my_wishlist).setVisible(false);
//            nav_Menu.findItem(R.id.my_account).setVisible(false);
//            nav_Menu.findItem(R.id.logout).setVisible(false);
//            nav_Menu.findItem(R.id.my_wallet).setVisible(false);
//            nav_Menu.findItem(R.id.share_app).setVisible(false);

            tvAddress.setVisibility(View.GONE);
            findViewById(R.id.move).setVisibility(View.GONE);
        }

        if (preferences.getLoginEmail() != null && !preferences.getLoginEmail().isEmpty()) {
            tvEmail.setText(String.format("%s", preferences.getLoginEmail()));

        } else {
            tvEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DashboardActivity.this, SignInActivity.class));
                }
            });
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogFwd(view);
            }
        });
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(
                new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                },
                2000
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_home: {
                startActivity(new Intent(DashboardActivity.this, ShopByCategoryActivity.class));
                break;
            }
            case R.id.nav_gallery: {
                startActivity(new Intent(DashboardActivity.this, MyHistoryActivity.class));
                break;
            }
            case R.id.my_basket: {
                startActivity(new Intent(DashboardActivity.this, CartActivity.class));
                break;
            }
            case R.id.my_wishlist: {
                startActivity(new Intent(DashboardActivity.this, WishListActivity.class));
                break;
            }
            case R.id.vendors: {
                startActivity(new Intent(DashboardActivity.this, VendorActivity.class));
                break;
            }
            case R.id.my_account: {
                AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                if (!preferences.getLoginUserLoginId().isEmpty()) {
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                } else {
                    Toast.makeText(this, "Login or Create a Account", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.my_wallet: {
                AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                if (!preferences.getLoginUserLoginId().isEmpty()) {

                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashboardActivity.this, WalletActivity.class));
                } else {
                    Toast.makeText(this, "Login or Create a Account", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.terms: {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.TERMS_CONDITION);
                intent.putExtra(WebViewActivity.TITLE, "Terms & Conditions");
                startActivity(intent);
                break;
            }
            case R.id.privacy_policy: {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.PRIVACY_POLICY);
                 intent.putExtra(WebViewActivity.TITLE, "Privacy Policy");
                startActivity(intent);
                break;
            }
            case R.id.refund_policy: {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.REFUND_POLICY);
                intent.putExtra(WebViewActivity.TITLE, "Refund Policy");
                startActivity(intent);
                break;
            }
            case R.id.about_us: {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.ABOUT_US);
                intent.putExtra(WebViewActivity.TITLE, "About Us");
                startActivity(intent);
                break;
            }
            case R.id.live_chat: {
                try {
                    String url = "https://api.whatsapp.com/send?phone="+"+917037481805";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to open your whatsapp", Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
//                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.LIVE_CHAT);
////                intent.putExtra(WebViewActivity.DATA, "https://www.google.com/maps/dir/26.8610992,80.8462559/Smart Web Arts, Kailash Plaza,Kanpur Road, Sector G, LDA Colony, Lucknow, Uttar Pradesh 226012/");
//                intent.putExtra(WebViewActivity.TITLE, "Live Chat");
//                startActivity(intent);
                break;
            }
            case R.id.share_app: {
                createlink();
                break;
            }
            case R.id.contact_us: {
                startActivity(new Intent(DashboardActivity.this, ContactUsActivity.class));
                break;
            }
            case R.id.faq: {
                Intent intent = new Intent(DashboardActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.DATA, ApplicationConstants.INSTANCE.FAQ);
                intent.putExtra(WebViewActivity.TITLE, "FAQs");
                startActivity(intent);
                break;
            }
            case R.id.logout: {
                logout();
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createlink() {

//        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(ApplicationConstants.INSTANCE.SITE_URL))
//                .setDomainUriPrefix("globalfreshbasket.page.link")
//                // Open links with this app on Android
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                // Open links with com.example.ios on iOS
//                .setIosParameters(new DynamicLink.IosParameters.Builder(BuildConfig.APPLICATION_ID).build())
//                .buildDynamicLink();
//
//        Uri dynamicLinkUri = dynamicLink.getUri();

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String shareapp = "https://apnamartonline.page.link?" +
                "apn=" + BuildConfig.APPLICATION_ID +
               /* "&st=My Refer Link" +
                "&sd=Register using this link to earn rewards" +*/
                "&si=https://www.apnamartonline.com/Company%20Logo/apnamart.jpg" +
                "&link=" + ApplicationConstants.INSTANCE.SITE_URL + "api.php?custid=" + preferences.getLoginUserLoginId();

//        Log.e("referlink", dynamicLinkUri.toString());
        Log.e("shareapp", shareapp);
        /*short the link*/
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLongLink(dynamicLinkUri)
                .setLongLink(Uri.parse(shareapp))
//                .setDomainUriPrefix("https://example.page.link")
                // Set parameters
                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();


                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            // Error
                            // ...
                        }
                    }
                });


    }

    private void logout() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
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
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(DashboardActivity.this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                AppSharedPreferences preferences = new AppSharedPreferences(DashboardActivity.this.getApplication());
                preferences.logout(DashboardActivity.this);
                dialog.dismiss();
            }
        });
    }

    public void showCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

    public void refresh(View view) {
        getCartList();
    }

    public void move(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_gallery: {
                        startActivity(new Intent(DashboardActivity.this, MyHistoryActivity.class));
                        break;
                    }

                    case R.id.my_wishlist: {
                        startActivity(new Intent(DashboardActivity.this, WishListActivity.class));
                        break;
                    }

                    case R.id.my_account: {
                        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                        if (!preferences.getLoginUserLoginId().isEmpty()) {
                            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                        } else {
                            Toast.makeText(DashboardActivity.this, "Login or Create a Account", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case R.id.my_wallet: {
                        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                        if (!preferences.getLoginUserLoginId().isEmpty()) {

                            drawer.closeDrawer(GravityCompat.START);
                            startActivity(new Intent(DashboardActivity.this, WalletActivity.class));
                        } else {
                            Toast.makeText(DashboardActivity.this, "Login or Create a Account", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case R.id.logout: {
                        logout();
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void getCartList() {

        class GetTasks extends AsyncTask<Void, Void, ArrayList<com.smartwebarts.apnamart.database.Task>> {

            @Override
            protected ArrayList<com.smartwebarts.apnamart.database.Task> doInBackground(Void... voids) {
                List<com.smartwebarts.apnamart.database.Task> tasks = DatabaseClient.getmInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return new ArrayList<>(tasks);
            }

            @Override
            protected void onPostExecute(ArrayList<com.smartwebarts.apnamart.database.Task> tasks) {
//                Toast.makeText(getApplicationContext(), ""+tasks.size(), Toast.LENGTH_SHORT).show();
                int size = tasks != null ? tasks.size() : 0;
                TextView cartItemsCount = findViewById(R.id.cartItemsCount);
                cartItemsCount.setText("" + size);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }

    public void openDrawer(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // You can use the API that requires the permission.
            turnongps();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    turnongps();
                } else {
                    turnongps();
                }
                return;
        }
    }
}
