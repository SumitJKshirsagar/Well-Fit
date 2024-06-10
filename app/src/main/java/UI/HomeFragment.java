package UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
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
import Models.Category;
import Models.Suggest;
import adapters.CategoryAdapter;
import adapters.ExerciseAdapter;
import adapters.ModeAdapter;
import adapters.SuggestAdapter;
import adapters.ModePagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private CircleImageView img;
    private ImageView dp, drawer;
    private TextView username, time, see, user;
    private FirebaseFirestore db;
    private LinearLayout dot;
    private ViewPager modeViewPager;
    private ImageView[] dots;
    private RecyclerView categoryRecyclerView, suggestRecyclerView, exerciseRecyclerView, mode;
    private CategoryAdapter categoryAdapter;
    private SuggestAdapter suggestAdapter;
    private ExerciseAdapter exerciseAdapter;
    private ModeAdapter modeAdapter;
    private ModePagerAdapter modePagerAdapter;
    private List<Category> categoryList;
    private List<Suggest> suggestList;
    private List<Suggest> exerciseList;
    private List<Suggest> modeList;
    private DrawerLayout drawerLayout;
    private Spinner spinner;
    private CardView searchView;

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
        exerciseRecyclerView = view.findViewById(R.id.exrecises_rv);
        modeViewPager = view.findViewById(R.id.view1);
        modeList = new ArrayList<>();
        dot = view.findViewById(R.id.dot);
        mode = view.findViewById(R.id.img);
        categoryList = new ArrayList<>();
        suggestList = new ArrayList<>();
        exerciseList = new ArrayList<>();
        modeList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, "category");
        suggestAdapter = new SuggestAdapter(getContext(), suggestList, "suggestion");
        exerciseAdapter = new ExerciseAdapter(exerciseList, getContext(), "exercise");
        modeAdapter = new ModeAdapter(modeList, getContext(), "mode");
        searchView = view.findViewById(R.id.searchView);

        // Set up RecyclerViews
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        suggestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        suggestRecyclerView.setAdapter(suggestAdapter);

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        mode.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mode.setAdapter(modeAdapter);

        modePagerAdapter = new modeAdapter(modeList, getContext());
        modeViewPager.setAdapter(modeAdapter);
        modeViewPager.addOnPageChangeListener(viewListener);

        // Set the OnClickListener for the see button
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });

        NavigationView navigationView = view.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        user = headerView.findViewById(R.id.user);
        img = headerView.findViewById(R.id.profile_img);

        // Load user's data and set time
        loadUserData();
        setTimeGreeting();
        loadMode();
        loadCategories();
        loadSuggestions();
        loadExercise();
        drawerLayoutToggle();
        levelSpinner(view);
    }

    private void loadMode() {
        db.collection("homeworkout")
                .document("mode")
                .collection("workout")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String sname = document.getString("name");
                            String simageUrl = document.getString("imageUrl");
                            String sid = document.getString("id");
                            modeList.add(new Suggest(sname, simageUrl, sid));
                        }
                        modePagerAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void addDotsIndicator(int position) {
        dots = new ImageView[modePagerAdapter.getCount()];
        dot.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(R.drawable.dot); // use your inactive dot drawable
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dot.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[position].setImageResource(R.drawable.dot_active); // use your active dot drawable
        }
    }

    private void openSearchActivity() {
        Intent intent = new Intent(requireContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void loadExercise() {
        db.collection("homeworkout")
                .document("exercise")
                .collection("workout")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String sname = document.getString("name");
                            String simageUrl = document.getString("imageUrl");
                            String sid = document.getString("id");
                            exerciseList.add(new Suggest(sname, simageUrl, sid));
                        }
                        exerciseAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void levelSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner);
        int verticalOffsetInPixels = getResources().getDimensionPixelOffset(R.dimen.dropdown_vertical_offset);
        spinner.setDropDownVerticalOffset(verticalOffsetInPixels);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_items, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.style_spinner);
        spinner.setAdapter(adapter);

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
                        // Handle error
                    }
                }
            });
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLevel = parent.getItemAtPosition(position).toString();
                if (currentUser != null) {
                    DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                    userRef.update("level", selectedLevel)
                            .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Level updated to " + selectedLevel, Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to update level", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void drawerLayoutToggle() {
        NavigationView navigationView = getView().findViewById(R.id.navigationView);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("name");
                            String imageUrl = document.getString("imageUrl");
                            username.setText(name);
                            user.setText(name);
                            Glide.with(requireContext()).load(imageUrl).into(dp);
                            Glide.with(requireContext()).load(imageUrl).into(img);
                        }
                    }
                }
            });
        }
    }

    private void setTimeGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 0 && hourOfDay < 12) {
            time.setText("Good morning");
        } else if (hourOfDay >= 12 && hourOfDay < 16) {
            time.setText("Good afternoon");
        } else if (hourOfDay >= 16 && hourOfDay < 21) {
            time.setText("Good evening");
        } else if (hourOfDay >= 21 && hourOfDay < 24) {
            time.setText("Good night");
        }
    }

    private void openCategoryFragment() {
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, categoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadCategories() {
        db.collection("homeworkout")
                .document("category")
                .collection("workout")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String name = document.getString("name");
                            String imageUrl = document.getString("imageUrl");
                            String id = document.getString("id");
                            categoryList.add(new Category(name, imageUrl, id));
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void loadSuggestions() {
        db.collection("homeworkout")
                .document("suggestion")
                .collection("workout")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String sname = document.getString("name");
                            String simageUrl = document.getString("imageUrl");
                            String sid = document.getString("id");
                            suggestList.add(new Suggest(sname, simageUrl, sid));
                        }
                        suggestAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
