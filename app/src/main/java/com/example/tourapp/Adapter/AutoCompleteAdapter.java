package com.example.tourapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tourapp.Models.Tour;
import com.example.tourapp.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AutoCompleteAdapter extends ArrayAdapter<Tour> {
    private List<Tour> tourListFull;

    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<Tour> tourList) {
        super(context, 0, tourList);
        tourListFull = new ArrayList<>(tourList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return tourFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_search,parent,false
            );
        }

        TextView tv_tour_name = convertView.findViewById(R.id.tv_tour_name);
        Tour tour = getItem(position);
        tv_tour_name.setText(tour.getAddress().toLowerCase());

        return convertView;
    }

    private Filter tourFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Tour> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() ==0 ){
                suggestions.addAll(tourListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Tour tour: tourListFull){
                    if(removeAccents(tour.getAddress().toLowerCase()).contains(removeAccents(filterPattern))){
                        suggestions.add(tour);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results.values != null) {
                addAll((List) results.values);
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Tour) resultValue).getAddress().toLowerCase();
        }

        private String removeAccents(String input) {
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(normalized).replaceAll("");
        }

    };

}
