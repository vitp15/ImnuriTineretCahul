package project.rew.imnuritineretcahul.tablayouts.hymns.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.AllHymnsFragment;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.CategoriesFragment;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.SavedHymnsFragment;

public class HymnTabsAdapter extends FragmentStateAdapter {
    public HymnTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new CategoriesFragment();
            case 2:
                return new SavedHymnsFragment();
        }

        return new AllHymnsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
