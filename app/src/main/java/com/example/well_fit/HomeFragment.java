package com.example.well_fit;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;


public class HomeFragment extends Fragment {



    FrameLayout frameLayout;
    FirebaseAuth mAuth;
    ImageView dp;
    TextView username, time;
    FirebaseFirestore db;


    public HomeFragment ( ) {
        // Required empty public constructor
    }


    public static HomeFragment newInstance () {
        HomeFragment fragment = new HomeFragment ( );
        Bundle args = new Bundle ( );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        frameLayout = view.findViewById(R.id.fragment);
        username = view.findViewById(R.id.username);
        time = view.findViewById(R.id.greetings_txt);
        dp = view.findViewById(R.id.profile_img);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                            Glide.with(HomeFragment.this).load(userImageUrl).into(dp);
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