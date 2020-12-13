package uk.ac.rgu.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class settings extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREF_FILE), MODE_PRIVATE);

        Spinner spinner = findViewById(R.id.sp_sortPref);
        Integer personalised_sort = sharedPreferences.getInt(getString(R.string.SHARED_PREF_FILE_SORT),0);
        spinner.setSelection(personalised_sort);

        Button btnWatchList = findViewById(R.id.btn_savePref);
        btnWatchList.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_savePref){
/**
 * Saving with a button for users peace of mind
 */
            Spinner spinner = findViewById(R.id.sp_sortPref);
            int selectedIndex = spinner.getSelectedItemPosition();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(getString(R.string.SHARED_PREF_FILE_SORT), selectedIndex);

            Toast.makeText(getApplicationContext(), "Saved Settings", Toast.LENGTH_SHORT).show();
            editor.apply();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Spinner spinner = findViewById(R.id.sp_sortPref);
        int selectedIndex = spinner.getSelectedItemPosition();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.SHARED_PREF_FILE_SORT), selectedIndex);

        editor.apply();

    }
}