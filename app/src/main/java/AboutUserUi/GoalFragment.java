package AboutUserUi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.well_fit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoalFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    TextView beginner, intermediate, advanced;

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_goal , container , false );
        beginner = view.findViewById ( R.id.beginner );
        intermediate = view.findViewById ( R.id.intermediate );
        advanced = view.findViewById ( R.id.advanced );

        mAuth = FirebaseAuth.getInstance ( );
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser ( );
        if ( currentUser != null ) {
            userId = currentUser.getUid ( );
            checkUserAndSetupButtons ( );
        } else {
            //not logged in
            Toast.makeText ( getContext ( ) , "User not logged in" , Toast.LENGTH_SHORT ).show ( );
        }
        return view;
    }
    private void checkUserAndSetupButtons() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
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
        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginner.setTextColor( Color.WHITE);
                beginner.setBackground(getResources().getDrawable(R.drawable.txt));

                intermediate.setTextColor(Color.BLACK);
                intermediate.setBackground(getResources().getDrawable(R.drawable.txtbg));

                advanced.setTextColor(Color.BLACK);
                advanced.setBackground(getResources().getDrawable(R.drawable.txtbg));

                saveGenderToFirestore(userRef, "Weight loss");
            }
        });

        intermediate.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                intermediate.setTextColor ( Color.WHITE );
                intermediate.setBackground ( getResources ( ).getDrawable ( R.drawable.txt ) );

                beginner.setTextColor ( Color.BLACK );
                beginner.setBackground ( getResources ( ).getDrawable ( R.drawable.txtbg ) );

                advanced.setTextColor ( Color.BLACK );
                advanced.setBackground ( getResources ( ).getDrawable ( R.drawable.txtbg ) );
                saveGenderToFirestore(userRef, "Gain muscle");
            }
        } );

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advanced.setTextColor(Color.WHITE);
                advanced.setBackground(getResources().getDrawable(R.drawable.txt));

                beginner.setTextColor(Color.BLACK);
                beginner.setBackground(getResources().getDrawable(R.drawable.txtbg));
                intermediate.setTextColor(Color.BLACK);
                intermediate.setBackground(getResources().getDrawable(R.drawable.txtbg));

                saveGenderToFirestore(userRef, "Improve fitness");
            }
        });
    }

    private void saveGenderToFirestore(DocumentReference userRef, String gender) {
        userRef.update("goal", gender).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Gender updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update level", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}