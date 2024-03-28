package com.example.tourapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.ChangePasswordActivity;
import com.example.tourapp.Interface.Api;
import com.example.tourapp.LoginActivity;
import com.example.tourapp.MainActivity;
import com.example.tourapp.Models.LoginResponse;
import com.example.tourapp.Models.User;
import com.example.tourapp.R;
import com.example.tourapp.SignUpActivity;
import com.example.tourapp.UpdateUserActivity;
import com.example.tourapp.Utility.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    View view;
    TextView txt_id,txt_email, txt_phone,txt_info,txt_fullname;
    AppCompatButton btnSignin,btn_GoSignUp,btn_updateUser, btn_logOut, btn_changePassword;
    ConstraintLayout constraintLayout;
    LinearLayout ln_user;
    String token,idUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        txt_info = view.findViewById(R.id.txt_peopleinfo);
        txt_id = view.findViewById(R.id.userId);
        txt_fullname =view.findViewById(R.id.user_fullname);
        txt_email = view.findViewById(R.id.userEmail);
        txt_phone = view.findViewById(R.id.userphone);
        btnSignin = view.findViewById(R.id.btnSignin);
        btn_GoSignUp = view.findViewById(R.id.btn_GoSignUp);
        btn_changePassword = view.findViewById(R.id.btn_changePassword);
        btn_updateUser = view.findViewById(R.id.btn_updateUser);
        btn_logOut = view.findViewById(R.id.btn_logOut);

        constraintLayout = view.findViewById(R.id.ctr3);
        ln_user = view.findViewById(R.id.Ln_user);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_GoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFragment.this.getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDatas", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");
        Log.d("dienkhung",token);
        idUser = sharedPreferences.getString("idUser", "");
        Log.d("diendien",idUser);

        if(!token.isEmpty()){
            btnSignin.setVisibility(View.INVISIBLE);
            btn_GoSignUp.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            btn_logOut.setVisibility(View.VISIBLE);
            ln_user.setVisibility(View.VISIBLE);
            btn_logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logOut_User();
                }
            });
            btn_updateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), UpdateUserActivity.class);
                    startActivity(intent);
                }
            });

            btn_changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            btnSignin.setVisibility(View.VISIBLE);
            btn_GoSignUp.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
            ln_user.setVisibility(View.INVISIBLE);
            btn_logOut.setVisibility(View.INVISIBLE);

        }

        if (!token.isEmpty()) {
            // Gọi endpoint Retrofit chỉ khi token có sẵn
            Call<LoginResponse> call = RetrofitClient
                    .getInstanceAccess(token)
                    .create(Api.class)
                    .getUserDetail();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            User user = loginResponse.getUser();
                            if (user != null) {
                                txt_id.setText(user.get_id()); // Hoặc user.getFullName() nếu muốn hiển thị tên
                                txt_fullname.setText(user.getFullName());
                                txt_email.setText(user.getEmail());
                                txt_phone.setText(user.getPhone());
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("User",Context.MODE_PRIVATE).edit();
                                editor.putString("fullname",user.getFullName());
                                editor.apply();
                            }
                        }

                    } else {
                        // Xử lý khi phản hồi không thành công
                        Log.e("ProfileFragment", "API call failed with response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Xử lý lỗi khi gọi API không thành công
                    Log.e("ProfileFragment", "API call failed: " + t.getMessage());
                }
            });

        }

        return view;
    }

    private void logOut_User(){
        SharedPreferences preferences = getActivity().getSharedPreferences("UserDatas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Token");
        editor.remove("fullname");
        editor.remove("email");
        editor.remove("phone");
        editor.apply();

        SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("User",Context.MODE_PRIVATE).edit();
        editor1.remove("fullname");
        editor1.apply();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
