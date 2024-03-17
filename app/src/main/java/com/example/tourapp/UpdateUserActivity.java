package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourapp.Interface.Api;
import com.example.tourapp.Models.RegisterResponse;
import com.example.tourapp.Models.UpdateUserResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Utility.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    EditText edt_newfull_Name, edt_new_email,edt_new_phone;
    Button btn_Confirm_updateUser;
    SharedPreferences sharedPreferences;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        edt_newfull_Name = findViewById(R.id.edt_newfull_name);
        edt_new_email = findViewById(R.id.edt_new_email);
        edt_new_phone = findViewById(R.id.edt_new_phone);
        btn_Confirm_updateUser = findViewById(R.id.btn_Confirm_updateUser);

        sharedPreferences = getSharedPreferences("UserDatas", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");
        Log.d("dienkhung12",token);

        btn_Confirm_updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

    }

    private void updateUser() {
        String fullname = edt_newfull_Name.getText().toString().trim();
        String email = edt_new_email.getText().toString().trim();
        String phone = edt_new_phone.getText().toString().trim();

        if (fullname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateUserResponse.User user = new UpdateUserResponse.User();
        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);

        Call<UpdateUserResponse> call = RetrofitClient
                .getInstanceAccess(token)
                .create(Api.class)
                .updateUser(user);
        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.isSuccessful()) {
                    UpdateUserResponse updateUserResponse = response.body();
                    if (updateUserResponse != null) {
                        UpdateUserResponse.User user = updateUserResponse.getUser();
                        if (user != null) {
                            Log.d("mesd", "Full Name: " + user.getFullName());
                            Log.d("mesd", "Email: " + user.getEmail());
                            Log.d("mesd", "Phone: " + user.getPhone());
                        }

                        Toast.makeText(UpdateUserActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UpdateUserActivity.this, "User information is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UpdateUserActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                Toast.makeText(UpdateUserActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                Log.e("UpdateUserActivity", "Update failed: " + t.getMessage());
            }
        });
    }




}