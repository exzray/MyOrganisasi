package com.developer.athirah.myorganisasi.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.models.ModelEvent;

import java.text.DateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailDataFragment extends Fragment {

    private ModelEvent event;

    private View root;
    private ImageView image;
    private TextView location, date, time, description;


    public DetailDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        root = view;
        image = view.findViewById(R.id.detaildata_image);
        location = view.findViewById(R.id.detaildata_location);
        date = view.findViewById(R.id.detaildata_date);
        time = view.findViewById(R.id.detaildata_time);
        description = view.findViewById(R.id.detaildata_description);

        updateUI(event);
    }

    public void updateEvent(ModelEvent event) {

        if (root == null) this.event = event;
        else updateUI(event);
    }

    private void updateUI(ModelEvent event) {

        if (event != null) {
            String str_location = "Lokasi " + event.getLocation();
            String str_date = "Tarikh " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(event.getDate());
            String str_time = "Masa " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(event.getDate());

            Glide.with(this).load(event.getImage()).into(image);
            location.setText(str_location);
            date.setText(str_date);
            time.setText(str_time);
            description.setText(event.getDescription());
        }
    }
}
