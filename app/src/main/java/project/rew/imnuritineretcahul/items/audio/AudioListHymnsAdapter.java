package project.rew.imnuritineretcahul.items.audio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.Utils;

public class AudioListHymnsAdapter extends RecyclerView.Adapter<AudioListHymnsAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;

    public AudioListHymnsAdapter(List<Hymn> hymns) {
        this.hymns = new ArrayList<>(hymns);
        all_hymns = hymns;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lyst_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) holder.marginTop.setVisibility(View.VISIBLE);
        else holder.marginTop.setVisibility(View.GONE);

        final Hymn hymn = hymns.get(position);
        holder.textView.setText(hymn.getNr() + ". " + hymn.getTitle());

        if (hymn.getUriForMediaPlayer() != null) {
            if (hymn.isSaved())
                holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_enable01));
            else
                holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_disable01));

            holder.textView.setTextColor(context.getResources().getColor(R.color.text_color));

            holder.saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hymn.isSaved()) {
                        Utils.deleteFromSaved(context, String.valueOf(hymn.getId()));
                        hymn.setSaved(false);
                        holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_disable01));
                    } else {
                        Utils.addInSaved(context, String.valueOf(hymn.getId()));
                        hymn.setSaved(true);
                        holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_enable01));
                    }
                    Utils.needsToNotify = true;
                }
            });

            holder.constraintLayout.setBackground(context.getDrawable(R.drawable.hymn_list_press));

        } else {
            if (hymn.isSaved())
                holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_clicked));
            else
                holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_nonclicked));

            holder.textView.setTextColor(context.getResources().getColor(R.color.nonpressed_nonexis_contur));

            holder.constraintLayout.setBackground(context.getDrawable(R.drawable.hymn_nonexistent_list_press));

            holder.saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hymn.isSaved()) {
                        Utils.deleteFromSaved(context, String.valueOf(hymn.getId()));
                        hymn.setSaved(false);
                        holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_nonclicked));
                    } else {
                        Utils.addInSaved(context, String.valueOf(hymn.getId()));
                        hymn.setSaved(true);
                        holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_clicked));
                    }
                    Utils.needsToNotify = true;
                }
            });
        }
        holder.constraintLayout.setOnClickListener(view -> {
            HymnsAudioRealTime.setCurentPosition(position, all_hymns, hymn);
            Utils.saved = holder.saved;
            Utils.hymn_title_to_edit = holder.textView;
            Utils.constraintLayout = holder.constraintLayout;
            Intent startHymn = new Intent(context, AudioCanvas.class);
            context.startActivity(startHymn);
        });
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
        public ConstraintLayout constraintLayout;
        public ImageView saved;
        LinearLayout marginTop;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            saved = itemView.findViewById(R.id.saved);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            marginTop = itemView.findViewById(R.id.marginTop);
        }
    }
}
