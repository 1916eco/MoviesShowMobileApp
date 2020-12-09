package uk.ac.rgu.showlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import uk.ac.rgu.showlist.database.SeenRepository;
import uk.ac.rgu.showlist.database.ShowDao;

public class myshows extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshows);

        RecyclerView recyclerView = findViewById(R.id.rv_MyShowsOutput);



        List<Show> shows = (List<Show>) SeenRepository.getRepository(getApplicationContext()).getSeenShows();
        RecyclerView.Adapter adapter = new ShowRecyclerViewAdapter(getApplicationContext(),shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteAllMenu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Do this action");
            builder.setMessage("Are you sure you would like to Delete All?");

            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(getApplicationContext(), "clicked no! " , Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SeenRepository.getRepository(getApplicationContext()).deleteAllShows();
                    Toast.makeText(getApplicationContext(), "clicked yes! " , Toast.LENGTH_SHORT).show();

                    // I do not need any action here you might
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}