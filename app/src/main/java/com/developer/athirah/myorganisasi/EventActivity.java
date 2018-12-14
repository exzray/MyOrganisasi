package com.developer.athirah.myorganisasi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.developer.athirah.myorganisasi.adapters.FragmentEventAdapter;
import com.developer.athirah.myorganisasi.adapters.RecyclerEventAdapter;
import com.developer.athirah.myorganisasi.models.ModelEvent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class EventActivity extends AppCompatActivity implements EventListener<QuerySnapshot> {

    private List<ModelEvent> ongoingList = new ArrayList<>();
    private List<ModelEvent> finishList = new ArrayList<>();

    private FragmentEventAdapter adapter;
    private RecyclerEventAdapter ongoingAdapter;
    private RecyclerEventAdapter finishAdapter;

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager pager;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        adapter = new FragmentEventAdapter(getSupportFragmentManager());
        ongoingAdapter = new RecyclerEventAdapter(ongoingList);
        finishAdapter = new RecyclerEventAdapter(finishList);

        toolbar = findViewById(R.id.toolbar);
        tab = findViewById(R.id.event_tab);
        pager = findViewById(R.id.event_pager);

        initUI();
        getEventList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listener != null) listener.remove();

        listener = null;
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

        if (queryDocumentSnapshots != null) {

            // clear list from previous data
            ongoingList.clear();
            finishList.clear();

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                ModelEvent event = snapshot.toObject(ModelEvent.class);

                if (event != null) {
                    event.setUid(snapshot.getId());

                    if (event.getStatus().equals(ModelEvent.Status.Ongoing)) {

                        ongoingList.add(event);

                    } else {

                        finishList.add(event);

                    }
                }
            }

            // after data is updated, notify the adapter to update their view
            ongoingAdapter.notifyDataSetChanged();
            finishAdapter.notifyDataSetChanged();
        }
    }

    private void initUI() {
        // setup activity to use support action bar
        setSupportActionBar(toolbar);

        // setup swipe tab
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);

        adapter.setAdapters(ongoingAdapter, finishAdapter);
    }

    private void getEventList() {
        listener = firestore.collection("events").orderBy("date").addSnapshotListener(this);
    }
}
