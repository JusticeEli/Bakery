package com.justice.bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.justice.bakery.main.ApplicationClass;

public class ProductActivity extends AppCompatActivity {
    private TextView productNameTxtView;
    private ImageView imageView;
    private TextView shortDescTxtView;
    private TextView fullDescTxtView;
    private TextView priceTxtView;
    private Button orderBtn;
    private Product product;
    private UserSetup user;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product= ApplicationClass.documentSnapshot.toObject(Product.class);


        initWidgets();
        setDefaultValues();
        setOnClickListeners();
    }

    private void setDefaultValues() {
        productNameTxtView.setText(product.getProductName());
        Glide.with(this).load(product.getImage()).into(imageView);
        shortDescTxtView.setText(product.getShortDesc());
        fullDescTxtView.setText(product.getFullDesc());
        priceTxtView.setText(product.getPrice()+"");

    }

    private void setOnClickListeners() {
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOrderInDatabase();
            }
        });
    }

    private void putOrderInDatabase() {
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("user_setup").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    user=task.getResult().toObject(UserSetup.class);
                    uploadData();

                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(ProductActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void uploadData() {
        Order order=new Order(user.getFirstName(),user.getContact(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),user.getAddress(),product.getProductName(),product.getPrice());
       progressDialog.show();
        FirebaseFirestore.getInstance().collection("order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ProductActivity.this, "Order Success", Toast.LENGTH_SHORT).show();
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(ProductActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    private void initWidgets() {
        productNameTxtView=findViewById(R.id.productNameTxtView);
        imageView=findViewById(R.id.imageView);
        shortDescTxtView=findViewById(R.id.shortDescTxtView);
        fullDescTxtView=findViewById(R.id.fullDescTxtView);
        priceTxtView=findViewById(R.id.priceTxtView);
        orderBtn=findViewById(R.id.orderBtn);
        progressDialog=new ProgressDialog(this);
    }
}
