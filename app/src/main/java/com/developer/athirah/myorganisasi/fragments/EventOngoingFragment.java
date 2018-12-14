package com.developer.athirah.myorganisasi.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.adapters.RecyclerEventAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventOngoingFragment extends Fragment {

    private RecyclerEventAdapter adapter;

    private RecyclerView recycler;


    public EventOngoingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.eventongoing_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setAdapter(adapter);
    }

    public void setAdapter(RecyclerEventAdapter adapter){

        if (recycler != null)  recycler.setAdapter(adapter);
        else this.adapter = adapter;
    }
}
