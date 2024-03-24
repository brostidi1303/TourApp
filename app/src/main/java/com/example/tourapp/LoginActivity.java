package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.LoginResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Utility.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView txt_notice,txt_forgot;
    String token,realtoken;
    EditText edtEmail, edtPassword;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_forgot = findViewById(R.id.txt_forgot);
        txt_notice = findViewById(R.id.txt_notice);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtGmail);
        edtPassword = findViewById(R.id.edtpassword);


        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        txt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }


    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .create(Api.class)
                .login(new User(email,password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Log.d("Hjk",loginResponse.toString());
                    if (loginResponse != null) {
                        User user = loginResponse.getUser();
                        if (user != null) {
                            Log.d("mes", "User ID: " + user.get_id());
                            Log.d("mes", "Full Name: " + user.getFullName());
                            Log.d("mes", "Email: " + user.getEmail());
                            Log.d("mes", "Phone: " + user.getPhone());
                        }

                        realtoken = loginResponse.getToken();
                        Log.d("mes", "Token: " + realtoken);

                        // Lưu giá trị token vào SharedPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("UserDatas", MODE_PRIVATE).edit();
                        editor.putString("Token", realtoken);
                        editor.putString("fullname",user.getFullName());
                        editor.putString("email",user.getEmail());
                        editor.putString("phone",user.getPhone());
                        editor.apply();

                        // Chuyển sang MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc activity hiện tại để ngăn người dùng quay lại

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Thông tin User không có", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // In ra lỗi nếu có
                    Log.e("Login", "Error: " + response.message());
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // In ra lỗi nếu có
                Log.e("Login", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}