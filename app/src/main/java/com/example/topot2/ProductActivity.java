package com.example.topot2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.topot2.databinding.ActivityProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ProductActivity extends AppCompatActivity {


    ActivityProductBinding binding;
    Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        product = SingletonRepo.getInstance().getProductInfo();
        if(product == null) finish();

        updateUi();

        binding.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBooking)
                    startCancel();
                else
                    startBooking();
            }
        });
    }

    Boolean isBooking = false;
    String bookingId = "";
    void updateUi(){

        Intent intent = getIntent();
        int position = intent.getIntExtra("POS",0);
        String type = intent.getStringExtra("TYPE");
        if(type.equals("Booking")){
            binding.bookBtn.setText("Cancel booking");
            bookingId = intent.getStringExtra("BOOKING_ID");
            isBooking = true;
        }

        product.setProdOder(position);
        int imgId = 0;
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

        binding.prodImage.setImageResource(imgId);

        binding.prodTitle.setText(product.getName());
        binding.prodPrice.setText("Price : ₹" + product.getPrice() );
        binding.prodType.setText("Type : " + product.getType() );
        binding.prodPdesc.setText("Description : " + product.getDesc() );

    }

    FirebaseFirestore db;
    void startBooking(){

        Toast.makeText(this, "Booking..", Toast.LENGTH_SHORT).show();

        Booking booking = new Booking();
        booking.setProduct(product);
        booking.setBookingId(String.valueOf(System.currentTimeMillis()));
        booking.setTime(String.valueOf(System.currentTimeMillis()));

        SharedPreferences pref = getSharedPreferences("PREF",MODE_PRIVATE);
        String uid = pref.getString("UID","");
        booking.setUserId(uid);

        db = FirebaseFirestore.getInstance();
        db.collection("Bookings")
                .document(booking.bookingId)
                .set(booking)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductActivity.this, "Booked successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    void startCancel(){

        db = FirebaseFirestore.getInstance();

        Toast.makeText(this, "Cancelling booking... ", Toast.LENGTH_SHORT).show();


        db.collection("Bookings")
                .document(bookingId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductActivity.this, "Cancelling booking... "+ bookingId, Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(ProductActivity.this, "Cancelling booking... "+ bookingId, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
