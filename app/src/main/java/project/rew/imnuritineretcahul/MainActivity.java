package project.rew.imnuritineretcahul;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.databinding.ActivityMainBinding;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.fragments.AudioFragment;
import project.rew.imnuritineretcahul.utils.PrefConfig;
import project.rew.imnuritineretcahul.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String language = PrefConfig.load_saved_language(this);
        if (language.equals("RO"))
            Utils.language = Language.RO;
        else if (language.equals("RU"))
            Utils.language = Language.RU;
        Utils.dosentDownloadCorectly = PrefConfig.load_not_download_corectly(this);
        if (Utils.dosentDownloadCorectly != null && !Utils.dosentDownloadCorectly.isEmpty()) {
            for (String s : Utils.dosentDownloadCorectly) {
                File file = new File(s);
                Utils.DeleteRecursive(file);
                Utils.dosentDownloadCorectly.remove(s);
                PrefConfig.saveNotDownloadCorectly(this, Utils.dosentDownloadCorectly);
            }
        }
        Utils.savedHymnsRo = PrefConfig.load_saved_list_of_hymns_ro(this);
        if (Utils.savedHymnsRo == null) Utils.savedHymnsRo = new ArrayList<>();
        Utils.savedHymnsRu = PrefConfig.load_saved_list_of_hymns_ru(this);
        if (Utils.savedHymnsRu == null) Utils.savedHymnsRu = new ArrayList<>();
        Utils.loadHymns(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_hymns_ro,
                R.id.nav_audio_ro, R.id.nav_pdfs_ro,
                R.id.nav_updates)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (Utils.language == Language.RO) {
            navigationView.getMenu().getItem(0).setTitle(this.getResources().getString(R.string.hymns_ro));
            navigationView.getMenu().getItem(1).setTitle(this.getResources().getString(R.string.menu_audio_ro));
            navigationView.getMenu().getItem(2).setTitle(this.getResources().getString(R.string.menu_notspdf_ro));
            navigationView.getMenu().getItem(3).setTitle(this.getResources().getString(R.string.menu_updates_ro));
        } else if (Utils.language == Language.RU) {
            navigationView.getMenu().getItem(0).setTitle(this.getResources().getString(R.string.hymns_ru));
            navigationView.getMenu().getItem(1).setTitle(this.getResources().getString(R.string.menu_audio_ru));
            navigationView.getMenu().getItem(2).setTitle(this.getResources().getString(R.string.menu_notspdf_ru));
            navigationView.getMenu().getItem(3).setTitle(this.getResources().getString(R.string.menu_updates_ru));
        }
        if (Utils.language == Language.RO)
            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setActionView(R.layout.menu_language_ro);
        else if (Utils.language == Language.RU)
            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setActionView(R.layout.menu_language_ru);
        navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (Utils.language == Language.RO) {
                    PrefConfig.SaveLanguage(MainActivity.this, "RU");
                } else if (Utils.language == Language.RU) {
                    PrefConfig.SaveLanguage(MainActivity.this, "RO");
                }
                recreate();
                return true;
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        TextView customTitle = customView.findViewById(R.id.actionbarTitle);
        Utils.appBarTitle = customTitle;
        if (Utils.appBarTitleString != null)
            customTitle.setText(Utils.appBarTitleString);
        getSupportActionBar().setCustomView(customView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER));
        Utils.appBarTitle.setOnClickListener(v -> {
            if (Utils.language == Language.RO) {
                PrefConfig.SaveLanguage(MainActivity.this, "RU");
            } else if (Utils.language == Language.RU) {
                PrefConfig.SaveLanguage(MainActivity.this, "RO");
            }
            recreate();
        });
        if (Utils.audioList) {
            Utils.audioList = false;
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_hymns_ro, true)
                    .build();
            navController.navigate(R.id.nav_audio_ro, null, navOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return false;
            }
        };
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

    }
}