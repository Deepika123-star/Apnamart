package com.smartwebarts.apnamart.dashboard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.productlist.ProductListActivity2;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private Context context;
    private List<SubCategoryModel> list;
    private CategoryModel categoryModel;

    public GridAdapter(Context context, List<SubCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    public GridAdapter(Context context, List<SubCategoryModel> list, CategoryModel categoryModel) {
        this.context = context;
        this.list = list;
        this.categoryModel = categoryModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_view3, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES + list.get(position).getImage(), holder.imageView);

        holder.textView.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity2.class);
                intent.putExtra(ProductListActivity2.CID, categoryModel.getId());
                intent.putExtra(ProductListActivity2.SID, list.get(position).getId());
                intent.putExtra(ProductListActivity2.SNAME, list.get(position).getName());
                context.startActivity(intent);
            }
        });

        try {
           // holder.tvOffer.setText(list.get(position).getValue() + "%");
        } catch (Exception ignored){
           // holder.cvoffer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, tvOffer;
        CardView cvoffer;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tvName);
            textView = (TextView) itemView.findViewById(R.id.textView);
           // tvOffer = (TextView) itemView.findViewById(R.id.tvoffer);
           // cvoffer = (CardView) itemView.findViewById(R.id.cvoffer);
        }
    }
}

