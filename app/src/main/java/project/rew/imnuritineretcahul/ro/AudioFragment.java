package project.rew.imnuritineretcahul.ro;

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
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.items.audio.AudioListHymnsAdapter;
import project.rew.imnuritineretcahul.items.audio.SetMediaPlayer;
import project.rew.imnuritineretcahul.utils.Utils;

public class AudioFragment extends Fragment {
    private List<Hymn> all_hymns;
    private AudioListHymnsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio_ro, container, false);
        Utils.loadHymns(root.getContext(), Language.RO);
        SetMediaPlayer.setMediaPlayer(root.getContext());
        RecyclerView recyclerView = root.findViewById(R.id.rvHymns);
        all_hymns = Utils.hymns_ro;
        adapter = new AudioListHymnsAdapter(all_hymns, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        TextView textView = root.findViewById(R.id.textView);


        // Loading Hymns from local storage

        if (Utils.hymns_ro.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
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
        for (int i = 0; i < SetMediaPlayer.mediaPlayer.length; i++) {
            if (SetMediaPlayer.mediaPlayer[i] != null) {
                SetMediaPlayer.mediaPlayer[i].seekTo(0);
                SetMediaPlayer.mediaPlayer[i].stop();
            }
        }
    }
}