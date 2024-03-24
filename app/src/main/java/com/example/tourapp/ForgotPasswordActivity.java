package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.ForgotResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Utility.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edt_Gmail_forgot;
    Button btn_forgot;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edt_Gmail_forgot = findViewById(R.id.edt_Gmail_forgot);
        btn_forgot = findViewById(R.id.btn_Confirm_forgot);

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(ForgotPasswordActivity.this, "Đã gửi đến gmail của bạn !", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void forgotpassword(){
        String email = edt_Gmail_forgot.getText().toString().trim();

        Call<ForgotResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .forgotPassword(new User(email));

        call.enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                ForgotResponse forgotResponse = response.body();
            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable t) {

            }
        });
    }
}