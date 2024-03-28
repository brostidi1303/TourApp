package com.example.tourapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tourapp.DetailActivity;
import com.example.tourapp.Models.Tour;
import com.example.tourapp.R;
import com.example.tourapp.Utility.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Tour_Adapter extends RecyclerView.Adapter<Tour_Adapter.ViewHolder> {
    Context tourContext;
    List<Tour> tourList;
    String baseURL = RetrofitClient.getBaseUrl();


    public Tour_Adapter(Context tourContext, List<Tour> tourList) {
        this.tourContext = tourContext;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public Tour_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(tourContext).inflate(R.layout.item_tour,parent,false);
        return new Tour_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tour_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String imagePath = tourList.get(position).getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(tourContext)
                    .load(imagePath)
                    .placeholder(R.drawable.a1)
                    .error(R.drawable.a1)
                    .into(holder.image_tour);
        }

        holder.tour_name.setText(tourList.get(position).getTourName());

        Number price = tourList.get(position).getPrice();
        if (price != null) {
            // Format giá trị số sang định dạng tiền tệ VNĐ
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
            symbols.setCurrencySymbol("VNĐ");
            DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ", symbols);
            String formattedPrice = decimalFormat.format(price);
            holder.tour_price.setText(formattedPrice);
        } else {
            holder.tour_price.setText("N/A");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tourContext, DetailActivity.class);
                intent.putExtra("_id",tourList.get(position).get_id());
                Log.d("getid",tourList.toString());
                tourContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_tour;
        TextView tour_name,tour_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_tour =itemView.findViewById(R.id.image_tour);
            tour_name = itemView.findViewById(R.id.TOUR_NAME);
            tour_price = itemView.findViewById(R.id.TOUR_PRICE);
        }
    }
}
