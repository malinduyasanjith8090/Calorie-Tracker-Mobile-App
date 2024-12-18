package com.example.calorieapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealListActivity extends AppCompatActivity {

    private static final String TAG = "MealListActivity";
    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        recyclerView = findViewById(R.id.recyclerViewMeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveAllMeals();
    }

    private void retrieveAllMeals() {
        FirestoreHelper.getAllMeals(new FirestoreHelper.MealsCallback() {
            @Override
            public void onMealsReceived(List<Meal> meals) {
                Log.d(TAG, "Received meals: " + meals);
                mealAdapter = new MealAdapter(meals, meal -> deleteMeal(meal));
                recyclerView.setAdapter(mealAdapter);
            }
        });
    }

    private void deleteMeal(Meal meal) {
        // Remove meal from Firestore
        FirestoreHelper.deleteMeal(meal.getId(), new FirestoreHelper.DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                Log.d(TAG, "Meal deleted successfully: " + meal.getName());
                // Remove the meal from the adapter
                mealAdapter.removeMeal(meal);
            }

            @Override
            public void onDeleteFailure(Exception e) {
                Log.e(TAG, "Error deleting meal: " + meal.getName(), e);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Handle configuration changes here if needed
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save any state if needed
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore any state if needed
    }
}
