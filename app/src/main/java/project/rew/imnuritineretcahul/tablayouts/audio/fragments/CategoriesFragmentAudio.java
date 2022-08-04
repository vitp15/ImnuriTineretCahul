package project.rew.imnuritineretcahul.tablayouts.audio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import project.rew.imnuritineretcahul.R;

public class CategoriesFragmentAudio extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories_audio, container, false);
        return view;
    }
}