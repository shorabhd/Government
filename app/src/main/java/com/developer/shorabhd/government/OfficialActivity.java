package com.developer.shorabhd.government;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by shorabhd on 5/5/17.
 */


public class OfficialActivity extends AppCompatActivity {

    TextView textView, name, office, party, address, website, phone, email;
    ImageButton photo, fb, twitter, googlePlus, youtube;
    String location;
    Official official;
    ConstraintLayout layout;
    String color;
    String TAG = ".OfficialActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        layout = (ConstraintLayout)findViewById(R.id.layout);
        textView = (TextView) findViewById(R.id.location);
        office = (TextView) findViewById(R.id.office);
        name = (TextView) findViewById(R.id.name);
        party = (TextView) findViewById(R.id.party);
        website = (TextView) findViewById(R.id.url);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        photo = (ImageButton) findViewById(R.id.photo);
        fb = (ImageButton)findViewById(R.id.fb);
        twitter = (ImageButton)findViewById(R.id.twitter);
        googlePlus = (ImageButton)findViewById(R.id.googleplus);
        youtube = (ImageButton)findViewById(R.id.youtube);

        Intent intent = getIntent();
        location = intent.getStringExtra("LOCATION");

        textView.setText(location);
        official = (Official) intent.getSerializableExtra("Official");

        office.setText(official.getOffice());
        name.setText(official.getName());
        String value = "("+ official.getParty()+")";
        party.setText(value);

        if(official.getAddress()==null || official.getAddress().isEmpty()){
            address.setText("");

        }
        else{
            address.setText(official.getAddress());
        }

        if(official.getEmail()==null || official.getEmail().isEmpty()){
            email.setText("");

        }
        else{
            email.setText(official.getEmail());
        }

        //phone
        if(official.getPhone()==null || official.getPhone().isEmpty()){
            phone.setText("");

        }
        else{
            phone.setText(official.getPhone());
        }

        //website
        if(official.getWebsite()==null || official.getWebsite().isEmpty()){
            website.setText("");
        }
        else{
            website.setText(official.getWebsite());
        }

        if(official.getPhoto().equals("")){
            photo.setImageResource(R.drawable.missingimage);
        }
        else if (official.getPhoto() != null || (!official.getPhoto().isEmpty())) {
            openPicassoPhoto(official.getPhoto());
        }


        if(official.getYoutube()== null || official.getYoutube().isEmpty()){
            youtube.setVisibility(View.INVISIBLE);
        }
        else{
            youtube.setVisibility(View.VISIBLE);
            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "Redirecting to YouTube", Toast.LENGTH_LONG).show();
                    youTubeClicked(official.getYoutube());
                }
            });
        }

        if(official.getFb()== null){
            fb.setVisibility(View.INVISIBLE);
        }
        else{
            fb.setVisibility(View.VISIBLE);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "Redirecting to FB", Toast.LENGTH_LONG).show();
                    facebookClicked(official.getFb());
                }
            });
        }

        if(official.getTwitter()== null){
            twitter.setVisibility(View.INVISIBLE);
        }
        else{
            twitter.setVisibility(View.VISIBLE);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "Redirecting to Twitter", Toast.LENGTH_LONG).show();
                    twitterClicked(official.getTwitter());
                }
            });
        }
        if(official.getGooglePlus()== null){
            googlePlus.setVisibility(View.INVISIBLE);
        }
        else{
            googlePlus.setVisibility(View.VISIBLE);
            googlePlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "Redirecting to GooglePlus", Toast.LENGTH_LONG).show();
                    googlePlusClicked(official.getGooglePlus());
                }
            });
        }

        if (official.getParty().equals("Republican")) {
            layout.setBackgroundColor(Color.RED);
            color="Red";
        } else if (official.getParty().equals("Democratic") || official.getParty().equals("Democrat")) {
            layout.setBackgroundColor(Color.BLUE);
            color="Blue";
        } else {
            layout.setBackgroundColor(Color.BLACK);
            color="Black";
        }

        Linkify.addLinks(website, Linkify.WEB_URLS);
        Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (official.getPhoto() != null) {
                    Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
                    intent.putExtra("Picture", official.getPhoto());
                    intent.putExtra("Name", official.getName());
                    intent.putExtra("Office", official.getOffice());
                    intent.putExtra("Location", location);
                    intent.putExtra("Color",color);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                //Toast.makeText(this, "Redirecting to Home.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void openPicassoPhoto(String url) {
        final String photUrl = url;
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
                    .resize(180,180)
                    .centerInside()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
            Log.d(TAG, "openPicassoPhoto: "+url);

        } else {
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);

        }
    }

    public void facebookClicked(String fbId) {
        String FACEBOOK_URL = "https://www.facebook.com/" + fbId;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                //older versions of fb app
                urlToUse = "fb://page/" + fbId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
            //normal web url
        }

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(String handle) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + handle));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + handle));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(String gPLus) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", gPLus);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + gPLus)));
        }
    }

    public void youTubeClicked(String youId) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youId));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youId)));
        }
    }

}
