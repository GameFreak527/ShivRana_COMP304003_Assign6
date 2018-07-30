package com.example.shivrana.shivrana_comp304_assign6;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TTCActivity extends AppCompatActivity {
    Button submit;
    String[] originLatLon;
    String[] destinationLatLon;
    TextView showResult;
    EditText source, destination;
    String startPoint, endPoint;
    String APIKEY = "AIzaSyAqyCv1dPotA7Er5-LmN66MXPFdUsqGSN0";
    String URL = "http://maps.googleapis.com/maps/api/directions/json?";
    Geocoder locationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttc);

        declaration();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult.setText("");
                originLatLon = new String[2];
                destinationLatLon= new String[2];
                startPoint = source.getText().toString();
                endPoint = destination.getText().toString();
                originLatLon = getDirection(originLatLon,startPoint);
                destinationLatLon = getDirection(destinationLatLon,endPoint);
                createUrlString();
            }
        });
    }

    public void declaration(){
        submit = findViewById(R.id.tripSubmit);
        showResult = findViewById(R.id.tripResult);
        source = findViewById(R.id.fromLocation);
        destination = findViewById(R.id.destinationLocaiton);
        locationInfo = new Geocoder(getApplicationContext());

    }

    public String[] getDirection(String[] direction, String point){
        List<Address> geoCodes = null;
        try {
            geoCodes = locationInfo.getFromLocationName(point, 1);
            Iterator<Address> locations = geoCodes.iterator();
            //Getting values of longitude and latitude
            while (locations.hasNext()){
                direction[0] = String.valueOf(geoCodes.get(0).getLatitude());
                direction[1] = String.valueOf(geoCodes.get(0).getLongitude());
                break;
            }
            //Encoding latitude and longtitute
            direction[0] = URLEncoder.encode(direction[0],"UTF-8");
            direction[1] = URLEncoder.encode(direction[1],"UTF-8");
        }catch (IOException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return  direction;
    }

    public void createUrlString(){
        try {
            String origin = "origin=" + originLatLon[0] + "," + originLatLon[1];
            String destination = "destination=" + destinationLatLon[0] + "," + destinationLatLon[1];

            String sensorValue = URLEncoder.encode("false", "UTF-8");
            String sensor = "sensor="+ sensorValue;

            Date date = new Date(118,7,28,22,55,0);
            long l = date.getTime()/1000;

            String departureTimeValue = URLEncoder.encode(String.valueOf(l),"UTF-8");
            String departureTime = "departure_time="+ departureTimeValue;

            String modeValue = URLEncoder.encode("transit","UTF-8");
            String mode = "mode="+ modeValue;

            //Creating url
            URL = URL + origin+"&"+destination+"&"+sensor+"&"+departureTime+"&"+mode;


            new ReadTransitJSONFeedTask().execute(URL);
        }catch (UnsupportedEncodingException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class ReadTransitJSONFeedTask extends AsyncTask
            <String, Void, String> {
        protected String doInBackground(String... urls) {

            Log.d("url",urls[0]);
            System.out.println(urls[0]);
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            String directions=""; //to hold the result after parsing
            try {
                String str = result;
                Log.d("link",str);
                // build a JSON object
                JSONObject obj = new JSONObject(str);
                if (obj.getString("status").equals("OK"))
                    System.out.println("OK");
                JSONArray routes=obj.getJSONArray("routes");
                JSONObject data,data1;
                JSONObject bounds;
                //System.out.println(routes.length());
                //
                for (int i=0;i<routes.length();i++)
                {
                    data = routes.getJSONObject(i);
                    Iterator keys = data.keys();
                    while(keys.hasNext()){
                        System.out.println(keys.next());
                    }
                    System.out.println(data);
                    bounds= data.getJSONObject("bounds");
                    System.out.println(bounds);
                    Iterator keys1 = bounds.keys();
                    while(keys1.hasNext()){
                        System.out.println(keys1.next());
                    }

                    JSONArray legs= data.getJSONArray("legs");
                    //System.out.println(legs.length());
                    for (i=0;i<legs.length();i++)
                    {
                        data = legs.getJSONObject(i);
                        System.out.println(legs);
                        Iterator keys2 = data.keys();
                        while(keys2.hasNext()){
                            System.out.println(keys2.next());

                        }
                        JSONArray steps= data.getJSONArray("steps");
                        for (int j=0;j<steps.length();j++)
                        {
                            data1 = steps.getJSONObject(j);
                            System.out.println(steps);

                            Iterator keys3 = data1.keys();
                            while(keys3.hasNext()){
                                String key = (String)keys3.next();
                                if(key.equals("html_instructions")) {
                                    directions+="\n"+data1.getString(key);
                                    System.out.println(data1.getString(key));
                                }

                            }
                        }

                    }


                }
                // display the results in textview txtDirections
                Log.d("Directions", directions);
                Spanned mytext = Html.fromHtml(directions);
                showResult.setText(mytext.toString());
                showResult.setMovementMethod(ScrollingMovementMethod.getInstance());

            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        Log.d("url",URL);
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }
}
