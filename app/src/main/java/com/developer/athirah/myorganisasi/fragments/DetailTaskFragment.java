package com.developer.athirah.myorganisasi.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.athirah.myorganisasi.R;
import com.developer.athirah.myorganisasi.adapters.RecyclerTaskAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTaskFragment extends Fragment {

    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private DividerItemDecoration decoration;
    private RecyclerTaskAdapter adapter;


    public DetailTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.detailtask_recycler);
        manager = new LinearLayoutManager(view.getContext());
        decoration = new DividerItemDecoration(view.getContext(), manager.getOrientation());

        initUI();
    }

    private void initUI(){
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(decoration);
        recycler.setAdapter(adapter);
    }

    public void setAdapter(RecyclerTaskAdapter adapter) {

        if (recycler != null) recycler.setAdapter(adapter);

        this.adapter = adapter;
    }
}
