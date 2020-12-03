package com.smartwebarts.apnamart.productlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.ProductModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.models.SubSubCategoryModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView rvProductList, rvProductGrid;
    SubSubCategoryModel subSubCategory;
    CategoryModel category;
    SubCategoryModel subCategory;
    TextView tv_subsubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvProductList = findViewById(R.id.rvProductList);
        rvProductGrid = findViewById(R.id.rvProductGrid);

        tv_subsubCategory = findViewById(R.id.subsubCategory);

        subCategory = (SubCategoryModel) getIntent().getSerializableExtra("subCategory");
        category = (CategoryModel) getIntent().getSerializableExtra("category");
        subSubCategory = (SubSubCategoryModel) getIntent().getSerializableExtra("subsubcategory");
        tv_subsubCategory.setText(subSubCategory.getName());

        Toolbar_Set.INSTANCE.setToolbar(this, subCategory.getName());
        Toolbar_Set.INSTANCE.setBottomNav(this);

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.products(this, category.getId(),subCategory.getId(), subSubCategory.getId(),new mCallBackResponse() {
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

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    private void setProduct(List<ProductModel> list) {
        ProductListGridAdapter adapter = new ProductListGridAdapter(this, list);
        rvProductGrid.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductGrid.setAdapter(adapter);

        ProductListAdapter adapter2 = new ProductListAdapter(this, list);
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
