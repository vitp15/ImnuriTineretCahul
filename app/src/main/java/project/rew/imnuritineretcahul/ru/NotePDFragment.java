package project.rew.imnuritineretcahul.ru;

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

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.items.note_pdf.HymnsPdfAdapter;
import project.rew.imnuritineretcahul.items.note_pdf.SetPDF;
import project.rew.imnuritineretcahul.utils.Utils;


public class NotePDFragment extends Fragment {

    HymnsPdfAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notepdf, container, false);
        Utils.loadHymns(root.getContext(), Language.RU);

        RecyclerView recyclerView = root.findViewById(R.id.rvHymns);
        adapter=new HymnsPdfAdapter(Utils.hymns_ru,getActivity(),Language.RU);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        TextView textView=root.findViewById(R.id.textView);
        SetPDF.setPDF(root.getContext(),Language.RU);
        // Loading Hymns from local storage
        textView.setText(R.string.imns_not_found_ru);

        if (Utils.hymns_ru.isEmpty()) {
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