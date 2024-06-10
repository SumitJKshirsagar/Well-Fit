package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import Models.Category;
import UI.MainActivity;
import com.example.well_fit.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EndFragment extends Fragment {
    Button button;
    TextView exercise, total, time, calorie;
    private FirebaseFirestore db;
    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_end, container, false );
        button = view.findViewById ( R.id.btn);
        exercise = view.findViewById ( R.id.exercise );
        total = view.findViewById ( R.id.total );
        time = view.findViewById ( R.id.time );
        calorie = view.findViewById ( R.id.calorie );
        button.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( getContext (), MainActivity.class );
                startActivity ( intent );
            }
        } );

        loadDetails();
        return view;
    }

    private void loadDetails ( ) {
        db.collection("homeworkout").
                document ("category").
                collection ("workout").
                get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String sname = document.getString("name");
                            String simageUrl = document.getString("imageUrl");
                            String sid = document.getString("id"); // Assuming each document ID is the suggestion ID
                        }
                    }
                }).addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}