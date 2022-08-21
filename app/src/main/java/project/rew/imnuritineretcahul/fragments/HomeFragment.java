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
import project.rew.imnuritineretcahul.items.hymns.HymnsAdapter;
import project.rew.imnuritineretcahul.tablayouts.hymns.adapters.HymnTabsAdapter;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.AllHymnsFragment;
import project.rew.imnuritineretcahul.tablayouts.hymns.fragments.SavedHymnsFragment;
import project.rew.imnuritineretcahul.utils.Utils;

public class HomeFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private static ViewPager2 viewPager2;
    private int lastPosition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager2 = root.findViewById(R.id.viewPager);

        FragmentManager manager = getChildFragmentManager();
        HymnTabsAdapter adapter = new HymnTabsAdapter(manager, getLifecycle());
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
                Utils.appBarTitle.setText(R.string.app_name);
            } else if (Utils.language == Language.RU) {
                Utils.appBarTitle.setText(R.string.app_name_ru);
            }
        }
        if (Utils.language == Language.RO) {
            Utils.appBarTitleString = getString(R.string.app_name);
        } else if (Utils.language == Language.RU) {
            Utils.appBarTitleString = getString(R.string.app_name_ru);
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
        if (lastPosition == 2 && AllHymnsFragment.adapter != null && Utils.needsToNotify == true) {
            Utils.needsToNotify = false;
            AllHymnsFragment.adapter.notifyDataSetChanged();
        }
        if (lastPosition == 0 && SavedHymnsFragment.adapter != null && Utils.needsToNotify == true) {
            Utils.needsToNotify = false;
            Utils.loadHymnsSaved();
            if (Utils.language == Language.RO) {
                if (Utils.savedHymns_Ro.isEmpty()) {
                    SavedHymnsFragment.textView.setText(R.string.no_hymns_saved_ro);
                    SavedHymnsFragment.textView.setVisibility(View.VISIBLE);
                    SavedHymnsFragment.recyclerView.setVisibility(View.GONE);
                } else {
                    SavedHymnsFragment.textView.setVisibility(View.GONE);
                    SavedHymnsFragment.recyclerView.setVisibility(View.VISIBLE);
                    SavedHymnsFragment.adapter = new HymnsAdapter(Utils.savedHymns_Ro);
                    SavedHymnsFragment.recyclerView.setAdapter(SavedHymnsFragment.adapter);
                }
            } else if (Utils.language == Language.RU) {
                if (Utils.savedHymns_Ru.isEmpty()) {
                    SavedHymnsFragment.textView.setText(R.string.no_hymns_saved_ru);
                    SavedHymnsFragment.textView.setVisibility(View.VISIBLE);
                    SavedHymnsFragment.recyclerView.setVisibility(View.GONE);
                } else {
                    SavedHymnsFragment.textView.setVisibility(View.GONE);
                    SavedHymnsFragment.recyclerView.setVisibility(View.VISIBLE);
                    SavedHymnsFragment.adapter = new HymnsAdapter(Utils.savedHymns_Ru);
                    SavedHymnsFragment.recyclerView.setAdapter(SavedHymnsFragment.adapter);
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