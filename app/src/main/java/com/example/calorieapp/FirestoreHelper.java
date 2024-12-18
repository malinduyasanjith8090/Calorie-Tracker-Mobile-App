package com.example.calorieapp;

import android.util.Log;

import com.example.calorieapp.Meal;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ListenerRegistration mealListener;

    // Save or update meal data
    public static void saveMeal(String mealId, Meal meal) {
        db.collection("meals").document(mealId)
                .set(meal)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Meal successfully saved!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving meal", e));
    }

    // Retrieve meal data by mealId
    public static void getMeal(String mealId, MealCallback callback) {
        db.collection("meals").document(mealId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Meal meal = documentSnapshot.toObject(Meal.class);
                        if (meal != null) {
                            callback.onMealReceived(meal);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error getting meal", e));
    }

    // Retrieve all meals and listen for real-time updates
    public static void getAllMeals(MealsCallback callback) {
        mealListener = db.collection("meals").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            if (snapshots != null) {
                List<Meal> meals = new ArrayList<>();
                for (QueryDocumentSnapshot document : snapshots) {
                    Meal meal = document.toObject(Meal.class);
                    meal.setId(document.getId());  // Ensure the meal ID is set
                    meals.add(meal);
                }
                callback.onMealsReceived(meals);
            }
        });
    }

    // Stop listening for updates
    public static void removeMealListener() {
        if (mealListener != null) {
            mealListener.remove();
            mealListener = null;
        }
    }

    // Callback interfaces to return meal data
    public interface MealCallback {
        void onMealReceived(Meal meal);
    }

    public interface MealsCallback {
        void onMealsReceived(List<Meal> meals);
    }

    public static void deleteMeal(String mealId, DeleteCallback callback) {
        FirebaseFirestore.getInstance()
                .collection("meals")
                .document(mealId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(e -> callback.onDeleteFailure(e));
    }

    public interface DeleteCallback {
        void onDeleteSuccess();
        void onDeleteFailure(Exception e);
    }
}

