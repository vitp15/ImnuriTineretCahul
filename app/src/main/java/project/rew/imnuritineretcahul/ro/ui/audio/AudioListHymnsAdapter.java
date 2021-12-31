package project.rew.imnuritineretcahul.ro.ui.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.ro.hymns.Hymn;


public class AudioListHymnsAdapter extends RecyclerView.Adapter<AudioListHymnsAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;
    public final MediaPlayer[] mediaPlayer;
    Handler[] handler;
    Runnable[] runnable;

    // RecyclerView recyclerView;
    public AudioListHymnsAdapter(List<Hymn> hymns) {
        this.hymns = new ArrayList<>(hymns);
        all_hymns = hymns;
        this.mediaPlayer = new MediaPlayer[hymns.size()];
        this.handler = new Handler[hymns.size()];
        this.runnable = new Runnable[hymns.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lyst_hymn_audio, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Hymn hymn = hymns.get(position);
        File internalDir = context.getApplicationContext().getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();

        holder.textView.setText(hymn.getNr() + " " + hymn.getTitle());
        holder.textView.setOnClickListener(view -> {
            if (holder.linearLayout.getVisibility() == View.VISIBLE)
                holder.linearLayout.setVisibility(View.GONE);
            else holder.linearLayout.setVisibility(View.VISIBLE);
        });

        for (File dirFile : dirFiles) {
            String[] audio = dirFile.getName().split("\\.");
            if (audio[0].equals(String.valueOf(hymn.getId())))
                mediaPlayer[position] = MediaPlayer.create(context.getApplicationContext(), Uri.parse(dirFile.toURI().toString()));

        }

        if (mediaPlayer[position] != null) {
            handler[position]=new Handler();
            holder.btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.btPlay.setVisibility(View.GONE);
                            holder.btPause.setVisibility(View.VISIBLE);
                            mediaPlayer[position].start();
                            holder.playerTrack.setMax(mediaPlayer[position].getDuration());
                            handler[position].postDelayed(runnable[position], 0);
                        }
                    }, 400);
                }
            });
            holder.btPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btPause.setVisibility(View.GONE);
                    holder.btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer[position].pause();
                    handler[position].removeCallbacks(runnable[position]);
                }
            });
            runnable[position] = new Runnable() {
                @Override
                public void run() {
                    holder.playerTrack.setProgress(mediaPlayer[position].getCurrentPosition());
                    handler[position].postDelayed(this, 500);
                }
            };
            int duration = mediaPlayer[position].getDuration();
            String sDuration = convertFormat(duration);
            holder.playerDuration.setText(sDuration);
            holder.playerTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer[position].seekTo(progress);
                    }
                    holder.playerPosition.setText(convertFormat(mediaPlayer[position].getCurrentPosition()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.btFf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer[position].getCurrentPosition();
                    int duration = mediaPlayer[position].getDuration();
                    if (curentPosition + 5000 < duration) {
                        curentPosition = curentPosition + 5000;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer[position].seekTo(curentPosition);
                    } else {
                        curentPosition = duration;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer[position].seekTo(curentPosition);
                    }
                }
            });
            holder.btRew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curentPosition = mediaPlayer[position].getCurrentPosition();
                    if (curentPosition > 5000) {
                        curentPosition = curentPosition - 5000;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer[position].seekTo(curentPosition);
                    } else {
                        curentPosition = 0;
                        holder.playerTrack.setProgress(curentPosition);
                        holder.playerPosition.setText(convertFormat(curentPosition));
                        mediaPlayer[position].seekTo(curentPosition);
                    }
                }
            });
            mediaPlayer[position].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    holder.btPause.setVisibility(View.GONE);
                    holder.btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer[position].seekTo(0);
                }
            });
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
    public int getItemCount() {
        return hymns.size();
    }

    @Override
    public Filter getFilter() {
        return hymnsFilter;
    }

    private Filter hymnsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Hymn> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(all_hymns);
            } else {
                String filterPattern = charSequence.toString().toLowerCase();
                for (Hymn hymn : all_hymns) {
                    if (hymn.toString().toLowerCase().replaceAll("ă", "a").replaceAll("â", "a").replaceAll("î", "i").replaceAll("ș", "s").replaceAll("ț", "t").contains(filterPattern) || hymn.toString().toLowerCase().contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("ă", "a").replaceAll("â", "a").replaceAll("î", "i").replaceAll("ș", "s").replaceAll("ț", "t").replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\?", "").replaceAll("\\!", "").contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\?", "").replaceAll("\\!", "").contains(filterPattern)) {
                        filteredList.add(hymn);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            hymns.clear();
            hymns.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout relativeLayout;
        public LinearLayout linearLayout;
        public ImageView btPlay, btPause, btRew, btFf;
        public TextView playerPosition, playerDuration;
        public SeekBar playerTrack;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            btPlay = itemView.findViewById(R.id.player_btn);
            btPause = itemView.findViewById(R.id.player_btn_pause);
            btRew = itemView.findViewById(R.id.fast_rewind);
            btFf = itemView.findViewById(R.id.fast_forward);
            playerPosition = itemView.findViewById(R.id.player_position);
            playerDuration = itemView.findViewById(R.id.player_duration);
            playerTrack = itemView.findViewById(R.id.player_track);
        }
    }
}
