package com.example.well_fit;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Calendar;

public class home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FirebaseAuth mAuth;
    CircleImageView dp;
    TextView username, time;
    ImageView history;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom);
        frameLayout = findViewById(R.id.fragment);
        username = findViewById(R.id.username);
        time = findViewById(R.id.time);
        history = findViewById(R.id.history);
        dp = findViewById(R.id.userdp);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.dumbell) {
                    selectedFragment = new DumbellFragment();
                } else if (itemId == R.id.discover) {
                    selectedFragment = new DiscoverFragment();
                } else if (itemId == R.id.setting) {
                    selectedFragment = new SettingFragment();
                }

                if (selectedFragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                    fragmentTransaction.replace(R.id.fragment, selectedFragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

        // Load user's data and set time
        loadUserData();
        setTimeGreeting();
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            // Fetch additional data from Firestore
            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // There should be only one document corresponding to the user's email
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            // Update UI elements with fetched data
                            String userName = documentSnapshot.getString("name");
                            String userImageUrl = documentSnapshot.getString("photoUrl");
                            username.setText(userName + "!");
                            Glide.with(home.this).load(userImageUrl).into(dp);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }


    private void setTimeGreeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greetingMessage;
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingMessage = "Hello, Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingMessage = "Hello, Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingMessage = "Hello, Good Evening";
        } else {
            greetingMessage = "Hello, Good Night";
        }

        time.setText(greetingMessage);
    }
}
