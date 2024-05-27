package AboutUserUi;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinalFragment extends Fragment {
    private static final String TAG = "FinalFragment";
    private FirebaseFirestore db;

    ImageView yoga;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_final, container, false);
        yoga = view.findViewById ( R.id.i1 );
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
                                loadImage(document, "yoga", yoga);

                            }
                        } else {
                            Log.e(TAG, "Fragment not attached or context is null");
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void loadImage(QueryDocumentSnapshot document, String fieldName, ImageView imageView) {
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
