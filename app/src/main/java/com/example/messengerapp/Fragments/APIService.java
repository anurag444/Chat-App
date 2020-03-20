package com.example.messengerapp.Fragments;

import com.example.messengerapp.Notfication.MyResponse;
import com.example.messengerapp.Notfication.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsXyndsY:APA91bHD7p1d2O4SbgHWA27_2MtEV1dVMUxRdGXnkt_Cov1mIqd1-SI1vn2v9sREqqGa5Ix3FR-XNlHL_WomWlZEdxDQEkmBUVdGhmC52d_OJ0DWluYe4H9qB-QD46ARUXhg1ZLCtm2a"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
