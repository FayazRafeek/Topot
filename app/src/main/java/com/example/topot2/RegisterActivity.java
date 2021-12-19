package com.example.topot2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    EditText nameInp, phoneInp, emailInp, passwdinp;
    Button registerBtn, loginSwitchBtn;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        nameInp = findViewById(R.id.reg_name);
        phoneInp = findViewById(R.id.reg_phone);
        emailInp = findViewById(R.id.reg_email);
        passwdinp = findViewById(R.id.reg_password);

        registerBtn = findViewById(R.id.register_btn);
        loginSwitchBtn = findViewById(R.id.login_switch_btn);

        mAuth = FirebaseAuth.getInstance();

        loginSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailInp.getText().toString().trim();
                String password = passwdinp.getText().toString().trim();

                if(email != null && password != null) startRegister(email,password);
                else Toast.makeText(RegisterActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void startRegister(String email,String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToDb(user.getUid());


                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void showProgress(Boolean value) {

    }


    void addUserToDb(String uId) {

        String name = nameInp.getText().toString();
        String phone = phoneInp.getText().toString();
        String email = emailInp.getText().toString().trim();
        String password = passwdinp.getText().toString().trim();

        User user = new User(name,phone,email,password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().getRoot();



        myRef.child("Users").child(uId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }
                    },1000);

                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
