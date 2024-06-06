package UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import Models.Category;
import com.example.well_fit.R;
import Models.Suggest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import AboutUserUi.CategoryFragment;
import adapters.CategoryAdapter;
import adapters.SuggestAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;

    private CircleImageView img;
    private ImageView dp, drawer;
    private TextView username, time, see, user;
    private FirebaseFirestore db;
    private RecyclerView categoryRecyclerView;
    private RecyclerView suggestRecyclerView;
    private CategoryAdapter categoryAdapter;
    private SuggestAdapter suggestAdapter;
    private List<Category> categoryList;
    private List<Suggest> suggestList;
    private DrawerLayout drawerLayout;
    private Spinner spinner;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout = view.findViewById(R.id.fragment);
        username = view.findViewById(R.id.username);
        time = view.findViewById(R.id.greetings_txt);
        see = view.findViewById(R.id.see);
        dp = view.findViewById(R.id.profile_img);
        drawer = view.findViewById(R.id.drawer);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        categoryRecyclerView = view.findViewById(R.id.category_rv);
        suggestRecyclerView = view.findViewById(R.id.suggest_rv);
        categoryList = new ArrayList<>();
        suggestList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        suggestAdapter = new SuggestAdapter(getContext(), suggestList);



        // Set up RecyclerViews
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        suggestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        suggestRecyclerView.setAdapter(suggestAdapter);

        // Set the OnClickListener for the see button
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment();
            }
        });

        NavigationView navigationView = view.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        user = headerView.findViewById(R.id.user);
        img = headerView.findViewById(R.id.profile_img);

        // Load user's data and set time
        loadUserData();
        setTimeGreeting();
        loadCategories();
        loadSuggestions();
        drawerLayoutToggle();
        levelSpinner(view);
    }

    private void levelSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner);
        int verticalOffsetInPixels = getResources().getDimensionPixelOffset(R.dimen.dropdown_vertical_offset);
        spinner.setDropDownVerticalOffset(verticalOffsetInPixels);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_items, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.style_spinner);
        spinner.setAdapter(adapter);

        // Fetch and set the user's current level
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String userLevel = task.getResult().getString("level");
                        if (userLevel != null) {
                            int spinnerPosition = adapter.getPosition(userLevel);
                            spinner.setSelection(spinnerPosition);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to fetch user level", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLevel = parentView.getItemAtPosition(position).toString();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                    saveLevelToFirestore(userRef, selectedLevel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void drawerLayoutToggle() {
        drawer.setOnClickListener(view -> {
            drawerLayout.open();
        });

    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            // Fetch additional data from Firestore
            db.collection("users").whereEqualTo("email", userEmail).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // There should be only one document corresponding to the user's email
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    // Update UI elements with fetched data
                    String userName = documentSnapshot.getString("name");
                    String userImageUrl = documentSnapshot.getString("photoUrl");
                    username.setText(userName + "!");
                    user.setText(userName);
                    Glide.with(HomeFragment.this).load(userImageUrl).into(dp);
                    Glide.with(HomeFragment.this).load(userImageUrl).into(img);
                }
            }).addOnFailureListener(e -> {
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
        db.collection("category").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String name = document.getString("name");
                    String imageUrl = document.getString("imageUrl");
                    String id = document.getId(); // Assuming each document ID is the category ID
                    categoryList.add(new Category(name, imageUrl, id));
                }
                categoryAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    private void loadSuggestions() {
        db.collection("homeworkout").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String sname = document.getString("name");
                    String simageUrl = document.getString("imageUrl");
                    String sid = document.getId(); // Assuming each document ID is the suggestion ID
                    suggestList.add(new Suggest(sname, simageUrl, sid));
                }
                suggestAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    private void openCategoryFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, new CategoryFragment()); // Replace 'CategoryFragment' with your actual fragment class name
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveLevelToFirestore(DocumentReference userRef, String level) {
        userRef.update("level", level).addOnCompleteListener(new OnCompleteListener <Void> () {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()) {
                    // Optionally show a message
                    // Toast.makeText(getActivity(), "Level updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update level", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
