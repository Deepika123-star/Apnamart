package com.smartwebarts.apnamart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import com.smartwebarts.apnamart.cart.CartActivity;
import com.smartwebarts.apnamart.changepassword.ChangePasswordFragment;
import com.smartwebarts.apnamart.dashboard.DashboardActivity;
import com.smartwebarts.apnamart.models.OTPModel;
import com.smartwebarts.apnamart.models.SocialDataCheckModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.shared_preference.LoginData;
import com.smartwebarts.apnamart.shared_preference.SharedLinkPrefs;

public class SignInActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN=1;
    private static final String TAG = SignInActivity.class.getSimpleName();
    //    List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "AccessToken");
    List<String> permissionNeeds = Arrays.asList("email", "public_profile", "user_friends");
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView tvMobile, tvPassword;

    private String accessToken;
    private String Uid;
    private String Uname;
    private String Uemail;
    private String UimageUrl;
    private String Uphone;

    private String SocialName;
    private String SocialEmail;
    private String SocialImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getDynamicLink();
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        tvMobile = findViewById(R.id.mobile);
        tvPassword = findViewById(R.id.password);

        loginButton.setReadPermissions(permissionNeeds);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                accessToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
//                                Uid = object.getString("id");

                                    SocialImage = "http://graph.facebook.com/" + Uid + "/picture?type=large";
                                    SocialName = object.getString("name");
                                    SocialEmail = object.getString("email");
//                                saveUser(Uid, Uname, Uemail, UimageUrl, "", accessToken);
                                    checksocialuser(object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignInActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignInActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", ""+error.getMessage());
            }
        });
    }

    private void checksocialuser(String email) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.socialcheck(this, email, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    SocialDataCheckModel data = new Gson().fromJson(message, SocialDataCheckModel.class);
                    if (data.getMessage().equalsIgnoreCase("success")){
                        Uphone = data.getContact();
                        Uid = data.getId();
                        Uemail = data.getEmail();
                        UimageUrl = SocialImage;
                        Uname = data.getUsername();
                        saveUser(Uid, Uname, Uemail, UimageUrl, Uphone, "");
                    } else {
                        register();
                    }
                }

                @Override
                public void fail(String from) {
                    register();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    private void register() {
        Intent intent = new Intent(SignInActivity.this, RegisterSocial.class);
        intent.putExtra("cart", getIntent().getBooleanExtra("cart", false));
        intent.putExtra(RegisterSocial.NAME, SocialName);
        intent.putExtra(RegisterSocial.EMAIL, SocialEmail);
        intent.putExtra(RegisterSocial.IMAGE, SocialImage);
        startActivity(intent);
    }

    public void loginWithMobile(View view) {
        if (tvMobile.getText().toString().isEmpty() || tvMobile.getText().toString().length()<10){
            tvMobile.setError("10 digit Number Requied");
            return;
        }

        if (tvPassword.getText().toString().isEmpty()){
            tvMobile.setError("Password Requied");
            return;
        }

        requestlogin(tvMobile.getText().toString().trim(), tvPassword.getText().toString().trim());

    }

    private void requestlogin(String mobile, String password) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.Login(this, mobile, password, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                    preferences.setLoginDetails(message);
                    if (getIntent().getBooleanExtra("cart", false)) {
                        Intent intent = new Intent(SignInActivity.this, CartActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(SignInActivity.this, "Invalid User Id or Password", Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    public void loginWithGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void loginWithFacebook(View view) {
        loginButton.performClick();
    }

    public void signUp(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN && data !=null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("LoginActivity", "Google sign in failed"+e.getMessage());
                // ...
            }
            return;
        }
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e("LoginActivity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("LoginActivity", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("LoginActivity", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.container), "Authentication Failed."+task.getException(), Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
//            Uid=user.getUid();
            SocialName=user.getDisplayName();
            SocialEmail=user.getEmail();
            SocialImage=user.getPhotoUrl().toString();
            checksocialuser(user.getEmail());
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
            Intent intent = new Intent(SignInActivity.this, CartActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
        finishAffinity();
    }

    public void OpenDialogFwd(View v1) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.forgotpass, null);

        EditText edMobileFwp = (EditText) view.findViewById(R.id.ed_mobile_fwp);
        TextInputLayout tilMobileFwp=(TextInputLayout)view.findViewById(R.id.til_mobile_fwp);
        Button FwdokButton = (Button) view.findViewById(R.id.okButton);
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edMobileFwp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!validateMobileFwp(edMobileFwp, tilMobileFwp, FwdokButton)) {
                    return;
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FwdokButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String number = edMobileFwp.getText().toString();
                if (!validateMobileFwp(edMobileFwp, tilMobileFwp, FwdokButton)) {
                    return;
                }

                if (UtilMethods.INSTANCE.isNetworkAvialable(SignInActivity.this)) {

                    UtilMethods.INSTANCE.otp(SignInActivity.this, edMobileFwp.getText().toString().trim(), new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {
                            OTPModel otpModel = new Gson().fromJson(message, OTPModel.class);
                            Intent intent = new Intent(SignInActivity.this, ChangePasswordFragment.class);
                            intent.putExtra(ChangePasswordFragment.OTP, otpModel);
                            intent.putExtra(ChangePasswordFragment.NUMBER, number);
                            startActivity(intent);
                        }

                        @Override
                        public void fail(String from) {

                        }
                    });

                } else {
                    UtilMethods.INSTANCE.internetNotAvailableMessage(SignInActivity.this);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }



    public boolean validateMobileFwp(EditText edMobileFwp, TextInputLayout tilMobileFwp, Button FwdokButton){
        if (edMobileFwp.getText().toString().trim().isEmpty()) {
            tilMobileFwp.setError(getString(R.string.err_msg_mobile));
            requestFocus(edMobileFwp);
            return false;
        }
        else if (!(edMobileFwp.getText().toString().trim().length()==10)){
            tilMobileFwp.setError(getString(R.string.err_msg_mobile_length));
            requestFocus(edMobileFwp);
            return false;
        }else {
            tilMobileFwp.setErrorEnabled(false);
            FwdokButton.setEnabled(true);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void getDynamicLink() {

        SharedLinkPrefs prefs = new SharedLinkPrefs(getApplication());

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String referlink = deepLink.toString();
                            try {
                                String custid = referlink.substring(referlink.lastIndexOf("=")+1, referlink.length());
                                prefs.setSharedLinkUserId(custid);
//                                Toast.makeText(SignInActivity.this, ""+custid, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }
}