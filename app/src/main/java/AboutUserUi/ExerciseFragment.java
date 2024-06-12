package AboutUserUi;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import java.util.Locale;
import UI.Phase2;

public class ExerciseFragment extends Fragment implements TextToSpeech.OnInitListener {
    private TextView textViewName, textViewReps, skip;
    private Button buttonNext;
    private ImageView img;
    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        textViewName = view.findViewById(R.id.name);
        textViewReps = view.findViewById(R.id.set);
        img = view.findViewById(R.id.img);
        buttonNext = view.findViewById(R.id.next);
        skip = view.findViewById(R.id.skip);

        buttonNext.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2) {
                ((Phase2) getActivity()).displayNextFragment();
            }
        });

        skip.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2) {
                ((Phase2) getActivity()).displayNextFragment();
            }
        });

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), this);

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
            }
        }

        return view;
    }

    @Override
    public void onDestroy() {
        // Shutdown TextToSpeech when fragment is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language and pitch parameters
            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setPitch(1.0f); // Default pitch

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or language is not supported
                // Handle the error here, if needed
            } else {
                // Speak the name and reps
                String name = textViewName.getText().toString();
                String reps = textViewReps.getText().toString();
                textToSpeech.speak(name + ". " + reps, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } else {
            // Initialization failed
            // Handle the error here, if needed
        }
    }
}
