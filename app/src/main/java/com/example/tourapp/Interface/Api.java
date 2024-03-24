package com.example.tourapp.Interface;

import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingById;
import com.example.tourapp.Models.BookingHistory;
import com.example.tourapp.Models.BookingResponse;
import com.example.tourapp.Models.ChangePasswordResponse;
import com.example.tourapp.Models.ForgotResponse;
import com.example.tourapp.Models.LoginResponse;
import com.example.tourapp.Models.PaymentResponse;
import com.example.tourapp.Models.TourDetail;
import com.example.tourapp.Models.UpdateUserResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Models.RegisterResponse;
import com.example.tourapp.Models.TourResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @POST("/api/v1/pay/create_payment_url/{id_book}")
    Call<PaymentResponse> Payment(@Path("id_book") String id_book);
    @GET("/api/v1/user/history_booking")
    Call<BookingHistory> getHistoryBooking();

    @GET("/api/v1/booking/{id_book}")
    Call<BookingById> getBookingById(@Path("id_book") String id_book);

    @POST("/api/v1/booking/createbooking")
    Call<BookingResponse> createBooking(@Query("tourId") String tourId, @Body Booking booking, @Header("Authorization") String authorization);
    @POST("/api/v1/booking/createbooking")
    Call<BookingResponse> createBookingNoToken(@Query("tourId") String tourId, @Body Booking booking);
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
    @PATCH("/api/v1/auth/change-password")
    Call<ChangePasswordResponse> changePassword(@Body RequestBody user);

}
