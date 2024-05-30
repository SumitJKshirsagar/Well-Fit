package com.example.well_fit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private ImageView dp;
    private TextView username, time;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        recyclerView = view.findViewById(R.id.category_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerView.setAdapter(categoryAdapter);

        // Load user's data and set time
        loadUserData();
        setTimeGreeting();
        loadCategories();
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

    private void loadCategories() {
        db.collection("category")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String name = document.getString("name");
                            String imageUrl = document.getString("imageUrl");
                            String id = document.getString ("id");
                            categoryList.add(new Category( name, imageUrl, id));
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(getContext(), e.getMessage (), Toast.LENGTH_LONG).show();
                });
    }
}
