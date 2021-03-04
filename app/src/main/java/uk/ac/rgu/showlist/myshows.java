package uk.ac.rgu.showlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import uk.ac.rgu.showlist.database.SeenRepository;
import uk.ac.rgu.showlist.database.ShowDao;

public class myshows extends AppCompatActivity implements View.OnClickListener {

    private String newShowNameSearch;

    /**
     *  Setting a local shows with a getter and setter so it can be accessed by the
     *  displayRecyclerView method
     */
    public List<Show> shows;
    public List<Show> getShows() {
        return shows;
    }
    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshows);

        ((Button)findViewById(R.id.btn_myShowListSubmit)).setOnClickListener(this);
        shows = SeenRepository.getRepository(getApplicationContext()).getSeenShows(getString(R.string.listSeen));
        setShows(shows);
        displayRecyclerView();
    }

    public void displayRecyclerView(){
        shows = getShows();
        RecyclerView recyclerView = findViewById(R.id.rv_MyShowsOutput);
        RecyclerView.Adapter adapter = new ShowRecyclerViewAdapter(getApplicationContext(),shows);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seenshowmenu,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_myShowListSubmit){
            EditText etSearchTerm = findViewById(R.id.et_myMyShowListNameSearch);
            newShowNameSearch = String.valueOf(etSearchTerm.getText());
            newShowNameSearch = "%" +newShowNameSearch +"%";
            if (!newShowNameSearch.matches("")||!newShowNameSearch.matches(null) ) {
                shows = (List<Show>) SeenRepository.getRepository(getApplicationContext()).getSearchedShows(newShowNameSearch,getString(R.string.listSeen));
                setShows(shows);
                displayRecyclerView();
                Log.d("BRUH", shows + newShowNameSearch);
            }else {
                shows = SeenRepository.getRepository(getApplicationContext()).getSeenShows(getString(R.string.listSeen));
                setShows(shows);
                displayRecyclerView();
            }
        }
    }

     /**
     * Swipe Item handler on this page both left and right swipe it will delete either way
     */
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            SeenRepository.getRepository(getApplicationContext()).deleteShowbyName(shows.get(position).name,getString(R.string.listSeen));
            shows = SeenRepository.getRepository(getApplicationContext()).getSeenShows(getString(R.string.listSeen));
            setShows(shows);
            displayRecyclerView();

        }
    };

    /**
     * AlertDialog creating a "Are you sure" dialog to get users final choice
     * @param item Menu Item
     * @return Returning Boolean - "YES" or "NO"
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteAllMenu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Delete Everything");
            builder.setMessage("Are you sure you would like to Delete all Seen?");

            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Your data is safe again!" , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SeenRepository.getRepository(getApplicationContext()).deleteAllShows(getString(R.string.listSeen));
                    Toast.makeText(getApplicationContext(), "Deleted Everything! " , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    recreate();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else if (id == R.id.informationGuide) {
            int Temp = SeenRepository.getRepository(getApplicationContext()).getCountAll();
            Toast.makeText(getApplicationContext(), "The amount of shows you watched "  + Temp, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}