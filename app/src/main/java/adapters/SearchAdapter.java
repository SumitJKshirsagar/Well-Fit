package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;

import java.util.List;

import Models.Suggest;
import UI.Phase1a;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Suggest> suggestList;
    private Context context;
    private String type;
    public void seFilteredList(List<Suggest> filteredList){
        this.suggestList = filteredList;
        notifyDataSetChanged();
    }

    public SearchAdapter(Context context, List<Suggest> suggestList, String type) {
        this.context = context;
        this.suggestList = suggestList;
        this.type = type;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Suggest category = suggestList.get(position);
        holder.name.setText(category.getName());
        Glide.with(context).load(category.getImageUrl()).into(holder.img);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Phase1a.class);
//                intent.putExtra("category_id", category.getId()); // Pass category ID if needed
//                intent.putExtra("type", type);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return suggestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
