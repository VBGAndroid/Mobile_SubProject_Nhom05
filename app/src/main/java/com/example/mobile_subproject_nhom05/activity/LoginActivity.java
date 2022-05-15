package com.example.mobile_subproject_nhom05.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_subproject_nhom05.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText Email = findViewById(R.id.edtMail);
        EditText password = findViewById(R.id.edtPassword);

        fAuth = FirebaseAuth.getInstance();

        //============= chuyen sang form resister
        TextView edResiter = findViewById(R.id.tvResister);
        edResiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String passwordField = password.getText().toString().trim();


                fAuth.signInWithEmailAndPassword(email, passwordField).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, ListActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(LoginActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}