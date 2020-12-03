package com.smartwebarts.apnamart.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.SignInActivity;
import com.smartwebarts.apnamart.address.AddressActivity;
import com.smartwebarts.apnamart.dashboard.DashboardActivity;
import com.smartwebarts.apnamart.database.DatabaseClient;
import com.smartwebarts.apnamart.database.Task;
import com.smartwebarts.apnamart.models.CouponModels;
import com.smartwebarts.apnamart.retrofit.DeliveryChargesModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvFinalPrice;
    private ArrayList<Task> list = new ArrayList<>();
    private LinearLayout llNormal, llNoCart;
    HashMap<String, String> hashMap = new HashMap<>();
    private List<DeliveryChargesModel> chargesModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        tvFinalPrice = findViewById(R.id.tvFinalPrice);
        llNormal = findViewById(R.id.ll_normal);
        llNoCart = findViewById(R.id.ll_nocart);

        Toolbar_Set.INSTANCE.setToolbar(this, "My Basket");

        adapter = new CartAdapter(this, list);
        recyclerView.setAdapter(adapter);

        getCartList(this, null);
    }

    public void getCartList(final Activity activity, HashMap<String, CouponModels> map) {

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
                Comparator<Task> comparator = new Comparator<Task>() {
                    @Override
                    public int compare(Task left, Task right) {
                        int intLeft = Integer.parseInt("0"+left.getId());
                        int intRight = Integer.parseInt("0"+right.getId());
                        return intLeft - intRight; // use your logic
                    }
                };
                Collections.sort(tasks, comparator);

                list = tasks;
                adapter.updateList(list);
                upDateUI(list.size());

                int size = tasks!=null?tasks.size():0;
                TextView cartItemsCount = activity.findViewById(R.id.cartItemsCount);
                cartItemsCount.setText(""+size);

                double sum = 0.00;

                for (Task task: tasks) {
                    double price = Double.parseDouble("0"+(task.getCurrentprice()==null?"":task.getCurrentprice()));
                    double qty = Double.parseDouble("0"+task.getQuantity());
                    double total = price*qty;

                    if (map != null && map.get(task.getId())!=null) {
                        CouponModels model = map.get(task.getId());

                        int discount = 0;
                        try {
                            discount = Integer.parseInt("0"+model.getCouponValue().trim().replaceAll("[^0-9]", ""));
                            double cal = total * discount / 100;

                            if (cal > 50) {
                                total = (int) (total - 50);
                            } else {
                                total = (int) (total - cal);
                            }

                            hashMap.put(task.getId(), ""+total);
                        } catch (Exception ignored){}
                    }
                    sum+=total;
                }

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                tvFinalPrice.setText(""+nf.format(sum));

                if (sum == 0) {
                    findViewById(R.id.button).setVisibility(View.GONE);
                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void proceedToCheckout(View view) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.getDeliveryCharges(this, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type type = new TypeToken<List<DeliveryChargesModel>>(){}.getType();
                    chargesModelList = new Gson().fromJson(message, type);
                    addDeliveryCharges();
                }

                @Override
                public void fail(String from) {

                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    private void addDeliveryCharges() {
        int total = Integer.parseInt(tvFinalPrice.getText().toString().trim().replaceAll("[^0-9]", ""));

        if (total<=0) {
            Toast.makeText(this, "Add Items to Cart", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chargesModelList !=null && chargesModelList.size()>0) {

            for (DeliveryChargesModel model : chargesModelList) {
                int min = Integer.parseInt("0"+model.getMinAmt());
                int max = Integer.parseInt("0"+model.getMaxAmt());
                int deliveryCharges = Integer.parseInt("0"+model.getAmt());

                if (total>min && total <max) {

                    if (deliveryCharges!=0) {
                        AlertDialog dialog = new AlertDialog.Builder(this).create();
                        dialog.setTitle("Delivery Charges");
                        dialog.setMessage("Your Basket has less than "+getString(R.string.currency)+ max+" So "+getString(R.string.currency)+deliveryCharges+" delivery charges will be applied. Click OK to proceed");
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                proceed();
                            }
                        });
                        dialog.show();
                    } else {
                        proceed();
                    }

                    break;
                }
            }
        }

    }

    private void proceed() {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String user = preferences.getLoginUserName();

        if (user!=null && !user.trim().isEmpty())
        {
            checkPermission();
        } else {
            SweetAlertDialog sad = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sad.setTitleText("You are not logged in");
            sad.setContentText("Login to Continue");
            sad.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent intent = new Intent(CartActivity.this, SignInActivity.class);
                    intent.putExtra("cart", true);
                    startActivity(intent);
                    sad.dismiss();
                }
            });
            sad.show();
        }
    }

    private void checkPermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // You can use the API that requires the permission.
            openNextActivity();
        }  else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void openNextActivity() {
        System.out.println(hashMap);
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra(AddressActivity.PRODUCT_LIST, list);
        intent.putExtra(AddressActivity.HASHMAP, hashMap);
        int total = Integer.parseInt(tvFinalPrice.getText().toString().trim().replaceAll("[^0-9]", ""));

        for (DeliveryChargesModel model : chargesModelList) {
            int min = Integer.parseInt("0"+model.getMinAmt());
            int max = Integer.parseInt("0"+model.getMaxAmt());
            int deliveryCharges = Integer.parseInt("0"+model.getAmt());

            if (total>min && total <max) {
                total = total + deliveryCharges;
                break;
            }
        }
        intent.putExtra(AddressActivity.AMOUNT, ""+total);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toolbar_Set.INSTANCE.getCartList(this);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openNextActivity();
                }  else {
                    openNextActivity();
                }
                return;
        }
    }

    public void upDateUI(int size) {
        if (size > 0) {
            llNormal.setVisibility(View.VISIBLE);
            llNoCart.setVisibility(View.GONE);
        } else {
            llNormal.setVisibility(View.GONE);
            llNoCart.setVisibility(View.VISIBLE);
        }
    }

    public void shopNow(View view) {

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
