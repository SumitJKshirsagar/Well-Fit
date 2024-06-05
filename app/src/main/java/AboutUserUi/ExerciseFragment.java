package AboutUserUi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.well_fit.Phase2;
import com.example.well_fit.R;

public class ExerciseFragment extends Fragment {
    private TextView textViewName, textViewReps,  skip;
    private Button buttonNext;
    private ImageView img;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        textViewName = view.findViewById(R.id.name);
        textViewReps = view.findViewById(R.id.set);
        img = view.findViewById(R.id.img);
        buttonNext = view.findViewById(R.id.next);
        skip = view.findViewById(R.id.skip);

        buttonNext.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2 ) {
                ((Phase2) getActivity()).displayNextFragment() ;
            }
        });

        skip.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2 ) {
                ((Phase2) getActivity()).displayNextFragment() ;
            }
        });

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String reps = getArguments().getString("reps");
            String imageUrl = getArguments().getString("imageUrl");
            textViewName.setText(name);
            textViewReps.setText(reps);

            // Load image using Glide
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(img);
            } else {
            }
        }

        return view;
    }
}
