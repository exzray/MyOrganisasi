package com.developer.athirah.myorganisasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.developer.athirah.myorganisasi.adapters.FragmentDetailAdapter;
import com.developer.athirah.myorganisasi.adapters.RecyclerTaskAdapter;
import com.developer.athirah.myorganisasi.models.ModelEvent;
import com.developer.athirah.myorganisasi.models.ModelTask;
import com.developer.athirah.myorganisasi.utilities.EventUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_UID = "uid";
    public static final List<ModelTask> TASKS = new ArrayList<>();

    public static ModelEvent eventSaved;

    private FragmentDetailAdapter adapter;
    private RecyclerTaskAdapter taskAdapter;

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager pager;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerEvent;
    private ListenerRegistration listenerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        adapter = new FragmentDetailAdapter(getSupportFragmentManager());
        taskAdapter = new RecyclerTaskAdapter(TASKS);

        toolbar = findViewById(R.id.toolbar);
        tab = findViewById(R.id.detail_tab);
        pager = findViewById(R.id.detail_pager);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get pass uid
        String uid = getIntent().getStringExtra(EXTRA_UID);

        if (uid != null) {

            getEventDetail(uid);
            getEventTask(uid);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listenerEvent != null) listenerEvent.remove();
        if (listenerTask != null) listenerTask.remove();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_option, menu);

        if (eventSaved != null) {
            menu.getItem(0).setVisible(eventSaved.getStatus().equals(ModelEvent.Status.Ongoing));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            String eventID = data.getStringExtra(VolunteerActivity.EXTRA_EVENT_UID);
            final String taskID = data.getStringExtra(VolunteerActivity.EXTRA_TASK_UID);
            final String userID = data.getStringExtra(VolunteerActivity.EXTRA_TASK_PEOPLE);

            // event that want to change
            final DocumentReference reference = firestore.collection("events").document(eventID);

            firestore.runTransaction(new Transaction.Function<ModelEvent>() {

                @android.support.annotation.Nullable
                @Override
                public ModelEvent apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(reference);
                    ModelEvent event = snapshot.toObject(ModelEvent.class);

                    if (event != null) {
                        EventUtils utils = EventUtils.getInstance(DetailActivity.this, event);

                        switch (requestCode){

                            case 1:
                            case 2:
                                // clear this user from any joined task
                                for (String key : event.getTask().keySet()) {
                                    List<String> list = utils.getTask(key);
                                    list.remove(userID);
                                }

                                if (requestCode == 1) {
                                    List<String> list = utils.getTask(taskID);
                                    list.add(userID);
                                }

                                transaction.set(reference, event);
                                break;

                            case 3:
                                break;
                        }
                    }

                    return event;
                }
            });
        }
    }

    private void initUI() {
        // setup activity to use support action bar
        setSupportActionBar(toolbar);

        // setup swipe tab
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);

        adapter.setAdapter(taskAdapter);
    }

    private void getEventDetail(String uid) {
        listenerEvent = firestore.collection("events").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {

                    if (documentSnapshot.exists()) {

                        // convert data snapshot into object model
                        ModelEvent event = documentSnapshot.toObject(ModelEvent.class);

                        if (event != null) {
                            event.setUid(documentSnapshot.getId());

                            adapter.updateEvent(event);

                            // control activity behaviour
                            setTitle(event.getTitle());
                            eventSaved = event;
                            taskAdapter.updateEvent(event);
                        }
                    }

                } else {
                    if (e != null)
                        Toast.makeText(DetailActivity.this, "Event : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getEventTask(String uid) {
        listenerTask = firestore.collection("events").document(uid).collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {

                    TASKS.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        // convert data snapshot into object model
                        ModelTask task = snapshot.toObject(ModelTask.class);

                        if (task != null) {
                            task.setUid(snapshot.getId());

                            TASKS.add(task);
                        }
                    }

                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}