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
    private String userId, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase2);

        db = FirebaseFirestore.getInstance();
        exerciseIds = new ArrayList<>();
        exercises = new ArrayList<>();

        // Get the category ID and user ID from the intent
        categoryId = getIntent().getStringExtra("category_id");
        userId = getIntent().getStringExtra("user_id");
        type = getIntent().getStringExtra("type");

        if (categoryId != null && userId != null && type != null) {
            fetchUserLevel();
        } else {
            Toast.makeText(this, "Category ID, User ID, or Type not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserLevel() {
        db.collection("users").document(userId).get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                DocumentSnapshot userDocument = userTask.getResult();
                if (userDocument.exists()) {
                    String userLevel = userDocument.getString("level");
                    if (userLevel != null) {
                        fetchExercises(userLevel);
                    } else {
                        showToast("User level not found");
                    }
                } else {
                    showToast("User document not found");
                }
            } else {
                showToast("Failed to fetch user document");
            }
        });
    }

    private void fetchExercises(String userLevel) {
        db.collection("homeworkout").document(type)
                .collection("workout").document(categoryId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String calorie = document.getString("calorie");
                            String time = document.getString("time");
                            String rest = document.getString("rest");
                            exerciseIds = (List<String>) document.get("ids");
                            fetchExerciseDetails(userLevel, calorie, time, rest);
                        } else {
                            showToast("No exercises found");
                        }
                    } else {
                        showToast("Failed to fetch exercises");
                    }
                });
    }

    private void fetchExerciseDetails(String userLevel, String calorie, String time, String rest) {
        for (String id : exerciseIds) {
            db.collection("Exercise").document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot exerciseDocument = task.getResult();
                    if (exerciseDocument.exists()) {
                        db.collection("Exercise").document(id)
                                .collection("Level").document(userLevel).get().addOnCompleteListener(levelTask -> {
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
                                                details.put("calorie", calorie);
                                                details.put("time", time);
                                                details.put("rest", rest);
                                                exercises.add(details);

                                                if (exercises.size() == exerciseIds.size()) {
                                                    displayNextFragment();
                                                }
                                            } else {
                                                logError("One of the required fields is null for exercise ID: " + id);
                                            }
                                        } else {
                                            showToast("Exercise details not found for level: " + userLevel + " and ID: " + id);
                                        }
                                    } else {
                                        showToast("Failed to fetch exercise details for level: " + userLevel + " and ID: " + id);
                                    }
                                });
                    } else {
                        showToast("Exercise details not found for ID: " + id);
                    }
                } else {
                    showToast("Failed to fetch exercise details for ID: " + id);
                }
            });
        }
    }

    public void displayNextFragment() {
        runOnUiThread(() -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;

            if (currentIndex >= exercises.size()) {
                fragment = new EndFragment();
                Bundle bundle = createBundle(currentIndex - 1);
                fragment.setArguments(bundle);
            } else if (showExerciseFragment) {
                fragment = new ExerciseFragment();
                Bundle bundle = createBundle(currentIndex);
                fragment.setArguments(bundle);
            } else {
                fragment = new RestFragment();
                Bundle bundle = createBundle(currentIndex);
                fragment.setArguments(bundle);
            }

            currentIndex++;
            showExerciseFragment = !showExerciseFragment;
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
        });
    }

    private Bundle createBundle(int index) {
        Bundle bundle = new Bundle();
        if (index >= 0 && index < exercises.size()) {
            Map<String, String> exerciseDetails = exercises.get(index);
            bundle.putString("name", exerciseDetails.get("name"));
            bundle.putString("reps", exerciseDetails.get("reps"));
            bundle.putString("imageUrl", exerciseDetails.get("imageUrl"));
            bundle.putString("calorie", exerciseDetails.get("calorie"));
            bundle.putString("time", exerciseDetails.get("time"));
            bundle.putString("category_id", categoryId);
            bundle.putString("rest", exerciseDetails.get("rest"));
        }
        return bundle;
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(Phase2.this, message, Toast.LENGTH_SHORT).show());
    }

    private void logError(String message) {
        runOnUiThread(() -> {
            Log.e(TAG, message);
            Toast.makeText(Phase2.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}
