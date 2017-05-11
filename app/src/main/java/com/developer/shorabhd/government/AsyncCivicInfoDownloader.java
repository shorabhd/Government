package com.developer.shorabhd.government;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shorabhd on 4/7/17.
 */

public class AsyncCivicInfoDownloader extends AsyncTask<String,Void,String> {

    private MainActivity ma;
    private ArrayList<Official> officialArrayList = new ArrayList<Official>();
    private String name, office, party, photo, phone, website, add, gplus, youtube, fb, twitter;
    private String postal;

    public AsyncCivicInfoDownloader(MainActivity ma){
        this.ma = ma;
    }
    private String API_KEY = "AIzaSyAho17M1s9nY-X0Oy1RArAvtYD-zj8P-p8";
    private String URL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private String TAG = ".AsyncCivicInfo";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String[] params) {
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("key", API_KEY);
        buildURL.appendQueryParameter("address", params[0]);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            java.net.URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString().substring(4));

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return "Exception";
        }
        return sb.toString().replace("\n//","");
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject object= new JSONObject(s);
            JSONObject normalizedInput= new JSONObject(object.getString("normalizedInput"));
            postal = normalizedInput.getString("city")+", "
                    +normalizedInput.getString("state")+", "
                    +normalizedInput.getString("zip");
            JSONArray offices = new JSONArray(object.getString("offices"));
            JSONArray officials = new JSONArray(object.getString("officials"));
            for (int i = 0; i < offices.length(); i++) {
                JSONObject jo =  (JSONObject) offices.get(i);
                office = jo.getString("name");
                JSONArray jArr = jo.getJSONArray("officialIndices");

                for(int j=0;j<jArr.length();j++){
                    int val = Integer.parseInt(jArr.getString(j));
                    JSONObject tempArray = (JSONObject) officials.get(val);

                    Iterator<String> keysIt = tempArray.keys();
                    while (keysIt.hasNext())
                    {
                        String keyStr = keysIt.next();
                        switch (keyStr) {
                            case "name":
                                name = tempArray.getString("name");
                                Log.d(TAG, name);
                                break;
                            case "party":
                                party = tempArray.getString("party");
                                break;
                            case "photoUrl":
                                photo = tempArray.getString("photoUrl");
                                break;
                            case "phones":
                                JSONArray parr = (JSONArray) tempArray.get("phones");
                                phone = parr.getString(0);
                                break;
                            case "urls":
                                JSONArray warr = (JSONArray) tempArray.get("urls");
                                website = warr.getString(0);
                                break;
                            case "address":
                                JSONArray j1 = (JSONArray) tempArray.get("address");
                                JSONObject jadd = (JSONObject) j1.get(0);
                                Iterator<String> keysIterator = jadd.keys();
                                while (keysIterator.hasNext()) {
                                    String keyStr1 = keysIterator.next();
                                    add = add + jadd.getString(keyStr1);
                                    add = add + ", ";
                                }
                                add = add.substring(0, add.length() - 2);
                                Log.d(TAG, "doInBackground: " + add);
                                break;
                            case "channels":
                                JSONArray jObj1 = (JSONArray) tempArray.get("channels");
                                for (int m = 0; m < jObj1.length(); m++) {
                                    JSONObject jchannel = (JSONObject) jObj1.get(m);

                                    if (jchannel.getString("type").equals("GooglePlus")) {
                                        gplus = jchannel.getString("id");
                                    } else if (jchannel.getString("type").equals("YouTube")) {
                                        youtube = jchannel.getString("id");
                                    } else if (jchannel.getString("type").equals("Facebook")) {
                                        fb = jchannel.getString("id");
                                    } else if (jchannel.getString("type").equals("Twitter")) {
                                        twitter = jchannel.getString("id");
                                    }
                                }
                                break;
                        }
                    }
                    officialArrayList.add(new Official(name, office, party, add, phone,
                            website, photo, gplus, fb, twitter, youtube));
                    phone = "";
                    party = "Unknown";
                    website = "";
                    add = "";
                    gplus = null;
                    youtube = null;
                    fb = null;
                    twitter = null;
                    photo = "";
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();

        }
        ma.loadData(postal,officialArrayList);
    }
}
