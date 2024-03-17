package com.example.tourapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.tourapp.Adapter.BookingHistory_Adapter;
import com.example.tourapp.Adapter.Tour_Adapter;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.LoginActivity;
import com.example.tourapp.MainActivity;
import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingHistory;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.R;
import com.example.tourapp.SignUpActivity;
import com.example.tourapp.Utility.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Booking>  bookingList = new ArrayList<>();
    BookingHistory_Adapter bookingHistoryAdapter;
    String token;
    ConstraintLayout constraintLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        constraintLayout = view.findViewById(R.id.historyFG);
        initRecycleview();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDatas", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");
        Log.d("HistoryFG",token);
        if (constraintLayout != null && !token.isEmpty()) {
            constraintLayout.setVisibility(View.VISIBLE);
            fetchData(token);
        } else {
            Log.e("HistoryFragment", "constraintLayout is null or token is empty");
            constraintLayout.setVisibility(View.INVISIBLE);
            openNoticeDialog(Gravity.CENTER);
        }


        return view;
    }

    private void openNoticeDialog(int gravity){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_history);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        Button btnLogin = dialog.findViewById(R.id.dialog_btnLogin);
        Button btnSignup = dialog.findViewById(R.id.dialog_btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void initRecycleview() {
        recyclerView = view.findViewById(R.id.recycle_history);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void fetchData(String token){
        Call<BookingHistory> call = RetrofitClient
                .getInstanceAccess(token)
                .create(Api.class)
                .getHistoryBooking();

        call.enqueue(new Callback<BookingHistory>() {
            @Override
            public void onResponse(Call<BookingHistory> call, Response<BookingHistory> response) {
                if (response.isSuccessful()) {
                    BookingHistory bookingHistory = response.body();
                    if (bookingHistory != null && bookingHistory.getHistoryBookings() != null) {
                        bookingList.addAll(bookingHistory.getHistoryBookings());
                        bookingHistoryAdapter = new BookingHistory_Adapter(getContext(), bookingList);
                        recyclerView.setAdapter(bookingHistoryAdapter);
                        bookingHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Empty response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<BookingHistory> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HistoryFragment", "Failed to fetch data: ", t);
            }
        });
    }
}