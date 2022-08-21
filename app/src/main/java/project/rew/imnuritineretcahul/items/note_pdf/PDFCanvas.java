package project.rew.imnuritineretcahul.items.note_pdf;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.barteksc.pdfviewer.PDFView;


import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.DownloadSingleFileTask;
import project.rew.imnuritineretcahul.utils.Utils;

public class PDFCanvas extends AppCompatActivity {
    Hymn hymn;
    LinearLayout linearLayout;

    ImageView downloadBtn;
    ConstraintLayout constraintLayout;
    TextView indicationsToDownload;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_canvas);

        constraintLayout = findViewById(R.id.nonexist);

        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);
        if (Utils.language == Language.RO) {
            hymn = Utils.hymns_ro.get(nr - 1);
        } else if (Utils.language == Language.RU) {
            hymn = Utils.hymns_ru.get(nr - 1);
        }
        getSupportActionBar().setTitle(hymn.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hymn.getPdfView() != null) {

            constraintLayout.setVisibility(View.GONE);

            PDFView pdfView = findViewById(R.id.pdfView);
            linearLayout = findViewById(R.id.linear);
            pdfView.fromFile(hymn.getPdfView()).load();

            pdfView.setOnClickListener(view -> {
                if (getSupportActionBar().isShowing()) getSupportActionBar().hide();
                else getSupportActionBar().show();
            });
        } else {
            downloadBtn = findViewById(R.id.download);
            indicationsToDownload = findViewById(R.id.indication_to_download);

            constraintLayout.setVisibility(View.VISIBLE);

            if (Utils.language == Language.RO) {
                indicationsToDownload.setText(getString(R.string.update_pdf_ro));
            } else if (Utils.language == Language.RU) {
                indicationsToDownload.setText(getString(R.string.update_pdf_ru));
            }

            downloadBtn.setOnClickListener(v -> {
                new DownloadSingleFileTask(this, this, hymn, Type.PDF).execute();
                Utils.ifDownloadBrokedChangeListItem = true;
                Utils.constraintLayout.setBackground(this.getDrawable(R.drawable.hymn_list_press));
                Utils.hymn_title_to_edit.setTextColor(this.getResources().getColor(R.color.text_color));
                if (hymn.isSaved())
                    Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.to_save_btn_enable01));
                else
                    Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.to_save_btn_disable01));
            });
        }
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
            Utils.deleteFile(this, String.valueOf(hymn.getId()), Type.PDF);
            Utils.constraintLayout.setBackground(this.getDrawable(R.drawable.hymn_nonexistent_list_press));
            Utils.hymn_title_to_edit.setTextColor(this.getResources().getColor(R.color.nonpressed_nonexis_contur));
            if (hymn.isSaved())
                Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.nonexisting_save_clicked));
            else
                Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.nonexisting_save_nonclicked));
            Utils.loadHymns(this);
            recreate();
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
