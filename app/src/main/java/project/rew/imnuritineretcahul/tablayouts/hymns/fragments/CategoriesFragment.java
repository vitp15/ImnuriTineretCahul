package project.rew.imnuritineretcahul.tablayouts.hymns.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.categories.Category;
import project.rew.imnuritineretcahul.items.categories.CategoryAdapter;
import project.rew.imnuritineretcahul.items.categories.CategoryAdapter1;
import project.rew.imnuritineretcahul.utils.Utils;

public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories_home, container, false);
        setHasOptionsMenu(true);
        Utils.setCategoryes(getContext());
        recyclerView = root.findViewById(R.id.recyclerView);

        if (Utils.language == Language.RO) {
            adapter = new CategoryAdapter(Utils.categories_ro, Type.HYMN);
        } else if (Utils.language == Language.RU) {
            adapter = new CategoryAdapter(Utils.categories_ru, Type.HYMN);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_category, menu);
    }
}