package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingById;
import com.example.tourapp.Models.PaymentResponse;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourDetail;
import com.example.tourapp.Utility.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPay extends AppCompatActivity {
    String[] itemsPayment = {"VNPAY"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    Toolbar toolbar;
    TextView txt_tourDepartureBook,txt_tourSizeBook,txt_payment;
    EditText edt_BookingId,edt_nameBooK,edt_phoneBooK,edt_emailBooK;
    RoundedImageView roundedImageView;
    AppCompatButton btn_pay;
    String label,dam,total,quantity,dat,url,baseURL = RetrofitClient.getBaseUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);

        init();
        toolbar();

        Intent intent = getIntent();
        if (intent != null) {
            dat = intent.getStringExtra("_idBook");
            label = intent.getStringExtra("label");
            if (dat != null) {
                Log.d("_idBook", dat);
                bookingDetail(dat);

            } else {
                Log.e("_idBook", "Intent extra '_idBook' is null");
            }
        } else {
            Log.e("_idBook", "Intent is null");
        }
        if(label == null) {
            SharedPreferences sharedPreferences3 = getSharedPreferences("idBooking",Context.MODE_PRIVATE);
            dat = sharedPreferences3.getString("id_book","");
            if (dat != null) {
                Log.d("asd",dat);
            } else {
                Log.e("asd", "SharedPreferences 'id_book' is null");
            }
        }

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_payment_method, itemsPayment);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String items = adapterView.getItemAtPosition(i).toString();
            }
        });

        bookingDetail(dat);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(dat);
            }
        });

    }

    private void payment(String dat){
        Call<PaymentResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .Payment(dat);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                PaymentResponse paymentResponse = response.body();
                if (paymentResponse != null && paymentResponse.getVnpUrl()!= null) {
                    Log.d("url", paymentResponse.getVnpUrl());
                    url = paymentResponse.getVnpUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    Log.e("ConfirmPay", "PaymentResponse or URL is null");
                }
            }
            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {

            }
        });
        Intent intent = new Intent(ConfirmPay.this, ConfirmActivity.class);
        startActivity(intent);
    }
    
    private void bookingDetail(String dat){
    Call<BookingById> call = RetrofitClient
            .getInstance()
            .create(Api.class)
            .getBookingById(dat);

    call.enqueue(new Callback<BookingById>() {
        @Override
        public void onResponse(Call<BookingById> call, Response<BookingById> response) {
            if (response.isSuccessful()) {
                BookingById bookingById = response.body();
                if (bookingById != null) {
                    Booking booking = bookingById.getBooking();
                    if (booking != null) {
                        edt_BookingId.setText(booking.get_id());
                        edt_nameBooK.setText(booking.getFullName());
                        edt_emailBooK.setText(booking.getEmail());
                        edt_phoneBooK.setText(booking.getPhone());
                        txt_tourSizeBook.setText("Số lượng đặt: "+String.valueOf(booking.getQuantity()));

                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
                        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("vi", "VN"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", new Locale("vi", "VN"));
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", new Locale("vi", "VN"));

                        String dayStr = dayFormat.format(booking.getDepartureDate());
                        String monthStr = monthFormat.format(booking.getDepartureDate());
                        String dateStr = dateFormat.format(booking.getDepartureDate());
                        String yearStr = yearFormat.format(booking.getDepartureDate());
                        String departureDateStr = dayStr + ", " + dateStr +" "+ monthStr + " năm " + yearStr;

                        txt_tourDepartureBook.setText("Ngày khởi hành: " +departureDateStr);

                        String priceString = String.valueOf(booking.getTotalAmount());
                        try {
                            int price = Integer.parseInt(priceString);
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            String formattedPrice = decimalFormat.format(price) + " VNĐ";
                            txt_payment.setText(formattedPrice);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.e("dsds", "Error parsing price: " + e.getMessage());
                        }
                    } else {
                        Log.e("ConfirmPay", "Booking object is null");
                    }
                } else {
                    Log.e("ConfirmPay", "BookingById object is null");
                }
            } else {
                Log.e("ConfirmPay", "Response is not successful");
            }
        }

        @Override
        public void onFailure(Call<BookingById> call, Throwable t) {
            // Handle failure
        }
    });
}
    private void toolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        autoCompleteTextView = findViewById(R.id.autotext);
        txt_tourDepartureBook = findViewById(R.id.txt_tourDepartureBook);
        txt_tourSizeBook = findViewById(R.id.txt_tourSizeBook);
        txt_payment = findViewById(R.id.txt_payment);
        edt_BookingId = findViewById(R.id.edt_BookingId);
        edt_nameBooK = findViewById(R.id.edt_nameBooK);
        edt_emailBooK = findViewById(R.id.edt_emailBooK);
        edt_phoneBooK = findViewById(R.id.edt_phoneBooK);
        roundedImageView = findViewById(R.id.roundedImageView);
        btn_pay = findViewById(R.id.btn_pay);
    }
}