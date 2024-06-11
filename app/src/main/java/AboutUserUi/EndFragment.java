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

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EndFragment extends Fragment {
    Button button;
    TextView exercise, totalexe, timeexe, calorieexe;
    private FirebaseFirestore db;
    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_end, container, false );
        button = view.findViewById ( R.id.btn);
        exercise = view.findViewById ( R.id.exercise );
        totalexe = view.findViewById ( R.id.total );
        timeexe = view.findViewById ( R.id.time );
        calorieexe = view.findViewById ( R.id.calorie );
        button.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( getContext (), MainActivity.class );
                startActivity ( intent );
            }
        } );
        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String total = getArguments().getString("total");
            String time = getArguments().getString("time");
            String calorie = getArguments().getString("calorie");
            exercise.setText(name);
            totalexe.setText(total);
            this.timeexe.setText(time);
            this.calorieexe.setText(calorie);
        }

        return view;
    }

    private void loadexerice ( ) {
    }
}