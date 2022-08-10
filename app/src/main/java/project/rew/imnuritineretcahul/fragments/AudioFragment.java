package project.rew.imnuritineretcahul.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import project.rew.imnuritineretcahul.MainActivity;
import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.tablayouts.audio.adapters.AudioTabsAdapter;
import project.rew.imnuritineretcahul.tablayouts.hymns.adapters.HymnTabsAdapter;
import project.rew.imnuritineretcahul.utils.Utils;

public class AudioFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager2 = root.findViewById(R.id.viewPager);

        FragmentManager manager = getChildFragmentManager();
        AudioTabsAdapter adapter = new AudioTabsAdapter(manager, getLifecycle());
        viewPager2.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_home_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_language_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_system_update_alt_24));
        tabLayout.addOnTabSelectedListener(this);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (!Utils.isFirst)
                    viewPager2.getAdapter().notifyItemChanged(position);
                else
                    Utils.isFirst = false;
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        if (Utils.appBarTitle != null) {
            if (Utils.language == Language.RO) {
                Utils.appBarTitle.setText(R.string.menu_audio_ro);
            } else if (Utils.language == Language.RU) {
                Utils.appBarTitle.setText(R.string.menu_audio_ru);
            }
        }
        if (Utils.language == Language.RO) {
            Utils.appBarTitleString = getString(R.string.menu_audio_ro);
        } else if (Utils.language == Language.RU) {
            Utils.appBarTitleString = getString(R.string.menu_audio_ru);
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager2.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}