package uk.ac.rgu.showlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

import uk.ac.rgu.showlist.database.SeenDatabase;


public class ShowRecyclerViewAdapter
        extends RecyclerView.Adapter<ShowRecyclerViewAdapter.ShowViewHolder> {


    public static final String EXTRA_SHOW_NAME = "uk.ac.rgu.showlist.SHOW_NAME";
    public static final String EXTRA_OVERVIEW = "uk.ac.rgu.showlist.OVERVIEW";
    public static final String EXTRA_VOTEAVG = "uk.ac.rgu.showlist.VOTEAVG";
    public static final String EXTRA_BACKDROPIMG = "uk.ac.rgu.showlist.BACKDROPIMG";
    public static final String EXTRA_RELEASE = "uk.ac.rgu.showlist.RELEASE";
    public static final String EXTRA_POSTER = "uk.ac.rgu.showlist.POSTER";
    public static final String EXTRA_SHOWID = "uk.ac.rgu.showlist.SHOWID";



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

        /**
         *Using Glide library to get the full url for the poster as the api only gives the last
         * extension so converting the full and then loading it into the ImageViewer
         */
        if (shows.get(position).getPosterImage() == "null" || shows.get(position).getPosterImage() == null ){
            Glide.with(context)
                    .load(R.drawable.no_img_for_shows)
                    .into((ImageView) holder.showItemView.findViewById(R.id.iv_img));
        }
        else{
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500"+shows.get(position).getPosterImage())
                    .into((ImageView) holder.showItemView.findViewById(R.id.iv_img));
        }

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

            Intent intent = new Intent(context, ViewShowInfo.class);
            intent.putExtra(EXTRA_SHOW_NAME, show.getName());
            intent.putExtra(EXTRA_OVERVIEW, show.getOverview());
            intent.putExtra(EXTRA_VOTEAVG, show.getVoteAvg());
            intent.putExtra(EXTRA_BACKDROPIMG, show.getBackdropImage());
            intent.putExtra(EXTRA_POSTER, show.getPosterImage());
            intent.putExtra(EXTRA_RELEASE, show.getFirstAirDate());
            intent.putExtra(EXTRA_SHOWID, show.getId());


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            Log.d("SHOW_RECYCLER", "user clicked "+ show.getName());

        }
    }

}
