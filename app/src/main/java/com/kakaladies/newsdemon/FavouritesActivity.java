package com.kakaladies.newsdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    /**
     * ListView that contains articles
     */
    ListView articleList;

    /**
     * Adapter for articleList that render articles into ListView list items
     */
    private BaseAdapter listAdapter;

    /**
     * List of articles presented in this view
     */
    ArrayList<Article> articles;

    /**
     * How many more articles the user can view before being required to purchase upgrade
     */
    private int remaining;

    /**
     * Filename and key for articles remaining sharedprefs
     */
    public static final String ARTICLES_REMAINING = "remaining";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        articles = (ArrayList<Article>) (new ArticleRepository(this).findAll());
        this.listAdapter = initializeListAdapter();


        // Check for remaining articles
        SharedPreferences prefs = getSharedPreferences(ARTICLES_REMAINING, MODE_PRIVATE);
        remaining = prefs.getInt(ARTICLES_REMAINING, 5);

        articleList = findViewById(R.id.favourites_list);
        articleList.setAdapter(this.listAdapter);

        findViewById(R.id.favouritesHelpButton).setOnClickListener( v-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(FavouritesActivity.this);
            builder.setMessage(R.string.favouritesHelpText)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        });
    }


    /**
     * Create List adapter to inflate Articles into Views
     * @return an adapter to associate with ListView
     */
    private BaseAdapter initializeListAdapter() {
        return new BaseAdapter() {
            private void showDetailsDialog(Article article) {
                // Show FavouritesFragment

                FavouritesFragment.newInstance(article).show(
                        getFragmentManager(), FavouritesFragment.TAG
                );
            }

            @Override
            public int getCount() {
                return FavouritesActivity.this.articles.size();
            }

            @Override
            public Object getItem(int position) {
                return FavouritesActivity.this.articles.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View old, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                Article article = FavouritesActivity.this.articles.get(position);

                View row = inflater.inflate(R.layout.article_row, parent, false);

                ((TextView)row.findViewById(R.id.headline)).setText(article.getHeadline());
                ((TextView)row.findViewById(R.id.published)).setText(article.getPublished());

                row.findViewById(R.id.read).setOnClickListener( v -> article.display(FavouritesActivity.this));

                row.setOnClickListener(v -> showDetailsDialog(article));

                return row;
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Snackbar snackbar = Snackbar.make(findViewById(R.id.favourites_root), getString(R.string.free_articles) + this.remaining, 5000);

        snackbar.setAction(R.string.upgrade, v-> startActivity(new Intent(this, UnlockActivity.class)));

        snackbar.show();

        this.remaining--;
        SharedPreferences prefs = getSharedPreferences("articles", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(ARTICLES_REMAINING, this.remaining);
        edit.apply();
    }

}