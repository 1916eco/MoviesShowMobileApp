package uk.ac.rgu.showlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.rgu.showlist.database.SeenDatabase;
import uk.ac.rgu.showlist.database.ShowDao;

public class JsonConverter {

    public ShowDao showDao;

    /**
     * @apiNote The API sends all the results in the "results" array and it iterates in the array
     * adding the name,overview,first_air_date,poster_path,backdrop_path, show id and vote_average
     * @param jsonString The full Json string that the API resoponded with
     * @return  Returns the shows as a Show object
     */
    public List<Show> convertJsonToShow(String jsonString){
        List<Show> shows = new ArrayList<Show>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsObject = jsonObject.getJSONArray("results");
            for (int i =0, j = resultsObject.length();i<j;i++){
                JSONObject resultObject = resultsObject.getJSONObject(i);
                Show show = new Show();
                show.setName(resultObject.getString("name"));
                show.setOverview(resultObject.getString("overview"));
                show.setFirstAirDate(resultObject.getString("first_air_date"));
                show.setPosterImage(resultObject.getString("poster_path"));
                show.setBackdropImage(resultObject.getString("backdrop_path"));
                show.setId(resultObject.getInt("id"));
                show.setVoteAvg(resultObject.getDouble("vote_average"));
                shows.add(show);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shows;
    }
}