package uk.ac.rgu.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewShowInfo extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_show_info);
        ((Button)findViewById(R.id.btn_checkOnline)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_checkOnline){
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri baseUri = Uri.parse("https://www.google.com/search");
            Uri.Builder builder = baseUri.buildUpon();
            builder.appendQueryParameter("q", String.format("%s ", "Lucifer"));//("%s ",movieName)
            Uri dataUri = builder.build();
            intent.setData(dataUri);

            if (intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        }
    }
}