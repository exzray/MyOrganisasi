package com.developer.athirah.myorganisasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.developer.athirah.myorganisasi.adapters.RecyclerVolunteerAdapter;
import com.developer.athirah.myorganisasi.models.ModelProfile;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class VolunteerActivity extends AppCompatActivity implements EventListener<QuerySnapshot> {

    public static final String EXTRA_EVENT_UID = "event_uid";
    public static final String EXTRA_TASK_UID = "task_uid";
    public static final String EXTRA_TASK_PEOPLE = "people";

    private static final List<ModelProfile> LIST = new ArrayList<>();
    private static final List<String> VOLUNTEERS = new ArrayList<>();
    private static final RecyclerVolunteerAdapter ADAPTER = new RecyclerVolunteerAdapter(LIST);

    private String event_uid;
    private String[] peoples;
    private String task_uid;

    private LinearLayoutManager manager;
    private DividerItemDecoration decoration;
    private RecyclerView recycler;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        event_uid = getIntent().getStringExtra(EXTRA_EVENT_UID);
        peoples = getIntent().getStringArrayExtra(EXTRA_TASK_PEOPLE);
        task_uid = getIntent().getStringExtra(EXTRA_TASK_UID);

        manager = new LinearLayoutManager(this);
        decoration = new DividerItemDecoration(this, manager.getOrientation());
        recycler = findViewById(R.id.volunteer_recycler);

        initUI();
        getUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listener != null) listener.remove();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

        if (queryDocumentSnapshots != null) {

            LIST.clear();

            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                ModelProfile profile = snapshot.toObject(ModelProfile.class);

                if (profile != null) {
                    profile.setUid(snapshot.getId());

                    if (VOLUNTEERS.size() >= 1) {

                        if (VOLUNTEERS.contains(snapshot.getId())) {
                            LIST.add(profile);
                        }

                    } else {
                        LIST.add(profile);
                    }
                }
            }

            ADAPTER.setSelectVisibility(event_uid != null);
        }
    }

    public void editMember(String user_uid) {
        Intent data = new Intent();
        data.putExtra(EXTRA_TASK_PEOPLE, user_uid);
        data.putExtra(EXTRA_EVENT_UID, event_uid);
        data.putExtra(EXTRA_TASK_UID, task_uid);

        setResult(DetailActivity.RESULT_OK, data);
        finish();
    }

    private void initUI() {
        VOLUNTEERS.clear();

        recycler.setAdapter(ADAPTER);
        recycler.addItemDecoration(decoration);
        recycler.setLayoutManager(manager);

        if (peoples != null) {
            VOLUNTEERS.addAll(Arrays.asList(peoples));
        }
    }

    private void getUsers() {
        listener = firestore.collection("users").orderBy("name").addSnapshotListener(this);
    }
}
