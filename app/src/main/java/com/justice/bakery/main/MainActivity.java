package com.justice.bakery.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.justice.bakery.LoginActivity;
import com.justice.bakery.Product;
import com.justice.bakery.ProductEntryActivity;
import com.justice.bakery.R;
import com.justice.bakery.SetupActivity;
import com.justice.bakery.SortByFragment;
import com.justice.bakery.cart.CartActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //////////////////DRAWER LAYOUT////////////////////////

    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private RecyclerView recyclerView;
    private SortByFragment sortByFragment;
    private FloatingActionButton fob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        setOnClickListeners();
        setPermissions();
        initAdapter();
        initNavigationDrawer();

        sortByFragment.sortByInterface(new SortByFragment.SetupInterface() {
            @Override
            public void searchFor(int from, int to) {
                Query query = FirebaseFirestore.getInstance().collection("product").whereGreaterThan("price", from).whereLessThan("price", to);
                FirestoreRecyclerOptions<Product> recyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product.class).setLifecycleOwner(MainActivity.this).build();
                MainActivityAdapter adapter = new MainActivityAdapter(MainActivity.this, recyclerOptions);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }
        });

    }

    private void setOnClickListeners() {
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductEntryActivity.class));
            }
        });
    }

    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void initAdapter() {
        Query query = FirebaseFirestore.getInstance().collection("product");
        FirestoreRecyclerOptions<Product> recyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product.class).setLifecycleOwner(this).build();
        MainActivityAdapter adapter = new MainActivityAdapter(this, recyclerOptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }


    private void initNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData(String category) {

        Query query = FirebaseFirestore.getInstance().collection("product").whereEqualTo("category", category);
        FirestoreRecyclerOptions<Product> recyclerOptions = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product.class).setLifecycleOwner(this).build();
        MainActivityAdapter adapter = new MainActivityAdapter(this, recyclerOptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.allMenu:
                initAdapter();
                break;

            case R.id.cakeMenu:
                setData("Cake");
                break;
            case R.id.pastriesMenu:
                setData("Pastries");
                break;
            case R.id.cookiesMenu:
                setData("Cookies");
                break;
            case R.id.breadMenu:
                setData("Bread");
                break;
            case R.id.cartMenu:
                startActivity(new Intent(this, CartActivity.class));
                break;


        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initWidgets() {
        recyclerView = findViewById(R.id.recyclerView);
        sortByFragment = new SortByFragment();
        fob = findViewById(R.id.fob);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        FirebaseFirestore.getInstance().collection("user_setup").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    startActivity(new Intent(MainActivity.this, SetupActivity.class));
                    finish();
                }

            }
        });

    }


}
