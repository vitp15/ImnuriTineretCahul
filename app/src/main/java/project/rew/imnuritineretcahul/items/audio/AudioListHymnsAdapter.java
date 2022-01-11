package project.rew.imnuritineretcahul.items.audio;

import android.annotation.SuppressLint;
import android.content.Context;;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.UpdateFilesTask;
import project.rew.imnuritineretcahul.utils.Utils;


public class AudioListHymnsAdapter extends RecyclerView.Adapter<AudioListHymnsAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;
    private FragmentActivity fragment;

    // RecyclerView recyclerView;
    public AudioListHymnsAdapter(List<Hymn> hymns, FragmentActivity frag) {
        this.hymns = new ArrayList<>(hymns);
        all_hymns = hymns;
        this.fragment = frag;
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
        holder.textView.setText(hymn.getNr() + "  " + hymn.getTitle());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.linearAll.getVisibility() == View.VISIBLE)
                    holder.linearAll.setVisibility(View.GONE);
                else holder.linearAll.setVisibility(View.VISIBLE);
            }
        });
        holder.linearAll.setVisibility(View.GONE);
        MediaPlayer mediaPlayer = hymn.getMediaPlayer();
        Handler handler = new Handler();
        Runnable runnable;

        if (mediaPlayer != null) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.linearLayout1.setVisibility(View.GONE);
            holder.title.setBackgroundColor(context.getColor(R.color.audio_exist_title));
            holder.linearAll.setBackgroundColor(context.getColor(R.color.audio_exist_background));
            holder.playerTrack.getProgressDrawable().setColorFilter(context.getColor(R.color.seekbar_list), PorterDuff.Mode.MULTIPLY);
            holder.playerTrack.getThumb().setColorFilter(context.getColor(R.color.thumb_list),PorterDuff.Mode.SRC_ATOP);
            runnable = new Runnable() {
                @Override
                public void run() {
                    holder.playerTrack.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            };
            if (mediaPlayer.isPlaying()) {
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btPlay.setVisibility(View.GONE);
                        holder.btPause.setVisibility(View.VISIBLE);
                        mediaPlayer.start();
                        handler.postDelayed(runnable, 0);
                    }
                }, 400);
            }
            holder.playerTrack.setProgress(mediaPlayer.getCurrentPosition());
            int duration = mediaPlayer.getDuration();
            String sDuration = convertFormat(duration);
            holder.playerDuration.setText(sDuration);

            holder.playerTrack.setMax(mediaPlayer.getDuration());
            holder.btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.btPlay.setVisibility(View.GONE);
                            holder.btPause.setVisibility(View.VISIBLE);
                            mediaPlayer.start();
                            handler.postDelayed(runnable, 0);
                        }
                    }, 400);
                }
            });
            holder.btPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btPause.setVisibility(View.GONE);
                    holder.btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
            });

            holder.playerTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                    holder.playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
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
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    holder.btPause.setVisibility(View.GONE);
                    holder.btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.seekTo(0);
                }
            });
            final int[] i = {0};
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i[0]++;
                    Handler handlerDelete = new Handler();
                    handlerDelete.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i[0] == 1) {
                                Toast.makeText(context, "Apăsați de două ori pentru a sterge", Toast.LENGTH_SHORT).show();
                            } else if (i[0] == 2) {
                                Utils.deleteFile(context, String.valueOf(hymn.getId()) + ".mp3", context.getString(R.string.ro_internal_mp3_folder));
                                holder.linearLayout.setVisibility(View.GONE);
                                holder.linearLayout1.setVisibility(View.VISIBLE);
                                holder.btnUpdSingle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new UpdateFilesTask(context, fragment, String.valueOf(hymn.getId()) + ".mp3",
                                                Type.AUDIO, Language.RO).execute();
                                    }
                                });
                            }
                            i[0] = 0;
                        }
                    }, 800);
                }
            });
        } else {
            holder.linearAll.setBackgroundColor(context.getColor(R.color.audio_miss));
            holder.title.setBackgroundColor(context.getColor(R.color.audio_miss));
            holder.linearLayout.setVisibility(View.GONE);
            holder.linearLayout1.setVisibility(View.VISIBLE);
            holder.btnUpdSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new UpdateFilesTask(context, fragment, String.valueOf(hymn.getId()) + ".mp3",
                            Type.AUDIO, Language.RO).execute();
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
        public TextView textView, delete;
        public RelativeLayout relativeLayout;
        public LinearLayout linearLayout, linearLayout1, linearAll, title;
        public ImageView btPlay, btPause, btRew, btFf;
        public TextView playerPosition, playerDuration;
        public SeekBar playerTrack;
        public Button btnUpdSingle;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            linearLayout1 = itemView.findViewById(R.id.linearLayout1);
            linearAll = itemView.findViewById(R.id.linearLayoutAll);
            btPlay = itemView.findViewById(R.id.player_btn);
            btPause = itemView.findViewById(R.id.player_btn_pause);
            btRew = itemView.findViewById(R.id.fast_rewind);
            btFf = itemView.findViewById(R.id.fast_forward);
            playerPosition = itemView.findViewById(R.id.player_position);
            playerDuration = itemView.findViewById(R.id.player_duration);
            playerTrack = itemView.findViewById(R.id.player_track);
            btnUpdSingle = itemView.findViewById(R.id.btnAudioUpdateone);
            delete = itemView.findViewById(R.id.delete);
            title = itemView.findViewById(R.id.textViewLay);
        }
    }
}
