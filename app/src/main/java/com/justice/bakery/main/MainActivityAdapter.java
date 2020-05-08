package com.justice.bakery.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.justice.bakery.Product;
import com.justice.bakery.ProductActivity;
import com.justice.bakery.R;

public class MainActivityAdapter extends FirestoreRecyclerAdapter<Product, MainActivityAdapter.ViewHolder> {
    private Context context;

    public MainActivityAdapter(Context context, @NonNull FirestoreRecyclerOptions options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Product model) {
        Glide.with(context).load(model.getImage()).into(holder.imageView);
        holder.productNameTxtView.setText(model.getProductName());
        holder.priceTxtView.setText(model.getPrice() + "");

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationClass.documentSnapshot = getSnapshots().getSnapshot(position);
                context.startActivity(new Intent(context, ProductActivity.class));
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView productNameTxtView;
        private TextView priceTxtView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            productNameTxtView = itemView.findViewById(R.id.productNameTxtView);
            priceTxtView = itemView.findViewById(R.id.priceTxtView);

        }
    }
}
