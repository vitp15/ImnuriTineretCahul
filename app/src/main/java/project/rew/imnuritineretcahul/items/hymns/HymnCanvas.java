package project.rew.imnuritineretcahul.items.hymns;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.fragments.AudioFragment;
import project.rew.imnuritineretcahul.items.audio.AudioCanvas;
import project.rew.imnuritineretcahul.items.audio.HymnsAudioRealTime;
import project.rew.imnuritineretcahul.items.note_pdf.PDFCanvas;
import project.rew.imnuritineretcahul.tablayouts.audio.fragments.AllHymnsFragmentAudio;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.AllHymnsFragment;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.utils.Utils;

public class HymnCanvas extends AppCompatActivity {

    int seekvalue;
    int minsbprogress = 30;
    String content;
    SeekBar seekBar;
    TextView hymnNumber, hymnTitle;
    ImageView btnBack, btnAudio, btnChords, btnSave;
    Hymn hymn;
    boolean showChords = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_canvas);

        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);

        if (Utils.language == Language.RO) {
            hymn = Utils.hymns_ro.get(nr - 1);
        } else if (Utils.language == Language.RU) {
            hymn = Utils.hymns_ru.get(nr - 1);
        }
        seekvalue = PrefConfig.load_saved_progress(this);

        WebView webView = findViewById(R.id.wvCanvas);
        webView.getSettings().setJavaScriptEnabled(true);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        hymnNumber = findViewById(R.id.hymn_number);
        hymnTitle = findViewById(R.id.hymn_title);
        btnBack = findViewById(R.id.btnBack);
        btnAudio = findViewById(R.id.startAudio);
        btnChords = findViewById(R.id.chordsOrPDF);
        btnSave = findViewById(R.id.saveHymn);

        seekBar.getProgressDrawable().setColorFilter(this.getColor(R.color.seekbar_text), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(this.getColor(R.color.thumb_text), PorterDuff.Mode.SRC_ATOP);

        hymnNumber.setText(String.valueOf(hymn.getNr()));
        hymnTitle.setText(hymn.getTitle());

        if (hymn.isSaved())
            btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.save_acces_btn_clicked_white01));
        else
            btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.save_acces_btn_white01));

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnAudio.setOnClickListener(v -> {
            Intent startAudio = new Intent(this, AudioCanvas.class);
            this.startActivity(startAudio);
        });

        btnChords.setOnClickListener(v -> {
            if (!showChords) {
                showChords = true;
                content = Utils.readContent(id, true, getApplicationContext());
                SetWebView(webView, seekvalue, content);
            } else {
                showChords = false;
                content = Utils.readContent(id, false, getApplicationContext());
                SetWebView(webView, seekvalue, content);
            }
        });

        btnChords.setOnLongClickListener(v -> {
            Intent startPDF = new Intent(HymnCanvas.this, PDFCanvas.class);
            startPDF.putExtra("id", hymn.getId());
            startPDF.putExtra("nr", hymn.getNr());
            HymnCanvas.this.startActivity(startPDF);
            return false;
        });

        btnSave.setOnClickListener(v -> {
            if (hymn.isSaved()) {
                Utils.deleteFromSaved(this, String.valueOf(hymn.getId()));
                hymn.setSaved(false);
                btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.save_acces_btn_white01));
                Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_not_black_48dp));
            } else {
                Utils.addInSaved(this, String.valueOf(hymn.getId()));
                hymn.setSaved(true);
                btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.save_acces_btn_clicked_white01));
                Utils.saved.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_black_48dp));
            }
        });

        content = Utils.readContent(id, false, getApplicationContext());
        SetWebView(webView, seekvalue, content);
        seekBar.setProgress(seekvalue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekvalue = i;
                if (seekvalue < minsbprogress) {
                    seekvalue = minsbprogress;
                }
                SetWebView(webView, seekvalue, content);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekvalue < minsbprogress) {
                    seekvalue = minsbprogress;
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekvalue < minsbprogress) {
                    seekvalue = minsbprogress;
                }

                PrefConfig.SaveSBprogress(getApplicationContext(), seekvalue);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void SetWebView(WebView webView, int seekvalue, String content) {
        webView.loadDataWithBaseURL(null, "<font size=" + '"' + String.valueOf(seekvalue / 10) + "px" + '"' + ">" + content + "</font><br><br>", "text/html", "utf-8", null);
    }
}
