package com.example.tourapp.Interface;

import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingHistory;
import com.example.tourapp.Models.BookingResponse;
import com.example.tourapp.Models.ForgotResponse;
import com.example.tourapp.Models.LoginResponse;
import com.example.tourapp.Models.TourDetail;
import com.example.tourapp.Models.UpdateUserResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Models.RegisterResponse;
import com.example.tourapp.Models.TourResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("/api/v1/user/history_booking")
    Call<BookingHistory> getHistoryBooking();
    @POST("/api/v1/booking/createbooking")
    Call<BookingResponse> createBooking(@Query("tourId") String tourId, @Body Booking booking, @Header("Authorization") String authorization);

    @GET("/api/v1/tour")
    Call<TourResponse> getTour();

    @GET("/api/v1/tour/featured")
    Call<TourResponse> getFeatureTour();

    @GET("/api/v1/tour/{id}")
    Call<TourDetail> getSingleTour(@Path("id") String id);

    @GET("/api/v1/tour/search")
    Call<TourResponse> getTourBySearch(@Query("keyword") String keyword);

    @GET("/api/v1/user/login")
    Call<LoginResponse> getUserDetail();

    @POST("/api/v1/auth/register")
    Call<RegisterResponse> register(@Body User user);

    @POST("/api/v1/auth/login")
    Call<LoginResponse> login(@Body User user);

    @POST("/api/v1/auth/forgotpassword")
    Call<ForgotResponse> forgotPassword(@Body User user);

    @PATCH("/api/v1/auth/updateUser")
    Call<UpdateUserResponse> updateUser(@Body UpdateUserResponse.User user);

}
