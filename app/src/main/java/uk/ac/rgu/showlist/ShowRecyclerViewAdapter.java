package uk.ac.rgu.showlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

public class ShowRecyclerViewAdapter extends RecyclerView.Adapter<ShowRecyclerViewAdapter.ShowViewHolder> {

    //member variables for context its working in
    private Context context;
    //data that will be displayed
    private List<Show> shows;

    /**
     *
     * @param context that the adapter is working on
     * @param shows data to display
     */

    public ShowRecyclerViewAdapter(Context context,List<Show> shows){
        this.context = context;
        this.shows = shows;

    }


    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.show_item,parent,false);
        ShowViewHolder viewHolder = new ShowViewHolder(itemView,this);
        Log.d("SHOW_RECYCLER","CREATING");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
        //get the show at position
        Show show = this.shows.get(position);

        TextView tv_name = holder.showItemView.findViewById(R.id.tv_name);
        tv_name.setText(show.getName());

        TextView firstairDate = holder.showItemView.findViewById(R.id.tv_rating);
        firstairDate.setText(show.getFirstAirDate());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+shows.get(position).getPosterImage())
                .into((ImageView) holder.showItemView.findViewById(R.id.iv_img));

        Log.d("SHOW_RECYCLER","BINDING");


    }

//    @Override
//    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
//
//    }

    @Override
    public int getItemCount() {
        return this.shows.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View showItemView;
        private ShowRecyclerViewAdapter adapter;
        public ShowViewHolder(View showItemView,ShowRecyclerViewAdapter adapter){
            super(showItemView);
            this.showItemView = showItemView;
            this.adapter = adapter;
            this.showItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get the clicked item's position
            int position = getAdapterPosition();

            Show show = shows.get(position);

            Log.d("SHOW_RECYCLER", "user clicked "+ show.getName());

        }
    }
}