package com.smartwebarts.apnamart.dashboard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.category.CategoryActivity;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;


public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryModel> list;


    public BottomAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

//        if (list.get(position).getName().toLowerCase().contains("vegetable")){
//            MyGlide.with(context, context.getDrawable(R.drawable.vegetable), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("foodgrains")){
//            MyGlide.with(context, context.getDrawable(R.drawable.foodgrains), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("beverages")){
//            MyGlide.with(context, context.getDrawable(R.drawable.beverages), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("personel care")){
//            MyGlide.with(context, context.getDrawable(R.drawable.personalcare), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("packaged food")){
//            MyGlide.with(context, context.getDrawable(R.drawable.packagedfood), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("dairy")){
//            MyGlide.with(context, context.getDrawable(R.drawable.dairy), holder.imageView);
//        } else if (list.get(position).getName().toLowerCase().contains("household")){
//            MyGlide.with(context, context.getDrawable(R.drawable.household), holder.imageView);
//        } else {
        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES+list.get(position).getImage(), holder.imageView);
//        }

        holder.categoryName.setText(list.get(position).getName());

        holder.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(CategoryActivity.CATEGORY, list.get(position));
                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvViewAll.performClick();
            }
        });

        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvViewAll.performClick();
            }
        });

        UtilMethods.INSTANCE.subCategories(context, list.get(position).getId(), new mCallBackResponse() {
            @Override
            public void success(String from, String message) {
                Type listType = new TypeToken<ArrayList<SubCategoryModel>>(){}.getType();
                List<SubCategoryModel> sublist = new Gson().fromJson(message, listType);
                setCategoryRecycler(holder.recyclerViewCategory,sublist, list.get(position));
            }
            @Override
            public void fail(String from) {
            }
        });
    }

    private void setCategoryRecycler(RecyclerView recyclerView, List<SubCategoryModel> list, CategoryModel categoryModel) {
        GridAdapter adapter = new GridAdapter(context,list, categoryModel);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoryName, tvViewAll;
        RecyclerView recyclerViewCategory, recyclerViewBanner;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tvName);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            tvViewAll = (TextView) itemView.findViewById(R.id.tv_viewAll);
            recyclerViewCategory = (RecyclerView) itemView.findViewById(R.id.recyclerViewCategory);
            recyclerViewBanner = (RecyclerView) itemView.findViewById(R.id.recyclerViewBanner);
        }
    }
}
