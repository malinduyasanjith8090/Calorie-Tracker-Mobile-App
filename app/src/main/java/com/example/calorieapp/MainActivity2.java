package com.example.calorieapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;
    private static final String TAG = "MainActivity";

    private EditText mealName;
    private EditText mealType;
    private EditText calories;
    private ImageView mealPhoto;
    private Button capturePhoto;
    private Button chooseFromGallery;
    private Button addMeal;

    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealName = findViewById(R.id.editTextMealName);
        mealType = findViewById(R.id.editTextMealType);
        calories = findViewById(R.id.editTextCalories);
        mealPhoto = findViewById(R.id.imageViewMealPhoto);
        capturePhoto = findViewById(R.id.buttonCapturePhoto);
        chooseFromGallery = findViewById(R.id.buttonChooseFromGallery);
        addMeal = findViewById(R.id.buttonAddMeal);

        capturePhoto.setOnClickListener(view -> openCamera());
        chooseFromGallery.setOnClickListener(view -> openGallery());
        addMeal.setOnClickListener(view -> uploadMealData());

        // Restore photo URI if available
        if (savedInstanceState != null) {
            photoUri = savedInstanceState.getParcelable("photoUri");
            if (photoUri != null) {
                mealPhoto.setImageURI(photoUri);
            }
        }

        // Start listening for meal updates
        retrieveAllMeals();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mealPhoto.setImageBitmap(photo);

                // Convert Bitmap to Uri
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "MealPhoto", null);
                photoUri = Uri.parse(path);
            } else if (requestCode == REQUEST_GALLERY) {
                photoUri = data.getData();
                mealPhoto.setImageURI(photoUri);
            }
        }
    }

    private void uploadMealData() {
        String name = mealName.getText().toString();
        String type = mealType.getText().toString();
        String cal = calories.getText().toString();

        if (name.isEmpty() || type.isEmpty() || cal.isEmpty() || photoUri == null) {
            Toast.makeText(this, "Please fill all fields and add a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new meal object
        String mealId = String.valueOf(System.currentTimeMillis()); // Unique ID for the meal
        Meal meal = new Meal(name, type, cal, photoUri.toString());
        // Save the meal using FirestoreHelper
        FirestoreHelper.saveMeal(mealId, meal);

        // Display a confirmation message
        Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to MealListActivity after adding the meal
        Intent intent = new Intent(MainActivity2.this, MealListActivity.class);
        startActivity(intent);

        // Optionally, finish the current activity to remove it from the back stack
        finish();
    }

    // Retrieve all meals and listen for updates
    private void retrieveAllMeals() {
        FirestoreHelper.getAllMeals(new FirestoreHelper.MealsCallback() {
            @Override
            public void onMealsReceived(List<Meal> meals) {
                // Update your UI here with the list of meals
                Log.d(TAG, "Received meals: " + meals);
                // TODO: Update your RecyclerView or ListView to show the meals
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening for updates
        FirestoreHelper.removeMealListener();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the photo URI to the instance state
        outState.putParcelable("photoUri", photoUri);
    }
}
