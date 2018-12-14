package com.developer.athirah.myorganisasi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private NavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);

        initUI();
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
                Intent intent = new Intent(this, DetailActivity.class);
                startActivity(intent);
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

            case R.id.nav_event:
                showEvent();
                break;

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

    private void showEvent() {

        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);

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
}
