package project.rew.imnuritineretcahul.tablayouts.hymns.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.rew.imnuritineretcahul.R;

public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        return root;
    }
}