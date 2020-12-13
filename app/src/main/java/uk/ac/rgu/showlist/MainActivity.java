package uk.ac.rgu.showlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.ac.rgu.showlist.database.SeenRepository;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private String newShowNameSearch;

    /**
     *
     */
    public List<Show> shows;

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    /**     API url to get the front page this would preferably be on a proxy, making a call to a proxy link
     *      would be more protective of the apikey and users would have no way of getting it
     */


    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREF_FILE),MODE_PRIVATE);

        /**
         * Assigning buttons and setOnClickListeners which then sends the id of its self as the parameter
         * with that we can filter out which button was clicked
         */
        Button btnWatchList = findViewById(R.id.btn_watchList);
        btnWatchList.setOnClickListener(this);

        Button btnmyList = findViewById(R.id.btn_myList);
        btnmyList.setOnClickListener(this);

        Button btnNewShowSearch = findViewById(R.id.btn_SearchNewShowSubmit);
        btnNewShowSearch.setOnClickListener(this);


        /**
         * As the Main activity loads the application starts the getShowFromAPI function
         */
        getShowFromAPI(getJsonUrl());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    public String getJsonUrl() {
        String JSON_URL = "https://api.themoviedb.org/3/discover/tv?api_key=2af2618736137dad0ac52770650060d6";
        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREF_FILE),MODE_PRIVATE);

        /**
         * Setting working with index of the array picked instead of the long string
         *
         * sorting the front page with specific ways
         */
        Integer personalised_sort = sharedPreferences.getInt(getString(R.string.SHARED_PREF_FILE_SORT),0);

        if (personalised_sort == 1){
            JSON_URL = JSON_URL + "&sort_by=popularity.asc&page=1";
        }
        else if (personalised_sort == 2){
            JSON_URL = JSON_URL + "&sort_by=release_date.desc&page=1";
        }
        else if (personalised_sort == 3){
            JSON_URL = JSON_URL + "&sort_by=release_date.asc&page=1";
        }
        else if (personalised_sort == 4){
            JSON_URL = JSON_URL + "&sort_by=vote_average.desc&page=1";
        }
        else if (personalised_sort == 5){
            JSON_URL = JSON_URL + "&sort_by=vote_average.asc&page=1";
        }
        else{JSON_URL = JSON_URL + "&sort_by=popularity.desc&page=1";}

        return JSON_URL;
    }

    @Override
    public void onClick(View v) {
        /**
         * Filter out what button was clicked so it does the required step
         */
        if (v.getId() == R.id.btn_watchList){
            Intent intent = new Intent(getApplicationContext(), myWatchlist.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_myList) {
            Intent intent = new Intent(getApplicationContext(), myshows.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_SearchNewShowSubmit){

            EditText etSearchTerm = findViewById(R.id.et_mainNameSearch);
            newShowNameSearch = String.valueOf(etSearchTerm.getText());
            if (!newShowNameSearch.matches("")){
                //get the Search word that is in the etSearchTerm
                String searched = "https://api.themoviedb.org/3/search/tv?api_key=2af2618736137dad0ac52770650060d6&page=1&query="+ newShowNameSearch;
                getShowFromAPI(searched);
                Toast.makeText(getApplicationContext(), "Searching for "+newShowNameSearch, Toast.LENGTH_SHORT).show();
            }else {getShowFromAPI(getJsonUrl());}//if the search is empty it should clear the search and reload the main page with no filtrations
        }
    }

    public void displayRecyclerView(){
        shows = getShows();
        RecyclerView recyclerView = findViewById(R.id.rv_newShowsOutput);
        RecyclerView.Adapter adapter = new ShowRecyclerViewAdapter(getApplicationContext(),shows);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void getShowFromAPI(String JSON_URL) {
        //Starting a Request queue

        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {

            /**
             * When the response comes is starts the onResponse where then it gets all the
             * information from the json converter and adds it to the Recycler adapter
             * then into a new layout manager in this case a linearLayoutManager is fine
             * @param response
             */
            @Override
            public void onResponse(String response) {

                Log.d(TAG,"ResponseSuccess "+response);
                JsonConverter converter = new JsonConverter();
                List<Show> shows = converter.convertJsonToShow(response);
                setShows(shows);
                displayRecyclerView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //If Error occurs just an error message in debug happens
                Log.d(TAG,"Error "+error.getLocalizedMessage());
            }
        });
        /**
         * Adding the request to the actual que without it the above code is just starting a queue
         * and nothing would happen
         */
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         *
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                /**
                 * s Is the show in the shows list then adding a setListName so the 2 list activities can
                 * display the correct information
                 */
                case ItemTouchHelper.RIGHT:
                    Show s = shows.get(position);
                    s.setListName("seenList");
                    SeenRepository.getRepository(getApplicationContext()).storeSeenShows(s);
                    displayRecyclerView();
                    break;
                case ItemTouchHelper.LEFT:
                    Show s1 = shows.get(position);
                    s1.setListName("toWatchList");
                    SeenRepository.getRepository(getApplicationContext()).storeSeenShows(s1);
                    displayRecyclerView();
                    break;
            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.informationGuide) {
            Intent intent = new Intent(getApplicationContext(), application_guide.class);
            startActivity(intent);
        }else if(id == R.id.settingsButton){
            Intent intent = new Intent(getApplicationContext(), settings.class);
            startActivity(intent);
        }
        else if(id == R.id.deleteAllMenu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Delete Watch Later");
            builder.setMessage("Are you sure you would like to Delete all Watch Later?");

            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Your data is safe again!" , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SeenRepository.getRepository(getApplicationContext()).deleteAll();
                    Toast.makeText(getApplicationContext(), "Deleted Everything! " , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    recreate();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * To keep the application on the same state as last used calling these over rides are needed
     * otherwise flipping the screen or minimizing the app will forget what it was doing
     */

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
