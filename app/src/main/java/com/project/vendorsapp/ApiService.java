package com.project.vendorsapp;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

   /* @FormUrlEncoded
    @POST(" api/Business/PostVendor")
    Call<ResponseBody> PostVendor(@Field("BusinessName") String BusinessName, @Field("OwnerName") String OwnerName, @Field("MobileNo") String MobileNo, @Field("AltMobileNo")String AltMobileNo,
                                  @Field("SellerTypeID")int SellerTypeID, @Field("BusinessTANNo") String BusinessTANNo, @Field("BusinessMIDNo")String BusinessMIDNo, @Field("BusinessTIDNo") String BusinessTIDNo,
                                  @Field("BusinessGSTNo") String BusinessGSTNo, @Field("AddressLine1") String AddressLine1, @Field("AddressLine2") String AddressLine2, @Field("StateID") int StateID, @Field("aliascategoryid") int aliascategoryid,
                                  @Field("CityID") int CityID, @Field("CountryID")int CountryID, @Field("Zipcode")String Zipcode, @Field("CategoryID")int CategoryID, @Field("GeoLongitude") String GeoLongitude, @Field("GeoLatitude")String GeoLatitude,
                                  @Field("Avatar") JSONObject Avatar, @Field("BranchID") int BranchID, @Field("MobileDeviceID")String MobileDeviceID, @Field("MapGeoLocationID")String MapGeoLocationID, @Field("Documents") JSONArray file, @Field("MemoryPlanID") int MemoryPlanID,
                                  @Field("SmsPlanID") int SmsPlanID, @Field("AccountTypeID")int AccountTypeID, @Field("PaymentTypeID") int PaymentTypeID, @Field("SalesManID")int SalesManID, @Field("Amount") double Amount, @Field("EconomyPaymentType") int EconomyPaymentType);*/

   @POST("api/Business/PostVendor")
   Call<ResponseBody> PostVendor(@Body JsonObject object);

    @POST("api/Token/Login?")
    Call<ResponseBody> Tokenlogin(@Query("mobileno") String mobileno,
                                  @Query("password") String password,
                                  @Query("deviceid") String deviceid);


    @POST("api/Business/PostStoreHolidays?")
    Call<ResponseBody> PostStoreHolidays(@Query("holiday") String holiday,
                                  @Query("vendorid") String vendorid,
                                  @Query("branchid") String branchid,
                                  @Query("description") String description );


    @POST("api/Business/UpdateAccountDetails?")
    Call<ResponseBody> UpdateAccountDetails(@Query("accno") String accno,
                                         @Query("branchid") String branchid,
                                         @Query("holdername") String holdername,
                                         @Query("bank") String bank,
                                          @Query("ifsccode") String ifsccode );




    @POST("api/Grocerry/PostGrocVendorServices")
    Call<ResponseBody> PostGrocVendorServices(@Body JsonObject xobject);

    @POST("api/Admin/ChangeVendorPassword")
    @FormUrlEncoded
    Call<ResponseBody> ChangePassword(@Field("mobileno") String mobileno,@Field("VendorId") String VendorId,@Field("branchId") String branchId,@Field("password") String password);

    @GET("api/Business/GetStoreMessages?")
    Call<ResponseBody> GetStoreMessages(@Query("vendorid") String vendorid,
                                  @Query("branchid") String branchid);

    @GET("api/Business/GetAccountDetails?")
    Call<ResponseBody> GetAccountDetails(@Query("branchid") String branchid);


    @GET("api/Grocerry/GetGrocVendorServices?")
    Call<ResponseBody> GetGrocVendorServices(@Query("vendorid") String vendorid,
                                        @Query("branchid") String branchid);

    @GET("api/Business/GetSettingInfo?")
    Call<ResponseBody> GetSettingInfo(@Query("vendorid") String vendorid,
                                             @Query("branchid") String branchid);


    @GET("api/Grocerry/GetGrocServices")
    Call<ResponseBody> GetGrocServices();

    @GET("api/Business/GetRoles?")
    Call<ResponseBody> GetRoles(@Query("categoryid") String categoryid);

    @GET("api/Business/GetStaff?")
    Call<ResponseBody> GetStaff(@Query("vendorid") String vendorid,@Query("branchid")String branchid);

    @GET("api/Grocerry/GetDeliveryDistance")
    Call<ResponseBody> GetDeliveryDistance();

    @POST("api/Business/PostStoreMessages?")
    Call<ResponseBody> PostStoreMessages(@Query("message") String message,
                                        @Query("vendorid") String vendorid,
                                        @Query("branchid") String branchid);

    @POST("api/Business/PostStaff")
    Call<ResponseBody> PostStaff(@Body JsonObject object);

    @PUT("api/Business/UpdateStoreMessages?")
    Call<ResponseBody> UpdateStoreMessages(@Query("messageid") String messageid,
                                           @Query("currentMsgid") String currentMsgid);

    @PUT("api/Business/UpdateVendorGST?")
    Call<ResponseBody> UpdateVendorGST(@Query("gst") boolean gst,
                                           @Query("gstpercent") String gstpercent,@Query("gstno") String gstno,
                                       @Query("vendorid") String vendorid,@Query("branchid") String branchid);
    @PUT("api/Business/UpdateVendorDiscount?")
    Call<ResponseBody> UpdateVendorDiscount(@Query("hasdiscoumt") boolean hasdiscoumt,
                                       @Query("discountprice") String discountprice,@Query("discountpercent") String discountpercent,
                                       @Query("vendorid") String vendorid,@Query("branchid") String branchid);

    @PUT("api/Business/GrocStoreResponse?")
    Call<ResponseBody> GrocStoreResponse(@Query("response") String response,
                                           @Query("branchid") String branchid);

    @PUT("api/Grocerry/DeleteVendorService?")
    Call<ResponseBody> DeleteVendorService(@Query("serviceid") String serviceid);

    @DELETE("api/Business/DeleteStoreMessages?")
    Call<ResponseBody> DeleteStoreMessages(@Query("messageid") String messageid);

@FormUrlEncoded
 @POST("api/Business/PostVendorSettings")
 Call<ResponseBody> PostVendorSettings(@Field("VendorID") int VendorID,@Field("BranchId")int BranchId,@Field("Email")String Email,@Field("OpenTime") String OpenTime,@Field("CloseTime") String CloseTime,@Field("WorkingDays") int WorkingDays,@Field("Currency") String Currency,@Field("HaveBranches") boolean HaveBranches);

    @GET("api/Business/LoginUser?")
    Call<ResponseBody> LoginUser( @Header("Authorization") String Authorization,
                                  @Query("mobileno") String mobileno,
                                  @Query("pwd") String password);


    @GET("api/Admin/GetvendorStores?")
    Call<ResponseBody> MolsExtended(@Query("MobileNo") String MobileNo);

    @GET("api/Business/Categories")
    Call<ResponseBody> Categories();

    @GET("api/Business/GetSalesPersons")
    Call<ResponseBody> GetSalesPersons();

    @GET("api/Business/GetSellerType")
    Call<ResponseBody> GetSellerType();

    @GET(" api/Business/States?")
    Call<ResponseBody> GetStates(@Query("CountryID") String CountryID);


    @GET(" api/Business/Cities?")
    Call<ResponseBody> GetCities(@Query("stateID") String stateID);

    @GET("api/User/SendOTP?")
    Call<ResponseBody> SendOTP (@Query("mobileno") String mobileno);

    @GET("api/User/VerifyOTP?")
    Call<ResponseBody> VerifyOTP (@Query("mobileno") String mobileno,@Query("otpCode") String otpCode);

    @GET("api/Grocerry/GetOrders?")
    Call<ResponseBody> GetOrders( @Query("vendorid") String vendorid,
                                  @Query("BranchID") String BranchID);


    @GET("api/Grocerry/GetOrderDetails?")
    Call<ResponseBody> GetOrderDetails( @Query("orderid") String orderid);



    @PUT("api/Grocerry/GrocOrderStatus?")
    Call<ResponseBody> GrocOrderStatus(@Query("orderid") String orderid,
                                       @Query("statusid") String statusid,
                                       @Query("vendorid") String vendorid,
                                       @Query("BranchID") String BranchID);
  /* @FormUrlEncoded
    @POST("api/VendorAdmin/ChangePassword")
    Call<ResponseBody> ChangePassword (@Field("VendorId")String VendorId,
                                       @Field("BranchId")String BranchId,
                                       @Field("MobileNo")String MobileNo);*/

    @FormUrlEncoded
    @POST("api/VendorAdmin/ForgotPassword")
    Call<ResponseBody> ForgotPassword(@Field("VendorId")String VendorId,
                                       @Field("BranchId")String BranchId,
                                       @Field("MobileNo")String MobileNo);

    @POST("api/Grocerry/PostDeliveryCharges?")
    Call<ResponseBody> PostDeliveryCharges(@Query("branchid") String branchid,@Query("distanceid") String distanceid,@Query("charges") String charges);



    @GET("api/VendorAdmin/GetStaffByCategory?")
    Call<ResponseBody> GetStaffByCategory(@Query("VendorID") String VendorID,
                                         @Query("BranchID") String BranchID,
                                         @Query("CategoryName") String CategoryName);


    @FormUrlEncoded
    @POST("api/VendorAdmin/AssignStaffForDelivery")
    Call<ResponseBody> AssignStaffForDelivery (@Field("VendorID")String VendorId,
                                               @Field("BranchID")String BranchId,
                                               @Field("StaffID")String StaffID,
                                               @Field("OrderID") String OrderID,
                                               @Field("Condition")String Condition,
                                               @Field("CustomerMobile")String CustomerMobile);

    @GET("api/Grocerry/GetDeliveryCharges?")
    Call<ResponseBody> GetDeliveryCharges(@Query("branchid") String branchid);


    @GET("api/Business/GetVendorDocumentInfo?")
    Call<ResponseBody> GetVendorDocumentInfo(@Query("vendorid") String vendorid,@Query("branchid")String branchid);

    @GET("api/Business/GetVendorHolidays?")
    Call<ResponseBody> GetVendorHolidays(@Query("branchid") String branchid);

    @PUT(" api/Business/DeleteHoliday?")
    Call<ResponseBody> DeleteHoliday(@Query("holidayid") String holidayid);

    @FormUrlEncoded
    @POST("api/Admin/IsStaffExists")
    Call<ResponseBody> IsStaffExists (@Field("MobileNo")String MobileNo,
                                               @Field("emailId")String emailId);

    @POST("api/Admin/UpdateStaffDeviceId")
    @FormUrlEncoded
    Call<ResponseBody>UpdateStaffDeviceId(@Field("UserId") String UserId,@Field("DeviceId")String DeviceId,
                                          @Field("DeviceType")String DeviceType,@Field("Type")String Type);


}
