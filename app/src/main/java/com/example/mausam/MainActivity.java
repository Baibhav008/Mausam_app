package com.example.mausam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Boolean nature_theme=true;
    Boolean city_theme=false;

    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {

            String results="";
            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1)
                {
                    char current = (char) data;
                    results+=current;
                    data=reader.read();
                }


            } catch (Exception e) {
                e.printStackTrace();
              //  Toast.makeText(MainActivity.this, "Sorry, Could Not Show Weather", Toast.LENGTH_SHORT).show();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try

            {

                if(s!=null) {

                    JSONObject jsonObject = new JSONObject(s);
                    String weatherinfo = jsonObject.getString("weather");
                    JSONArray arr = new JSONArray(weatherinfo);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonPart = arr.getJSONObject(i);
                        Log.i("main", jsonPart.getString("description"));
                        TextView cloudinfo;
                        TextView tempinfo;
                        TextView humidinfo;
                        TextView windinfo;

                        windinfo=findViewById(R.id.windinfo);
                        humidinfo=findViewById(R.id.humidinfo);
                        tempinfo=findViewById(R.id.tempinfo);
                        cloudinfo = findViewById(R.id.cloudinfo);

                        cloudinfo.setText("Weather :\t"+jsonPart.getString("main")+"\n\t\t              "+jsonPart.getString("description"));
                        String temp = jsonObject.getJSONObject("main").getString("temp");
                        String humid = jsonObject.getJSONObject("main").getString("humidity");
                        Log.i("ct", temp);
                        double ct = Double.parseDouble(temp);
                        ct=ct-273.15;
                        tempinfo.setText("Temperature : "+String.format("%.2f",ct)+"°C");
                        humidinfo.setText("Humidity : "+humid+"%");
                        String wind = jsonObject.getJSONObject("wind").getString("speed");

                        String direc = jsonObject.getJSONObject("wind").getString("deg");
                        double degree = Double.parseDouble(direc);

                        windinfo.setText("Wind Speed : "+wind+" m/s "+convertDegreeToCardinalDirection(degree));
                        ConstraintLayout constraintLayout;
                        constraintLayout = findViewById(R.id.layo);
                        if(jsonPart.getString("main").contains("ain")) // FOR RAIN
                        {
                            if (nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.rainbg_forest));
                            }
                            else if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.rain_city));
                            }


                        }

                        else if(jsonPart.getString("main").contains("oud")) // FOR CLOUDS
                        {
                            if (nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.clouds_forest));
                            }
                            else if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.clouds_city));
                            }


                        }

                        else if(jsonPart.getString("main").contains("ear")) // FOR CLEAR
                        {
                            if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.clear_city));
                            }
                            else if(nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.clear_forest));
                            }



                        }

                        else if(jsonPart.getString("main").contains("ist")||jsonPart.getString("main").contains("fog")||jsonPart.getString("main").contains("Fog")) //FOR FOG
                        {
                            if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.mist_city));
                            }
                            else if(nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.mist_forest));
                            }


                        }

                        else if(jsonPart.getString("main").contains("hunder")) // FOR THUNDER
                        {
                            if(nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.thunder_forest));
                            }
                            else if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.thunder_city));
                            }


                        }

                        else if(jsonPart.getString("main").contains("now")) // FOR SNOW
                        {
                            if(nature_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.snow_forest));
                            }
                            else if(city_theme)
                            {
                                constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.snow_city));
                            }


                        }
                        else
                        {
                            constraintLayout.setBackground(ContextCompat.getDrawable(constraintLayout.getContext(),R.drawable.initial_forest));
                        }



                    }
                }

                else

                {
                    Toast.makeText(MainActivity.this, "Sorry, Could Not Show Weather", Toast.LENGTH_SHORT).show();
                }
        } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Sorry, Could Not Find Weather ", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("City Theme");
        menu.add("Sound On (COMING SOON)");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("City Theme"))
        {
            city_theme=true;
            nature_theme=false;
            item.setTitle("Nature Theme");
            Toast.makeText(this, "Search again to see results", Toast.LENGTH_SHORT).show();
        }
        else if(item.getTitle().equals("Nature Theme"))
        {
            city_theme=false;
            nature_theme=true;
            item.setTitle("City Theme");
            Toast.makeText(this, "Search again to see results", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public String convertDegreeToCardinalDirection(double directionInDegrees){
        String cardinalDirection = null;
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "N";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "NNE";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "NE";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "ENE";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "E";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "ESE";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "SE";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "SSE";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "S";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "SSW";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "SW";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "WSW";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "W";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "WNW";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "NW";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }
    public void getWeather(View view)
    {
        EditText cityname;
        cityname=findViewById(R.id.cityname);

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityname.getText().toString()+"&appid=f62312887aeceeece315fa9df3587ae8");
        InputMethodManager mgr  = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);

    }
}