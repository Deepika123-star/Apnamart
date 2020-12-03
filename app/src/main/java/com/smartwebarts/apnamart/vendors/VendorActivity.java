package com.smartwebarts.apnamart.vendors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.VendorModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VendorActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    VendorAdapter adapter;
    List<VendorModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new VendorAdapter(this, list);
        recyclerView.setAdapter(adapter);

        Toolbar_Set.INSTANCE.setBottomNav(this);
        Toolbar_Set.INSTANCE.setToolbar(this, "Vendors");

        search("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                search(newText);
                upDate(newText);
                return false;
            }
        });
    }

    private void upDate(String newText) {
        if (newText.isEmpty()) {
            adapter.updateList(this.list);
        } else {
            List<VendorModel> models = new ArrayList<>();
            for (VendorModel model: list) {
                if (model.getName().trim().toLowerCase().contains(newText.toLowerCase().trim())) {
                    models.add(model);
                }
            }
            adapter.updateList(models);
        }
    }

    private void search(String newText) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.vendor(this, newText, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    Type listType = new TypeToken<ArrayList<VendorModel>>(){}.getType();
                    List<VendorModel> list = new Gson().fromJson(message, listType);
                    VendorActivity.this.list = list;
                    adapter.updateList(VendorActivity.this.list);
                }

                @Override
                public void fail(String from) {

                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }

}