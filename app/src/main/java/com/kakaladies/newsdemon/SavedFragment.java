package com.kakaladies.newsdemon;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    public static String TAG = "FavouritesDialogFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARTICLE = "article";



    // TODO: Rename and change types of parameters
    private Article article;

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param article Parameter 1.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(Article article) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE, article);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = (Article) getArguments().getSerializable(ARTICLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        TextView headline = view.findViewById(R.id.saved_fragment_headline);
        Button delete = view.findViewById(R.id.removeFromFavBtn);

        headline.setText(article.getHeadline());
        delete.setOnClickListener(v -> {
            article.delete(getContext());
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        return view;
    }


}