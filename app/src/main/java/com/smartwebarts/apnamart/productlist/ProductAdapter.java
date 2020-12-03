package com.smartwebarts.apnamart.productlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.models.SubSubCategoryModel;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<SubSubCategoryModel> list;
    CategoryModel category;
    SubCategoryModel subCategory;

    public ProductAdapter(Context context, List<SubSubCategoryModel> list , CategoryModel category, SubCategoryModel subCategory) {
        this.context = context;
        this.list = list;
        this.category = category;
        this.subCategory = subCategory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_subcategory, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {

            MyGlide.withCircle(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES + list.get(position).getImage(), holder.imageView);

            holder.textView.setText(list.get(position).getName());

        } catch (Exception | OutOfMemoryError ignored){
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductListActivity.class);

                intent.putExtra("subsubcategory", list.get(position));
                intent.putExtra("subCategory", subCategory);
                intent.putExtra("category", category);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.tvName);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
