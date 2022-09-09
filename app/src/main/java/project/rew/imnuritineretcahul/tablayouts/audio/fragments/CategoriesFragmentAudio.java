package project.rew.imnuritineretcahul.tablayouts.audio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.categories.Category;
import project.rew.imnuritineretcahul.items.categories.CategoryAdapter;
import project.rew.imnuritineretcahul.items.categories.CategoryAdapter1;
import project.rew.imnuritineretcahul.utils.Utils;

public class CategoriesFragmentAudio extends Fragment {
    RecyclerView recyclerView;
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories_audio, container, false);
        Utils.setCategoryes(getContext());
        recyclerView = root.findViewById(R.id.recyclerView);

        if (Utils.language == Language.RO) {
            adapter = new CategoryAdapter(Utils.categories_ro, Type.AUDIO);
        } else if (Utils.language == Language.RU) {
            adapter = new CategoryAdapter(Utils.categories_ru, Type.AUDIO);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }
}