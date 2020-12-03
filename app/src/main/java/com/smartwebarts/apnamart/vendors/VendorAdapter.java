package com.smartwebarts.apnamart.vendors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.VendorModel;
import com.smartwebarts.apnamart.productlist.ProductListActivity3;

import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder>{

    private Context context;
    private List<VendorModel> list;

    public VendorAdapter(Context context, List<VendorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VendorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VendorAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.vendor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAdapter.MyViewHolder holder, final int position) {

        String name = list.get(position).getShopName().trim();

        holder.categoryName.setText(name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity3.class);
                intent.putExtra(ProductListActivity3.VID, list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<VendorModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
