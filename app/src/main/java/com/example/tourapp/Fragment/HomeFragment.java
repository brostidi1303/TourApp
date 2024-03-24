package com.example.tourapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tourapp.Adapter.AutoCompleteAdapter;
import com.example.tourapp.Adapter.Tour_Adapter;
import com.example.tourapp.DetailActivity;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.Models.TourResponse;
import com.example.tourapp.R;
import com.example.tourapp.SearchActivity;
import com.example.tourapp.Utility.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ImageSlider imageSlider, imageSlider1;
    TextView txt_hiUser;
    AppCompatButton btn_seemore;
    String fullname;
    ImageButton btn_search;
    AutoCompleteTextView edt_search;
    List<Tour> tourList = new ArrayList<>();
    Tour_Adapter tourAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        btn_search = view.findViewById(R.id.btn_search);
        edt_search = view.findViewById(R.id.edt_search);
        txt_hiUser =view.findViewById(R.id.txt_hiUser);
        imageSlider = view.findViewById(R.id.imageSlider);
        imageSlider1 = view.findViewById(R.id.imageSlider1);


        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.a6,ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.a7,ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.a9,ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.a2,ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

        ArrayList<SlideModel> slideModels1 = new ArrayList<>();
        slideModels1.add(new SlideModel(R.drawable.a5,ScaleTypes.CENTER_CROP));
        slideModels1.add(new SlideModel(R.drawable.a10,ScaleTypes.CENTER_CROP));
        slideModels1.add(new SlideModel(R.drawable.a14,ScaleTypes.CENTER_CROP));

        imageSlider1.setImageList(slideModels1,ScaleTypes.CENTER_CROP);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        fullname = sharedPreferences.getString("fullname","");
        Log.d("homefull",fullname);
        if (fullname.isEmpty()){
            txt_hiUser.setVisibility(View.INVISIBLE);
        }else {
            txt_hiUser.setText("Xin chào "+ fullname);
            txt_hiUser.setVisibility(View.VISIBLE);
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = edt_search.getText().toString().trim();
                if(keyword.isEmpty()){
                    Toast.makeText(getActivity(), "Làm ơn không được để trống !!!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent_search = new Intent(HomeFragment.this.getActivity(), SearchActivity.class);
                    intent_search.putExtra("keyword",keyword);
                    Log.d("search",keyword.toString());
                    startActivity(intent_search);
                }

            }
        });

        initRecycleview();

        return view;
    }


    private void initRecycleview() {
        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        fetchData();
    }

    private void fetchData() {

        Call<TourResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .getFeatureTour();

        call.enqueue(new Callback<TourResponse>() {
            @Override
            public void onResponse(Call<TourResponse> call, Response<TourResponse> response) {
                TourResponse tourResponse = response.body();
                if(response.isSuccessful() && null!= tourResponse){
                    tourList.addAll(tourResponse.getTourList());
                    Collections.shuffle(tourList);
                    tourAdapter =new Tour_Adapter(getContext(),tourList);
                    recyclerView.setAdapter(tourAdapter);
                    tourAdapter.notifyDataSetChanged();

                    AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(HomeFragment.this.getContext(),tourList);
                    edt_search.setAdapter(autoCompleteAdapter);
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