package com.smartwebarts.apnamart.productlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.UnitModel;

import java.util.List;

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.MyViewHolder> {

    private Context context;
    private List<UnitModel> list;
    private static int currentposition = 0;

    public UnitListAdapter(Context context, List<UnitModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.unit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (list!=null && list.get(position)!=null){
            holder.tvUnit.setText(list.get(position).getUnit() + list.get(position).getUnitIn());
            holder.tvPrice.setText(context.getString(R.string.currency) + list.get(position).getBuingprice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ProductDetailActivity) context).onClick(position);
                    currentposition = position;
                    notifyDataSetChanged();
                }
            });
        }
        if (currentposition == position) {
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.zoomin);
            holder.itemView.startAnimation(animation);
        }

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvUnit, tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUnit = itemView.findViewById(R.id.unit);
            tvPrice = itemView.findViewById(R.id.price);
        }
    }
}
