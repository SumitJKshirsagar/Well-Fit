package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import UI.MainActivity;
import com.example.well_fit.R;

public class EndFragment extends Fragment {
    Button button;
    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_end, container, false );
        button = view.findViewById ( R.id.btn);
        button.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( getContext (), MainActivity.class );
                startActivity ( intent );
            }
        } );
        return view;
    }
}