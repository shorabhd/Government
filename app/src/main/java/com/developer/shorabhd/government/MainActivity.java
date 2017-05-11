package com.developer.shorabhd.government;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView textView;
    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static List<Official> mOfficialList = new ArrayList<Official>();
    public String TAG = "MainActivity";
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.location);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        if(!isNetworkAvailable()){
            textView.setText("No Data for Location");
        }
        else {
            mAdapter = new Adapter(mOfficialList,this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            if(allowLocationAccess()) {
                geocoder = new Geocoder(this, Locale.getDefault());
                doLocationWork(41.8764396, -87.6343844);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public void doLocationWork(double latitude, double longitude){
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            Log.d(TAG,addresses.get(0).getPostalCode());
            new AsyncCivicInfoDownloader(this).execute(addresses.get(0).getPostalCode());
        } catch (IOException e) {
            Toast.makeText(this," Address cannot be acquired from provided latitude/longitude",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class));
                return true;
            case R.id.search:
                if(isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(et);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG,et.getText().toString());
                            if (isNetworkAvailable())
                                Log.d(TAG,et.getText().toString());
                                new AsyncCivicInfoDownloader(MainActivity.this).execute(et.getText().toString());
                        }
                    });
                    builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.setMessage("Please enter Location name:");
                    builder.setTitle("Location");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.setMessage("Network Unavailable");
                    builder.setTitle("No Internet Connection");
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = mRecyclerView.getChildPosition(v);
        Official o = mOfficialList.get(pos);
        Intent i = new Intent(this, OfficialActivity.class);
        //i.putExtra(Intent.EXTRA_TEXT, OfficialActivity.class.getSimpleName());
        i.putExtra("LOCATION", textView.getText());
        i.putExtra("Official",o);
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean allowLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

    public void loadData(String zip, ArrayList<Official> officialArrayList) {
        if(zip==null || zip.isEmpty()){
            textView.setText("No Data for Location");
        }
        else{
            textView.setText(zip);
        }
        mOfficialList.clear();
        mOfficialList = officialArrayList;
        Log.d(TAG, "loadData: ");
        mAdapter = new Adapter(mOfficialList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
