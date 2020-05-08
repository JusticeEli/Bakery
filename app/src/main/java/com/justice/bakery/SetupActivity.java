package com.justice.bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.justice.bakery.main.MainActivity;

public class SetupActivity extends AppCompatActivity {
    private TextInputLayout firstNameInput;
    private TextInputLayout middleNameInput;
    private TextInputLayout lastNameInput;
    private Spinner genderSpinner;

    private TextInputLayout contactNoInput;
    private TextInputLayout cityInput;
    private TextInputLayout addressInput;
    private Button submitBtn;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        initWidgets();
        initSpinner();
        setOnClickListeners();
    }

    private void initSpinner() {
        String[] categories={"Male","Female"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

    }

    private void setOnClickListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtnTapped();
            }
        });
    }

    private void submitBtnTapped() {
        String firstName = firstNameInput.getEditText().getText().toString().trim();
        String middleName = middleNameInput.getEditText().getText().toString().trim();
        String lastName = lastNameInput.getEditText().getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String contactNo = contactNoInput.getEditText().getText().toString().trim();
        String city = cityInput.getEditText().getText().toString().trim();
        String address = addressInput.getEditText().getText().toString().trim();


        if (firstName.isEmpty() || middleName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || contactNo.isEmpty() || city.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        UserSetup userSetup = new UserSetup(firstName, middleName, lastName, gender, contactNo, city, address);
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("user_setup").document(FirebaseAuth.getInstance().getUid()).set(userSetup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SetupActivity.this, MainActivity.class));
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    private void initWidgets() {
        firstNameInput = findViewById(R.id.firstNameInput);
        middleNameInput = findViewById(R.id.middleNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        genderSpinner = findViewById(R.id.genderSpinner);
        contactNoInput = findViewById(R.id.contactNoInput);
        cityInput = findViewById(R.id.cityInput);
        addressInput = findViewById(R.id.addressInput);
        submitBtn = findViewById(R.id.submitBtn);

        progressDialog = new ProgressDialog(this);


    }
}
