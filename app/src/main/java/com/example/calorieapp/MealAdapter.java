package com.example.calorieapp;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList;
    private OnDeleteClickListener onDeleteClickListener;

    // Interface for handling delete clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, OnDeleteClickListener onDeleteClickListener) {
        this.mealList = mealList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName());
        holder.mealType.setText(meal.getType());
        holder.calories.setText(meal.getCalories() + " kcal");

        // Log the photo URL for debugging
        Log.d("MealAdapter", "Photo URL: " + meal.getPhotoUrl());

        // Load the photo using Glide with a placeholder
        Glide.with(holder.itemView.getContext())
                .load(meal.getPhotoUrl())
                .into(holder.mealPhoto);

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            // Call the listener to perform the delete action
            onDeleteClickListener.onDeleteClick(meal);
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    // Method to update the adapter after deletion
    public void removeMeal(Meal meal) {
        int position = mealList.indexOf(meal);
        if (position >= 0) {
            mealList.remove(position);
            notifyItemRemoved(position);  // This is an instance method, so it will work properly
        }
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, mealType, calories;
        ImageView mealPhoto, deleteButton;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.textMealName);
            mealType = itemView.findViewById(R.id.textMealType);
            calories = itemView.findViewById(R.id.textCalories);
            mealPhoto = itemView.findViewById(R.id.imageMealPhoto);
            deleteButton = itemView.findViewById(R.id.imageDeleteMeal);
        }
    }
}

