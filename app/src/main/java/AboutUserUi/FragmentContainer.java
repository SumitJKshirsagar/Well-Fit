package AboutUserUi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.well_fit.R;


public class FragmentContainer extends AppCompatActivity {


    private int currentFragmentIndex = 0;
    private Fragment[] fragments = {new SelectFevFragment(),new GenderFragment(),new AgeFragment(),new HeightFragment(),new WeightFragment(),new GoalFragment(),new LevelFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);


        // Display the initial fragment
        displayFragment(currentFragmentIndex);

        ImageView previousButton = findViewById(R.id.previous);
        Button nextButton = findViewById(R.id.nextSteps);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragmentIndex > 0) {
                    currentFragmentIndex--;
                    displayFragment(currentFragmentIndex);
                }

                if(currentFragmentIndex == 0){
                    previousButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragmentIndex < fragments.length - 1) {
                    currentFragmentIndex++;
                    displayFragment(currentFragmentIndex);
                }

                if(currentFragmentIndex >= 1){
                    previousButton.setVisibility(View.VISIBLE);
                } else if (currentFragmentIndex==0) {
                    previousButton.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void displayFragment(int fragmentIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragments[fragmentIndex]);
        fragmentTransaction.commit();
    }
}