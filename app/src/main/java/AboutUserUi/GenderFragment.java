package AboutUserUi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.well_fit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GenderFragment extends Fragment {

    private Button male, female;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender, container, false);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize buttons
        male = view.findViewById(R.id.button1);
        female = view.findViewById(R.id.button2);

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            checkUserAndSetupButtons();
        } else {
            // Handle case where there is no user logged in
            Toast.makeText(getActivity(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void checkUserAndSetupButtons() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        setupButtons(userRef);
                    } else {
                        // Handle case where user document does not exist
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle failure to access Firestore
                    Toast.makeText(getActivity(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupButtons(DocumentReference userRef) {
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setTextColor(Color.WHITE);
                male.setBackground(getResources().getDrawable(R.drawable.txt));

                female.setTextColor(Color.BLACK);
                female.setBackground(getResources().getDrawable(R.drawable.txtbg));

                saveGenderToFirestore(userRef, "male");
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setTextColor(Color.WHITE);
                female.setBackground(getResources().getDrawable(R.drawable.txt));
                male.setTextColor(Color.BLACK);
                male.setBackground(getResources().getDrawable(R.drawable.txtbg));

                saveGenderToFirestore(userRef, "female");
            }
        });
    }

    private void saveGenderToFirestore(DocumentReference userRef, String gender) {
        userRef.update("gender", gender).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Gender updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update gender", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
