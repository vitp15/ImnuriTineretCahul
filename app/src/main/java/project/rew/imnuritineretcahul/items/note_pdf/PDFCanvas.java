package project.rew.imnuritineretcahul.items.note_pdf;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.barteksc.pdfviewer.PDFView;


import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.Utils;

public class PDFCanvas extends AppCompatActivity {
    Hymn hymn;
    Language language;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_canvas);
        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);
        int languageInt = getIntent().getIntExtra("language", 0);
        if (languageInt == 1) {
            language = Language.RO;
            hymn = Utils.hymns_ro.get(nr - 1);
        } else if (languageInt == 0) {
            language = Language.RU;
            hymn = Utils.hymns_ru.get(nr - 1);
        }
        PDFView pdfView = findViewById(R.id.pdfView);
        linearLayout = findViewById(R.id.linear);
        pdfView.fromFile(hymn.getPdfView()).load();
        getSupportActionBar().setTitle(hymn.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfView.setOnClickListener(view -> {
            if(getSupportActionBar().isShowing()) getSupportActionBar().hide();
            else  getSupportActionBar().show();
        });

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            params.setMargins(0, 0, 0, 0);
            linearLayout.setLayoutParams(params);
        } else {
            getSupportActionBar().show();
            params.setMargins(0, 50, 0, 0);
            linearLayout.setLayoutParams(params);
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_canvas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Utils.deleteFile(this, String.valueOf(hymn.getId()) + ".pdf", language, Type.PDF);
            onBackPressed();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
