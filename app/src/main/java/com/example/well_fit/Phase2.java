package com.example.well_fit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AboutUserUi.ExerciseFragment;
import AboutUserUi.RestFragment;
import AboutUserUi.EndFragment;

public class Phase2 extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<String> exerciseIds;
    private List<Map<String, String>> exercises;
    private int currentIndex = 0;
    private boolean showExerciseFragment = true;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase2);

        db = FirebaseFirestore.getInstance();
        exerciseIds = new ArrayList<>();
        exercises = new ArrayList<>();

        // Get the category ID from the intent
        categoryId = getIntent().getStringExtra("category_id");

        if (categoryId != null) {
            Toast.makeText(this, categoryId, Toast.LENGTH_SHORT).show();
            fetchExercises();
        } else {
            Toast.makeText(this, "Category ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchExercises() {
        db.collection("homeworkout").document(categoryId)
                .collection("workout").document("workout")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            exerciseIds = (List<String>) document.get("id");
                            Toast.makeText(this, exerciseIds.toString(), Toast.LENGTH_SHORT).show();
                            fetchExerciseDetails();
                        } else {
                            Toast.makeText(this, "No exercises found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch exercises", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchExerciseDetails() {
        for (String id : exerciseIds) {
            db.collection("Exercise").document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, String> details = new HashMap<>();
                        details.put("name", document.getString("name"));
                        details.put("reps", document.getString("reps"));
                        details.put("imageUrl", document.getString("imageUrl"));
                        exercises.add(details);

                        if (exercises.size() == exerciseIds.size()) {
                            displayNextFragment();
                        }
                    } else {
                        Toast.makeText(this, "Exercise details not found for ID: " + id, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to fetch exercise details for ID: " + id, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void displayNextFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;

        if (currentIndex >= exercises.size()) {
            fragment = new EndFragment(); // Display EndFragment when all exercises are done
        } else if (showExerciseFragment) {
            fragment = new ExerciseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", exercises.get(currentIndex).get("name"));
            bundle.putString("reps", exercises.get(currentIndex).get("reps"));
            bundle.putString("imageUrl", exercises.get(currentIndex).get("imageUrl"));
            fragment.setArguments(bundle);
            currentIndex++;
        } else {
            fragment = new RestFragment();
        }

        showExerciseFragment = !showExerciseFragment;
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}
