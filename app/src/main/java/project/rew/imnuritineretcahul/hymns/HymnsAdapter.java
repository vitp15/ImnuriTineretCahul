package project.rew.imnuritineretcahul.hymns;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.utils.Utils;


public class HymnsAdapter extends RecyclerView.Adapter<HymnsAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;

    // RecyclerView recyclerView;
    public HymnsAdapter(List<Hymn> hymns) {
        this.hymns = new ArrayList<>(hymns);
        all_hymns=hymns;
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
        holder.textView.setText(hymn.getNr()+" "+hymn.getTitle());
        holder.relativeLayout.setOnClickListener(view -> {

            Intent startHymn = new Intent(context, HymnCanvas.class);
            startHymn.putExtra("id", hymn.getId());
            startHymn.putExtra("nr",hymn.getNr());
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
                    if (hymn.getTitle().toLowerCase().replaceAll("ă","a").replaceAll("â","a").replaceAll("î","i").replaceAll("ș","s").replaceAll("ț","t").contains(filterPattern)||hymn.getTitle().toLowerCase().contains(filterPattern)||
                            hymn.getTitle().toLowerCase().replaceAll("ă","a").replaceAll("â","a").replaceAll("î","i").replaceAll("ș","s").replaceAll("ț","t").replaceAll("\\,","").replaceAll("\\.","").replaceAll("\\-","").replaceAll("\\?","").replaceAll("\\!","").contains(filterPattern)||
                            hymn.getTitle().toLowerCase().replaceAll("\\,","").replaceAll("\\.","").replaceAll("\\-","").replaceAll("\\?","").replaceAll("\\!","").contains(filterPattern)) {
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

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
