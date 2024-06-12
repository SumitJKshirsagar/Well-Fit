package UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.well_fit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Models.Suggest;
import adapters.SearchAdapter;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Suggest> suggestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_item_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Display 3 items per row
        suggestList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, suggestList, ""); // Empty string for now
        recyclerView.setAdapter(searchAdapter);

        // Pass the collection name and type to the fetchCollection method
        fetchCollection("homeworkout", "exercise");
        fetchCollection("homeworkout", "category");
        fetchCollection("homeworkout", "mode");
        fetchCollection("homeworkout", "suggestion");
    }

    private void fetchCollection(String collectionName, String typeName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName)
                .document(typeName)
                .collection("workout")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Map each document fields to your model class
                                String name = document.getString("name");
                                String imageUrl = document.getString("imageUrl");
                                String sid = document.getString("id");
                                Suggest suggest = new Suggest(name, imageUrl, sid);
                                suggestList.add(suggest);
                            }
                            // Notify adapter after fetching all documents
                            searchAdapter.setType(typeName);
                            searchAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                });
    }
}
