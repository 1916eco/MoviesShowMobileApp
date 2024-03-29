package uk.ac.rgu.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import uk.ac.rgu.showlist.database.SeenRepository;

public class ViewShowInfo extends AppCompatActivity implements View.OnClickListener {

    private  Show show;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvName = (TextView) findViewById(R.id.tv_showInfoTitle);

        Intent launcher = getIntent();
        /**
         * Receive the Extras that were "sent" by the previous page
         */

        if (launcher.hasExtra(ShowRecyclerViewAdapter.EXTRA_SHOW_NAME)) {
            String showName = launcher.getStringExtra(ShowRecyclerViewAdapter.EXTRA_SHOW_NAME);
            show = new Show();
            show.setName(showName);
            show.setOverview(launcher.getStringExtra(ShowRecyclerViewAdapter.EXTRA_OVERVIEW));
            show.setFirstAirDate(launcher.getStringExtra(ShowRecyclerViewAdapter.EXTRA_RELEASE));
            show.setVoteAvg(launcher
                    .getDoubleExtra(ShowRecyclerViewAdapter.EXTRA_VOTEAVG,0.0));
            show.setBackdropImage(launcher
                    .getStringExtra(ShowRecyclerViewAdapter.EXTRA_BACKDROPIMG));
            show.setPosterImage(launcher
                    .getStringExtra(ShowRecyclerViewAdapter.EXTRA_POSTER));
            show.setId(launcher.getIntExtra(ShowRecyclerViewAdapter.EXTRA_SHOWID,0));

        }

        /**
         *  assigning the button to a onclick listener which then would filter by Button.id
         */
        setContentView(R.layout.activity_view_show_info);
        ((Button)findViewById(R.id.btn_checkOnline)).setOnClickListener(this);

        ((Button)findViewById(R.id.btn_showInfoAddToWatchlist)).setOnClickListener(this);

        ((Button)findViewById(R.id.btn_showInfoAddToWatched)).setOnClickListener(this);



        String tvName = show.getName();
        ((TextView) findViewById(R.id.tv_showInfoTitle)).setText(tvName);

        /**
         * filtering getting the information and checking if there is no data it wont display it
         */
        String tvOverview = show.getOverview();
        if (tvOverview == null) {
            ((TextView) findViewById(R.id.tv_showInfoDescription)).setVisibility(View.INVISIBLE);
        }else {
            ((TextView) findViewById(R.id.tv_showInfoDescription)).setText(tvOverview);
        }

        Double tvAvg = show.getVoteAvg();
        if (tvAvg == 0.0){
            ((TextView) findViewById(R.id.tv_showInfoRatingAvarage)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.tv_userRatingTitle)).setVisibility(View.INVISIBLE);
            ((ImageView) findViewById(R.id.iv_star)).setVisibility(View.INVISIBLE);
        }else {
            ((TextView) findViewById(R.id.tv_showInfoRatingAvarage)).setText(Double.toString(tvAvg));
        }

        String tvRelease = show.getFirstAirDate();
        if ("".equals(tvRelease)){
            ((TextView) findViewById(R.id.tv_airDateTitle)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.tv_showInfoAirDate)).setVisibility(View.INVISIBLE);
        }else{
            ((TextView) findViewById(R.id.tv_showInfoAirDate)).setText(tvRelease);
        }

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/original"+show.getBackdropImage())
                .into((ImageView) findViewById(R.id.iv_showInfoBackdrop));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_checkOnline){
            /** Creating new intent that searches online for the show title*/
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri baseUri = Uri.parse("https://www.google.com/search");
            Uri.Builder builder = baseUri.buildUpon();
            builder.appendQueryParameter("q", String.format("%s ", show.getName()));//("%s ",movieName)
            Uri dataUri = builder.build();
            intent.setData(dataUri);

            if (intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        }else if (v.getId() == R.id.btn_showInfoAddToWatched){
            /** Added the viewing show to the need to watch list*/
            show.setListName("seenList");
            SeenRepository.getRepository(getApplicationContext()).storeSeenShows(show);
            Toast.makeText(getApplicationContext(), "Added "+ show.getName()+" to Seen list! " , Toast.LENGTH_SHORT).show();

        }
        else if (v.getId() == R.id.btn_showInfoAddToWatchlist){
            /** Added the viewing show to the seen list*/
            show.setListName("toWatchList");
            SeenRepository.getRepository(getApplicationContext()).storeSeenShows(show);
            Toast.makeText(getApplicationContext(), "Added "+ show.getName()+" to need-to-watch list! " , Toast.LENGTH_SHORT).show();
        }

    }
}