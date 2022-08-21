package project.rew.imnuritineretcahul.tablayouts.audio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.items.audio.AudioListHymnsAdapter;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.Utils;

public class AllHymnsFragmentAudio extends Fragment {

    public static AudioListHymnsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_hymns_audio, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvHymns);
        TextView textView = root.findViewById(R.id.textView);
        if (Utils.language == Language.RO) {
            if (Utils.hymns_ro.isEmpty()) {
                textView.setText(R.string.imns_not_found_ro);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
                adapter = new AudioListHymnsAdapter(Utils.hymns_ro);
                recyclerView.setAdapter(adapter);
            }
        } else if (Utils.language == Language.RU) {
            if (Utils.hymns_ru.isEmpty()) {
                textView.setText(R.string.imns_not_found_ru);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
                adapter = new AudioListHymnsAdapter(Utils.hymns_ru);
                recyclerView.setAdapter(adapter);
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_audio, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);

                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}