package AboutUserUi;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private FirebaseFirestore db;
    CircleImageView strength, calisthenics, home, barbell, hiit, stretching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        strength = view.findViewById(R.id.c1);
        calisthenics = view.findViewById(R.id.c2);
        home = view.findViewById(R.id.c4);
        barbell = view.findViewById(R.id.c6);
        hiit = view.findViewById(R.id.c5);
        stretching = view.findViewById(R.id.c3);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        loadImageFromFirestore();

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
                                loadImage(document, "home", home);
                                loadImage(document, "barbell", barbell);
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
}
