package com.smartwebarts.apnamart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.smartwebarts.apnamart.utils.ApplicationConstants;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

//        MyGlide.withCircle(this, getResources().getDrawable(R.drawable.logo), (ImageView) findViewById(R.id.logo));
    }

    public void back(View view) {
        onBackPressed();
    }

    public void facebook(View view) {
        openUrl(ApplicationConstants.INSTANCE.FACEBOOK);
    }

    public void instagram(View view) {
        openUrl(ApplicationConstants.INSTANCE.INSTAGRAM);
    }

    public void twitter(View view) {
        openUrl(ApplicationConstants.INSTANCE.TWITTER);
    }

    public  void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void linkedin(View view) {
        openUrl(ApplicationConstants.INSTANCE.LINKEDIN);
    }

    public void youtube(View view) {
        openUrl(ApplicationConstants.INSTANCE.YOUTUBE);
    }
}