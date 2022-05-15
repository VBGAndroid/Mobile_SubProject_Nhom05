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

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

        Button btn_register = findViewById(R.id.btnRegister);
        EditText edtName = findViewById(R.id.edtName);
        EditText edtEmail = findViewById(R.id.edtMail);
        EditText edtPass = findViewById(R.id.edtPassword);
        EditText edtRePass = findViewById(R.id.edtTypePassword);

        if(fAuth.getCurrentUser() != null) {

        }

        TextView edLogin = findViewById(R.id.tvSignIn);
        edLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Srceen_Resister.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String rePass = edtRePass.getText().toString().trim();

                if(pass.equals(rePass)) {
                    Toast.makeText(RegisterActivity.this, "SOS", Toast.LENGTH_SHORT).show();

                    fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "create success" , Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(RegisterActivity.this, "create Fail" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Pass không giống nhau", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}