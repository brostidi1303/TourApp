package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingResponse;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourDetail;
import com.example.tourapp.Utility.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView value ,txt_Tourname,txt_tourAddressBook,txt_tourDepartureBook,txt_priceBook,txt_much;
    EditText book_edtemail,book_edtphone,book_peoplename;
    RoundedImageView roundedImageView;
    AppCompatButton btn_booking;
    int count = 0,pricetour,total=0;
    String dam,fullnamebook,emailbook,phonebook,tokenbook,idbook1,idbook2, baseURL = RetrofitClient.getBaseUrl();;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        init();
        toolbar();

        SharedPreferences sharedPreferences1 =getSharedPreferences("UserDatas",Context.MODE_PRIVATE);
        fullnamebook = sharedPreferences1.getString("fullname","");
        Log.d("namebook",fullnamebook);
        if (fullnamebook.isEmpty()){
        }else {
            book_peoplename.setEnabled(false);
            book_peoplename.setText(fullnamebook);
        }

        emailbook = sharedPreferences1.getString("email","");
        Log.d("emailbook",emailbook);
        if (emailbook.isEmpty()){
        }else {
            book_edtemail.setEnabled(false);
            book_edtemail.setText(emailbook);
        }

        phonebook = sharedPreferences1.getString("phone","");
        Log.d("phonebook",phonebook);
        if (phonebook.isEmpty()){
        }else {
            book_edtphone.setEnabled(false);
            book_edtphone.setText(phonebook);
        }

        tokenbook = sharedPreferences1.getString("Token", "");
        Log.d("booktoken",tokenbook);

        SharedPreferences sharedPreferences = getSharedPreferences("BookingTour", Context.MODE_PRIVATE);
        dam = sharedPreferences.getString("tourId", "");
        Log.d("idtour",dam);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiBooking(dam);
            }
        });

        tourDetail(dam);
    }

    private void callApiBooking(String dam) {

        if (tokenbook.isEmpty()) {
            // Nếu có token, sử dụng phương thức createBooking với token
            createBookingWithoutToken(dam);
        } else {
            // Nếu không có token, sử dụng phương thức createBooking mà không có token
            createBookingWithToken(dam);
        }
    }

    private void createBookingWithToken(String dam){

        Booking booking = createBookingOB();
        Call<BookingResponse> call = RetrofitClient
                .getInstanceAccess(tokenbook)
                .create(Api.class)
                .createBooking(dam, booking, "Bearer " + tokenbook);


        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (response.isSuccessful()) {
                    BookingResponse bookingResponse = response.body();
                    if (bookingResponse != null) {
                        Booking booking = bookingResponse.getNewBooking();
                        if (booking != null) {
                            Log.d("BookingInfo1","BookingId" + booking.get_id());
                            idbook1 = booking.get_id();
                        } else {
                            Log.e("Error", "Booking object is null");
                        }//65fac0b75c0371694d3eb6fd
                    } else {
                        Log.e("Error", "BookingResponse object is null");
                    }
                    Log.d("Idbook",idbook1);
                    SharedPreferences.Editor bookingid = getSharedPreferences("idBooking",MODE_PRIVATE).edit();
                    bookingid.putString("id_book",idbook1);
                    bookingid.apply();
                    Intent intent = new Intent(BookingActivity.this, ConfirmPay.class);
                    startActivity(intent);
                } else {
                    // In ra thông báo lỗi nếu có
                    Log.e("Error", "Unsuccessful response: " + response.message());
                    Toast.makeText(BookingActivity.this, "Số lượng hành khách đã đầy", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Log.e("Error", "Error occurred while creating booking: " + t.getMessage());
            }
        });
    }

    private void createBookingWithoutToken(String dam){

        Booking booking = createBookingOB();
        Call<BookingResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .createBookingNoToken(dam,booking);

        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (response.isSuccessful()) {
                    BookingResponse bookingResponse = response.body();
                    if (bookingResponse != null) {
                        Booking booking = bookingResponse.getNewBooking();
                        Log.d("Booker",bookingResponse.getNewBooking().toString()+"");
                        if (booking != null) {
                            // In ra thông tin booking để kiểm tra
                            Log.d("BookingInfo2","BookingId" + booking.get_id());
                            idbook2 = booking.get_id();
                        } else {
                            Log.e("Error", "Booking object is null");
                        }
                    } else {
                        Log.e("Error", "BookingResponse object is null");
                    }
                    Log.d("Idbook2",idbook2);
                    SharedPreferences.Editor bookingid = getSharedPreferences("idBooking",MODE_PRIVATE).edit();
                    bookingid.putString("id_book",idbook2);
                    bookingid.apply();
                    Intent intent = new Intent(BookingActivity.this, ConfirmPay.class);
                    startActivity(intent);
                } else {
                    // In ra thông báo lỗi nếu có
                    Log.e("Error", "Unsuccessful response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Log.e("Error", "Error occurred while creating booking: " + t.getMessage());
            }
        });
    }

    private Booking createBookingOB(){
        String fullname = book_peoplename.getText().toString().trim();
        Log.d("Duma",fullname);
        String email = book_edtemail.getText().toString().trim();
        Log.d("Duma",email);
        String phone = book_edtphone.getText().toString().trim();
        Log.d("Duma",phone);
        String quantity = value.getText().toString().trim();
        Log.d("Duma",quantity);
        String totalAmount = txt_much.getText().toString().trim();
        Log.d("Duma",totalAmount);

        totalAmount = totalAmount.replaceAll("\\D+", "");
        SharedPreferences.Editor editor = getSharedPreferences("BookingtoPayment",MODE_PRIVATE).edit();
        editor.putString("total", totalAmount);
        editor.putString("quantity", quantity);
        editor.apply();

        // Tạo đối tượng Booking và gửi yêu cầu tạo booking
        Booking booking = new Booking();
        booking.setTourId(dam);
        booking.setFullName(fullname);
        booking.setEmail(email);
        booking.setPhone(phone);
        booking.setQuantity(Integer.parseInt(quantity));
        booking.setTotalAmount(Integer.parseInt(totalAmount));

        return booking;
    }

    private void tourDetail(String dam){
        Call<TourDetail> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .getSingleTour(dam);

        call.enqueue(new Callback<TourDetail>() {
            @Override
            public void onResponse(Call<TourDetail> call, Response<TourDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TourDetail tourDetail = response.body();

                    Tour tour = tourDetail.getTour();
                    if (tour != null) {
                        Log.d("detail45",tour.toString());
                        Picasso.get().load(baseURL + tour.getImagePath())
                                .placeholder(R.drawable.a1)
                                .error(R.drawable.a1)
                                .into(roundedImageView);

                        if (tour != null) {
                            txt_Tourname.setText(tour.getTourName());
                            txt_tourAddressBook.setText(tour.getAddress());

                            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("vi", "VN"));
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", new Locale("vi", "VN"));
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", new Locale("vi", "VN"));

                            String dayStr = dayFormat.format(tour.getDepartureDate());
                            String monthStr = monthFormat.format(tour.getDepartureDate());
                            String dateStr = dateFormat.format(tour.getDepartureDate());
                            String yearStr = yearFormat.format(tour.getDepartureDate());
                            String departureDateStr = dayStr + ", " + dateStr +" "+ monthStr + " năm " + yearStr;

                            txt_tourDepartureBook.setText(departureDateStr);
                            //txt_priceBook.setText(tour.getPrice()+" "+ "VNĐ");
                            pricetour = tour.getPrice().intValue();

                            String priceString = tour.getPrice().toString();
                            try {
                                int price = Integer.parseInt(priceString);
                                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                String formattedPrice = decimalFormat.format(price) + " VNĐ";
                                txt_priceBook.setText(formattedPrice);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Log.e("DetailActivity", "Error parsing price: " + e.getMessage());
                            }
                            Log.d("pricetour", String.valueOf(pricetour));

                        } else {
                            Log.e("DetailActivity", "First tour is null");
                        }
                    } else {
                        Log.e("DetailActivity", "Tour list is null or empty");
                    }

                } else {
                    Log.e("DetailActivity", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<TourDetail> call, Throwable t) {

            }
        });
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        value = findViewById(R.id.txt_amount);
        btn_booking = findViewById(R.id.btn_Booking);
        roundedImageView = findViewById(R.id.roundedImageView);
        txt_Tourname = findViewById(R.id.txt_tournameBook);
        txt_tourAddressBook = findViewById(R.id.txt_tourAddressBook);
        txt_tourDepartureBook = findViewById(R.id.txt_tourDepartureBook);
        txt_priceBook = findViewById(R.id.txt_priceBook);
        book_peoplename = findViewById(R.id.book_peoplename);
        book_edtemail = findViewById(R.id.book_edtemail);
        book_edtphone =findViewById(R.id.book_edtphone);
        txt_much = findViewById(R.id.txt_much);
    }

    private void toolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void increase(View v){
        count++;
        value.setText("" + count);
        total = pricetour *count;
        String formattedTotal = formatCurrency(total);
        txt_much.setText(formattedTotal);
    }

    @SuppressLint("SuspiciousIndentation")
    public void decrease(View v){
        if(count <= 0 ){
            count = 0;
        }else {
            count--;
            value.setText("" + count);
            total = pricetour * count;
            String formattedTotal = formatCurrency(total);
            txt_much.setText(formattedTotal);

        }

    }

    private String formatCurrency(int amount) {
        return String.format(Locale.getDefault(), "%,d VNĐ", amount);
    }
}
