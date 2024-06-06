package UI;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.well_fit.R;
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
    private String userId; // Variable to store user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase2);

        db = FirebaseFirestore.getInstance();
        exerciseIds = new ArrayList<>();
        exercises = new ArrayList<>();

        // Get the category ID and user ID from the intent
        categoryId = getIntent().getStringExtra("category_id");
        userId = getIntent().getStringExtra("user_id"); // Assuming userId is passed via intent

        if (categoryId != null && userId != null) {
            fetchUserLevel(); // Fetch user level firs
        } else {
            Toast.makeText(this, "Category ID or User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserLevel() {
        db.collection("users").document(userId)
                .get().addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        DocumentSnapshot userDocument = userTask.getResult();
                        if (userDocument.exists()) {
                            String userLevel = userDocument.getString("level");
                            if (userLevel != null) {
                                fetchExercises(userLevel);
                            } else {
                                Toast.makeText(this, "User level not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch user document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchExercises(String userLevel) {
        db.collection("homeworkout").document(categoryId)
                .collection("workout").document("workout")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            exerciseIds = (List<String>) document.get("id");
                            fetchExerciseDetails(userLevel); // Pass user's level to fetchExerciseDetails
                        } else {
                            Toast.makeText(this, "No exercises found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch exercises", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchExerciseDetails(String userLevel) {
        for (String id : exerciseIds) {
            db.collection("Exercise").document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot exerciseDocument = task.getResult();
                    if (exerciseDocument.exists()) {
                        // Check if the exercise document has a subcollection "Level" instead of "levels"
                        db.collection("Exercise").document(id)
                                .collection("Level").document(userLevel).get()
                                .addOnCompleteListener(levelTask -> {
                                    if (levelTask.isSuccessful()) {
                                        DocumentSnapshot levelDocument = levelTask.getResult();
                                        if (levelDocument.exists()) {
                                            String name = levelDocument.getString("name");
                                            String reps = levelDocument.getString("reps");
                                            String imageUrl = levelDocument.getString("imageUrl");

                                            if (name != null && reps != null && imageUrl != null) {
                                                Map<String, String> details = new HashMap<>();
                                                details.put("name", name);
                                                details.put("reps", reps);
                                                details.put("imageUrl", imageUrl);
                                                exercises.add(details);

                                                if (exercises.size() == exerciseIds.size()) {
                                                    displayNextFragment();
                                                }
                                            } else {
                                                Toast.makeText(this, "One of the required fields is null for exercise ID: " + id, Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "One of the required fields is null for exercise ID: " + id);
                                            }
                                        } else {
                                            Toast.makeText(this, "Exercise details not found for level: " + userLevel + " and ID: " + id, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "Failed to fetch exercise details for level: " + userLevel + " and ID: " + id, Toast.LENGTH_SHORT).show();
                                    }
                                });
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