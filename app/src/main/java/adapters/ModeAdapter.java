package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;

import java.util.List;

import Models.Suggest;
import UI.Phase1a;

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ViewHolder> {
    private List<Suggest> modeList;
    private Context context;
    private String type;

    public ModeAdapter(List<Suggest> modeList, Context context, String type) {
        this.modeList = modeList;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ModeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModeAdapter.ViewHolder holder, int position) {
        Suggest exercise = modeList.get(position);
        Glide.with(context).load(exercise.getImageUrl()).into(holder.img);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Phase1a.class);
            intent.putExtra("category_id", exercise.getId()); // Pass category ID if needed
            intent.putExtra("type", type);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
