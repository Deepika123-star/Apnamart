package com.smartwebarts.apnamart.retrofit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.rampo.updatechecker.UpdateChecker;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.address.DeliveryProductDetails;
import com.smartwebarts.apnamart.dashboard.ui.home.SliderImageData;
import com.smartwebarts.apnamart.models.AddWishListResponse;
import com.smartwebarts.apnamart.models.AddressModel;
import com.smartwebarts.apnamart.models.AmountModel;
import com.smartwebarts.apnamart.models.CategoryModel;
import com.smartwebarts.apnamart.models.CouponModels;
import com.smartwebarts.apnamart.models.HomeProductsModel;
import com.smartwebarts.apnamart.models.MessageModel;
import com.smartwebarts.apnamart.models.MyOrderModel;
import com.smartwebarts.apnamart.models.OTPModel;
import com.smartwebarts.apnamart.models.OrderDetailModel;
import com.smartwebarts.apnamart.models.OrderIdModel;
import com.smartwebarts.apnamart.models.OrderListModel;
import com.smartwebarts.apnamart.models.OrderUpdateModel;
import com.smartwebarts.apnamart.models.OrderedResponse;
import com.smartwebarts.apnamart.models.ProductDetailImagesModel;
import com.smartwebarts.apnamart.models.ProductDetailModel;
import com.smartwebarts.apnamart.models.ProductModel;
import com.smartwebarts.apnamart.models.RegisterSocialModel;
import com.smartwebarts.apnamart.models.SignUpModel;
import com.smartwebarts.apnamart.models.SocialDataCheckModel;
import com.smartwebarts.apnamart.models.SubCategoryModel;
import com.smartwebarts.apnamart.models.SubSubCategoryModel;
import com.smartwebarts.apnamart.models.VendorModel;
import com.smartwebarts.apnamart.models.VersionModel;
import com.smartwebarts.apnamart.shared_preference.LoginData;
import com.smartwebarts.apnamart.utils.ApplicationConstants;
import com.smartwebarts.apnamart.BuildConfig;

import org.jetbrains.annotations.NotNull;

public enum UtilMethods {

    INSTANCE;

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void internetNotAvailableMessage(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog .setContentText("Internet Not Available");
        dialog.show();
    }

    public boolean isValidMobile(String mobile) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mobilePattern = "^(?:0091|\\\\+91|0)[7-9][0-9]{9}$";
        String mobileSecPattern = "[7-9][0-9]{9}$";

        if (mobile.matches(mobilePattern) || mobile.matches(mobileSecPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidEmail(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void Login(final Context context, String mobile, String password , final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<LoginData> call = git.login( mobile, password);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body().getId()!=null && !response.body().getId().isEmpty()) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail("Invalid UserId or Password");
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void socialcheck(final Context context, String email , final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<SocialDataCheckModel> call = git.checkssocialuser( "1", email);
            call.enqueue(new Callback<SocialDataCheckModel>() {
                @Override
                public void onResponse(Call<SocialDataCheckModel> call, Response<SocialDataCheckModel> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body().getMessage()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(""+response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId");
                   }
                }

                @Override
                public void onFailure(Call<SocialDataCheckModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void signupsocialuser(final Context context, String email, String mobile, String name, String custid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<RegisterSocialModel> call = git.signupsocialuser("1", email, mobile, custid, name);
            call.enqueue(new Callback<RegisterSocialModel>() {
                @Override
                public void onResponse(Call<RegisterSocialModel> call, Response<RegisterSocialModel> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body().getMessage()!=null && !response.body().getMessage().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(""+response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId");
                   }
                }

                @Override
                public void onFailure(Call<RegisterSocialModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail("Unable to signin. Try after some time");
            dialog.dismiss();
        }

    }

    public void otp(final Context context,  String mobile, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OTPModel> call = git.otp( mobile, "1");
            call.enqueue(new Callback<OTPModel>() {
                @Override
                public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<OTPModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void returnProductByProductID(final Context context, String productID, String mob, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<MessageModel> call = git.returnProductByProId( productID, mob);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getMsg()!=null && response.body().getMsg().equalsIgnoreCase("sucess") ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
//                            callBackResponse.fail("No orders");
                            callBackResponse.fail(response.body().getMsg());
                        }
                    } else {
//                        callBackResponse.fail("No orders");
                        callBackResponse.fail(response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void order(final Context context, DeliveryProductDetails data, String totalamount, String discount, String deliverycharge, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderedResponse> call = git.order(data.getId(),
                     data.getQty(), data.getProId(), data.getAmount(), data.getName(), data.getUnit(), data.getUnit_in()
            ,data.getThumbnail(), data.getMobile(), data.getOrderid(), "1", data.getPaymentmethod(), data.getAddress(),
                    data.getLandmark(), data.getPincode(), data.getUserdate(), data.getUsertime(), totalamount, discount, deliverycharge);
            call.enqueue(new Callback<OrderedResponse>() {
                @Override
                public void onResponse(Call<OrderedResponse> call, Response<OrderedResponse> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<OrderedResponse> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void signup(final Context context,  String fullname, String email,String mobile, String password, String custid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<SignUpModel> call = git.signup(fullname, email, mobile, custid, password);
            call.enqueue(new Callback<SignUpModel>() {
                @Override
                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().getMsg().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(response.body().getMsg());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<SignUpModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void TodayOrder(final Context context, String userid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<MyOrderModel>> call = git.TodayOrder("1", userid);
            call.enqueue(new Callback<List<MyOrderModel>>() {
                @Override
                public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No orders");
                        }
                    } else {
                        callBackResponse.fail("No orders");
                    }
                }

                @Override
                public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void orderhistory(final Context context, String userid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<OrderListModel>> call = git.orderhistory(userid);
            call.enqueue(new Callback<List<OrderListModel>>() {
                @Override
                public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No orders");
                        }
                    } else {
                        callBackResponse.fail("No orders");
                    }
                }

                @Override
                public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void OrderDetails(final Context context, String orderid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<OrderDetailModel>> call = git.OrderDetails("1", orderid);
            call.enqueue(new Callback<List<OrderDetailModel>>() {
                @Override
                public void onResponse(Call<List<OrderDetailModel>> call, Response<List<OrderDetailModel>> response) {

                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No orders");
                        }
                    } else {
                        callBackResponse.fail("No orders");
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDetailModel>> call, Throwable t) {
                    dialog.dismiss();
                    callBackResponse.fail(t.getMessage());
                }
            });

        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
        }

    }

    public void OrderUpdate(final Context context, String orderid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderUpdateModel> call = git.OrderUpdate("1", orderid);
            call.enqueue(new Callback<OrderUpdateModel>() {
                @Override
                public void onResponse(Call<OrderUpdateModel> call, Response<OrderUpdateModel> response) {

                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No orders");
                        }
                    } else {
                        callBackResponse.fail("No orders");
                    }
                }

                @Override
                public void onFailure(Call<OrderUpdateModel> call, Throwable t) {
                    dialog.dismiss();
                    callBackResponse.fail(t.getMessage());
                }
            });

        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
        }

    }

    public void OrderHistory(final Context context, String userid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<MyOrderModel>> call = git.OrderHistory("1", userid);
            call.enqueue(new Callback<List<MyOrderModel>>() {
                @Override
                public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No orders");
                        }
                    } else {
                        callBackResponse.fail("No orders");
                    }
                }

                @Override
                public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void addtowishlist(final Context context, String productid, String userid, String unitin, String unit, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddWishListResponse> call = git.addtowishlist(productid, userid, unitin, unit, "1");
            call.enqueue(new Callback<AddWishListResponse>() {
                @Override
                public void onResponse(Call<AddWishListResponse> call, Response<AddWishListResponse> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.isSuccessful()) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No Response");
                        }
                    } else {
                        callBackResponse.fail("No Response");
                    }
                }

                @Override
                public void onFailure(Call<AddWishListResponse> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void categories(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<CategoryModel>> call = git.categories();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void subCategories(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SubCategoryModel>> call = git.subCategory(id);
            call.enqueue(new Callback<List<SubCategoryModel>>() {
                @Override
                public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<SubCategoryModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void subCategories2(Context context, String id, final mCallBackResponse callBackResponse) {

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SubCategoryModel>> call = git.subCategory(id);
            call.enqueue(new Callback<List<SubCategoryModel>>() {
                @Override
                public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<SubCategoryModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
        }
    }


    public void imageSlider(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SliderImageData>> call = git.imageSlider();
            call.enqueue(new Callback<List<SliderImageData>>() {
                @Override
                public void onResponse(Call<List<SliderImageData>> call, Response<List<SliderImageData>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<SliderImageData>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void homeProducts(Context context, String id, final mCallBackResponse callBackResponse) {

//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.default_progress_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
//        DoubleBounce doubleBounce = new DoubleBounce();
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<HomeProductsModel>> call = git.homeProducts(id);
            call.enqueue(new Callback<List<HomeProductsModel>>() {
                @Override
                public void onResponse(Call<List<HomeProductsModel>> call, Response<List<HomeProductsModel>> response) {
//                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<HomeProductsModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
//            dialog.dismiss();
        }
    }

    public void subSubCategory(Context context, String id, String subid, final mCallBackResponse callBackResponse) {

//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.default_progress_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
//        DoubleBounce doubleBounce = new DoubleBounce();
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SubSubCategoryModel>> call = git.subsubCategory(id, subid);
            call.enqueue(new Callback<List<SubSubCategoryModel>>() {
                @Override
                public void onResponse(Call<List<SubSubCategoryModel>> call, Response<List<SubSubCategoryModel>> response) {
//                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<SubSubCategoryModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
//            dialog.dismiss();
        }
    }

    public void products(Context context, String id, String subid, String subsubid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductModel>> call = git.products(id, subid, subsubid);
            call.enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void products(Context context, String id, String subid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductModel>> call = git.products(id, subid);
            call.enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void getProductDetails(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductDetailModel>> call = git.getProductDetails(id);
            call.enqueue(new Callback<List<ProductDetailModel>>() {
                @Override
                public void onResponse(Call<List<ProductDetailModel>> call, Response<List<ProductDetailModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductDetailModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void getProductImages(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductDetailImagesModel>> call = git.getProductImages(id);
            call.enqueue(new Callback<List<ProductDetailImagesModel>>() {
                @Override
                public void onResponse(Call<List<ProductDetailImagesModel>> call, Response<List<ProductDetailImagesModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductDetailImagesModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void getWishlist(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductModel>> call = git.apiGetwishlist(id);
            call.enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void search(Context context, String search, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SearchModel>> call = git.search(search);
            call.enqueue(new Callback<List<SearchModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<SearchModel>> call, @NotNull Response<List<SearchModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<SearchModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void coupons(Context context, String uid, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<CouponModels>> call = git.getCouponCodes(uid);
            call.enqueue(new Callback<List<CouponModels>>() {
                @Override
                public void onResponse(@NotNull Call<List<CouponModels>> call, @NotNull Response<List<CouponModels>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No Coupons");
                        }
                    } else {
                        callBackResponse.fail("No Coupons");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<CouponModels>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void orderid(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderIdModel> call = git.orderid("1");
            call.enqueue(new Callback<OrderIdModel>() {
                @Override
                public void onResponse(Call<OrderIdModel> call, Response<OrderIdModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getOrderid()!=null && !response.body().getOrderid().isEmpty()) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("Unable to get order id");
                        }
                    } else {
                        callBackResponse.fail("Unable to get order id");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderIdModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void cancelOrder(Context context, String orderId, String reason, String mobile, mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderIdModel> call = git.cancelorder("1", orderId, reason, mobile);
            call.enqueue(new Callback<OrderIdModel>() {
                @Override
                public void onResponse(Call<OrderIdModel> call, Response<OrderIdModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getOrderid()!=null && !response.body().getOrderid().isEmpty()) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("Unable to get order id");
                        }
                    } else {
                        callBackResponse.fail("Unable to get order id");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderIdModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void addwallet(Context context, String userid, String amount, String rpayid, mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<MessageModel> call = git.addWallet("1", userid, amount, rpayid);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getMsg()!=null && response.body().getMsg().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("Failed");
                        }
                    } else {
                        callBackResponse.fail("Failed");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void userWallet(Context context, String id, final mCallBackResponse callBackResponse) {

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<AmountModel>> call = git.userWallet(id);
            call.enqueue(new Callback<List<AmountModel>>() {
                @Override
                public void onResponse(Call<List<AmountModel>> call, Response<List<AmountModel>> response) {
//                    dialog.dismiss();
                    if (response.body()!=null) {
                        if (response.body().size()>0 && !response.body().get(0).getAmount().isEmpty() ) {

                            String strResponse = new Gson().toJson(response.body().get(0));
                            Log.e("strResponse",strResponse);
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("0.00");
                        }
                    } else {
                        callBackResponse.fail("0.00");
                    }
                }

                @Override
                public void onFailure(Call<List<AmountModel>> call, Throwable t) {
                    callBackResponse.fail("0.00");
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail("0.00");
//            dialog.dismiss();
        }
    }

    public void addWallet(Context context, String transactionid, String amount, String userid, String usermobile, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<MessageModel> call = git.addwallet(transactionid, amount, userid, usermobile);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().getMsg() !=null && !response.body().getMsg().isEmpty() ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("0.00");
                        }
                    } else {
                        callBackResponse.fail("0.00");
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    callBackResponse.fail("0.00");
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail("0.00");
            dialog.dismiss();
        }
    }

    public void vendor(Context context, String newText, mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<VendorModel>> call = git.getVendors();
            call.enqueue(new Callback<List<VendorModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<VendorModel>> call, @NotNull Response<List<VendorModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<VendorModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void getTimeSlot(Context context, String pincode, mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<TimeModel>> call = git.getTimeSlot(pincode );
            call.enqueue(new Callback<List<TimeModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<TimeModel>> call, @NotNull Response<List<TimeModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {

                            if (response.body().get(0).getMessage()==null) {
                                callBackResponse.success("", strResponse);
                            }
                            else if (response.body().get(0).getMessage()!=null && response.body().get(0).getMessage().equalsIgnoreCase("Our service is not avaiable in this pincode.")) {
                                callBackResponse.fail("Our service is not avaiable in this pincode.");
                            } else {
                                callBackResponse.success("", strResponse);
                            }
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<TimeModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void getDeliveryCharges(Context context,  mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<DeliveryChargesModel>> call = git.getDeliveryCharges();
            call.enqueue(new Callback<List<DeliveryChargesModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<DeliveryChargesModel>> call, @NotNull Response<List<DeliveryChargesModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {

                            SharedPreferences sharedpreferences = context.getSharedPreferences(ApplicationConstants.INSTANCE.DELIVERY_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(ApplicationConstants.INSTANCE.DELIVERY_CHARGES, strResponse);
                            editor.apply();
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<DeliveryChargesModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void vendorwiseproducts(Context context, String id, mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ProductModel>> call = git.vendorwiseproduct(id);
            call.enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0 ) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void changePassword(final Context context, String number, String newpassword, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OTPModel> call = git.changePassword( number,newpassword, "1");
            call.enqueue(new Callback<OTPModel>() {
                @Override
                public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail(response.body().getMessage());
                        }
                    } else {
                        callBackResponse.fail("Invalid Mobile Number");
                    }
                }

                @Override
                public void onFailure(Call<OTPModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void getaddress(final Context context, String number, String newpassword, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddressModel> call = git.getaddress( number,newpassword);
            call.enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail(response.body().getMessage());
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<AddressModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void getaddress2(final Context context, String number, String newpassword, final mCallBackResponse callBackResponse) {

//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.default_progress_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
//        DoubleBounce doubleBounce = new DoubleBounce();
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddressModel> call = git.getaddress( number,newpassword);
            call.enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
//                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail(response.body().getMessage());
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<AddressModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
//            dialog.dismiss();
        }

    }


    public void setAddress(final Context context, String id, String area, String city, String house, String pin, String landmark, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddressModel> call = git.setAddress("1", id, house+", "+ area, city, landmark, pin);
            call.enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail(response.body().getMessage());
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<AddressModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }
    public void version(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<VersionModel>> call = git.version("user");
            call.enqueue(new Callback<List<VersionModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<VersionModel>> call, @NotNull Response<List<VersionModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0) {
//                            callbackresponse.success("", strresponse);
                            if (response.body().get(0).getVcode() > BuildConfig.VERSION_CODE) {
                                UpdateDialog(context);
                            }
                        }
                        else {
//                            callBackResponse.fail("No data");
                        }
                    } else {
//                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<VersionModel>> call, @NotNull Throwable t) {
//                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void UpdateDialog(final Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.update_available, null);

        TextView tvLater = (TextView) view.findViewById(R.id.tv_later);
        Button tvOk = (Button) view.findViewById(R.id.tv_ok);

        final Dialog dialog = new Dialog(context);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarket(context);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static void goToMarket(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.ROOT_PLAY_STORE_DEVICE + mContext.getPackageName())));
    }
}
