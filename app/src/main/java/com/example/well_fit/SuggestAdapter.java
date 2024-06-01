package com.example.well_fit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.well_fit.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {

    private List<Suggest> suggestList;
    private Context context;

    public SuggestAdapter(Context context, List<Suggest> suggestList) {
        this.context = context;
        this.suggestList = suggestList;
    }

    @NonNull
    @Override
    public SuggestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestAdapter.ViewHolder holder, int position) {
        Suggest category = suggestList.get(position);
        holder.name.setText(category.getName());
        Glide.with(context).load(category.getImageUrl()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Phase1a.class);
                intent.putExtra("category_id", category.getId()); // Pass category ID if needed
                context.startActivity(intent);
            }
        });
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
}
