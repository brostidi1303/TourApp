package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourDetail;
import com.example.tourapp.Models.TourResponse;
import com.example.tourapp.Utility.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatButton btn_Booking;
    Tour tour ;
    ImageView imageDetail;
    TextView txt_tourname, txt_tourAddress, txt_tourSize, txt_Depature, txt_Des2, txt_tourPrice;
    Button btnShowmore;
    String baseURL = RetrofitClient.getBaseUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        Actiontoolbar();

        btnShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnShowmore.getText().toString().equalsIgnoreCase("Nhiều hơn..."))
                {
                    txt_Des2.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btnShowmore.setText("Ít hơn");
                }
                else
                {
                    txt_Des2.setMaxLines(6);//your TextView
                    btnShowmore.setText("Nhiều hơn...");

                }
            }
        });

        btn_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, BookingActivity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            String _id = intent.getStringExtra("_id");
            Log.d("_id", _id);
            SharedPreferences.Editor editor = getSharedPreferences("BookingTour",MODE_PRIVATE).edit();
            editor.putString("tourId",_id);
            editor.apply();
            showDetails(_id);
        }

    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        btn_Booking = findViewById(R.id.btn_Booking);
        imageDetail = findViewById(R.id.imageDetail);
        txt_tourname = findViewById(R.id.txt_tourname);
        txt_tourAddress = findViewById(R.id.txt_tourAddress);
        txt_Depature = findViewById(R.id.txt_tourDeparture);
        txt_tourSize = findViewById(R.id.txt_tourSize);
        txt_Des2 = findViewById(R.id.txt_tourDes2);
        txt_tourPrice = findViewById(R.id.txt_tourPrice);
        btnShowmore = findViewById(R.id.btnShowmore);

    }

    private void Actiontoolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDetails(String _id) {
        Call<TourDetail> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .getSingleTour(_id);

        call.enqueue(new Callback<TourDetail>() {
            @Override
            public void onResponse(Call<TourDetail> call, Response<TourDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TourDetail tourDetail = response.body();

                    tour = tourDetail.getTour();

                    if (tour != null) {
                        Log.d("detail",tour.toString());
                        Glide.with(DetailActivity.this)
                                .load(tour.getImagePath())
                                .placeholder(R.drawable.a1)
                                .error(R.drawable.a1)
                                .into(imageDetail);

                        if (tour != null) {
                            txt_tourname.setText(tour.getTourName());
                            txt_tourAddress.setText("Địa chỉ: "+tour.getAddress());

                            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("vi", "VN"));
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", new Locale("vi", "VN"));
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", new Locale("vi", "VN"));

                            String dayStr = dayFormat.format(tour.getDepartureDate());
                            String monthStr = monthFormat.format(tour.getDepartureDate());
                            String dateStr = dateFormat.format(tour.getDepartureDate());
                            String yearStr = yearFormat.format(tour.getDepartureDate());
                            String departureDateStr = dayStr + ", " + dateStr +" "+ monthStr + " năm " + yearStr;

                            txt_Depature.setText("Ngày đi: " +departureDateStr);

                            txt_tourSize.setText("Số lượng: "+ tour.getSize());
                            txt_Des2.setText(tour.getDescription());
                            txt_Des2.setMaxLines(5);


                            //txt_tourPrice.setText(tour.getPrice() +" "+ "VNĐ");
                            String priceString = tour.getPrice().toString();
                            try {
                                int price = Integer.parseInt(priceString);
                                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                String formattedPrice = decimalFormat.format(price) + " VNĐ";
                                txt_tourPrice.setText(formattedPrice);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Log.e("DetailActivity", "Error parsing price: " + e.getMessage());
                            }
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





}