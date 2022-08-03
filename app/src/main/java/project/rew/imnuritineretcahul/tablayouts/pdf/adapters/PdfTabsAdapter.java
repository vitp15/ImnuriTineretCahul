package project.rew.imnuritineretcahul.tablayouts.pdf.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.AllHymnsFragment;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.CategoriesFragment;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.SavedHymnsFragment;
import project.rew.imnuritineretcahul.tablayouts.pdf.fragments.AllHymnsFragmentPDF;
import project.rew.imnuritineretcahul.tablayouts.pdf.fragments.CategoriesFragmentPDF;
import project.rew.imnuritineretcahul.tablayouts.pdf.fragments.SavedHymnsFragmentPDF;

public class PdfTabsAdapter extends FragmentStateAdapter {
    public PdfTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new CategoriesFragmentPDF();
            case 2:
                return new SavedHymnsFragmentPDF();
        }

        return new AllHymnsFragmentPDF();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
