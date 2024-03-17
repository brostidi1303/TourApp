package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.Adapter.Tour_Adapter;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourResponse;
import com.example.tourapp.Utility.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Tour> tourList = new ArrayList<>();
    TextView txt_notice_search;
    Tour_Adapter tourAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycle_view);
        txt_notice_search = findViewById(R.id.txt_notice_search);

        Actiontoolbar();
        initRecycleview();

        Intent intent = getIntent();
        if (intent != null) {
            String keyword = intent.getStringExtra("keyword");
            Log.d("searchtour", keyword);
            showTourBySearch(keyword);
        }else {
            Log.d("keyword","Keyword is null");
        }

    }

    private void Actiontoolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initRecycleview(){
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void showTourBySearch(String keyword){
        Call<TourResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .getTourBySearch(keyword);


        call.enqueue(new Callback<TourResponse>() {
            @Override
            public void onResponse(Call<TourResponse> call, Response<TourResponse> response) {
                if (response.isSuccessful()) {
                    txt_notice_search.setVisibility(View.INVISIBLE);
                    TourResponse tourResponse = response.body();
                    tourList.addAll(tourResponse.getTourList());
                    Log.d("cvb",tourList.size()+"");
                    Collections.shuffle(tourList);
                    tourAdapter = new Tour_Adapter(SearchActivity.this,tourResponse.getTourList());
                    recyclerView.setAdapter(tourAdapter);
                    tourAdapter.notifyDataSetChanged();
                } else {
                    txt_notice_search.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchActivity.this, "Tour " +response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}