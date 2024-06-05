package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import Models.Display;
import com.example.well_fit.R;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter< DisplayAdapter.ViewHolder >{
    private List <Display> displayList;
    private Context context;

    public DisplayAdapter ( Context context, List <Display> displayList) {
        this.displayList = displayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DisplayAdapter.ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.display_exercise , parent , false );
        return new ViewHolder ( view );
    }

    @Override
    public void onBindViewHolder ( @NonNull DisplayAdapter.ViewHolder holder , int position ) {
        Display display = displayList.get ( position );
        holder.name.setText ( display.getExercise () );
        holder.sets.setText ( display.getSets () );
        holder.reps.setText ( display.getReps () );
        Glide.with ( context ).load ( display.getImage () ).into ( holder.img );
    }

    @Override
    public int getItemCount ( ) {
        return displayList.size ( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, sets, reps;
        ImageView img;
        public ViewHolder ( @NonNull View itemView ) {
            super ( itemView );
            name = itemView.findViewById ( R.id.name );
            sets = itemView.findViewById ( R.id.sets );
            reps = itemView.findViewById ( R.id.reps );
            img = itemView.findViewById ( R.id.img );
        }
    }
}
