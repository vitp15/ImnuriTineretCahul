package project.rew.imnuritineretcahul.ro.ui.home;

import android.media.MediaPlayer;
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
import project.rew.imnuritineretcahul.ro.hymns.Hymn;
import project.rew.imnuritineretcahul.ro.hymns.HymnsAdapter;

import static project.rew.imnuritineretcahul.ro.ui.audio.SetMediaPlayer.mediaPlayer;

public class HomeFragment extends Fragment {
    private List<Hymn> all_hymns;
    private HymnsAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Utils.loadHymns(root.getContext(), getString(R.string.ro_internal_hymns_folder));
        for (MediaPlayer mediaPlayer:mediaPlayer){
            if (mediaPlayer!=null){
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();}
        }
        RecyclerView recyclerView = root.findViewById(R.id.rvHymns);
        all_hymns=Utils.hymns;
        adapter=new HymnsAdapter(all_hymns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        TextView textView=root.findViewById(R.id.textView);
        // Loading Hymns from local storage

        if (Utils.hymns.isEmpty()) {
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
        inflater= getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_home,menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView=(SearchView) searchItem.getActionView();
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
}