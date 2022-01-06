package project.rew.imnuritineretcahul.ro.hymns;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.concurrent.TimeUnit;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.ro.ui.audio.SetMediaPlayer;
import project.rew.imnuritineretcahul.ro.ui.audio.UpdateAudioTask;
import project.rew.imnuritineretcahul.ro.ui.note_pdf.PDFCanvas;
import project.rew.imnuritineretcahul.ro.ui.note_pdf.SetPDF;
import project.rew.imnuritineretcahul.ro.ui.note_pdf.UpdatePdfTask;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.ro.ui.home.Utils;

import static project.rew.imnuritineretcahul.ro.ui.home.Utils.hymns;

public class HymnCanvas extends AppCompatActivity {

    int seekvalue;
    int minsbprogress = 30;
    Handler handler = new Handler();
    Runnable runnable;
    SwitchCompat switchCompat;
    String content;
    Dialog dialog;
    ImageView btPlay, btPause, btFf, btRew;
    SeekBar playerTrack;
    TextView playerDuration, playerPosition, delete;
    LinearLayout audioElements, audioMiss, notePDF, pdfMiss;
    Button downald,downaldPdf;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_canvas);
        int id = getIntent().getIntExtra("id", 0);
        int nr = getIntent().getIntExtra("nr", 0);
        Hymn hymn = hymns.get(nr - 1);
        seekvalue = PrefConfig.load_saved_progress(this);
        SetMediaPlayer.setMediaPlayer(this);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.right_corner_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.y = 0;
        wlp.x = 0;
        window.setAttributes(wlp);
        WebView webView = findViewById(R.id.wvCanvas);
        webView.getSettings().setJavaScriptEnabled(true);

        btPlay = dialog.findViewById(R.id.player_btn);
        btPause = dialog.findViewById(R.id.player_btn_pause);
        btRew = dialog.findViewById(R.id.fast_rewind);
        btFf = dialog.findViewById(R.id.fast_forward);
        playerTrack = dialog.findViewById(R.id.player_track);
        playerDuration = dialog.findViewById(R.id.player_duration);
        playerPosition = dialog.findViewById(R.id.player_position);
        delete = dialog.findViewById(R.id.txt_right);
        audioElements = dialog.findViewById(R.id.audioElements);
        audioMiss = dialog.findViewById(R.id.audioMiss);
        downald = dialog.findViewById(R.id.btnAudioUpdateone);
        mediaPlayer = hymn.getMediaPlayer();
        notePDF = dialog.findViewById(R.id.linear_notePdf);
        pdfMiss = dialog.findViewById(R.id.pdfMiss);
        downaldPdf=dialog.findViewById(R.id.btnPDFUpdate);
        SetPDF.setPDF(this);

        if (hymn.getPdfView() != null) {
            pdfMiss.setVisibility(View.GONE);
            notePDF.setVisibility(View.VISIBLE);
            notePDF.setOnClickListener(view -> {
                Intent startPDF = new Intent(this, PDFCanvas.class);
                startPDF.putExtra("id", hymn.getId());
                startPDF.putExtra("nr",hymn.getNr());
                this.startActivity(startPDF);
            });
        } else {
            notePDF.setVisibility(View.GONE);
            pdfMiss.setVisibility(View.VISIBLE);
            downaldPdf.setOnClickListener(view -> {
                new UpdatePdfTask(this,this,String.valueOf(hymn.getId())+".pdf").execute();
            });
        }

        if (mediaPlayer != null) {
            audioMiss.setVisibility(View.GONE);
            audioElements.setVisibility(View.VISIBLE);
            btRew.setVisibility(View.VISIBLE);
            btFf.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            final int[] i = {0};

            btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i[0]++;
                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i[0] == 1) {
                                Toast.makeText(HymnCanvas.this, "Apăsați de două ori pentru a reda", Toast.LENGTH_SHORT).show();
                            } else if (i[0] == 2) {
                                btPlay.setVisibility(View.GONE);
                                btPause.setVisibility(View.VISIBLE);
                                mediaPlayer.start();
                                playerTrack.setMax(mediaPlayer.getDuration());
                                handler.postDelayed(runnable, 0);
                            }
                            i[0] = 0;
                        }
                    }, 400);
                }
            });
            btPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btPause.setVisibility(View.GONE);
                    btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
            });

            runnable = new Runnable() {
                @Override
                public void run() {
                    playerTrack.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            };

            int duration = mediaPlayer.getDuration();
            String sDuration = convertFormat(duration);
            playerDuration.setText(sDuration);
            playerTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                    playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            btFf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    if (curentPosition + 5000 < duration) {
                        curentPosition = curentPosition + 5000;
                        playerTrack.setProgress(curentPosition);
                        playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    } else {
                        curentPosition = duration;
                        playerTrack.setProgress(curentPosition);
                        playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    }
                }
            });
            btRew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer.getCurrentPosition();
                    if (curentPosition > 5000) {
                        curentPosition = curentPosition - 5000;
                        playerTrack.setProgress(curentPosition);
                        playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    } else {
                        curentPosition = 0;
                        playerTrack.setProgress(curentPosition);
                        playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btPause.setVisibility(View.GONE);
                    btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.seekTo(0);
                }
            });

            final int[] j = {0};
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    j[0]++;
                    Handler handlerDelete = new Handler();
                    handlerDelete.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (j[0] == 1) {
                                Toast.makeText(HymnCanvas.this, "Apăsați de două ori pentru a sterge", Toast.LENGTH_SHORT).show();
                            } else if (j[0] == 2) {
                                project.rew.imnuritineretcahul.ro.ui.audio.Utils.deleteAudio(HymnCanvas.this, String.valueOf(hymn.getId()) + ".mp3");
                                audioElements.setVisibility(View.GONE);
                                btFf.setVisibility(View.GONE);
                                btRew.setVisibility(View.GONE);
                                delete.setVisibility(View.GONE);
                                audioMiss.setVisibility(View.VISIBLE);
                                downald.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new UpdateAudioTask(HymnCanvas.this, HymnCanvas.this, String.valueOf(hymn.getId()) + ".mp3").execute();
                                    }
                                });
                            }
                            j[0] = 0;
                        }
                    }, 800);
                }
            });
        } else {
            audioElements.setVisibility(View.GONE);
            btFf.setVisibility(View.GONE);
            btRew.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            audioMiss.setVisibility(View.VISIBLE);
            downald.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new UpdateAudioTask(HymnCanvas.this, HymnCanvas.this, String.valueOf(hymn.getId()) + ".mp3").execute();
                }
            });
        }

        switchCompat = dialog.findViewById(R.id.switch1);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked()) {
                    content = Utils.readContent(id, true, getApplicationContext());
                    SetWebView(webView, seekvalue, content);
                } else {
                    content = Utils.readContent(id, false, getApplicationContext());
                    SetWebView(webView, seekvalue, content);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(hymn.getTitle());
        getSupportActionBar().setHideOnContentScrollEnabled(true);
        content = Utils.readContent(id, false, getApplicationContext());
        SetWebView(webView, seekvalue, content);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hymn_canvas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.rightcorneroptions) {
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        finish();
        super.onBackPressed();
    }

    private void SetWebView(WebView webView, int seekvalue, String content) {
        webView.loadDataWithBaseURL(null, "<font size=" + '"' + String.valueOf(seekvalue / 10) + "px" + '"' + ">" + content + "</font><br><br>", "text/html", "utf-8", null);
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

}
