package com.developer.athirah.myorganisasi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.athirah.myorganisasi.models.ModelTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_UID = "event_uid";
    public static final String EXTRA_TASK_UID = "task_uid";

    private static ModelTask savedTask;
    private static String event_uid;
    private static String task_uid;

    private EditText title, limit, description;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        title = findViewById(R.id.title);
        limit = findViewById(R.id.limit);
        description = findViewById(R.id.description);

        getData();
    }

    public void onClickReset(View view) {
        update();
    }

    public void onClickSubmit(View view) {
        collectData();
    }

    public void onClickDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notis");
        builder.setMessage("Adakah anda pasti untuk buang tugasan daripada aktiviti ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                delete();
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

    private void getData() {
        event_uid = getIntent().getStringExtra(EXTRA_EVENT_UID);
        task_uid = getIntent().getStringExtra(EXTRA_TASK_UID);

        Toast.makeText(this, "event: " + event_uid + ", task: " + task_uid, Toast.LENGTH_SHORT).show();

        if (task_uid != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            firestore.collection("events").document(event_uid).collection("tasks").document(task_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        DocumentSnapshot snapshot = task.getResult();

                        if (snapshot != null) {

                            ModelTask data = snapshot.toObject(ModelTask.class);

                            if (data != null) {

                                data.setUid(snapshot.getId());
                                savedTask = data;

                                setTitle(data.getTitle());
                                update();
                            }

                        }

                    } else {
                        Toast.makeText(TaskActivity.this, "Fail to load task!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    dialog.dismiss();
                }

            });
        } else {
            savedTask = new ModelTask();
        }
    }

    private void collectData() {
        String str_title = title.getText().toString().trim();
        String str_limit = limit.getText().toString().trim();
        String str_description = description.getText().toString().trim();

        int int_limit = 0;

        if (str_title.isEmpty() || str_limit.isEmpty() || str_description.isEmpty()) {
            Toast.makeText(this, "Please fill all field!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int_limit = Integer.parseInt(str_limit);

        } catch (NumberFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        savedTask.setTitle(str_title);
        savedTask.setLimit(int_limit);
        savedTask.setDescription(str_description);

        submit();
    }

    private void update() {
        title.setText(savedTask.getTitle());
        limit.setText(String.valueOf(savedTask.getLimit()));
        description.setText(savedTask.getDescription());
    }

    private void submit() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        if (savedTask.getUid() != null) {
            // edit old
            firestore
                    .collection("events")
                    .document(event_uid)
                    .collection("tasks")
                    .document(task_uid)
                    .set(savedTask)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(TaskActivity.this, "Berjaya!", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });

        } else {
            // create new
            firestore
                    .collection("events")
                    .document(event_uid)
                    .collection("tasks")
                    .add(savedTask)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (task.isSuccessful()) {

                                DocumentReference reference = task.getResult();

                                if (reference != null) savedTask.setUid(reference.getId());

                                Toast.makeText(TaskActivity.this, "Berjaya!", Toast.LENGTH_SHORT).show();

                            } else {
                                if (task.getException() != null)
                                    Toast.makeText(TaskActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });
        }

    }

    private void delete() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        if (savedTask.getUid() != null) {

            firestore
                    .collection("events")
                    .document(event_uid)
                    .collection("tasks")
                    .document(savedTask.getUid())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            dialog.dismiss();

                            if (task.isSuccessful()) finish();

                        }
                    });
        }
    }
}
