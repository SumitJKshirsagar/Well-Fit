package AboutUserUi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import UI.Phase2;
import com.example.well_fit.R;

public class RestFragment extends Fragment {
    private TextView textViewTimer;
    private CountDownTimer countDownTimer;
    private Button buttonNext, add;
    private long timeLeftInMillis = 30000; // 30 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest, container, false);
        textViewTimer = view.findViewById(R.id.set);
        buttonNext = view.findViewById(R.id.next);
        add = view.findViewById(R.id.add);
        startTimer(timeLeftInMillis);

        buttonNext.setOnClickListener(v -> {
            if (getActivity() instanceof Phase2) {
                ((Phase2) getActivity()).displayNextFragment();
            }
        });

        add.setOnClickListener(v -> {
            timeLeftInMillis += 20000; // Add 20 seconds
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
                textViewTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (getActivity() instanceof Phase2) {
                    ((Phase2) getActivity()).displayNextFragment();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
