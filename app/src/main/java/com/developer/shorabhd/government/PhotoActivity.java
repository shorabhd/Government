package com.developer.shorabhd.government;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by shorabhd on 5/5/17.
 */

public class PhotoActivity extends AppCompatActivity{

    TextView office, name, location;
    ImageView photo;
    ConstraintLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        if(getActionBar()!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        office= (TextView)findViewById(R.id.pOffice);
        name = (TextView)findViewById(R.id.pName);
        location = (TextView)findViewById(R.id.pLoc);
        photo = (ImageView) findViewById(R.id.Picture);
        layout =  (ConstraintLayout)findViewById(R.id.layout);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("Name"));
        office.setText(intent.getStringExtra("Office"));
        location.setText(intent.getStringExtra("Location"));

        String url =  intent.getStringExtra("Picture");
        String color = intent.getStringExtra("Color");

        switch (color){
            case "Red": layout.setBackgroundColor(Color.RED);
                break;
            case "Blue": layout.setBackgroundColor(Color.BLUE);
                break;
            case "Black": layout.setBackgroundColor(Color.BLACK);
                break;
        }

        final String photUrl =url;

        if (url != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.load(url)
                    .resize(360,360)
                    .centerInside()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }
        else {
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
