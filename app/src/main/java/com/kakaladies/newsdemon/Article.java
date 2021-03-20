package com.kakaladies.newsdemon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A news Article
 */
public class Article {

    /**
     * Article headline
     */
    private String headline;

    /**
     * Description of article content
     */
    private String description;

    /**
     * Public URL to view full article
     */
    private String url;

    /**
     * When the article was published in a readable form (ie '1 day ago')
     */
    private String published;

    public Article() {

    }

    /**
     * Save this article to users list of saved articles
     * @param activity Parent activity used to create Toast
     */
    public void save(Context activity) {
        Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();

        // TODO: Save to list of saved articles
    }

    /**
     * Open web browser and navigate to article full text
     * @param activity
     */
    public void display(AppCompatActivity activity) {
        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), SearchActivity.VIEW_ARTICLE);
    }

    /**
     * Set Article headline
     * @param headline headline text
     * @return self
     */
    public Article setHeadline(String headline) {
        this.headline = headline;

        return this;
    }

    /**
     * Set Article description
     * @param description description text
     * @return self
     */
    public Article setDescription(String description) {
        this.description = description;

        return this;
    }

    /**
     * Set Article url
     * @param url url text
     * @return self
     */
    public Article setUrl(String url) {
        this.url = url;

        return this;
    }

    /**
     * Set Article when published
     * @param published published text
     * @return self
     */
    public Article setPublished(String published) {
        this.published = published;

        return this;
    }

    /**
     * Get article headline
     * @return headline
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * Get article description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get article url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get when article was published
     * @return when article was published
     */
    public String getPublished() {
        return published;
    }
}
