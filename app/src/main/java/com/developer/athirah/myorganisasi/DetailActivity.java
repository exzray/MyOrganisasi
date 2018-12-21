package com.developer.athirah.myorganisasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.developer.athirah.myorganisasi.adapters.FragmentDetailAdapter;
import com.developer.athirah.myorganisasi.adapters.RecyclerTaskAdapter;
import com.developer.athirah.myorganisasi.models.ModelEvent;
import com.developer.athirah.myorganisasi.models.ModelTask;
import com.developer.athirah.myorganisasi.utilities.EventUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class DetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String EXTRA_UID = "uid";
    public static final List<ModelTask> TASKS = new ArrayList<>();

    public static ModelEvent eventSaved;

    private FragmentDetailAdapter adapter;
    private RecyclerTaskAdapter taskAdapter;

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager pager;
    private FloatingActionButton fab;

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
        fab = findViewById(R.id.fab);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:

                firestore.collection("events").document(eventSaved.getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) onBackPressed();

                    }
                });

                break;

            case R.id.action_edit:

                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_UID, eventSaved.getUid());
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
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

                        switch (requestCode) {

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

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 1) fab.animate().alpha(1).start();
        else fab.animate().alpha(0).start();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void initUI() {
        // setup activity to use support action bar
        setSupportActionBar(toolbar);

        // setup swipe tab
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        tab.setupWithViewPager(pager);

        adapter.setAdapter(taskAdapter);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, TaskActivity.class);
                intent.putExtra(TaskActivity.EXTRA_EVENT_UID, eventSaved.getUid());

                startActivity(intent);
            }
        });
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
