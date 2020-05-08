package com.justice.bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class ProductEntryActivity extends AppCompatActivity {
    private TextInputLayout productNameInput;
    private ImageView imageView;
    private TextInputLayout shortDescInput;
    private TextInputLayout fullDescInput;
    private TextInputLayout priceInput;
    private Spinner categorySpinner;
    private Button submitBtn;
    private Uri uri = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_entry);
        initWidgets();
        setSpinnerValues();
        setOnClickListeners();
    }

    private void setSpinnerValues() {
        String[] categories={"Cake","Pastries","Cookies","Bread"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


    }

    private void setOnClickListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtnTapped();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(uri).into(imageView);

    }


    private void choosePhoto() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);


    }


    private void submitBtnTapped() {
        String productName = productNameInput.getEditText().getText().toString().trim();
        String shortDesc = shortDescInput.getEditText().getText().toString().trim();
        String fullDesc = fullDescInput.getEditText().getText().toString().trim();
        String price = priceInput.getEditText().getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();


        if (uri == null || productName.isEmpty() || shortDesc.isEmpty() || fullDesc.isEmpty() || price.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        String id = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("product").child(id);

        UploadTask uploadTask = ref.putFile(uri);


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uri = downloadUri;
                    putDataIntoDatabase();
                    Toast.makeText(ProductEntryActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductEntryActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    private void putDataIntoDatabase() {
        String productName = productNameInput.getEditText().getText().toString().trim();
        String shortDesc = shortDescInput.getEditText().getText().toString().trim();
        String fullDesc = fullDescInput.getEditText().getText().toString().trim();
        int price = Integer.parseInt(priceInput.getEditText().getText().toString().trim());
        String category = categorySpinner.getSelectedItem().toString();

        Product product = new Product(productName, uri.toString(), shortDesc, fullDesc, price, category);
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("product").add(product).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductEntryActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductEntryActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }
        });
    }

    private void initWidgets() {
        productNameInput = findViewById(R.id.productNameInput);
        imageView = findViewById(R.id.imageView);
        shortDescInput = findViewById(R.id.shortDescriptionInput);
        fullDescInput = findViewById(R.id.fullDescriptionInput);
        priceInput = findViewById(R.id.priceInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        submitBtn = findViewById(R.id.submitBtn);

        progressDialog = new ProgressDialog(this);
    }
}
