package UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.well_fit.R;

import AboutUserUi.FragmentContainer;
import adapters.ViewAdapter;

public class Widget extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotLayout;
    ViewAdapter viewAdapter;
    TextView back, next;
    TextView skip;
    ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        viewPager = findViewById(R.id.view);
        dotLayout = findViewById(R.id.dot);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);

        viewAdapter = new ViewAdapter(this);
        viewPager.setAdapter(viewAdapter);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < viewAdapter.getCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    // Optionally handle the last page action
                    Intent intent = new Intent ( Widget.this, FragmentContainer.class );
                    intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity ( intent );
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( Widget.this, FragmentContainer.class );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity (intent);
            }
        });
    }

    public void addDotsIndicator(int position) {
        dots = new ImageView[viewAdapter.getCount()];
        dotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot); // use your inactive dot drawable
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotLayout.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[position].setImageResource(R.drawable.dot_active); // use your active dot drawable
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Optionally handle page scroll events
        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            updateButtons();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Optionally handle page scroll state changes
        }
    };

    private void updateButtons() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem == 0) {
            back.setVisibility(View.INVISIBLE);
        } else {
            back.setVisibility(View.VISIBLE);
        }

        if (currentItem == viewAdapter.getCount() - 1) {
            next.setText("Finish");
        } else {
            next.setText("Next");
        }
    }
}
