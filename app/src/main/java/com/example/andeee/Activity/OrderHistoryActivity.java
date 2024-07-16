package com.example.andeee.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andeee.Adapter.FoodListAdapter;
import com.example.andeee.Adapter.OrderHistoryAdapter;
import com.example.andeee.Domain.Foods;
import com.example.andeee.Domain.Order;
import com.example.andeee.R;
import com.example.andeee.databinding.ActivityListFoodsBinding;
import com.example.andeee.databinding.ActivityOrderHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private RecyclerView.Adapter adapterHistory;
    private ArrayList<Order> orderList;
    ActivityOrderHistoryBinding binding;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initList();
        setVariable();
    }

    private void setVariable() {

    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Orders");
        ArrayList<Order> list = new ArrayList<>();

        Query query;
        SharedPreferences preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        userEmail = preferences.getString("email", "default_value");

        query=myRef.orderByChild("userEmail").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Order.class));
                    }
                    if(list.size()>0){
                        binding.orderHistoryRecyclerView.setLayoutManager(new GridLayoutManager
                                (OrderHistoryActivity.this, 2));
                        adapterHistory = new OrderHistoryAdapter(list);
                        binding.orderHistoryRecyclerView.setAdapter(adapterHistory);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
