package project.rew.imnuritineretcahul.tablayouts.hymns.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.items.categories.Category;
import project.rew.imnuritineretcahul.items.categories.CategoryAdapter;

public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    List<Category> categories;
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        categories = new ArrayList<>();
        categories.add(new Category("1", "Noul Pământ", getActivity().getDrawable(R.drawable.c1), getActivity().getDrawable(R.drawable.c1)));
        categories.add(new Category("2", "Fericire", getActivity().getDrawable(R.drawable.c2), getActivity().getDrawable(R.drawable.c2)));
        categories.add(new Category("3", "Bunătate", getActivity().getDrawable(R.drawable.c3), getActivity().getDrawable(R.drawable.c3)));
        categories.add(new Category("4", "Iubire", getActivity().getDrawable(R.drawable.c4), getActivity().getDrawable(R.drawable.c4)));
        categories.add(new Category("5", "Mântuire", getActivity().getDrawable(R.drawable.c5), getActivity().getDrawable(R.drawable.c5)));
        categories.add(new Category("6", "Încercări", getActivity().getDrawable(R.drawable.c6), getActivity().getDrawable(R.drawable.c6)));
        categories.add(new Category("7", "Speranță", getActivity().getDrawable(R.drawable.c7), getActivity().getDrawable(R.drawable.c7)));

        adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }
}