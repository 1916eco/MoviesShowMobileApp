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
    //private static String JSON_URL = "https://api.themoviedb.org/3/search/tv?api_key=2af2618736137dad0ac52770650060d6&page=1&query=lucifer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showList = new ArrayList<>();
        Log.d(TAG,"IT CREATED");
        getShowFromAPI(JSON_URL);

        Button btnWatchList = findViewById(R.id.btn_watchList);
        btnWatchList.setOnClickListener(this);

        Button btnmyList = findViewById(R.id.btn_myList);
        btnmyList.setOnClickListener(this);

        Button btnNewShowSearch = findViewById(R.id.btn_SearchNewShowSubmit);
        btnNewShowSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_watchList){
            Intent intent = new Intent(getApplicationContext(), myWatchlist.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_myList) {
            Intent intent = new Intent(getApplicationContext(), myshows.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_SearchNewShowSubmit){

            EditText etLocation = findViewById(R.id.et_myMyShowNameSearch);
            newShowNameSearch = String.valueOf(etLocation.getText());
            if (!newShowNameSearch.matches("")){
                String searched = "https://api.themoviedb.org/3/search/tv?api_key=2af2618736137dad0ac52770650060d6&page=1&query="+ newShowNameSearch;

                getShowFromAPI(searched);
                Toast.makeText(getApplicationContext(), "Searching for "+newShowNameSearch, Toast.LENGTH_SHORT).show();
            }else {getShowFromAPI(JSON_URL);}
        }
    }



    private void getShowFromAPI(String JSON_URL) {

        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {

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
                Log.d(TAG,"Error "+error.getLocalizedMessage());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }


    //Keeping app state

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
