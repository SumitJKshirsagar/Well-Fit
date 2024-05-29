package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.well_fit.HomeFragment;
import com.example.well_fit.MainActivity;
import com.example.well_fit.R;
import com.example.well_fit.Widget;
import com.example.well_fit.home;


public class FragmentContainer extends AppCompatActivity {


    private int currentFragmentIndex = 0;
    private TextView skip;
    private Fragment[] fragments = {new SelectFevFragment(),new GenderFragment(),new AgeFragment(),new HeightFragment(),new WeightFragment(),new GoalFragment(),new LevelFragment(),new FinalFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);


        // Display the initial fragment
        displayFragment(currentFragmentIndex);

        ImageView previousButton = findViewById(R.id.previous);
        Button nextButton = findViewById(R.id.nextSteps);
        skip = findViewById(R.id.skip);

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

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FragmentContainer.this, home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

                if(currentFragmentIndex == 6){
                    nextButton.setText("FINISH STEPS");
                } else if (currentFragmentIndex == 7) {
                    skip.setVisibility(View.INVISIBLE);
                    nextButton.setText("GET STARTED!");
                }else {
                    nextButton.setText("NEXT STEP");
                    skip.setVisibility(View.VISIBLE);
                }
                if(currentFragmentIndex == 7){
                    Intent intent = new Intent(FragmentContainer.this, home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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