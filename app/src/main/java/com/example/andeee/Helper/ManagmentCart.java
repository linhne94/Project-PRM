package com.example.andeee.Helper;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.andeee.Domain.Foods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.andeee.Domain.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagmentCart {
    private DatabaseReference databaseReference;
    private Context context;
    private TinyDB tinyDB;
    private FirebaseAuth mAuth;



    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
        this.mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listpop.add(item);
        }
        String userEmail = getUserEmail();
        tinyDB.putListObject("CartList_" + userEmail, listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart() {
        String userEmail = getUserEmail();
        return tinyDB.getListObject("CartList_" + userEmail);
    }

    public Double getTotalFee() {
        ArrayList<Foods> listItem = getListCart();
        double fee = 0;
        for (int i = 0; i < listItem.size(); i++) {
            fee = fee + (listItem.get(i).getPrice() * listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    public void minusNumberItem(ArrayList<Foods> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() - 1);
        }
        String userEmail = getUserEmail();
        tinyDB.putListObject("CartList_" + userEmail, listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<Foods> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        String userEmail = getUserEmail();
        tinyDB.putListObject("CartList_" + userEmail, listItem);
        changeNumberItemsListener.change();
    }


    public void placeOrder(String phoneNumber, String address, String paymentMethod, String paymentStatus, double totalPrice ) {
        ArrayList<Foods> listItem = getListCart();
        double totalFee = getTotalFee();
        String userEmail = getUserEmail();

        if (!listItem.isEmpty() && !userEmail.isEmpty()) {
            DatabaseReference orderCountRef = databaseReference.child("orderCount");

            orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long orderCount = 0;
                    if (snapshot.exists()) {
                        orderCount = (long) snapshot.getValue();
                    }

                    // Tăng số thứ tự
                    orderCount++;

                    // Cập nhật số thứ tự mới
                    orderCountRef.setValue(orderCount);

                    // Tạo đơn hàng mới với số thứ tự tăng dần
                    Order order = new Order(String.valueOf(orderCount), userEmail, listItem, totalPrice, paymentMethod, paymentStatus, phoneNumber, address);
                    databaseReference.child("orders").child(String.valueOf(orderCount)).setValue(order)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    clearCart();
                                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    // Clear the cart after placing the order
                                    tinyDB.remove("listCard" + userEmail);
                                } else {
                                    Toast.makeText(context, "Failed to place order.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to get order count.", Toast.LENGTH_SHORT).show();
                }
            });
                } else {
            Toast.makeText(context, "Cart is empty or user is not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }


    public void clearCart() {
        String userEmail = getUserEmail();
        tinyDB.remove("CartList_" + userEmail);
        Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show();
    }



    private String getUserEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        }
        return "";
    }
}

