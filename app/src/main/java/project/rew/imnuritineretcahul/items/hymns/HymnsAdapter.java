package project.rew.imnuritineretcahul.items.hymns;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.databinding.FragmentAllHymnsAudioBinding;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.items.audio.HymnsAudioRealTime;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.utils.Utils;


public class HymnsAdapter extends RecyclerView.Adapter<HymnsAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;

    public HymnsAdapter(List<Hymn> hymns) {
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
        final Hymn hymn = hymns.get(position);
        holder.textView.setText(hymn.getNr() + ". " + hymn.getTitle());
        if (hymn.isSaved())
            holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_enable01));
        else
            holder.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.to_save_btn_disable01));
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
            }

        });
        holder.constraintLayout.setOnClickListener(view -> {
            HymnsAudioRealTime.setCurentPosition(position, all_hymns, hymn);
            Utils.saved = holder.saved;
            Intent startHymn = new Intent(context, HymnCanvas.class);
            startHymn.putExtra("id", hymn.getId());
            startHymn.putExtra("nr", hymn.getNr());
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
                    if (hymn.toString().toLowerCase().replaceAll("ă", "a").replaceAll("â", "a").replaceAll("î", "i").replaceAll("ș", "s").replaceAll("ț", "t").contains(filterPattern) ||
                            hymn.toString().toLowerCase().contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("ă", "a").replaceAll("â", "a").replaceAll("î", "i").replaceAll("ș", "s").replaceAll("ț", "t").replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\?", "").replaceAll("\\!", "").contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\?", "").replaceAll("\\!", "").contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("x", "х").replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\?", "").replaceAll("\\!", "").contains(filterPattern) ||
                            hymn.toString().toLowerCase().replaceAll("x", "х").contains(filterPattern)) {
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

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            saved = itemView.findViewById(R.id.saved);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
