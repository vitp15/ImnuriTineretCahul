package project.rew.imnuritineretcahul.items.categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.utils.Utils;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    Type type;

    public CategoryAdapter(List<Category> categories, Type type) {
        this.categories = categories;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position + 1 == getItemCount())
            holder.marginBottom.setVisibility(View.VISIBLE);
        else holder.marginBottom.setVisibility(View.GONE);

        if (categories.size() > position * 4) {
            holder.image1.setImageDrawable(categories.get(position * 4).getImg());
            holder.t1.setText(categories.get(position * 4).getTitle());
            holder.materialCardView1.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).putExtra("type", type));
                Utils.curentOpenedCategory = categories.get(position * 4);
            });
        } else {
            holder.materialCardView1.setVisibility(View.INVISIBLE);
        }
        if (categories.size() > position * 4 + 1) {
            holder.image2.setImageDrawable(categories.get(position * 4 + 1).getImg());
            holder.t2.setText(categories.get(position * 4 + 1).getTitle());
            holder.materialCardView2.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).putExtra("type", type));
                Utils.curentOpenedCategory = categories.get(position * 4 + 1);
            });
        } else {
            holder.materialCardView2.setVisibility(View.INVISIBLE);
        }
        if (categories.size() > position * 4 + 2) {
            holder.image3.setImageDrawable(categories.get(position * 4 + 2).getImg());
            holder.t3.setText(categories.get(position * 4 + 2).getTitle());
            holder.materialCardView3.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).putExtra("type", type));
                Utils.curentOpenedCategory = categories.get(position * 4 + 2);
            });
        } else {
            holder.materialCardView3.setVisibility(View.INVISIBLE);
        }
        if (categories.size() > position * 4 + 3) {
            holder.image4.setImageDrawable(categories.get(position * 4 + 3).getImg());
            holder.t4.setText(categories.get(position * 4 + 3).getTitle());
            holder.materialCardView4.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).putExtra("type", type));
                Utils.curentOpenedCategory = categories.get(position * 4 + 3);
            });
        } else {
            holder.materialCardView4.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        if (categories.size() % 4 != 0)
            return categories.size() / 4 + 1;
        else return categories.size() / 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image1;
        public ImageView image2;
        public ImageView image3;
        public ImageView image4;
        public MaterialCardView materialCardView1;
        public MaterialCardView materialCardView2;
        public MaterialCardView materialCardView3;
        public MaterialCardView materialCardView4;
        public LinearLayout marginBottom;
        public TextView t1;
        public TextView t2;
        public TextView t3;
        public TextView t4;

        public ViewHolder(View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            materialCardView1 = itemView.findViewById(R.id.materialCardView1);
            materialCardView2 = itemView.findViewById(R.id.materialCardView2);
            materialCardView3 = itemView.findViewById(R.id.materialCardView3);
            materialCardView4 = itemView.findViewById(R.id.materialCardView4);
            marginBottom = itemView.findViewById(R.id.marginBottom);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
            t4 = itemView.findViewById(R.id.t4);
        }
    }
}
