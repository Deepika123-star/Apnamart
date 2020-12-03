package com.smartwebarts.apnamart.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class WalletActivity extends AppCompatActivity {

    private WalletActivity activity;
    TextView Wallet_Ammount;
    RelativeLayout Recharge_Wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        activity = WalletActivity.this;

//        Toolbar_Set.INSTANCE.setToolbar(this, "My Wallet");
//        Toolbar_Set.INSTANCE.setBottomNav(this);

        Wallet_Ammount = (TextView) findViewById(R.id.wallet_ammount);
        Recharge_Wallet = (RelativeLayout) findViewById(R.id.recharge_wallet);
        Recharge_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RechargeWallet.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toolbar_Set.INSTANCE.setToolbar(this, "My Wallet");
        Toolbar_Set.INSTANCE.setBottomNav(this);
        Toolbar_Set.INSTANCE.userWallet(this);
    }
}