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

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryAdapter2 extends RecyclerView.Adapter<HistoryAdapter2.MyViewHolder> {
    private Context context;
    private HashMap<String, List<OrderListModel>> map;
    private List<String> list;

    public HistoryAdapter2(Context context, HashMap<String, List<OrderListModel>> map, List<String> list) {
        this.context = context;
        this.map = map;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.orderID.setText("Order ID :\n" + list.get(position).trim());
        holder.paymentMethod.setText("Payment Method :" +map.get(list.get(position)).get(0).getPaymentmethod());
        holder.orderadte.setText("Order Date :\n" +map.get(list.get(position)).get(0).getOrderdate());
        holder.totalAmount.setText("Total Amount :" +map.get(list.get(position)).get(0).getTotalamount());
        holder.userdatetime.setText("Reaching Date & Time :" +map.get(list.get(position)).get(0).getUserdate()+" "+map.get(list.get(position)).get(0).getUsertime());
        OrderDetailAdapter adapter = new OrderDetailAdapter(context, map.get(list.get(position)));
        holder.recyclerView.setAdapter(adapter);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(list.get(position));
            }
        });
    }

    private void showDialog(String orderListModel) {

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

    private void showReasonDialog(String orderListModel) {

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

    private void cancelOrder(String orderListModel, String reason) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

            AppSharedPreferences preferences = new AppSharedPreferences(((MyHistoryActivity) context).getApplication());
            String mobile = preferences.getLoginMobile();

            UtilMethods.INSTANCE.cancelOrder(context, orderListModel, reason, mobile, new mCallBackResponse() {
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
        return map!=null?map.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView delete;
        private TextView paymentMethod, orderadte, orderID, totalAmount, userdatetime;
        private Button status;
        private RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.delete);
            orderID = itemView.findViewById(R.id.orderId);
            paymentMethod = itemView.findViewById(R.id.paymentmethod);
            totalAmount = itemView.findViewById(R.id.totalamount);
            orderadte = itemView.findViewById(R.id.orderdate);
            status = itemView.findViewById(R.id.status);
            userdatetime = itemView.findViewById(R.id.userdatetime);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    public void setList(HashMap<String, List<OrderListModel>> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
