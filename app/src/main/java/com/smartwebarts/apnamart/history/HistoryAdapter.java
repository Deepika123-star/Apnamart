package com.smartwebarts.apnamart.history;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.OrderListModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context context;
    private List<OrderListModel> list;

    public HistoryAdapter(Context context, List<OrderListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyGlide.with(context, ApplicationConstants.INSTANCE.PRODUCT_IMAGES + list.get(position).getThumbnail(), holder.productPic);
        holder.orderID.setText("Order ID :\n" + list.get(position).getOrderId().trim());
        holder.paymentMethod.setText(list.get(position).getPaymentmethod().trim());
        holder.orderadte.setText(list.get(position).getOrderdate().trim());
        holder.productName.setText(list.get(position).getName());
        holder.unit.setText(list.get(position).getUnit() + list.get(position).getUnitIn());
        holder.status.setText(list.get(position).getStatus());
        if (list.get(position).getStatus().equalsIgnoreCase("Cancelled")) {
            holder.delete.setVisibility(View.GONE);
        }
        holder.amount.setText(context.getString(R.string.currency) + " "+list.get(position).getAmount());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(list.get(position));
            }
        });

        holder.qty.setText("QTY : "+list.get(position).getQuantity());
    }

    private void showDialog(OrderListModel orderListModel) {

        SweetAlertDialog sad = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sad.setTitleText("Are you sure to cancel order?");
        sad.setContentText("Order will be cancelled");
        sad.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                showReasonDialog(orderListModel);
                sad.dismiss();
            }
        });
        sad.setCancelButton("Not Now", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sad.dismiss();
            }
        });
        sad.show();
    }

    private void showReasonDialog(OrderListModel orderListModel) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_request, null);
        Spinner spinReasons = dialogview.findViewById(R.id.spinReasons);
        EditText etReasons = dialogview.findViewById(R.id.etReasons);

        spinReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    etReasons.setText("");
                } else {
                    etReasons.setText(spinReasons.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setView(dialogview);
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String reason = spinReasons.getSelectedItem().toString();
                if (reason.equalsIgnoreCase(context.getResources().getStringArray(R.array.reasonsforcancellation)[0])) {
                    reason = etReasons.getText().toString();
                    if (reason.isEmpty()) {
                        etReasons.setError("Enter a reason for cancellation");
                    } else {
                        cancelOrder(orderListModel, reason);
                    }
                } else {
                    cancelOrder(orderListModel, spinReasons.getSelectedItem().toString());
                }

                dialog.dismiss();
            }
        });
    }

    private void cancelOrder(OrderListModel orderListModel, String reason) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

            AppSharedPreferences preferences = new AppSharedPreferences(((MyHistoryActivity) context).getApplication());
            String mobile = preferences.getLoginMobile();

            UtilMethods.INSTANCE.cancelOrder(context, orderListModel.getOrderId(), reason, mobile, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    ((MyHistoryActivity) context).setOrderData();
                }

                @Override
                public void fail(String from) {

                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }


    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView productPic, delete;
        private TextView productName, orderID;
        private EditText unit, paymentMethod, orderadte;
        private Button status;
        private TextView amount, qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productPic = itemView.findViewById(R.id.productPic);
            delete = itemView.findViewById(R.id.delete);
            productName = itemView.findViewById(R.id.productName);
            orderID = itemView.findViewById(R.id.orderId);
            unit = itemView.findViewById(R.id.unit);
            paymentMethod = itemView.findViewById(R.id.paymentmethod);
            orderadte = itemView.findViewById(R.id.orderdate);
            status = itemView.findViewById(R.id.status);
            amount = itemView.findViewById(R.id.amount);
            qty = itemView.findViewById(R.id.qty);
        }
    }

    public void setList(List<OrderListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
