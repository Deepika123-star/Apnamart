package com.smartwebarts.apnamart.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.smartwebarts.apnamart.productlist.ProductDetailActivity;

public class SaveProductList extends AsyncTask<String, String, String> {

    private List<Task> Tasks;
    private Context context;

    public SaveProductList(Context context, List<Task> Tasks) {
        this.Tasks = Tasks;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        DatabaseClient.getmInstance(context)
                .getAppDatabase()
                .taskDao()
                .insert(Tasks);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Toast.makeText(context, "Added to bag", Toast.LENGTH_SHORT).show();
        if (context instanceof ProductDetailActivity) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog .setContentText("Added to Basket");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismissWithAnimation();
                }
            });
            dialog.show();
        }
    }
}
