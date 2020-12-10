package uk.ac.rgu.showlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private String newShowNameSearch;

    //key 2af2618736137dad0ac52770650060d6

    private static String JSON_URL = "https://api.themoviedb.org/3/discover/tv?api_key=2af2618736137dad0ac52770650060d6&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        getShowFromAPI(JSON_URL);
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
            }else {getShowFromAPI(JSON_URL);}//if the search is empty it should clear the search and reload the main page with no filtrations
        }
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
                RecyclerView recyclerView = findViewById(R.id.rv_newShowsOutput);

                Log.d(TAG,"ResponseSuccess "+response);
                JsonConverter converter = new JsonConverter();
                List<Show> shows = converter.convertJsonToShow(response);
                RecyclerView.Adapter adapter = new ShowRecyclerViewAdapter(getApplicationContext(),shows);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
