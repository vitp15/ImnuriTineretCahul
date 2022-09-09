package project.rew.imnuritineretcahul.items.categories;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.audio.AudioListHymnsAdapter;
import project.rew.imnuritineretcahul.items.hymns.HymnsAdapter;
import project.rew.imnuritineretcahul.items.note_pdf.HymnsPdfAdapter;
import project.rew.imnuritineretcahul.utils.Utils;

public class CategoryActivity extends AppCompatActivity {

    private HymnsAdapter adapter;
    private AudioListHymnsAdapter audioListHymnsAdapter;
    private HymnsPdfAdapter pdfAdapter;
    String title;
    Type type;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        category = Utils.curentOpenedCategory;
        title = category.getTitle();
        type = (Type) getIntent().getSerializableExtra("type");
        RecyclerView recyclerView = findViewById(R.id.rvHymns);
        TextView textView = findViewById(R.id.textView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_title_category, null);
        TextView customTitle = customView.findViewById(R.id.actionbarTitle);
        customTitle.setText(title);
        getSupportActionBar().setCustomView(customView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER));

        if (category.getHymns().isEmpty()) {
            if (Utils.language == Language.RO)
                textView.setText(R.string.no_hymns_in_category_ro);
            else if (Utils.language == Language.RU)
                textView.setText(R.string.no_hymns_in_category_ru);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            if (type == Type.HYMN) {
                adapter = new HymnsAdapter(category.getHymns());
                recyclerView.setAdapter(adapter);
            } else if (type == Type.AUDIO) {
                audioListHymnsAdapter = new AudioListHymnsAdapter(category.getHymns());
                recyclerView.setAdapter(audioListHymnsAdapter);
            } else if (type == Type.PDF) {
                pdfAdapter = new HymnsPdfAdapter(category.getHymns());
                recyclerView.setAdapter(pdfAdapter);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_home, menu);
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
                if (type == Type.HYMN)
                    adapter.getFilter().filter(s);
                else if (type == Type.AUDIO)
                    audioListHymnsAdapter.getFilter().filter(s);
                else if (type == Type.PDF)
                    pdfAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}