package com.developer.athirah.myorganisasi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventListener<QuerySnapshot> {

    private List<ModelEvent> ongoingList = new ArrayList<>();
    private List<ModelEvent> finishList = new ArrayList<>();

    private FragmentEventAdapter adapter;
    private RecyclerEventAdapter ongoingAdapter;
    private RecyclerEventAdapter finishAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private NavigationView navigation;
    private TabLayout tab;
    private ViewPager pager;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new FragmentEventAdapter(getSupportFragmentManager());
        ongoingAdapter = new RecyclerEventAdapter(ongoingList);
        finishAdapter = new RecyclerEventAdapter(finishList);

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);

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
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                break;

            case R.id.action_about:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {

            case R.id.nav_volunteer:
                showVolunteer();
                break;

            case R.id.nav_profil:
                showProfile();
                break;

            case R.id.nav_logout:
                showLogout();
                break;
        }

        return true;
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

        // setup on click for float button
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showEditEvent();
            }
        });

        // setup drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // setup menu item behaviour
        navigation.setNavigationItemSelectedListener(this);

        // setup swipe tab
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);

        adapter.setAdapters(ongoingAdapter, finishAdapter);
    }

    private void showLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Keluar");
        builder.setMessage("Adakah anda pasti untuk log keluar daripada akaun ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Berjaya keluar!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showVolunteer() {

        Intent intent = new Intent(this, VolunteerActivity.class);
        startActivity(intent);

    }

    private void showProfile() {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    private void showEditEvent() {

        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);

    }

    private void getEventList() {
        listener = firestore.collection("events").orderBy("date").addSnapshotListener(this);
    }
}
