package project.rew.imnuritineretcahul.items.categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.utils.Utils;


public class CategoryAdapter1 extends RecyclerView.Adapter<CategoryAdapter1.ViewHolder> {
    private List<Category> categories;
    private Context context;
    Type type;

    public CategoryAdapter1(List<Category> categories, Type type) {
        this.categories = categories;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item1, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == categories.size() - 1) holder.lineSeparate.setVisibility(View.INVISIBLE);
        else holder.lineSeparate.setVisibility(View.VISIBLE);
        holder.icon.setImageDrawable(categories.get(position).getImg());
        holder.categoryName.setText(categories.get(position).getTitle());
        holder.hymns_in_category.setText(String.valueOf(categories.get(position).getHymns().size()));
        holder.constraintLayout.setOnClickListener(v -> {
            Utils.curentOpenedCategory = categories.get(position);
            context.startActivity(new Intent(context, CategoryActivity.class).putExtra("type", type)
            );
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public ConstraintLayout constraintLayout;
        public TextView categoryName, hymns_in_category;
        public LinearLayout lineSeparate;


        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            constraintLayout = itemView.findViewById(R.id.constraint);
            categoryName = itemView.findViewById(R.id.category_name);
            hymns_in_category = itemView.findViewById(R.id.hymns_in_category);
            lineSeparate = itemView.findViewById(R.id.line_separate);
        }
    }
}
