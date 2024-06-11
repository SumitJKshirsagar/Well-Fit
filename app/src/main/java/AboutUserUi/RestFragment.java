package AboutUserUi;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import UI.Phase2;
import com.bumptech.glide.Glide;
import com.example.well_fit.R;

public class RestFragment extends Fragment {
    private TextView textViewTimer, reps, names;
    private CountDownTimer countDownTimer;
    private ImageView img;
    private Button buttonNext, add;
    private long timeLeftInMillis = 30000; // 30 seconds
    private static final long ADD_TIME_IN_MILLIS = 20000; // 20 seconds

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

        startTimer(timeLeftInMillis);

        buttonNext.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2) {
                ((Phase2) getActivity()).displayNextFragment();
            }
        });

        add.setOnClickListener(v -> {
            timeLeftInMillis += ADD_TIME_IN_MILLIS; // Add 20 seconds
            updateTimerDisplay(timeLeftInMillis); // Update the timer display without restarting
        });

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String rep = getArguments().getString("reps");
            String imageUrl = getArguments().getString("imageUrl");
            names.setText(name);
            reps.setText(rep);

            // Load image using Glide
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(img);
            }
        }

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
    }
}
