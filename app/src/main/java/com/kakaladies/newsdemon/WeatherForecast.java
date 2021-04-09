package com.kakaladies.newsdemon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.kakaladies.newsdemon.R;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        findViewById(R.id.progress).setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute(
                "https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String uv, minTemp, maxTemp, currentTemp;
        private Bitmap weatherPic;

        @Override
        protected String doInBackground(String... strings) {
            try {

                // Create a URL object for the API.
                URL url = new URL(strings[0]);

                // Open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                // Wait for data
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(response, "UTF-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    if (eventType == XmlPullParser.START_TAG) {
                        if (tagName.equalsIgnoreCase("temperature")) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (tagName.equalsIgnoreCase("weather")) {
                            String iconName = parser.getAttributeValue(null, "icon");
                            Log.i("weather", "Checking for bitmap file: " + iconName + ".png");
                            if (fileExistence(iconName)) {
                                Log.i("weather", "Found " + iconName + ".png!");
                                try (FileInputStream fis = openFileInput(iconName + ".png")) {
                                    weatherPic = BitmapFactory.decodeStream(fis);
                                }
                            } else {
                                Log.i("weather", "Did not find file, downloading...");
                                weatherPic = null;
                                URL urlWeatherPic = new URL(
                                        "http://openweathermap.org/img/w/" + iconName + ".png");
                                urlConnection = (HttpURLConnection) urlWeatherPic.openConnection();
                                urlConnection.connect();
                                int responseCode = urlConnection.getResponseCode();
                                if (responseCode == 200) {
                                    weatherPic = BitmapFactory
                                            .decodeStream(urlConnection.getInputStream());
                                    publishProgress(100);
                                    FileOutputStream outputStream = openFileOutput(
                                            iconName + ".png", Context.MODE_PRIVATE);
                                    weatherPic.compress(CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            }
                        }
                    }
                    eventType = parser.next();
                }

                URL urlUV = new URL(
                        "https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

                urlConnection = (HttpURLConnection) urlUV.openConnection();
                urlConnection.connect();
                response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                double value = jObject.getDouble("value");
                Log.e("weather", "UV: " + value);
                uv = String.valueOf(value);
            } catch (Exception e) {
                Log.e("weather", "Something went wrong...", e);
            }

            return "Done";
        }

        public boolean fileExistence(String name) {
            File file = getBaseContext().getFileStreamPath(name + ".png");
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        @Override
        protected void onPostExecute(String fromDoInBackground) {
            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.INVISIBLE);

            final String CELSIUS = " Celsius";

            TextView currentTempObject = findViewById(R.id.currentTemp);
            String text =
                    getResources().getString(R.string.currentTemp) + ": " + currentTemp + CELSIUS;
            currentTempObject.setText(text);

            TextView minTempObject = findViewById(R.id.minTemp);
            text = getResources().getString(R.string.minTemp) + ": " + minTemp + CELSIUS;
            minTempObject.setText(text);

            TextView maxTempObject = findViewById(R.id.maxTemp);
            text = getResources().getString(R.string.maxTemp) + ": " + maxTemp + CELSIUS;
            maxTempObject.setText(text);

            TextView uvRatingObject = findViewById(R.id.uvRating);
            text = getResources().getString(R.string.uvRating) + ": " + uv;
            uvRatingObject.setText(text);

            ImageView weather = findViewById(R.id.weatherImage);
            weather.setImageBitmap(weatherPic);
        }
    }
}