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

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.items.audio.AudioListHymnsAdapter;
import project.rew.imnuritineretcahul.tablayouts.audio.adapters.AudioTabsAdapter;
import project.rew.imnuritineretcahul.tablayouts.audio.fragments.AllHymnsFragmentAudio;
import project.rew.imnuritineretcahul.tablayouts.audio.fragments.SavedHymnsFragmentAudio;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.SavedHymnsFragment;
import project.rew.imnuritineretcahul.utils.Utils;

public class AudioFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private int lastPosition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager2 = root.findViewById(R.id.viewPager);

        FragmentManager manager = getChildFragmentManager();
        AudioTabsAdapter adapter = new AudioTabsAdapter(manager, getLifecycle());
        viewPager2.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.first_pg_btn_white01));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.category_pg_btn_white01));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.save_pg_btn_white01));
        tabLayout.addOnTabSelectedListener(this);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
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

        Utils.needsToNotify = false;
        lastPosition = tabLayout.getSelectedTabPosition();
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
        if (lastPosition == 2 && AllHymnsFragmentAudio.adapter != null && Utils.needsToNotify == true) {
            Utils.needsToNotify = false;
            AllHymnsFragmentAudio.adapter.notifyDataSetChanged();
        }
        if (lastPosition == 0 && SavedHymnsFragmentAudio.adapter != null && Utils.needsToNotify == true) {
            Utils.needsToNotify = false;
            Utils.loadHymnsSaved();
            if (Utils.language == Language.RO) {
                if (Utils.savedHymns_Ro.isEmpty()) {
                    SavedHymnsFragmentAudio.textView.setText(R.string.no_hymns_saved_ro);
                    SavedHymnsFragmentAudio.textView.setVisibility(View.VISIBLE);
                    SavedHymnsFragmentAudio.recyclerView.setVisibility(View.GONE);
                } else {
                    SavedHymnsFragmentAudio.textView.setVisibility(View.GONE);
                    SavedHymnsFragmentAudio.recyclerView.setVisibility(View.VISIBLE);
                    SavedHymnsFragmentAudio.adapter = new AudioListHymnsAdapter(Utils.savedHymns_Ro);
                    SavedHymnsFragmentAudio.recyclerView.setAdapter(SavedHymnsFragmentAudio.adapter);
                }
            } else if (Utils.language == Language.RU) {
                if (Utils.savedHymns_Ru.isEmpty()) {
                    SavedHymnsFragmentAudio.textView.setText(R.string.no_hymns_saved_ru);
                    SavedHymnsFragmentAudio.textView.setVisibility(View.VISIBLE);
                    SavedHymnsFragmentAudio.recyclerView.setVisibility(View.GONE);
                } else {
                    SavedHymnsFragmentAudio.textView.setVisibility(View.GONE);
                    SavedHymnsFragmentAudio.recyclerView.setVisibility(View.VISIBLE);
                    SavedHymnsFragmentAudio.adapter = new AudioListHymnsAdapter(Utils.savedHymns_Ru);
                    SavedHymnsFragmentAudio.recyclerView.setAdapter(SavedHymnsFragmentAudio.adapter);
                }
            }
        }
        lastPosition = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}