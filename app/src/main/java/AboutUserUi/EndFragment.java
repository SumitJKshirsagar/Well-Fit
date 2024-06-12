package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Models.Category;
import UI.MainActivity;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class EndFragment extends Fragment implements TextToSpeech.OnInitListener {
    Button button;
    TextView exercise, totalexe, timeexe, calorieexe;
    private FirebaseFirestore db;
    private TextToSpeech textToSpeech;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end, container, false);
        button = view.findViewById(R.id.btn);
        exercise = view.findViewById(R.id.exercise);
        totalexe = view.findViewById(R.id.total);
        timeexe = view.findViewById(R.id.time);
        calorieexe = view.findViewById(R.id.calorie);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if (getArguments() != null) {
            String name = getArguments().getString("category_id");
            String time = getArguments().getString("time");
            String calorie = getArguments().getString("calorie");
            exercise.setText(name);
            timeexe.setText(time);
            calorieexe.setText(calorie);
        } else {
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or language is not supported
                // Handle the error here, if needed
            } else {
                // Speak "Congratulations"
                textToSpeech.speak("Congratulations", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } else {
            // Initialization failed
            // Handle the error here, if needed
        }
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
}
