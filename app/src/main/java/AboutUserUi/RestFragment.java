package AboutUserUi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;

import java.util.Locale;

import UI.Phase2;

public class RestFragment extends Fragment implements TextToSpeech.OnInitListener {
    private TextView textViewTimer, reps, names;
    private CountDownTimer countDownTimer;
    private ImageView img;
    private Button buttonNext, add;
    private long timeLeftInMillis = 30000; // Default 30 seconds
    private static final long ADD_TIME_IN_MILLIS = 20000; // 20 seconds
    private TextToSpeech textToSpeech;
    private boolean isHalfTimeAnnounced = false;
    private boolean isNextExerciseAnnounced = false;
    private boolean isInitialAnnounced = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest, container, false);
        textViewTimer = view.findViewById(R.id.set);
        buttonNext = view.findViewById(R.id.next);
        add = view.findViewById(R.id.add);
        img = view.findViewById(R.id.img);
        reps = view.findViewById(R.id.rep);
        names = view.findViewById(R.id.name);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), this);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String rep = getArguments().getString("reps");
            String imageUrl = getArguments().getString("imageUrl");
            String categoryId = getArguments().getString("category_id");

            // Set the rest time based on category_id
            if ("strength".equals(categoryId)) {
                timeLeftInMillis = 180000; // 3 minutes
            }

            names.setText(name);
            reps.setText(rep);

            // Load image using Glide
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(img);
            } else {
                Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }

        startTimer(timeLeftInMillis);

        buttonNext.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2) {
                ((Phase2) getActivity()).displayNextFragment();
            }
        });

        add.setOnClickListener(v -> {
            timeLeftInMillis += ADD_TIME_IN_MILLIS; // Add 20 seconds
            startTimer(timeLeftInMillis); // Restart the timer with the new time
        });

        return view;
    }

    private void startTimer(long timeInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay(millisUntilFinished);
                // Speak rest time updates
                speakRestTimeUpdates(millisUntilFinished);
            }

            public void onFinish() {
                if (getActivity() instanceof Phase2) {
                    ((Phase2) getActivity()).displayNextFragment();
                }
            }
        }.start();
    }

    private void updateTimerDisplay(long millisUntilFinished) {
        textViewTimer.setText(String.valueOf(millisUntilFinished / 1000));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Shutdown TextToSpeech when fragment is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or language is not supported
                // Handle the error here, if needed
            }
        } else {
            // Initialization failed
            // Handle the error here, if needed
        }
    }

    private void speakRestTimeUpdates(long millisUntilFinished) {
        long halfTime = timeLeftInMillis / 2;
        long tenSeconds = 10000; // 10 seconds

        if (!isInitialAnnounced) {
            // Speak "Rest" only once at the beginning
            textToSpeech.speak("Rest", TextToSpeech.QUEUE_FLUSH, null, null);
            isInitialAnnounced = true;
        } else if (!isHalfTimeAnnounced && millisUntilFinished <= halfTime) {
            // Speak when half time is reached
            textToSpeech.speak("Half time reached", TextToSpeech.QUEUE_FLUSH, null, null);
            isHalfTimeAnnounced = true;
        } else if (!isNextExerciseAnnounced && millisUntilFinished <= tenSeconds) {
            // Speak the name of the next exercise when there are 10 seconds remaining
            String nextExerciseName = names.getText().toString();
            textToSpeech.speak("Next exercise: " + nextExerciseName, TextToSpeech.QUEUE_FLUSH, null, null);
            isNextExerciseAnnounced = true;
        }
    }
}
