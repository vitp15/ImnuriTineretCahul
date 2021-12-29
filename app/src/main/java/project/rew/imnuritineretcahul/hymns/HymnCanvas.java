package project.rew.imnuritineretcahul.hymns;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.utils.Utils;

public class HymnCanvas extends AppCompatActivity {

    int seekvalue;
    int minsbprogress = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_canvas);
        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);
        seekvalue = PrefConfig.load_saved_progress(this);
        Hymn hymn = Utils.hymns.get(nr - 1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(hymn.getTitle());
        getSupportActionBar().setHideOnContentScrollEnabled(true);
        WebView webView = findViewById(R.id.wvCanvas);
        webView.getSettings().setJavaScriptEnabled(true);
        String content = Utils.readContent(id, true, getApplicationContext());
        webView.loadDataWithBaseURL(null, "<font size=" +'"'+ String.valueOf(seekvalue/10)+"px" +'"'+">"+ content + "</font><br><br>", "text/html", "utf-8", null);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(seekvalue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekvalue = i;
                if (seekvalue < minsbprogress) {
                    seekvalue = minsbprogress;
                }
                webView.loadDataWithBaseURL(null, "<font size=" +'"'+ String.valueOf(seekvalue/10)+"px" +'"'+">"+ content + "</font><br><br>", "text/html", "utf-8", null);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hymn_canvas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
