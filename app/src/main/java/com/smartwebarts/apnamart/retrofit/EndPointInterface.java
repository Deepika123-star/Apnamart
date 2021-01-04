package com.smartwebarts.apnamart.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

public interface EndPointInterface {

    @POST("API/api_login")
    @FormUrlEncoded
    Call<LoginData> login(@Field("mobile") String mobile,
                          @Field("password") String password);

    @POST("api.php")
    @FormUrlEncoded
    Call<OTPModel> otp(@Field("mobile") String mobile,
                       @Field("sms") String sms);

    @POST("api.php")
    @FormUrlEncoded
    Call<OrderedResponse> order(@Field("id") String id,
                                @Field("qty") String qty,
                                @Field("proId") String proId,
                                @Field("amount") String amount,
                                @Field("name") String name,
                                @Field("unit") String unit,
                                @Field("unit_in") String unit_in,
                                @Field("thumbnail") String thumbnail,
                                @Field("mobile") String mobile,
                                @Field("orderid") String orderid,
                                @Field("checkout") String checkout,
                                @Field("paymentmethod") String paymentmethod,
                                @Field("address") String address,
                                @Field("landmark") String landmark,
                                @Field("pincode") String pincode,
                                @Field("userdate") String userdate,
                                @Field("usertime") String usertime,
                                @Field("totalamount") String totalamount,
                                @Field("discount") String discount,
                                @Field("deliverycharge") String deliverycharge
                         );

    @POST("api.php")
    @FormUrlEncoded
    Call<OrderIdModel> orderid(@Field("getorderid") String getorderid);

    @POST("API/User_Signup")
    @FormUrlEncoded
    Call<SignUpModel> signup(@Field("fullname") String fullname,
                             @Field("email") String email,
                             @Field("mobile") String mobile,
                             @Field("custid") String custid,
                             @Field("password") String password);

    @POST("api.php")
    @FormUrlEncoded
    Call<RegisterSocialModel> signupsocialuser(@Field("socialregister") String socialregister,
                                               @Field("email") String email,
                                               @Field("mobile") String mobile,
                                               @Field("custid") String custid,
                                               @Field("name") String name);

    @POST("api.php")
    @FormUrlEncoded
    Call<SocialDataCheckModel> checkssocialuser(@Field("socialcheck") String socialcheck,
                                                @Field("email") String email);

    @GET("API/api_historyorders/{id}")
    Call<List<OrderListModel>> orderhistory(@Path("id") String id);


    @POST("api.php")
    @FormUrlEncoded
    Call<List<MyOrderModel>> TodayOrder(@Field("homepage") String homepage,
                                        @Field("id") String userid);


    @POST("api.php")
    @FormUrlEncoded
    Call<List<OrderDetailModel>> OrderDetails(@Field("orderdetails") String orderdetails,
                                              @Field("orderid") String orderid);

    @POST("api.php")
    @FormUrlEncoded
    Call<OrderUpdateModel> OrderUpdate(@Field("orderupdate") String orderupdate,
                                       @Field("orderid") String orderid);

    @POST("api.php")
    @FormUrlEncoded
    Call<List<MyOrderModel>> OrderHistory(@Field("orderhistory") String homepage,
                                          @Field("id") String userid);

    @GET("api.php")
    Call<MessageModel> addWallet(@Field("addwallet") String addwallet,
                                 @Field("amount") String amount,
                                 @Field("rpayid") String rpayid,
                                 @Field("id") String userid);

    @POST("api.php")
    @FormUrlEncoded
    Call<AddWishListResponse> addtowishlist(@Field("productid") String productid,
                                                  @Field("userid") String userid,
                                                  @Field("unitin") String unitin,
                                                  @Field("unit") String unit,
                                                  @Field("wishlist") String wishlist);

    @GET("API/api_categories")
    Call<List<CategoryModel>> categories();

    @GET("API/api_offers/")
    Call<List<SliderImageData>> imageSlider();

    @GET("API/api_homeproducts/{id}")
    Call<List<HomeProductsModel>> homeProducts(@Path("id") String id);

    @GET("API/api_getwallet/{id}")
    Call<List<AmountModel>> userWallet(@Path("id") String id);

    @GET("API/api_addwallet/{transactionid}/{amount}/{userid}/{usermobile}")
    Call<MessageModel> addwallet(@Path("transactionid") String transactionid,
                                 @Path("amount") String amount,
                                 @Path("userid") String userid,
                                 @Path("usermobile") String usermobile
    );

    @GET("API/api_getwishlist/{id}")
    Call<List<ProductModel>> apiGetwishlist(@Path("id") String id);

    @GET("API/api_searchproducts/{s}")
    Call<List<SearchModel>> search(@Path("s") String s);

    @GET("API/api_subcategory/{id}")
    Call<List<SubCategoryModel>> subCategory(@Path("id") String id);

    @GET("API/api_subsubcategory/{id}/{subid}")
    Call<List<SubSubCategoryModel>> subsubCategory(@Path("id") String id,
                                                   @Path("subid") String subid);

    @GET("API/api_subcategorywiseproduct/{catid}/{subcatid}")
    Call<List<ProductModel>> products(@Path("catid") String catid,
                                                   @Path("subcatid") String subcatid);

    @GET("API/api_vendorwiseproduct/{id}/")
    Call<List<ProductModel>> vendorwiseproduct(@Path("id") String id);

    @GET("API/api_gnewproduct/{id}/{subid}/{subsubid}")
    Call<List<ProductModel>> products(@Path("id") String id,
                                      @Path("subid") String subid,
                                      @Path("subsubid") String subsubid);

    @GET("API/api_specificproduct/{id}")
    Call<List<ProductDetailModel>> getProductDetails(@Path("id") String id);

    @GET("API/api_vendors")
    Call<List<VendorModel>> getVendors();

    @POST("API/api_timeslots")
    @FormUrlEncoded
    Call<List<TimeModel>> getTimeSlot(@Field("pincode") String pincode);

    @GET("API/api_deliverycharges/")
    Call<List<DeliveryChargesModel>> getDeliveryCharges();

    @GET("API/api_productimages/{id}")
    Call<List<ProductDetailImagesModel>> getProductImages(@Path("id") String id);

    @GET("API/api_useraddress/{id}")
    Call<List<AddressModel>> getAddress(@Path("id") String userid);

    @GET("API/api_getcouponcodes/{id}")
    Call<List<CouponModels>> getCouponCodes(@Path("id") String userid);

    @POST("api.php")
    @FormUrlEncoded
    Call<OrderIdModel> cancelorder(@Field("cancel") String s,
                                   @Field("orderid") String orderId,
                                   @Field("reason") String reason,
                                   @Field("mobile") String mobile);

    @POST("api.php")
    @FormUrlEncoded
    Call<OrderIdModel> setAddress(@Field("insertaddress") String insertaddress,
                                  @Field("userid") String userid,
                                  @Field("house_number") String house_number,
                                  @Field("landmark") String landmark,
                                  @Field("city") String city,
                                  @Field("pin_code") String pin_code,
                                  @Field("addr_type") String addr_type);


    @GET("API/returnproductbyid/{id}/{mob}")
    Call<MessageModel> returnProductByProId(@Path("id") String userid,
                                            @Path("mob") String mob);


    @POST("api.php")
    @FormUrlEncoded
    Call<OTPModel> changePassword(@Field("number") String number,
                                  @Field("newpassword") String newpassword,
                                  @Field("changepassword") String changepassword);


    @POST("api.php")
    @FormUrlEncoded
    Call<AddressModel> getaddress(@Field("getaddress") String getaddress,
                                  @Field("userid") String userid);

    @POST("api.php")
    @FormUrlEncoded
    Call<AddressModel> setAddress(@Field("insertaddress") String insertaddress,
                                  @Field("userid") String userid,
                                  @Field("address") String house_number,
                                  @Field("city") String city,
                                  @Field("landmark") String landmark,
                                  @Field("pin_code") String pin_code);

    @POST("api.php")
    @FormUrlEncoded
    Call<List<VersionModel>> version(@Field("colval") String colval);

}
