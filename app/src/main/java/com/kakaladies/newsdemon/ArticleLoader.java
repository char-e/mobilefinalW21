package com.kakaladies.newsdemon;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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

    private final static String ENDPOINT = "https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

    private ArrayList<Article> articles;
    private BaseAdapter listAdapter;
    private ProgressBar progressBar;

    /**
     * Load articles based on a query string
     * @return ArrayList of relevant articles
     */
    public ArrayList<Article> load(BaseAdapter listAdapter, ProgressBar progressBar) {
        this.listAdapter = listAdapter;
        this.progressBar = progressBar;
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
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream stream = fetch(ENDPOINT);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(stream , "UTF-8");

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("item")) {
                        Article article = new Article();
                        do {
                            eventType = xpp.next();

                            if(eventType == XmlPullParser.START_TAG)
                            switch (xpp.getName()) {
                                case "title":
                                    xpp.next();
                                    article.setHeadline(xpp.getText());
                                    break;
                                case "description":
                                    xpp.next();
                                    article.setDescription(xpp.getText());
                                    break;
                                case "link":
                                    xpp.next();
                                    article.setUrl(xpp.getText());
                                    break;
                                case "pubDate":
                                    xpp.next();
                                    article.setPublished(xpp.getText());
                                    break;
                            }
                        } while( !(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("item")) );

                        articles.add(article);
                    }
                }
                eventType = xpp.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

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
