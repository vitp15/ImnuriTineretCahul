package project.rew.imnuritineretcahul.tablayouts.audio.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.rew.imnuritineretcahul.tablayouts.audio.fragments.AllHymnsFragmentAudio;
import project.rew.imnuritineretcahul.tablayouts.audio.fragments.CategoriesFragmentAudio;
import project.rew.imnuritineretcahul.tablayouts.audio.fragments.SavedHymnsFragmentAudio;

public class AudioTabsAdapter extends FragmentStateAdapter {
    public AudioTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new CategoriesFragmentAudio();
            case 2:
                return new SavedHymnsFragmentAudio();
        }

        return new AllHymnsFragmentAudio();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
