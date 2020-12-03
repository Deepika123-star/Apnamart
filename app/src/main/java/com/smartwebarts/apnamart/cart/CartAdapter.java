package com.smartwebarts.apnamart.cart;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.database.DatabaseClient;
import com.smartwebarts.apnamart.database.SaveProductList;
import com.smartwebarts.apnamart.database.Task;
import com.smartwebarts.apnamart.models.CouponModels;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.utils.MyGlide;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    CartActivity activity;
    ArrayList<Task> list;
    HashMap<String, CouponModels> map;
    HashMap<String, String> pricemap;

    public CartAdapter(CartActivity activity, ArrayList<Task> list) {
        this.activity = activity;
        this.list = list;
        map = new HashMap<>();
        pricemap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            pricemap.put(list.get(i).getId(), list.get(i).getPrice());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_cart_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        MyGlide.with(activity,ApplicationConstants.INSTANCE.PRODUCT_IMAGES+ list.get(position).getThumbnail(),holder.productImage);
        holder.tvProductName.setText(list.get(position).getName());
        holder.tvUnit.setText(list.get(position).getUnit()+list.get(position).getUnitIn());
        holder.tvCurrentPrice.setText(activity.getString(R.string.currency)+" "+list.get(position).getCurrentprice()+" X "+list.get(position).getQuantity());
        holder.txtQuan.setText(list.get(position).getQuantity());
        try {
            double price = Double.parseDouble("0"+list.get(position).getCurrentprice());
            double qty = Double.parseDouble("0"+list.get(position).getQuantity());
            double total = price*qty;
            holder.tvTotalPrice.setText(" "+total);

        } catch (Exception ignored){

        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(activity, list.get(position));
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt("0"+list.get(position).getQuantity()) + 1;
                Task task = list.get(position);
                task.setQuantity(""+qty);

                int p = Integer.parseInt("0"+task.getPrice().trim().replaceAll("[^0-9]", ""));
                int f = p*qty;
                task.setPrice(""+f);

                holder.addToBag(task);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt("0"+list.get(position).getQuantity());

                if (qty>1){
                    --qty;
                    Task task = list.get(position);
                    task.setQuantity(""+qty);

                    int p = Integer.parseInt("0"+task.getPrice());
                    int f = p*qty;
                    task.setPrice(""+f);

                    holder.addToBag(task);
                }

            }
        });

        if (map.get(list.get(position).getId())!=null) {
            changeLayout(holder, position, list.get(position), map, map.get(list.get(position).getId()));
        }

        holder.btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilMethods.INSTANCE.isNetworkAvialable(activity)) {

                    UtilMethods.INSTANCE.coupons(activity, list.get(position).getId(), new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {

                            Type type = new TypeToken<List<CouponModels>>(){}.getType();
                            List<CouponModels> couponList = new Gson().fromJson(message, type);
                            showDialog(holder, position, list.get(position), couponList);
                        }

                        @Override
                        public void fail(String from) {
                            new SweetAlertDialog(activity)
                                    .setTitleText("No Coupons")
                                    .setContentText("No coupons available for this item")
                                    .show();

//                            holder.btnApplyCoupon.setVisibility(View.GONE);
                            holder.btnApplyCoupon.setText("NO COUPONS");
                            holder.btnApplyCoupon.setEnabled(false);
                            holder.offerprice.setVisibility(View.GONE);
                            holder.discount.setVisibility(View.GONE);
                        }
                    });
                } else {
                    UtilMethods.INSTANCE.internetNotAvailableMessage(activity);
                }
            }
        });

    }

    private void showDialog(MyViewHolder holder, int position, Task task, List<CouponModels> couponList) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle(couponList.size() + " Coupon Available");
        View view = LayoutInflater.from(activity).inflate(R.layout.coupon_item, null);
        ListView listView = view.findViewById(R.id.list);

        ArrayAdapter<CouponModels> itemsAdapter =
                new ArrayAdapter<CouponModels>(activity, android.R.layout.simple_list_item_1, couponList);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map.put(task.getId(), itemsAdapter.getItem(position));
                holder.addToBag(list.get(position));
                dialog.dismiss();
                changeLayout(holder, position, task, map, itemsAdapter.getItem(position));
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void changeLayout(MyViewHolder holder, int position, Task task, HashMap<String, CouponModels> map, CouponModels item) {

        holder.btnApplyCoupon.setText(item.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.btnApplyCoupon.setBackgroundColor(activity.getColor(R.color.bg_green));
        }
        holder.tvCurrentPrice.setPaintFlags(holder.tvCurrentPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvTotalPrice.setPaintFlags(holder.tvTotalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        int discount = 0;
        try {
            discount = Integer.parseInt("0"+item.getCouponValue().trim().replaceAll("[^0-9]", ""));
            double total = Double.parseDouble(holder.tvTotalPrice.getText().toString().trim());
            double cal = total * discount / 100;


            if (cal > 50) {
                total = (int) (total - 50);
                holder.offerprice.setText(activity.getString(R.string.currency) + total);
                holder.discount.setText(activity.getString(R.string.currency) + "50" + "off");
            } else {
                total = (int) (total - cal);
                holder.offerprice.setText(activity.getString(R.string.currency) + total);
                holder.discount.setText("" + item.getCouponValue() + "% off");
            }

            holder.discount.setVisibility(View.VISIBLE);
            holder.offerprice.setVisibility(View.VISIBLE);

//            holder.addToBag(task);
        } catch (Exception ignored){}

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvCurrentPrice, tvTotalPrice, tvUnit, offerprice, discount;
        TextView minus, plus, txtQuan;
        ImageView ivDelete, productImage;
        Button btnApplyCoupon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvCurrentPrice = (TextView) itemView.findViewById(R.id.tvCurrentPrice);
            tvTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
            tvUnit = (TextView) itemView.findViewById(R.id.tvUnit);

            minus = (TextView) itemView.findViewById(R.id.minus);
            plus = (TextView) itemView.findViewById(R.id.plus);
            txtQuan = (TextView) itemView.findViewById(R.id.txtQuan);

            offerprice = (TextView) itemView.findViewById(R.id.offerprice);
            discount = (TextView) itemView.findViewById(R.id.discount);

            productImage = (ImageView) itemView.findViewById(R.id.productimage);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);

            btnApplyCoupon = (Button) itemView.findViewById(R.id.btncoupon);
        }

        public void addToBag(Task task) {
            List<Task> items = new ArrayList<>();
            items.add(task);
            new SaveProductList(activity,items).execute();
            (activity).getCartList(activity, map);
        }
    }

    public void updateList(ArrayList<Task> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public  void delete(final CartActivity activity, final Task task) {

        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                DatabaseClient.getmInstance(activity.getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {
                activity.getCartList(activity, map);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}
