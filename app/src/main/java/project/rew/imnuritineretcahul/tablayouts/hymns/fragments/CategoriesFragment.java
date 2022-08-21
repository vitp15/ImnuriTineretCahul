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
import project.rew.imnuritineretcahul.enums.Type;
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
        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.recyclerView);

        categories = new ArrayList<>();
        categories.add(new Category("1", "Noul Pământ", "Новая Земля", getActivity().getDrawable(R.drawable.c1), getActivity().getDrawable(R.drawable.c1ru)));
        categories.add(new Category("2", "Fericire", "Счастье", getActivity().getDrawable(R.drawable.c2), getActivity().getDrawable(R.drawable.c2ru)));
        categories.add(new Category("3", "Bunătate", "Доброта", getActivity().getDrawable(R.drawable.c3), getActivity().getDrawable(R.drawable.c3ru)));
        categories.add(new Category("4", "Iubire", "Любовь", getActivity().getDrawable(R.drawable.c4), getActivity().getDrawable(R.drawable.c4ru)));
        categories.add(new Category("5", "Mântuire", "Спасение", getActivity().getDrawable(R.drawable.c5), getActivity().getDrawable(R.drawable.c5ru)));
        categories.add(new Category("6", "Încercări", "Испытание", getActivity().getDrawable(R.drawable.c6), getActivity().getDrawable(R.drawable.c6ru)));
        categories.add(new Category("7", "Devotament", "Преданность", getActivity().getDrawable(R.drawable.c7), getActivity().getDrawable(R.drawable.c7ru)));
        categories.add(new Category("8", "Speranță", "Надежда", getActivity().getDrawable(R.drawable.c8), getActivity().getDrawable(R.drawable.c8ru)));
        categories.add(new Category("9", "Chemare", "Вызов", getActivity().getDrawable(R.drawable.c9), getActivity().getDrawable(R.drawable.c9ru)));

        adapter = new CategoryAdapter(categories, Type.HYMN);
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