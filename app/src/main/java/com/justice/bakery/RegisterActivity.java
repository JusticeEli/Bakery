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

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private TextInputLayout confirmPasswordInput;

    private TextView loginTxtView;
    private Button registerBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidgets();
        setOnClickListeners();


    }

    private void setOnClickListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String email = emailInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getEditText().getText().toString().trim();
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
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Please Fill field");
            confirmPasswordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, SetupActivity.class));
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

        });
        loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private void initWidgets() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        loginTxtView = findViewById(R.id.loginTxtView);
        registerBtn = findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);
    }
}
