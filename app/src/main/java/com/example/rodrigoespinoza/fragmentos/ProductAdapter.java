package com.example.rodrigoespinoza.fragmentos;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodrigoespinoza.fragmentos.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;
    public ProductAdapter(Context context, List<Product> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product product = list.get(position);
        viewHolder.textName.setText(product.getStock());
        viewHolder.textStock.setText(product.getName());

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textStock;


        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.single_name);
            textStock = itemView.findViewById(R.id.single_stock);



        }
    }
}
