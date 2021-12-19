package com.example.topot2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topot2.databinding.ProductItemBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {

    List<Product> items;
    Context context;
    ProductClick listner;
    String type;

    public ProductAdapter(List<Product> items, Context context, ProductClick listner, String type) {
        this.items = items;
        this.context = context;
        this.listner = listner;
        this.type = type;
    }

    public ProductAdapter(List<Product> items, Context context, ProductClick listner) {
        this.items = items;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductVH(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false),listner);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {

        Product item = items.get(position);
        holder.binding.prodName.setText(item.getName());
        holder.binding.prodPrice.setText(item.getPrice() + "/-");

        int imgId = 0;

        if(item.getProdOder() != -1)
            position = item.getProdOder();

        if(position % 6 == 0)
            imgId = R.drawable.pot6;
        else if(position % 5 == 0)
            imgId = R.drawable.pot5;
        else if(position % 4 == 0)
            imgId = R.drawable.pot4;
        else if(position % 3 == 0)
            imgId = R.drawable.pot3;
        else if(position % 2 == 0)
            imgId = R.drawable.pot2;
        else if(position % 1 == 0)
            imgId = R.drawable.pot1;

        holder.binding.prodImage.setImageResource(imgId);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void updateList(List<Product> newItems){
        this.items = newItems;
        notifyDataSetChanged();
    }

    class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProductItemBinding binding;
        ProductClick listner;
        public ProductVH(@NonNull ProductItemBinding binding, ProductClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onProductClick(items.get(getAdapterPosition()),type, getAdapterPosition());
        }
    }

    public interface ProductClick{
        void onProductClick(Product product,String type, int position);
    }
}
