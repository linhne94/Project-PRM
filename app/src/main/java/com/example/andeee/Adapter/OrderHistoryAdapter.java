package com.example.andeee.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andeee.Activity.DetailActivity;
import com.example.andeee.Domain.Order;
import com.example.andeee.R;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.viewholder> {
    private ArrayList<Order> orderList;
    Context context;

    public OrderHistoryAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,
                parent, false);
        return new viewholder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.viewholder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderDate.setText("Date: " + order.getDateOrder());
        holder.orderTotal.setText("Total: $" + order.getTotalFee());
        holder.orderItems.setText("Items: " + TextUtils.join(", ", order.getItems()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", (CharSequence) orderList.get(position));
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderItems;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderItems = itemView.findViewById(R.id.orderItems);
        }
    }
}