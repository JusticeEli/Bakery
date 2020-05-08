package com.justice.bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.justice.bakery.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private TextView registerTxtView, forgotPasswordTxtView;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidgets();
        setOnClickListeners();


    }

    private void setOnClickListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        final String email = emailInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Please Fill field");
            emailInput.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Please Fill field");
            passwordInput.requestFocus();
            return;
        }
        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        registerTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        progressDialog.show();
        forgotPasswordTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Reset Link Send", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private void initWidgets() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerTxtView = findViewById(R.id.registerTxtView);
        forgotPasswordTxtView = findViewById(R.id.forgotPasswordTxtView);
        loginBtn = findViewById(R.id.loginBtn);
        progressDialog = new ProgressDialog(this);
    }
}
