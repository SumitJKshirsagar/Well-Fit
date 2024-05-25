package AboutUserUi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Color;
import com.example.well_fit.R;

public class GenderFragment extends Fragment {

    private Button button1;
    private Button button2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender, container, false);

        // Initialize buttons
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);

        // Set click listeners
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setTextColor(Color.WHITE);
                button1.setBackgroundColor(Color.BLACK);

                button2.setTextColor(Color.BLACK);
                button2.setBackgroundColor(Color.WHITE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button2.setTextColor(Color.WHITE);
                button2.setBackgroundColor(Color.BLACK);

                button1.setTextColor(Color.BLACK);
                button1.setBackgroundColor(Color.WHITE);
            }
        });

        return view;
    }
}