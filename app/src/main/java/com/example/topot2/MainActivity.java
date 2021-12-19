package com.example.topot2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.topot2.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductClick {

    private FirebaseAuth mAuth;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        fetchProducts();
        fetchBookings();

        binding.mainRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchProducts();
                fetchBookings();
            }
        });

        binding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                SharedPreferences pref = getSharedPreferences("PREF",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("LOGIN",false);
                editor.putString("UID","");
                editor.apply();

                Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                },800);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProducts();
        fetchBookings();

    }

    FirebaseFirestore db;

    void fetchProducts() {

        db = FirebaseFirestore.getInstance();
        showloading();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<Product> list = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Product item = doc.toObject(Product.class);
                                list.add(item);
                            }
                            updateRecycler(list);
                        }
                    }
                });
    }

    List<Booking> bookings = new ArrayList<>();
    void fetchBookings(){

        SharedPreferences pref = getSharedPreferences("PREF",MODE_PRIVATE);
        String uid = pref.getString("UID","");

        db = FirebaseFirestore.getInstance();
        showloading();
        db.collection("Bookings")
                .whereEqualTo("userId",uid )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideLoading();
                        if (task.isSuccessful()) {

                            List<Product> list = new ArrayList<>();
                            bookings = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                Booking item = doc.toObject(Booking.class);
                                list.add(item.getProduct());
                                bookings.add(item);
                            }
                            updateBookingRecycler(list);
                        }
                    }
                });

    }

    ProductAdapter productAdapter;
    void updateRecycler(List<Product> list){
        if(productAdapter == null){
            productAdapter = new ProductAdapter(list,this,this,"Product");
            binding.mainRecycler.setAdapter(productAdapter);
            binding.mainRecycler.setLayoutManager(new GridLayoutManager(this,2));
        } else productAdapter.updateList(list);

    }

    ProductAdapter bookingAdapter;
    void updateBookingRecycler(List<Product> list){
        if(bookingAdapter == null){
            bookingAdapter = new ProductAdapter(list,this,this,"Booking");
            binding.bookingRecycler.setAdapter(bookingAdapter);
            binding.bookingRecycler.setLayoutManager(new GridLayoutManager(this,2));
        } else bookingAdapter.updateList(list);

    }

    void showloading(){ binding.mainRefresh.setRefreshing(true);}
    void hideLoading(){ binding.mainRefresh.setRefreshing(false);}

    @Override
    public void onProductClick(Product product, String type,int pos) {

        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.putExtra("TYPE",type);
        if(type.equals("Booking")){
            Booking item = bookings.get(pos);
            intent.putExtra("BOOKING_ID",item.getBookingId());
            intent.putExtra("POS",product.getProdOder());
        }
        intent.putExtra("POS",pos);
        SingletonRepo.getInstance().setProductInfo(product);
        startActivity(intent);
    }
}