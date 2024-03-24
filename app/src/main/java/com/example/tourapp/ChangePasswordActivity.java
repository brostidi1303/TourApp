package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.tourapp.Models.ChangePasswordResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.Utility.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edt_currentPassword, edt_newPassword,edt_confirmNewPassword;
    Button btn_confirmChange;
    String token;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edt_currentPassword = findViewById(R.id.edt_currentPassword);
        edt_newPassword = findViewById(R.id.edt_newPassword);
        edt_confirmNewPassword = findViewById(R.id.edt_confirmNewPassword);
        btn_confirmChange = findViewById(R.id.btn_confirmChange);

        SharedPreferences sharedPreferences = getSharedPreferences("UserDatas", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");
        Log.d("changetoken",token);

//        btn_confirmChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangePassword();
//            }
//        });
        btn_confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePasswords()) {
                    ChangePassword();
                }
            }
        });

    }

    private boolean validatePasswords() {
        String newPassword = edt_newPassword.getText().toString().trim();
        String confirmNewPassword = edt_confirmNewPassword.getText().toString().trim();

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void ChangePassword() {
        String currentPassword = edt_currentPassword.getText().toString().trim();
        String newPassword = edt_newPassword.getText().toString().trim();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("currentPassword", currentPassword);
            requestBody.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tạo một RequestBody từ đối tượng JSONObject
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());

        // Khởi tạo một Call để gửi yêu cầu thay đổi mật khẩu
        Call<ChangePasswordResponse> call = RetrofitClient
                .getInstanceAccess(token)
                .create(Api.class)
                .changePassword(body);

        // Thực hiện yêu cầu bất đồng bộ
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    ChangePasswordResponse changePasswordResponse = response.body();
                    if (changePasswordResponse != null) {
                        String message = changePasswordResponse.getMessage();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(ChangePasswordActivity.this, /*message*/ "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                        // Xử lý thông báo thành công
                        // Ví dụ: Hiển thị thông báo thành công cho người dùng
                    }
                } else {
                    // Xử lý lỗi khi yêu cầu không thành công
                    // Ví dụ: Hiển thị thông báo lỗi cho người dùng
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                // Xử lý lỗi khi không thể kết nối tới server
                // Ví dụ: Hiển thị thông báo lỗi cho người dùng
                Toast.makeText(ChangePasswordActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getMessage());
            }
        });
    }

}