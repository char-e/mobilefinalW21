package com.kakaladies.newsdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * SearchActivity renders a list of Articles based on a query term and allows users to view and save articles
 */
public class SearchActivity extends AppCompatActivity {

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

        String query = ((EditText)findViewById(R.id.search_query)).getText().toString();

        loadArticles(query);
        fragment = new FavouritesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, fragment)
                .commit();
    }

    /**
     * Loads articles to be displayed given a search query
     * @param query Term to filter loaded articles by
     */
    private void loadArticles(String query) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        ArticleLoader articleLoader = new ArticleLoader();
        this.articles = articleLoader.load(query);
        this.listAdapter.notifyDataSetChanged();

        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Snackbar snackbar = Snackbar.make(findViewById(R.id.root), getString(R.string.free_articles) + this.remaining, 5000);

        snackbar.setAction(R.string.upgrade, v-> {
            // TODO: navigate to unlock screen
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
}