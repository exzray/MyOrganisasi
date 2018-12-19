package com.developer.athirah.myorganisasi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.athirah.myorganisasi.models.ModelEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, OnCompleteListener<DocumentSnapshot> {

    public static final String EXTRA_UID = "uid";

    private static ModelEvent EVENT;

    private EditText title, image, location, date, description, longitud, lalitud;
    private Button sdate, submit;
    private Spinner status;

    private String _title, _image, _location, _description, _longitud, _lalitud, _status;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.title);
        status = findViewById(R.id.status);
        image = findViewById(R.id.image);
        location = findViewById(R.id.location);
        longitud = findViewById(R.id.longitud);
        lalitud = findViewById(R.id.latitud);
        date = findViewById(R.id.date);
        sdate = findViewById(R.id.sdate);
        description = findViewById(R.id.description);
        submit = findViewById(R.id.submit);

        initUI();
        isCreateNew();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _status = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v.equals(sdate)) {

            DatePickerDialog dialog = new DatePickerDialog(this);
            dialog.setOnDateSetListener(this);
            dialog.show();

        } else if (v.equals(submit)) {
            collectData();

            if (validateField()) {

                bindDataToObject();

            } else {
                Toast.makeText(this, "All field must be fill!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar now = Calendar.getInstance();

        Calendar future = Calendar.getInstance();
        future.set(Calendar.YEAR, year);
        future.set(Calendar.MONTH, month);
        future.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (future.before(now)) {
            Toast.makeText(this, "Invalid date to choose!", Toast.LENGTH_SHORT).show();

        } else {
            EVENT.setDate(future.getTime());
            updateUI();
        }
    }

    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        toggleUI(true);
        DocumentSnapshot snapshot = task.getResult();

        if (snapshot != null && snapshot.exists()) {
            EVENT = snapshot.toObject(ModelEvent.class);

            if (EVENT != null) {
                EVENT.setUid(snapshot.getId());
                updateUI();
            }
        }
    }

    private void initUI() {
        status.setOnItemSelectedListener(this);
        sdate.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void updateUI() {
        title.setText(EVENT.getTitle());
        image.setText(EVENT.getImage());
        location.setText(EVENT.getLocation());
        description.setText(EVENT.getDescription());
        date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(EVENT.getDate()));

        switch (EVENT.getStatus()) {
            case Ongoing:
                status.setSelection(2);
            case Complete:
                status.setSelection(1);
            case Cancel:
                status.setSelection(0);
        }
    }

    private void toggleUI(boolean state) {
        title.setEnabled(state);
        image.setEnabled(state);
        location.setEnabled(state);
        description.setEnabled(state);
        longitud.setEnabled(state);
        lalitud.setEnabled(state);
        status.setEnabled(state);
        sdate.setEnabled(state);
        submit.setEnabled(state);
        date.setEnabled(state);
    }

    private void isCreateNew() {
        String uid = getIntent().getStringExtra(EXTRA_UID);

        if (uid == null) {
            // create new
            EVENT = new ModelEvent();

        } else {
            toggleUI(false);

            // edit
            firestore.collection("events").document(uid).get().addOnCompleteListener(this, this);
        }
    }

    private void collectData() {
        _title = title.getText().toString().trim();
        _image = image.getText().toString().trim();
        _location = location.getText().toString().trim();
        _description = location.getText().toString().trim();
        _longitud = longitud.getText().toString().trim();
        _lalitud = lalitud.getText().toString().trim();
    }

    private boolean validateField() {
        return !(_title.isEmpty() && _image.isEmpty() && _location.isEmpty() && _description.isEmpty() && _longitud.isEmpty() && _lalitud.isEmpty());
    }

    private void bindDataToObject() {
        toggleUI(false);

        EVENT.setTitle(_title);
        EVENT.setImage(_image);
        EVENT.setLocation(_location);
        EVENT.setStatus(ModelEvent.Status.valueOf(_status));
        EVENT.setDescription(_description);

        if (EVENT.getUid() != null && !EVENT.getUid().isEmpty()) {

            firestore.collection("events").document(EVENT.getUid()).set(EVENT).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    toggleUI(true);

                    if (task.isSuccessful())
                        Toast.makeText(EditActivity.this, "Success update event!", Toast.LENGTH_SHORT).show();

                    if (task.getException() != null) {
                        Toast.makeText(EditActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            firestore.collection("events").add(EVENT).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    toggleUI(true);

                    if (task.isSuccessful())
                        Toast.makeText(EditActivity.this, "Success upload event!", Toast.LENGTH_SHORT).show();

                    if (task.getException() != null) {
                        Toast.makeText(EditActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
