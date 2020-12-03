package com.smartwebarts.apnamart.shopbycategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShopByCategoryActivity extends AppCompatActivity {

    //initialize
    private Activity activity;
    RecyclerView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    private List<CategoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_by_category);
        activity = ShopByCategoryActivity.this;

        Toolbar_Set.INSTANCE.setToolbar(this, "Shop by Categories");
        Toolbar_Set.INSTANCE.setBottomNav(this);

        SharedPreferences sharedpreferences = getSharedPreferences(ApplicationConstants.INSTANCE.MyPREFERENCES, Context.MODE_PRIVATE);
        String message = sharedpreferences.getString(ApplicationConstants.INSTANCE.PRODUCT_LIST, "");
        if (!message.isEmpty()) {
            Type listType = new TypeToken<ArrayList<CategoryModel>>() {
            }.getType();
            list = new Gson().fromJson(message, listType);
        }

        findViews();
        init();

    }

    private void findViews() {
        expandableListView = findViewById(R.id.ExpandableListView);
    }

    private void init() {
        expandableListAdapter = new ExpandableListAdapter(this, list);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setHasFixedSize(true);
    }
}