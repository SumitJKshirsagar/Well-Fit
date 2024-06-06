package UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import Models.Display;
import com.example.well_fit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import adapters.DisplayAdapter;

public class Phase1a extends AppCompatActivity {
    private static final String TAG = "Phase1";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uid;
    private TextView exercise;
    private ImageView back, work;
    private Button start;
    private String categoryId;
    private String userLevel;
    private Intent intent;

    private RecyclerView display;
    private DisplayAdapter displayAdapter;
    private List<Display> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase1a);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        back = findViewById(R.id.back);
        work = findViewById(R.id.phase1);
        start = findViewById(R.id.start);
        exercise = findViewById(R.id.exerciseName);
        display = findViewById(R.id.display);
        displayList = new ArrayList<>();
        displayAdapter = new DisplayAdapter(Phase1a.this, displayList);
        display.setAdapter(displayAdapter);

        display.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        display.setAdapter(displayAdapter);

        // Get category ID from intent
        categoryId = getIntent().getStringExtra("category_id");

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Phase1a.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        if (categoryId != null) {
            loadImageFromFirestore(categoryId);
            loadUserLevelAndExercises(categoryId);
        } else {
            Log.e(TAG, "No category ID found in intent");
            Toast.makeText(this, "No category ID found in intent", Toast.LENGTH_SHORT).show();
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start Phase2 activity
                intent = new Intent(Phase1a.this, Phase2.class);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("user_id", uid);
                startActivity(intent);
            }
        });

    }

    private void loadUserLevelAndExercises(String categoryId) {
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userLevel = documentSnapshot.getString("level");
                if (userLevel != null) {
                    updateUserHistory(categoryId, userLevel);
                    loadExercises(categoryId, userLevel);
                    exercise.setText(categoryId);
                } else {
                    Toast.makeText(this, "User level not found", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "User level not found");
                }
            } else {
                Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "User document not found");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error getting user level: ", e);
        });
    }

    private void loadExercises(String categoryId, String userLevel) {
        db.collection("homeworkout")
                .document(categoryId)
                .collection("workout")
                .document("workout")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> exerciseIds = (List<String>) documentSnapshot.get("id");

                        if (exerciseIds != null) {
                            for (String exerciseId : exerciseIds) {
                                // Query the Exercise collection based on user's level
                                db.collection("Exercise")
                                        .document(exerciseId)
                                        .collection("Level")
                                        .document(userLevel)
                                        .get()
                                        .addOnSuccessListener(exerciseSnapshot -> {
                                            if (exerciseSnapshot.exists()) {
                                                String name = exerciseSnapshot.getString("name");
                                                String sets = exerciseSnapshot.getString("set");
                                                String reps = exerciseSnapshot.getString("reps");
                                                String imageUrl = exerciseSnapshot.getString("imageUrl");

                                                if (name != null && sets != null && reps != null && imageUrl != null) {
                                                    displayList.add(new Display(name, sets, reps, imageUrl));
                                                } else {
                                                    Toast.makeText(this, "One of the required fields is null", Toast.LENGTH_SHORT).show();
                                                    Log.e(TAG, "One of the required fields is null");
                                                }
                                                displayAdapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "Exercise not found for user level: " + userLevel);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error getting exercise document: ", e);
                                        });
                            }
                        } else {
                            Toast.makeText(this, "No exercises found for the workout", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No exercises found for the workout");
                        }
                    } else {
                        Toast.makeText(this, "Workout document not found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Workout document not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting workout document: ", e);
                });
    }


    private void loadImageFromFirestore(String categoryId) {
        db.collection("homeworkout")
                .document(categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(Phase1a.this)
                                        .load(imageUrl)
                                        .into(work);
                                Log.d(TAG, "Loaded image: " + imageUrl);
                            } else {
                                Log.w(TAG, "Image URL is null or empty for category: " + categoryId);
                            }
                        } else {
                            Log.w(TAG, "No such document for category: " + categoryId);
                        }
                    } else {
                        Log.e(TAG, "Error getting document: ", task.getException());
                    }
                });
    }

    private void updateUserHistory(String categoryId, String userLevel) {
        // Create a reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(uid);

        // Get the current history list from Firestore
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> history = (List<String>) documentSnapshot.get("history");

                // If history is null, initialize a new ArrayList
                if (history == null) {
                    history = new ArrayList<>();
                }

                // Add the new category ID to the history list
                history.add(categoryId+" "+userLevel);

                // Update the user's document with the updated history list
                userRef.update("history", history)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User history updated successfully");
                            } else {
                                Log.e(TAG, "Failed to update user history", task.getException());
                            }
                        });
            } else {
                Log.e(TAG, "User document does not exist");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error getting user document", e);
        });
    }

}
