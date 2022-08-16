package project.rew.imnuritineretcahul.items.categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.tablayouts.hymns.activites.CategoryActivity;
import project.rew.imnuritineretcahul.utils.Utils;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (Utils.language == Language.RO) {
            if (categories.size() > position * 4) {
                holder.image1.setImageDrawable(categories.get(position * 4).getImgRO());
            } else {
                holder.materialCardView1.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 1) {
                holder.image2.setImageDrawable(categories.get(position * 4 + 1).getImgRO());
            } else {
                holder.materialCardView2.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 2) {
                holder.image3.setImageDrawable(categories.get(position * 4 + 2).getImgRO());
            } else {
                holder.materialCardView3.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 3) {
                holder.image4.setImageDrawable(categories.get(position * 4 + 3).getImgRO());
            } else {
                holder.materialCardView4.setVisibility(View.INVISIBLE);
            }
        } else if (Utils.language == Language.RU) {
            if (categories.size() > position * 4) {
                holder.image1.setImageDrawable(categories.get(position * 4).getImgRU());
            } else {
                holder.materialCardView1.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 1) {
                holder.image2.setImageDrawable(categories.get(position * 4 + 1).getImgRU());
            } else {
                holder.materialCardView2.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 2) {
                holder.image3.setImageDrawable(categories.get(position * 4 + 2).getImgRU());
            } else {
                holder.materialCardView3.setVisibility(View.INVISIBLE);
            }
            if (categories.size() > position * 4 + 3) {
                holder.image4.setImageDrawable(categories.get(position * 4 + 3).getImgRU());
            } else {
                holder.materialCardView4.setVisibility(View.INVISIBLE);
            }
        }
        if (categories.size() > position * 4) {
            holder.materialCardView1.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).
                        putExtra("id", categories.get(position * 4).getId())
                        .putExtra("title", categories.get(position * 4).getTitle()));
            });
        }
        if (categories.size() > position * 4 + 1) {
            holder.materialCardView2.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).
                        putExtra("id", categories.get(position * 4 + 1).getId())
                        .putExtra("title", categories.get(position * 4 + 1).getTitle()));
            });
        }
        if (categories.size() > position * 4 + 2) {
            holder.materialCardView3.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).
                        putExtra("id", categories.get(position * 4 + 2).getId())
                        .putExtra("title", categories.get(position * 4 + 2).getTitle()));
            });
        }
        if (categories.size() > position * 4 + 3) {
            holder.materialCardView4.setOnClickListener(v -> {
                context.startActivity(new Intent(context, CategoryActivity.class).
                        putExtra("id", categories.get(position * 4 + 3).getId())
                        .putExtra("title", categories.get(position * 4 + 3).getTitle()));
            });
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
        }
    }
}
