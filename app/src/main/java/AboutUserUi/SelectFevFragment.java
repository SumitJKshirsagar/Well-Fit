package AboutUserUi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.well_fit.R;

public class SelectFevFragment extends Fragment {


    public SelectFevFragment() {
        // Required empty public constructor
    }


    public static SelectFevFragment newInstance() {
        SelectFevFragment fragment = new SelectFevFragment();
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
        return inflater.inflate(R.layout.fragment_select_fev, container, false);
    }
}