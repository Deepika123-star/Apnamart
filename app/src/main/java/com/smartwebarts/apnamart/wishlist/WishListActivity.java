package com.smartwebarts.apnamart.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.ProductModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class WishListActivity extends AppCompatActivity {
    RecyclerView rvProductList, rvProductGrid;
    TextView tv_subsubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        rvProductList = findViewById(R.id.rvProductList);
        rvProductGrid = findViewById(R.id.rvProductGrid);

        tv_subsubCategory = findViewById(R.id.subsubCategory);
        tv_subsubCategory.setText("My Wishlist");

        Toolbar_Set.INSTANCE.setToolbar(this, "My Wishlist");
        Toolbar_Set.INSTANCE.setBottomNav(this);

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
            if (!preferences.getLoginUserLoginId().isEmpty()) {

                UtilMethods.INSTANCE.getWishlist(this, preferences.getLoginUserLoginId(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        Type listType = new TypeToken<ArrayList<ProductModel>>(){}.getType();
                        List<ProductModel> list = new Gson().fromJson(message, listType);
                        setProduct(list);
                    }

                    @Override
                    public void fail(String from) {

                    }
                });
            } else {
                Toast.makeText(this, "Login to see your wishlist", Toast.LENGTH_SHORT).show();
            }

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    private void setProduct(List<ProductModel> list) {
        WishlistGridAdapter adapter = new WishlistGridAdapter(this, list);
        rvProductGrid.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductGrid.setAdapter(adapter);

        WishlistAdapter adapter2 = new WishlistAdapter(this, list);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 1));
        rvProductList.setAdapter(adapter2);
    }

    public void changeLayout(View view) {
        if (rvProductList.getVisibility() == View.VISIBLE) {
            rvProductList.setVisibility(View.GONE);
            rvProductGrid.setVisibility(View.VISIBLE);
            ((ImageView) view).setImageDrawable(getDrawable(R.drawable.ic_grid));
        } else {
            rvProductList.setVisibility(View.VISIBLE);
            rvProductGrid.setVisibility(View.GONE);
            ((ImageView) view).setImageDrawable(getDrawable(R.drawable.ic_view_list));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }
}
