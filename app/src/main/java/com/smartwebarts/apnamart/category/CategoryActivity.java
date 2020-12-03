package com.smartwebarts.apnamart.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

public class CategoryActivity extends AppCompatActivity {

    ImageView imageView;
    RecyclerView rvcategory;
    CategoryModel category;
    public static final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        imageView = (ImageView) findViewById(R.id.tvName);
        rvcategory = findViewById(R.id.rvCategories);

        category = (CategoryModel) getIntent().getSerializableExtra(CATEGORY);

        Toolbar_Set.INSTANCE.setToolbar(this, category.getName());
        Toolbar_Set.INSTANCE.setBottomNav(this);
//        MyGlide.with(this,ApplicationConstants.INSTANCE.CATEGORY_IMAGES + category.getImage(),imageView);

//        if (category.getName().toLowerCase().contains("vegetable")){
//            MyGlide.with(this, this.getDrawable(R.drawable.vegetable), imageView);
//        } else if (category.getName().toLowerCase().contains("foodgrains")){
//            MyGlide.with(this, this.getDrawable(R.drawable.foodgrains), imageView);
//        } else if (category.getName().toLowerCase().contains("beverages")){
//            MyGlide.with(this, this.getDrawable(R.drawable.beverages), imageView);
//        } else if (category.getName().toLowerCase().contains("personel care")){
//            MyGlide.with(this, this.getDrawable(R.drawable.personalcare), imageView);
//        } else if (category.getName().toLowerCase().contains("packaged food")){
//            MyGlide.with(this, this.getDrawable(R.drawable.packagedfood), imageView);
//        } else if (category.getName().toLowerCase().contains("dairy")){
//            MyGlide.with(this, this.getDrawable(R.drawable.dairy), imageView);
//        } else if (category.getName().toLowerCase().contains("household")){
//            MyGlide.with(this, this.getDrawable(R.drawable.household), imageView);
//        } else {
            MyGlide.with(this, ApplicationConstants.INSTANCE.CATEGORY_IMAGES+category.getImage(), imageView);
//        }


        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.subCategories(this, category.getId(),new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<SubCategoryModel>>(){}.getType();
                    List<SubCategoryModel> list = new Gson().fromJson(message, listType);
                    setCategory(list);
                }

                @Override
                public void fail(String from) {
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    public void setCategory(List<SubCategoryModel> list){
        CategoryListAdapter adapter = new CategoryListAdapter(this, list, category);
        rvcategory.setItemAnimator(new DefaultItemAnimator());
        rvcategory.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }
}
