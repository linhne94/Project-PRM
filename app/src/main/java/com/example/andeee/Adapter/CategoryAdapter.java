package com.example.andeee.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.andeee.Activity.ListFoodsActivity;
import com.example.andeee.Domain.Category;
import com.example.andeee.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {

        holder.titleTxt.setText(items.get(position).getName());

        switch (position) {
            case 0: {
                holder.pic.setBackgroundResource(R.drawable.cat_0_background);
                break;
            }
            case 1: {
                holder.pic.setBackgroundResource(R.drawable.cat_1_background);
                break;
            }
            case 2: {
                holder.pic.setBackgroundResource(R.drawable.cat_2_background);
                break;
            }
            case 3: {
                holder.pic.setBackgroundResource(R.drawable.cat_3_background);
                break;
            }
            case 4: {
                holder.pic.setBackgroundResource(R.drawable.cat_4_background);
                break;
            }
            case 5: {
                holder.pic.setBackgroundResource(R.drawable.cat_5_background);
                break;
            }
            case 6: {
                holder.pic.setBackgroundResource(R.drawable.cat_6_background);
                break;
            }
            case 7: {
                holder.pic.setBackgroundResource(R.drawable.cat_7_background);
                break;
            }

        }

        int drawableResourseId = context.getResources().
                getIdentifier(items.get(position).
                        getImagePath(), "drawable",
                        holder.itemView.getContext().getPackageName());
        Glide.with(context).
                load(drawableResourseId) // Set error placeholder drawable
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ListFoodsActivity.class);
                intent.putExtra("CategoryId", items.get(position).getId());

                intent.putExtra("CategoryName", items.get(position).getName());
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.catNameTxt);
            pic = itemView.findViewById(R.id.imgCat);

        }
    }
}
