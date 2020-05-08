package com.justice.bakery.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.justice.bakery.Product;
import com.justice.bakery.R;
import com.justice.bakery.main.MainActivityAdapter;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initWidgets();
   //     initAdapter();
    }

    private void initAdapter() {

        Query query = FirebaseFirestore.getInstance().collection("cart").document(FirebaseAuth.getInstance().getUid()).collection("cart");
        FirestoreRecyclerOptions<Product> recyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product.class).setLifecycleOwner(this).build();
        MainActivityAdapter adapter = new MainActivityAdapter(this, recyclerOptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void initWidgets() {
        recyclerView=findViewById(R.id.recyclerView);
    }
}
