package project.rew.imnuritineretcahul.items.note_pdf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.UpdateFilesTask;


public class HymnsPdfAdapter extends RecyclerView.Adapter<HymnsPdfAdapter.ViewHolder> implements Filterable {
    private List<Hymn> hymns;
    private List<Hymn> all_hymns;
    private Context context;
    private FragmentActivity fragment;

    // RecyclerView recyclerView;
    public HymnsPdfAdapter(List<Hymn> hymns, FragmentActivity ft) {
        this.hymns = new ArrayList<>(hymns);
        all_hymns = hymns;
        fragment = ft;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lyst_pdf_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Hymn hymn = hymns.get(position);
        holder.textView.setText(hymn.getNr() + " " + hymn.getTitle());
        holder.linearLayout.setVisibility(View.GONE);
        if (hymn.getPdfView() != null) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.textView.setOnClickListener(view -> {

                Intent startHymn = new Intent(context, PDFCanvas.class);
                startHymn.putExtra("id", hymn.getId());
                startHymn.putExtra("nr", hymn.getNr());
                context.startActivity(startHymn);
            });
        } else {
            holder.textView.setOnClickListener(view -> {
                if (holder.linearLayout.getVisibility() == View.GONE) {
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.pdfUpdate.setOnClickListener(v -> {
                        new UpdateFilesTask(context, fragment,
                                String.valueOf(hymn.getId()) + ".pdf", Type.PDF, Language.RO).execute();
                    });
                } else {
                    holder.linearLayout.setVisibility(View.GONE);
                }
            });
        }
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
        Button pdfUpdate;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            pdfUpdate = itemView.findViewById(R.id.btnPDFUpdate);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
