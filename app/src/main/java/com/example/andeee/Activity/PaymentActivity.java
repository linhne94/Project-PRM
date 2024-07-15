package com.example.andeee.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andeee.Domain.Foods;
import com.example.andeee.Helper.ManagmentCart;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import com.example.andeee.R;
import com.example.andeee.databinding.ActivityLoginBinding;
import com.example.andeee.databinding.ActivityPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    private TextInputEditText phoneInput, addressInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String userEmail = preferences.getString("email", "default_value");
        binding.emailInput.setText(userEmail);
        Intent intent = getIntent();
        binding.totalAmountText.setText("Total Amount: $" + intent.getStringExtra("total"));
        phoneInput = findViewById(R.id.phoneInput); // Initialize phoneInput
        addressInput = findViewById(R.id.addressInput);
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.confirmPaymentBtn.setOnClickListener(v -> {

            Intent intent = getIntent();
            double totalPrice = Double.parseDouble(intent.getStringExtra("total"));
            // Log the start of the payment process
            Log.d("PaymentActivity", "Confirm payment button clicked");

            // Retrieve the phone number
            String phoneNumber = phoneInput.getText().toString();
            String address = addressInput.getText().toString();

            Log.d("PaymentActivity", "Phone number: " + address);

            String paymentMethod = "Credit Card"; // Or retrieve from input
            String paymentStatus = "Pending"; // Or set as needed

            // Log before placing the order
            Log.d("PaymentActivity", "Placing order with phoneNumber: " + phoneNumber + ", paymentMethod: " + paymentMethod + ", paymentStatus: " + paymentStatus);
            new ManagmentCart(this).placeOrder(phoneNumber, address, paymentMethod, paymentStatus, totalPrice);
            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            // Log after placing the order
            Log.d("PaymentActivity", "Order placed successfully");
        });
    }
}