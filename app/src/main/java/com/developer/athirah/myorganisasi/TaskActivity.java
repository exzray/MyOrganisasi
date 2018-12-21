package com.developer.athirah.myorganisasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.athirah.myorganisasi.models.ModelTask;

public class TaskActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_UID = "event_uid";
    public static final String EXTRA_TASK_UID = "task_uid";

    private static ModelTask task;
    private static String event_uid;
    private static String task_uid;

    private EditText title, limit, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        title = findViewById(R.id.title);
        limit = findViewById(R.id.limit);
        description = findViewById(R.id.description);

        getData();
    }

    public void onClickReset(View view){

    }

    public void onClickSubmit(View view){

    }

    private void getData(){
        event_uid = getIntent().getStringExtra(EXTRA_EVENT_UID);
        task_uid = getIntent().getStringExtra(EXTRA_TASK_UID);

        Toast.makeText(this, "event: " + event_uid + ", task: " + task_uid, Toast.LENGTH_SHORT).show();
    }
}
