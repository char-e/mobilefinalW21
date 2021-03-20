package com.kakaladies.newsdemon;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for loading articles
 */
public class ArticleLoader {

    /**
     * Load articles based on a query string
     * @param query User provided search query
     * @return ArrayList of relevant articles
     */
    public ArrayList<Article> load(String query) {
        // TODO: Need to implement properly w/ AsyncTask

        Article test = new Article()
                .setHeadline("US and China trade angry words at high-level Alaska talks")
                .setDescription("The first high-level meeting of the new US administration and Beijing opened with sharp rebukes.")
                .setUrl("https://www.bbc.co.uk/news/world-us-canada-56452471")
                .setPublished("1 day ago");

        return new ArrayList<>(Arrays.asList(test));
    }
}
