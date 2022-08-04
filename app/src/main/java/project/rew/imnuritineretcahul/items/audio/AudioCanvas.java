package project.rew.imnuritineretcahul.items.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.Utils;

public class AudioCanvas extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    TextView hymn_nr, hymn_title, playerPosition, playerDuration;
    SeekBar playerTrack;
    ImageView btnPlay, btnPause, btnNext, btnPrevious, btnSave, btnHymnsList, imgBackground;
    Hymn hymn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_canvas);

        hymn_nr = findViewById(R.id.hymn_number);
        hymn_title = findViewById(R.id.hymn_title);
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        playerTrack = findViewById(R.id.player_track);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnNext = findViewById(R.id.nextSong);
        btnPrevious = findViewById(R.id.previousSong);
        btnSave = findViewById(R.id.saved);
        btnHymnsList = findViewById(R.id.hymns_list);
        imgBackground = findViewById(R.id.imgBackground);

        hymn = HymnsAudioRealTime.getHymnToPlay();

        hymn_nr.setText(String.valueOf(hymn.getNr()));
        hymn_title.setText(hymn.getTitle());

        if (hymn.getUriForImgInAudio() != null)
            imgBackground.setImageURI(Uri.parse(hymn.getUriForImgInAudio()));

        btnHymnsList.setOnClickListener(v -> {
            onBackPressed();
        });

        if (hymn.isSaved())
            btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_black_48dp));
        else
            btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_not_black_48dp));

        btnSave.setOnClickListener(v -> {
            if (hymn.isSaved()) {
                Utils.deleteFromSaved(this, String.valueOf(hymn.getId()));
                hymn.setSaved(false);
                btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_not_black_48dp));
            } else {
                Utils.addInSaved(this, String.valueOf(hymn.getId()));
                hymn.setSaved(true);
                btnSave.setImageDrawable(this.getResources().getDrawable(R.drawable.outline_turned_in_black_48dp));
            }
        });

        if (hymn.getUriForMediaPlayer() != null)
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(hymn.getUriForMediaPlayer()));
        else {
            HymnsAudioRealTime.goToNextSong();
            recreate();
        }
        if (mediaPlayer != null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playerTrack.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            };
            if (mediaPlayer.isPlaying()) {
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnPlay.setVisibility(View.GONE);
                        btnPause.setVisibility(View.VISIBLE);
                        mediaPlayer.start();
                        handler.postDelayed(runnable, 0);
                    }
                }, 400);
            }
            playerTrack.setProgress(mediaPlayer.getCurrentPosition());
            int duration = mediaPlayer.getDuration();
            String sDuration = convertFormat(duration);
            playerDuration.setText(sDuration);

            playerTrack.setMax(mediaPlayer.getDuration());

            btnPlay.setVisibility(View.INVISIBLE);
            btnPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            handler.postDelayed(runnable, 0);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnPlay.setVisibility(View.INVISIBLE);
                            btnPause.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                            handler.postDelayed(runnable, 0);
                        }
                    }, 400);
                }
            });
            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPause.setVisibility(View.INVISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
            });
            btnPrevious.setOnClickListener(v -> {
                mediaPlayer.stop();
                HymnsAudioRealTime.goToPreviousSong();
                recreate();
            });
            btnNext.setOnClickListener(v -> {
                mediaPlayer.stop();
                HymnsAudioRealTime.goToNextSong();
                recreate();
            });

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

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    HymnsAudioRealTime.goToNextSong();
                    recreate();
                }
            });

            /*
            holder.btFf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    if (curentPosition + 5000 < duration) {
                        curentPosition = curentPosition + 5000;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    } else {
                        curentPosition = duration;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    }
                }
            });
            holder.btRew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer.getCurrentPosition();
                    if (curentPosition > 5000) {
                        curentPosition = curentPosition - 5000;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    } else {
                        curentPosition = 0;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer.seekTo(curentPosition);
                    }
                }
            });
*/
            /*holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i[0]++;
                    Handler handlerDelete = new Handler();
                    handlerDelete.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i[0] == 1) {
                                if (Utils.language == Language.RO)
                                    Toast.makeText(context, R.string.delete_audio_ro, Toast.LENGTH_SHORT).show();
                                else if (Utils.language == Language.RU)
                                    Toast.makeText(context, R.string.delete_audio_ru, Toast.LENGTH_SHORT).show();
                            } else if (i[0] == 2) {
                                Utils.deleteFile(context, String.valueOf(hymn.getId()) + ".mp3", Type.AUDIO);
                                holder.linearAll.setBackgroundColor(context.getColor(R.color.audio_miss));
                                holder.title.setBackgroundColor(context.getColor(R.color.audio_miss));
                                holder.linearLayout.setVisibility(View.GONE);
                                holder.linearLayout1.setVisibility(View.VISIBLE);
                                holder.btnUpdSingle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new DownloadSingleFileTask(context, fragment, String.valueOf(hymn.getId()) + ".mp3",
                                                Type.AUDIO).execute();
                                    }
                                });
                            }
                            i[0] = 0;
                        }
                    }, 800);
                }
            });*/

        } else {
            HymnsAudioRealTime.goToNextSong();
            recreate();
        }
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}