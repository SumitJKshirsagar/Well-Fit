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

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Phase1 extends AppCompatActivity {
    private static final String TAG = "Phase1";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uid;
    TextView exercise;
    private ImageView back, work;
    private Button start;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase1);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        back = findViewById(R.id.back);
        work = findViewById(R.id.phase1);
        start = findViewById(R.id.start);
        exercise = findViewById(R.id.exerciseName);

        // Get category ID from intent
        categoryId = getIntent().getStringExtra("category_id");
        back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( Phase1.this , MainActivity.class );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        } );

        // Load image based on category ID
        if (categoryId != null) {
            loadImageFromFirestore(categoryId);
            Toast.makeText ( this , categoryId , Toast.LENGTH_SHORT ).show ( );
            exercise.setText ( categoryId );
        } else {
            Log.e(TAG, "No category ID found in intent");
        }
    }

    private void loadImageFromFirestore(String categoryId) {
        db.collection("user_ui")
                .document(categoryId) // Assuming each category is a document within 'user_ui' collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Assuming the document has an 'imageUrl' field
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(Phase1.this)
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
