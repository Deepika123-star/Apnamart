package com.smartwebarts.apnamart.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.cart.CartActivity;
import com.smartwebarts.apnamart.dashboard.DashboardActivity;
import com.smartwebarts.apnamart.database.DatabaseClient;
import com.smartwebarts.apnamart.database.Task;
import com.smartwebarts.apnamart.models.AmountModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.search.SearchActivity;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.shopbycategory.ShopByCategoryActivity;
import com.smartwebarts.apnamart.wallet.WalletActivity;

public enum Toolbar_Set {

    INSTANCE;

    public void setToolbar(final Activity activity){
        ImageView back = activity.findViewById(R.id.back);
        ImageView refresh = activity.findViewById(R.id.refresh);
        FrameLayout showCart = activity.findViewById(R.id.showCart);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(activity);
            }
        });

        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCart(activity);
            }
        });

        getCartList(activity);
    }

    public void setToolbar(final Activity activity, String name){
        ImageView back = activity.findViewById(R.id.back);
//        ImageView refresh = activity.findViewById(R.id.refresh);
        FrameLayout showCart = activity.findViewById(R.id.showCart);
        TextView tvName = activity.findViewById(R.id.tv_name);
        tvName.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

//        refresh.setOnClickListener(v -> refresh(activity));
        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toolbar_Set.this.showCart(activity);
            }
        });
        getCartList(activity);
    }

    public void getCartList(final Activity activity) {

        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                List<Task> tasks= DatabaseClient.getmInstance(activity.getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return new ArrayList<>(tasks);
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {
                int size = tasks!=null?tasks.size():0;
                TextView cartItemsCount = activity.findViewById(R.id.cartItemsCount);
                if (cartItemsCount != null) {
                    cartItemsCount.setText(""+size);
                }

                TextView cart_badge = activity.findViewById(R.id.cart_badge);
                if (cart_badge !=null) {
                    cart_badge.setText(""+size);
                }

                try{

                    double sum = 0;
                    for (Task task: tasks) {
                        double price = Double.parseDouble("0"+(task.getCurrentprice()==null?"":task.getCurrentprice()));
                        double qty = Double.parseDouble("0"+task.getQuantity());
                        double total = price*qty;
                        sum+=total;
                    }

                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);
                    TextView utility = activity.findViewById(R.id.utility);

                    if (activity instanceof CartActivity) {
                        if (utility != null) {
                            utility.setVisibility(View.GONE);
                        }
                        return;
                    }
                    if (utility!=null) {
                        utility.setText(activity.getString(R.string.currency) + " "+ nf.format(sum));
                        utility.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                userWallet(activity);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void showCart(Activity activity){
        Intent intent = new Intent(activity, CartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public void refresh(Activity activity) {
        Toolbar_Set.INSTANCE.getCartList(activity);
    }


    public  void delete(final Activity activity, final Task task) {

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
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void setBottomNav(final Activity activity) {
        final BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);

        BottomNavigationMenuView mbottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        View view = mbottomNavigationMenuView.getChildAt(3);

        BottomNavigationItemView itemView = (BottomNavigationItemView) view;

        View cart_badge = LayoutInflater.from(activity)
                .inflate(R.layout.view_alertsbadge,
                        mbottomNavigationMenuView, false);

        itemView.addView(cart_badge);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    if (!(activity instanceof DashboardActivity)) {
                        activity.startActivity(new Intent(activity, DashboardActivity.class));
                        activity.finishAffinity();
                    }
                } else if (item.getItemId() == R.id.search) {
                    if (!(activity instanceof SearchActivity)) {
                        activity.startActivity(new Intent(activity, SearchActivity.class));
                    }
                } else if (item.getItemId() == R.id.cart) {
                    if (!(activity instanceof CartActivity)) {
                        activity.startActivity(new Intent(activity, CartActivity.class));
                    }
                } else if (item.getItemId() == R.id.category) {
                    if (!(activity instanceof ShopByCategoryActivity)) {
                        activity.startActivity(new Intent(activity, ShopByCategoryActivity.class));
                    }
                }

                bottomNavigationView.setSelected(false);
                return false;
            }
        });
    }


    public void userWallet(Activity activity) {

        AppSharedPreferences preferences = new AppSharedPreferences(activity.getApplication());
        if (preferences.getLoginUserLoginId() !=null ){
            TextView utility = activity.findViewById(R.id.utility);
            if (UtilMethods.INSTANCE.isNetworkAvialable(activity)) {
                UtilMethods.INSTANCE.userWallet(activity, preferences.getLoginUserLoginId(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        AmountModel amountModel = new Gson().fromJson(message, AmountModel.class);

//                        if (utility!=null) {
//                            utility.setText(activity.getString(R.string.currency) + " "+ amountModel.getAmount());
//                            utility.setVisibility(View.VISIBLE);
//                        }

                        if (activity instanceof WalletActivity) {
                            TextView Wallet_Ammount = (TextView) activity.findViewById(R.id.wallet_ammount);
                            Wallet_Ammount.setText(activity.getString(R.string.currency) + " "+ amountModel.getAmount());
                        }
                    }

                    @Override
                    public void fail(String from) {

                    }
                });
            }
        }
    }
}
