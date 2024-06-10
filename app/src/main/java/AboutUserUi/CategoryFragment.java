package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import Models.Suggest;
import UI.Phase1a;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private FirebaseFirestore db;
    String cat;
    CircleImageView strength, calisthenics, home, barbell, hiit, stretching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        strength = view.findViewById(R.id.c1);
        calisthenics = view.findViewById(R.id.c2);
        hiit = view.findViewById(R.id.c5);
        stretching = view.findViewById(R.id.c3);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        loadImageFromFirestore();

        cat = "Category";

        // Set click listeners for each CircleImageView
        setClickListeners();

        return view;
    }

    private void loadImageFromFirestore() {
        db.collection("user_ui")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (isAdded() && getContext() != null) { // Check if fragment is added and has a valid context
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                loadImage(document, "strength", strength);
                                loadImage(document, "calisthenics", calisthenics);
                                loadImage(document, "hiit", hiit);
                                loadImage(document, "stretching", stretching);
                            }
                        } else {
                            Log.e(TAG, "Fragment not attached or context is null");
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void loadImage(QueryDocumentSnapshot document, String fieldName, CircleImageView imageView) {
        if (document.contains(fieldName)) {
            String imageUrl = document.getString(fieldName);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (isAdded() && getContext() != null) { // Ensure fragment is added and context is not null
                    Glide.with(getContext())
                            .load(imageUrl)
                            .into(imageView);
                    Log.d(TAG, "Loaded image for " + fieldName + ": " + imageUrl);
                } else {
                    Log.w(TAG, "Fragment not attached or context is null for field: " + fieldName);
                }
            } else {
                Log.w(TAG, "Image URL is null or empty for field: " + fieldName);
            }
        } else {
            Log.w(TAG, "Field not found in document: " + fieldName);
        }
    }

    private void setClickListeners() {
        strength.setOnClickListener(v -> onCategoryClicked("strength"));
        calisthenics.setOnClickListener(v -> onCategoryClicked("calisthenics"));
        hiit.setOnClickListener(v -> onCategoryClicked("hiit"));
        stretching.setOnClickListener(v -> onCategoryClicked("stretching"));
    }

    private void onCategoryClicked(String category) {
        Intent intent = new Intent ( getContext (), Phase1a.class );
        intent.putExtra ( "category_id",  category );
        intent.putExtra ( "type",  cat );
        startActivity ( intent );
    }
}
