package com.justice.bakery.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.justice.bakery.Product;
import com.justice.bakery.R;
import com.justice.bakery.main.MainActivityAdapter;

public class CartActivityAdapter extends FirestoreRecyclerAdapter<Product, CartActivityAdapter.ViewHolder> {
    private Context context;


    public CartActivityAdapter(Context context, @NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product model) {
        holder.productNameTxtView.setText(model.getProductName());
        holder.priceTxtView.setText(model.getPrice());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTxtView;
        private TextView priceTxtView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxtView = itemView.findViewById(R.id.productNameTxtView);
            priceTxtView = itemView.findViewById(R.id.priceTxtView);

        }
    }
}
