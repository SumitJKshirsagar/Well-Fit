package UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.well_fit.R;

import UI.MainActivity;

public class SettingFragment extends Fragment {
    TextView rem, log;
    Switch fit, mode;
    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        rem = view.findViewById ( R.id.t1 );
        log = view.findViewById ( R.id.t4 );
        fit = view.findViewById ( R.id.fit );
        mode = view.findViewById ( R.id.mode );

        rem.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
//                Intent intent = new Intent ( getContext (), MainActivity.class );
//                startActivity ( intent );
            }
        } );
        log.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                Intent intent = new Intent ( getContext (), MainActivity.class );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        } );
        return view;
    }
}