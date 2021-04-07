package com.kakaladies.newsdemon;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for loading articles
 */
public class ArticleLoader  extends AsyncTask<String, Integer, String> {

    private final static String ENDPOINT = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

    private ArrayList<Article> articles;
    private BaseAdapter listAdapter;

    /**
     * Load articles based on a query string
     * @return ArrayList of relevant articles
     */
    public ArrayList<Article> load(BaseAdapter listAdapter) {
        this.listAdapter = listAdapter;
        articles = new ArrayList<>();


        execute("here come dat boi");

        return articles;
    }

    private InputStream fetch(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL address = new URL(url);

            urlConnection = (HttpURLConnection) address.openConnection();

            return new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            Log.e(SearchActivity.class.toString(), e.toString());
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String huh) {
        this.listAdapter.notifyDataSetChanged();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream stream = fetch(ENDPOINT);

        //////// Parse stream here

        addArticle("fds", "SDf", "DGFdf", "bv");

        return "o shit waddup";
    }

    private void addArticle(String headline, String description, String url, String published) {
        articles.add(
                new Article()
                        .setHeadline(headline)
                        .setDescription(description)
                        .setUrl(url)
                        .setPublished(published)
        );
    }
}
