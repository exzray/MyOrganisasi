package com.developer.athirah.myorganisasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // declare view
    private ImageView image;
    private EditText email, password;
    private Button submit;

    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image = findViewById(R.id.login_image);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUI(auth.getCurrentUser());
    }

    private void initUI() {
        Glide
                .with(this)
                .load("https://images.unsplash.com/photo-1515601915049-08c8836c2204?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=133c7e7a614c3a168bbaa5f456b13cc9&auto=format&fit=crop&w=1350&q=80")
                .into(image);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                v.setEnabled(false);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login() {
        String str_email = email.getText().toString().trim();
        String str_password = password.getText().toString().trim();

        if (str_email.isEmpty() || str_password.isEmpty()) {
            Toast.makeText(this, "Please fill all field!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FirebaseUser user = task.getResult().getUser();

                    updateUI(user);
                } else {
                    if (task.getException() != null)
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                submit.setEnabled(true);
            }
        });
    }
}
