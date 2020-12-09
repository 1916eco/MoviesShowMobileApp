package uk.ac.rgu.showlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import uk.ac.rgu.showlist.database.SeenRepository;

public class myWatchlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watchlist);

        RecyclerView recyclerView = findViewById(R.id.rv_myWatchlistOutput);

        List<Show> shows = (List<Show>) SeenRepository.getRepository(getApplicationContext()).getSeenShows("toWatchList");
        RecyclerView.Adapter adapter = new ShowRecyclerViewAdapter(getApplicationContext(),shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}