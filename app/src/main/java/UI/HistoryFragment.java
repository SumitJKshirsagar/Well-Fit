package UI;

import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.well_fit.R;


public class HistoryFragment extends Fragment {




    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);


}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.applandeo.materialcalendarview.CalendarView calendarView = view.findViewById(R.id.calendarView);

        // Set current date to CalendarView
        Calendar calendar = Calendar.getInstance();
        calendarView.setDate(calendar.getTime());
        Drawable forward = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_forward);
        Drawable previous = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_previous);
        calendarView.setPreviousButtonImage(previous);
        calendarView.setForwardButtonImage(forward);
        calendarView.setHeaderColor(R.color.white);
        calendarView.setHeaderLabelColor(R.color.black);



    }


}