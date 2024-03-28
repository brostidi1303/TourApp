package com.example.tourapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourapp.ConfirmPay;
import com.example.tourapp.DetailActivity;
import com.example.tourapp.Models.Booking;
import com.example.tourapp.Models.BookingHistory;
import com.example.tourapp.R;
import com.example.tourapp.Utility.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class BookingHistory_Adapter extends RecyclerView.Adapter<BookingHistory_Adapter.ViewHolder> {
    Context bookingContext;
    List<Booking> bookingList;
    String baseURL = RetrofitClient.getBaseUrl();

    public BookingHistory_Adapter(Context bookingContext, List<Booking> bookingList) {
        this.bookingContext = bookingContext;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingHistory_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(bookingContext).inflate(R.layout.item_booking,parent,false);
        return new BookingHistory_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistory_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tourname.setText("Tên: "+ bookingList.get(position).getTourName());

        String imagePath = bookingList.get(position).getTourImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(bookingContext)
                    .load(imagePath)
                    .placeholder(R.drawable.a1)
                    .error(R.drawable.a1)
                    .into(holder.HistoryImage);
        }


        // Sử dụng SimpleDateFormat để đọc ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDateCreate = dateFormat.format(bookingList.get(position).getDateCreate());
        holder.dateCreate.setText("Ngày Đặt: "+strDateCreate);

        // Nếu bạn muốn hiển thị ngày xuất phát dưới dạng "HH:mm a", bạn cũng cần chuyển định dạng
        SimpleDateFormat dateFormatDeparture = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        String strDepartureDate = dateFormatDeparture.format(bookingList.get(position).getDepartureDate());
        holder.departureDate.setText("Ngày đi: "+strDepartureDate);

        holder.paymentStatus.setText(bookingList.get(position).getPaymentStatus());

        if (bookingList.get(position).getPaymentStatus().equals("Đã thanh toán")) {
            holder.payment.setVisibility(View.GONE);
        } else {
            holder.payment.setVisibility(View.VISIBLE);
        }
        holder.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bookingContext, ConfirmPay.class);
                intent.putExtra("_idBook",bookingList.get(position).get_id());
                intent.putExtra("label", "bookingHistory");
                Log.d("get_id",bookingList.get(position).get_id());
                bookingContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateCreate,tourname,departureDate,paymentStatus,payment;
        ImageView HistoryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateCreate = itemView.findViewById(R.id.dateCreate);
            tourname = itemView.findViewById(R.id.history_tour_name);
            departureDate = itemView.findViewById(R.id.departureDate);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            HistoryImage = itemView.findViewById(R.id.HistoryImage);
            payment = itemView.findViewById(R.id.payment);
        }
    }
}
