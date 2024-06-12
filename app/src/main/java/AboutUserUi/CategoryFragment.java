package AboutUserUi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Models.Category;
import Models.Suggest;
import UI.Phase1a;
import adapters.CategoryAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List< Category > categoryList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.category_rv);
        categoryList = new ArrayList <> (  );
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, "category");

        recyclerView.setLayoutManager ( new GridLayoutManager ( getContext (), 3 ) );
        recyclerView.setAdapter ( categoryAdapter );

        loadCategories();
        return view;
    }

    private void loadCategories() {
        db.collection("homeworkout").
                document ("category").
                collection ("workout").
                get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String sname = document.getString("name");
                            String simageUrl = document.getString("imageUrl");
                            String sid = document.getString("id"); // Assuming each document ID is the suggestion ID
                            categoryList.add(new Category(sname, simageUrl, sid));
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> {
                    // Handle failure
                });
    }
    
}
