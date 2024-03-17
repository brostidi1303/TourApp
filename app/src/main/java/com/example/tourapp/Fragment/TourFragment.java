package com.example.tourapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourapp.Adapter.Tour_Adapter;
import com.example.tourapp.DetailActivity;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourResponse;
import com.example.tourapp.R;
import com.example.tourapp.Utility.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TourFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Tour> tourList = new ArrayList<>();
    Tour_Adapter tourAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tour, container, false);

        initRecycleview();
        fetchData();



        return view;
    }

    private void initRecycleview() {
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void fetchData(){

        Call<TourResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .getTour();

        call.enqueue(new Callback<TourResponse>() {
            @Override
            public void onResponse(Call<TourResponse> call, Response<TourResponse> response) {
                TourResponse tourResponse = response.body();
                if(response.isSuccessful() && null!= tourResponse){
                    tourList.addAll(tourResponse.getTourList());
                    Collections.shuffle(tourList);
                    tourAdapter =new Tour_Adapter(getContext(),tourList/*tourResponse.getTourList()*/);
                    recyclerView.setAdapter(tourAdapter);
                    tourAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}