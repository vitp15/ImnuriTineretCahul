package project.rew.imnuritineretcahul.ro.hymns;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.ro.ui.home.Utils;

public class HymnCanvas extends AppCompatActivity {

    int seekvalue;
    int minsbprogress = 30;
    SwitchCompat switchCompat;
    String content;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_canvas);
        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);
        seekvalue = PrefConfig.load_saved_progress(this);

        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.right_corner_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window=dialog.getWindow();
        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity= Gravity.TOP|Gravity.RIGHT;
        wlp.y=0;
        wlp.x=0;
        window.setAttributes(wlp);
        TextView btnclose=(TextView)dialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WebView webView = findViewById(R.id.wvCanvas);
        webView.getSettings().setJavaScriptEnabled(true);

        switchCompat=dialog.findViewById(R.id.switch1);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchCompat.isChecked()){
                    content=Utils.readContent(id, true, getApplicationContext());
                    SetWebView(webView,seekvalue,content);
                }
                else{
                    content=Utils.readContent(id, false, getApplicationContext());
                    SetWebView(webView,seekvalue,content);
                }
            }
        });

        Hymn hymn = Utils.hymns.get(nr - 1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(hymn.getTitle());
        getSupportActionBar().setHideOnContentScrollEnabled(true);
        content = Utils.readContent(id, false, getApplicationContext());
        SetWebView(webView,seekvalue,content);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(seekvalue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekvalue = i;
                if (seekvalue < minsbprogress) {
                    seekvalue = minsbprogress;
                }
                SetWebView(webView,seekvalue,content);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.rightcorneroptions){
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void SetWebView(WebView webView,int seekvalue,String content){
        webView.loadDataWithBaseURL(null, "<font size=" +'"'+ String.valueOf(seekvalue/10)+"px" +'"'+">"+ content + "</font><br><br>", "text/html", "utf-8", null);
    }

}
