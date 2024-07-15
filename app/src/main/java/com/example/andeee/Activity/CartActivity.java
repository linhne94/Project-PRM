package com.example.andeee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.andeee.Adapter.CartAdapter;

import com.example.andeee.Domain.Foods;
import com.example.andeee.Helper.ManagmentCart;
import com.example.andeee.R;
import com.example.andeee.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());
        managmentCart= new ManagmentCart(this);

        setVariable();
        calculateCart();
        initList();
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void setVariable() {

        binding.backBtn.setOnClickListener(v -> finish());
        binding.button5.setOnClickListener(v -> {
            Toast.makeText(CartActivity.this, "Order success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("total", String.valueOf(total));
            intent.putExtra("listCard", managmentCart.getListCart());
            startActivity(intent);

        });
    }

    private void calculateCart() {
        double percentTax = 0.02; // percent 2% tax
        double delivery = 10; // 10 dollar

        tax = Math.round(managmentCart.getTotalFee() * percentTax*100) /100;
         total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) /100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}