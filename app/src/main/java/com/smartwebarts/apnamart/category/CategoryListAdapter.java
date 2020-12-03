package com.smartwebarts.apnamart.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.IsVisible;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.models.SubSubCategoryModel;
import com.smartwebarts.apnamart.productlist.ProductAdapter;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<SubCategoryModel> list;
    private static int currentPosition = -1;
    private CategoryModel category;
    private List<IsVisible> isVisible;

    public CategoryListAdapter(Context context, List<SubCategoryModel> list, CategoryModel category) {
        this.context = context;
        this.list = list;
        this.category = category;
        isVisible = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            isVisible.add(new IsVisible());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvCategoryName.setText(list.get(position).getName());

         holder.rvSubCategory.setVisibility(View.GONE);
         holder.dropdown.setImageDrawable(context.getDrawable(R.drawable.down_icon));

        UtilMethods.INSTANCE.subSubCategory(context, category.getId(), list.get(position).getId(), new mCallBackResponse() {
            @Override
            public void success(String from, String message) {
                Type listType = new TypeToken<ArrayList<SubSubCategoryModel>>(){}.getType();
                List<SubSubCategoryModel> list = new Gson().fromJson(message, listType);
                setProductRecycler(holder.rvSubCategory, list, CategoryListAdapter.this.list.get(position));
            }

            @Override
            public void fail(String from) {
            }
        });

        holder.shootup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.shootup.setVisibility(View.GONE);
                holder.dropdown.setVisibility(View.VISIBLE);
                holder.rvSubCategory.setVisibility(View.GONE);
                isVisible.get(position).setVisible(false);
            }
        });

        //if the position is equals to the item position which is to be expanded

        if (isVisible.get(position).isVisible()) {
            holder.shootup.setVisibility(View.GONE);
            holder.dropdown.setVisibility(View.VISIBLE);
            Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            holder.rvSubCategory.startAnimation(slideUp);
            holder.rvSubCategory.setVisibility(View.GONE);
        }

        holder.rvSubCategory.setVisibility(View.GONE);

        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.rvSubCategory.setVisibility(View.VISIBLE);
//            holder.dropdown.setImageDrawable(context.getDrawable(R.drawable.up_icon));
            holder.dropdown.setVisibility(View.GONE);
            holder.shootup.setVisibility(View.VISIBLE);
            //adding sliding effect
            holder.rvSubCategory.startAnimation(slideDown);
        }

        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                isVisible.get(position).setVisible(true);
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shootup.getVisibility() == View.VISIBLE) {
                    holder.shootup.performClick();
                } else if (holder.dropdown.getVisibility() == View.VISIBLE) {
                    holder.dropdown.performClick();
                }
            }
        });
    }

    private void setProductRecycler(RecyclerView rvSubCategory, List<SubSubCategoryModel> list, SubCategoryModel subCategory) {
        ProductAdapter adapter = new ProductAdapter(context, list, category, subCategory);
        rvSubCategory.setItemAnimator(new DefaultItemAnimator());
        rvSubCategory.setLayoutManager(new GridLayoutManager(context,3));
        rvSubCategory.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName;
        ImageView dropdown, shootup;
        RecyclerView rvSubCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            dropdown = itemView.findViewById(R.id.txt_price);
            shootup = itemView.findViewById(R.id.up);
            rvSubCategory = itemView.findViewById(R.id.rvSubCategory);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
