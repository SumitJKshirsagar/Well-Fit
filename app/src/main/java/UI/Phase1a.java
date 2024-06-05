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
    TextView exercise;
    private ImageView back, work;
    private Button start;
    private String categoryId;

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

        // Load image based on category ID
        if (categoryId != null) {
            loadImageFromFirestore(categoryId);
            loadExercise(categoryId);
            exercise.setText(categoryId);
        } else {
            Log.e(TAG, "No category ID found in intent");
            Toast.makeText(this, "No category ID found in intent", Toast.LENGTH_SHORT).show();
        }

        start.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( Phase1a.this, Phase2.class );
                intent.putExtra ( "category_id", categoryId );
                startActivity ( intent );
            }
        } );
    }

    private void loadExercise(String categoryId) {
        db.collection("homeworkout")
                .document(categoryId)
                .collection("workout")
                .document("workout") // Assuming 'workout' is the document ID for a specific workout
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> exerciseIds = (List<String>) documentSnapshot.get("id");

                        if (exerciseIds != null) {
                            for (String exerciseId : exerciseIds) {
                                db.collection("Exercise")
                                        .document(exerciseId)
                                        .get()
                                        .addOnSuccessListener(exerciseSnapshot -> {
                                            if (exerciseSnapshot.exists()) {
                                                String name = exerciseSnapshot.getString("name");
                                                String sets = exerciseSnapshot.getString("set");
                                                String reps = exerciseSnapshot.getString("reps");
                                                String imageUrl = exerciseSnapshot.getString("imageUrl");

                                                if (name != null && sets != null && reps != null && imageUrl != null) {
                                                    displayList.add(new Display(name, sets, reps, imageUrl));
                                                    String toastMessage = "Exercise: " + name + "\nSets: " + sets + "\nReps: " + reps + "\nImage URL: " + imageUrl;
                                                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(this, "One of the required fields is null", Toast.LENGTH_SHORT).show();
                                                    Log.e(TAG, "One of the required fields is null");
                                                }
                                                displayAdapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(this, "Exercise document not found for ID: " + exerciseId, Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Exercise document not found for ID: " + exerciseId);
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
}
