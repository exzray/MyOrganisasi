package com.developer.athirah.myorganisasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developer.athirah.myorganisasi.models.ModelEvent;

public class EditActivity extends AppCompatActivity {

    private static ModelEvent EVENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }
}
