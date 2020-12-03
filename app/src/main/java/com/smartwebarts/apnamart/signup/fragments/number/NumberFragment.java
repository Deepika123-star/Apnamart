package com.smartwebarts.apnamart.signup.fragments.number;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.OTPModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;

public class NumberFragment extends Fragment {

    private NumberViewModel mViewModel;
    private FloatingActionButton floatingActionButton;
    private TextInputEditText mobile;

    public static NumberFragment newInstance() {
        return new NumberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.number_fragment, container, false);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        mobile = view.findViewById(R.id.mobile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty()|| mobile.getText().toString().length()<10){
                    mobile.setError("10 digit number required");
                    return;
                }
                requestOtp(v);
            }
        });
        return view;
    }

    private void requestOtp(final View v) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(requireActivity())) {

            final String mobileNumber = mobile.getText().toString();
            UtilMethods.INSTANCE.otp(requireActivity(), mobileNumber, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    OTPModel otpModel = new Gson().fromJson(message, OTPModel.class);
                    if (otpModel.getMessage().equalsIgnoreCase("success")){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", otpModel);
                        bundle.putString("mobile", mobileNumber);
                        Navigation.findNavController(v).navigate(R.id.action_numberfragment_to_OTPFragment, bundle);

                    } else {
                        Toast.makeText(requireActivity(), ""+otpModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void fail(String from) {

                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(requireActivity());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NumberViewModel.class);
        // TODO: Use the ViewModel
    }

}