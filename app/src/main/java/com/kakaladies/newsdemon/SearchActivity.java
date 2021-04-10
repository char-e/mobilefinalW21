package com.kakaladies.newsdemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * SearchActivity renders a list of Articles based on a query term and allows users to view and save articles
 */
public class SearchActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * onActivityResult code for viewing an Article
     */
    public static final int VIEW_ARTICLE = 345;

    /**
     * Filename and key for articles remaining sharedprefs
     */
    public static final String ARTICLES_REMAINING = "remaining";

    /**
     * List of articles presented in this view
     */
    ArrayList<Article> articles;

    /**
     * ListView that contains articles
     */
    ListView articleList;

    /**
     * Adapter for articleList that render articles into ListView list items
     */
    private BaseAdapter listAdapter;

    /**
     * How many more articles the user can view before being required to purchase upgrade
     */
    private int remaining;
    /**
     * Created an object of fragment that can be inflated with the view
     */
    FavouritesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for remaining articles
        SharedPreferences prefs = getSharedPreferences(ARTICLES_REMAINING, MODE_PRIVATE);
        remaining = prefs.getInt(ARTICLES_REMAINING, 5);


        articles = new ArrayList<>();
        this.listAdapter = initializeListAdapter();

        articleList = findViewById(R.id.articleList);
        articleList.setAdapter(this.listAdapter);


        loadArticles();
        /**
         * Use fragment object to inflate view with Fragment manager
         */
        //fragment = new FavouritesFragment();
        //getSupportFragmentManager()
        //        .beginTransaction()
        //        .add(R.id.frame, fragment)
        //        .commit();

        // Toolbar stuff
        Toolbar toolbar = (Toolbar)findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }


    /**
     * Loads articles to be displayed given a search query
     * @param query Term to filter loaded articles by
     */
    private void loadArticles() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        ArticleLoader articleLoader = new ArticleLoader();
        this.articles = articleLoader.load(this.listAdapter, findViewById(R.id.progressBar));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Snackbar snackbar = Snackbar.make(findViewById(R.id.root), getString(R.string.free_articles) + this.remaining, 5000);

        snackbar.setAction(R.string.upgrade, v-> {
            startActivity(new Intent(this, UnlockActivity.class));
        });

        snackbar.show();

        this.remaining--;
        SharedPreferences prefs = getSharedPreferences("articles", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(ARTICLES_REMAINING, this.remaining);
        edit.apply();
    }

    /**
     * Create List adapter to inflate Articles into Views
     * @return an adapter to associate with ListView
     */
    private BaseAdapter initializeListAdapter() {
        return new BaseAdapter() {
            private void showDetailsDialog(Article article) {
                new AlertDialog.Builder(SearchActivity.this)
                        .setTitle(article.getHeadline())
                        .setMessage(article.getDescription())
                        .setNegativeButton(R.string.save, (click, arg) -> article.save(SearchActivity.this))
                        .setNeutralButton(R.string.close, (click, arg) -> {})
                        .setPositiveButton(R.string.read, (click, arg) -> {
                            article.display(SearchActivity.this);
                        })
                        .create()
                        .show();

            }

            @Override
            public int getCount() {
                return SearchActivity.this.articles.size();
            }

            @Override
            public Object getItem(int position) {
                return SearchActivity.this.articles.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View old, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                Article article = SearchActivity.this.articles.get(position);

                View row = inflater.inflate(R.layout.article_row, parent, false);

                ((TextView)row.findViewById(R.id.headline)).setText(article.getHeadline());
                ((TextView)row.findViewById(R.id.published)).setText(article.getPublished());

                row.findViewById(R.id.read).setOnClickListener( v -> article.display(SearchActivity.this));

                row.setOnClickListener(v -> showDetailsDialog(article));

                return row;
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_to_favourites:
                startActivity(new Intent(this, FavouritesActivity.class));
                break;

            case R.id.nav_to_unlock:
                startActivity(new Intent(this, UnlockActivity.class));
                break;

            case R.id.nav_to_payment:
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("Message", "Authorize $9.99 charge?");
                startActivity(intent);
                break;

        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {
            case R.id.purchaseButton:
                startActivity(new Intent(this, UnlockActivity.class));
                break;
            case R.id.favouriteButton:
                startActivity(new Intent(this, FavouritesActivity.class));
                break;
            case R.id.helpButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setMessage(R.string.searchHelp)
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                break;
        }


        return true;
    }


}