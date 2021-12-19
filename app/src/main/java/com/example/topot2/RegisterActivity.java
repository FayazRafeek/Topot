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

import com.example.topot2.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    EditText nameInp, phoneInp, emailInp, passwdinp;
    Button registerBtn, loginSwitchBtn;


    FirebaseAuth mAuth;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

                if(validateInputs()){
                    startRegister();
                }
                else Toast.makeText(RegisterActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void startRegister() {

        showLoading();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserToDb(mAuth.getUid());
                        } else {
                            hideLoading();
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    String name,phone,email,password;
    Boolean validateInputs(){

        name = nameInp.getText().toString();
        phone = phoneInp.getText().toString();
        email = emailInp.getText().toString().trim();
        password = passwdinp.getText().toString().trim();

        if(name != null && phone != null && email != null && password != null)
            return true;
        return false;

    }


    FirebaseFirestore db;
    void addUserToDb(String uId) {

        User user = new User(uId,name,phone,email,password);

        db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(uId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading();
                        if(task.isSuccessful()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    finish();
                                }
                            },1000);

                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    void showLoading() { binding.progress.setVisibility(View.VISIBLE);}
    void hideLoading() { binding.progress.setVisibility(View.GONE);}

}
