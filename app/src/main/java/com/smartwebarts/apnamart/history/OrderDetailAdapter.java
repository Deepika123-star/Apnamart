package com.smartwebarts.apnamart.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.OrderListModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.ApplicationConstants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    Context context;
    List<OrderListModel> list;

    public OrderDetailAdapter(Context context, List<OrderListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_order_detail_rv, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        OrderListModel detail = list.get(i);
        myViewHolder.price.setText(context.getString(R.string.currency)+" "+detail.getAmount());
        myViewHolder.name.setText(detail.getName());
        Glide.with(context).load(ApplicationConstants.INSTANCE.PRODUCT_IMAGES + detail.getThumbnail()).into(myViewHolder.productImage);
        myViewHolder.Qty.setText(detail.getQuantity());
        myViewHolder.unit.setText(detail.getUnit()+detail.getUnitIn());
        myViewHolder.status.setText(detail.getStatus());

        myViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                    AppSharedPreferences preferences = new AppSharedPreferences(((MyHistoryActivity) context).getApplication());
                    String mob = preferences.getLoginMobile();

                    UtilMethods.INSTANCE.returnProductByProductID(context, ""+detail.getId(), mob,new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {
                            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitleText("Order Cancel!");
                            dialog.setContentText("Order Cancelled Successfully");
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismissWithAnimation();
                                    ((MyHistoryActivity) context).onBackPressed();
                                }
                            });
                            dialog.show();
                        }

                        @Override
                        public void fail(String from) {

                        }
                    });
                } else {
                    UtilMethods.INSTANCE.internetNotAvailableMessage(context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView price, Qty, unit, name, status;
        ImageView productImage, cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            unit = (TextView) itemView.findViewById(R.id.unit);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            Qty = (TextView) itemView.findViewById(R.id.Qty);
            status = (TextView) itemView.findViewById(R.id.status);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            cancel = (ImageView) itemView.findViewById(R.id.cancel);

        }
    }
}
