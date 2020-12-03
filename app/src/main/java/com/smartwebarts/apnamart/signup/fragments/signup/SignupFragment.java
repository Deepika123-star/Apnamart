package com.smartwebarts.apnamart.signup.fragments.signup;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.SignInActivity;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.SharedLinkPrefs;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupFragment extends Fragment {

    private SignupViewModel mViewModel;
    private TextInputEditText fullname, email,  password;
    private TextInputEditText mobile;
    FloatingActionButton  floatingActionButton;
    private String mobileNumber;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        fullname = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        password = view.findViewById(R.id.password);

        if (getArguments()!=null) {
            mobileNumber = getArguments().getString("mobile", "");
            if (mobileNumber.isEmpty()){
                mobile.setEnabled(true);
            } else {
                mobile.setEnabled(false);
                mobile.setText(mobileNumber);
            }
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()){

                    requestSignUp(v);
                }
            }
        });
        return view;
    }

    private void requestSignUp(View v) {

        SharedLinkPrefs prefs = new SharedLinkPrefs(requireActivity().getApplication());

        if (UtilMethods.INSTANCE.isNetworkAvialable(requireActivity())) {
            UtilMethods.INSTANCE.signup(requireActivity(), fullname.getText().toString(),
                    email.getText().toString(), mobile.getText().toString(), password.getText().toString(), prefs.getSharedLinkUserId(), new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Sign Up Succesfully")
                                    .setContentText("Login to proceed")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            prefs.setSharedLinkUserId("");
                                            startActivity(new Intent(getActivity(), SignInActivity.class));
                                            getActivity().finishAffinity();
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void fail(String from) {
                            Toast.makeText(getActivity(), ""+from, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(requireActivity());
        }
    }

    private boolean validateForm() {
        if (fullname.getText().toString().isEmpty()) {
            fullname.setError("Full Name Required");
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Email Required");
            return false;
        }

        if (!UtilMethods.INSTANCE.isValidEmail(email.getText().toString())){
            email.setError("Inavlid Email");
            return false;
        }

        if (mobile.getText().toString().isEmpty()) {
            mobile.setError("Mobile Required");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password Required");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        // TODO: Use the ViewModel
    }

}