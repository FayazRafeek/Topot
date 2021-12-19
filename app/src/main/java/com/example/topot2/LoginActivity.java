package com.example.topot2;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.topot2.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailInp, passinp;
    Button loginBtn, regSwitchBtn;

    FirebaseAuth mAuth;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailInp = findViewById(R.id.login_email);
        passinp = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.login_btn);
        regSwitchBtn = findViewById(R.id.register_switch_btn);

        regSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailInp.getText().toString().trim();
                String password = passinp.getText().toString().trim();

                if(email != null && password != null) startLogin(email,password);
                else Toast.makeText(LoginActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();

            }
        });
    }


    void startLogin(String email, String password) {

        showLoading();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {

                            SharedPreferences pref = getSharedPreferences("PREF",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("LOGIN",true);
                            editor.putString("UID",mAuth.getUid());
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, 800);


                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void showLoading() { binding.progress.setVisibility(View.VISIBLE);}
    void hideLoading() { binding.progress.setVisibility(View.GONE);}
}
