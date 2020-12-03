package com.smartwebarts.apnamart.dashboard.ui.home;

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
import com.smartwebarts.apnamart.category.CategoryActivity;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryModel> list;

    public TopAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES+list.get(position).getImage(), holder.imageView);

        holder.categoryName.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(CategoryActivity.CATEGORY, list.get(position));
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
        TextView categoryName;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tvName);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }
}
