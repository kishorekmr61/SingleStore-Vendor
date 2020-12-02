package com.project.vendorsapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {

    @POST("api/Business/PostVendor")
    Call<RegisterResponse> PostVendor(@Body RegisterRecords registerRecords );
}
