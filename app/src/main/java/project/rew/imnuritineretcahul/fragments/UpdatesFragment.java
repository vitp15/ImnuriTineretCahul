package project.rew.imnuritineretcahul.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.utils.NetworkUtils;
import project.rew.imnuritineretcahul.utils.UpdateAllFilesTask;
import project.rew.imnuritineretcahul.utils.UpdateFilesTask;
import project.rew.imnuritineretcahul.utils.Utils;
import project.rew.imnuritineretcahul.utils.VerifyForUpdate;

public class UpdatesFragment extends Fragment {

    TextView textAll, textHymns, textAudio, textPDF;
    static ImageView downloadAll, downloadHymns, downloadAudio, downloadPDF, iconDAll, iconDHymns, iconDAudio, iconDPDF;
    static MenuItem currentState;
    public static int needsToDownload;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_updates, container, false);
        setHasOptionsMenu(true);

        textAll = root.findViewById(R.id.text_all);
        textHymns = root.findViewById(R.id.text_hymn);
        textAudio = root.findViewById(R.id.text_audio);
        textPDF = root.findViewById(R.id.text_pdf);
        downloadAll = root.findViewById(R.id.download_all);
        downloadHymns = root.findViewById(R.id.download_hymns);
        downloadAudio = root.findViewById(R.id.download_audio);
        downloadPDF = root.findViewById(R.id.download_pdf);
        iconDAll = root.findViewById(R.id.iconDAll);
        iconDHymns = root.findViewById(R.id.iconDHymn);
        iconDAudio = root.findViewById(R.id.iconDAudio);
        iconDPDF = root.findViewById(R.id.iconDPDF);

        if (!NetworkUtils.hasActiveNetworkConnection(getContext())) {
            textAll.setTextColor(getContext().getColor(R.color.no_network));
            textHymns.setTextColor(getContext().getColor(R.color.no_network));
            textAudio.setTextColor(getContext().getColor(R.color.no_network));
            textPDF.setTextColor(getContext().getColor(R.color.no_network));
            iconDAll.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDHymns.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDAudio.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDPDF.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
        }

        if (Utils.appBarTitle != null) {
            if (Utils.language == Language.RO) {
                Utils.appBarTitle.setText(R.string.menu_updates_ro);
            } else if (Utils.language == Language.RU) {
                Utils.appBarTitle.setText(R.string.menu_updates_ru);
            }
        }
        if (Utils.language == Language.RO) {
            Utils.appBarTitleString = getString(R.string.menu_updates_ro);
            textAll.setText(getContext().getString(R.string.update_all_ro));
            textHymns.setText(getContext().getString(R.string.update_hymns_ro));
            textAudio.setText(getContext().getString(R.string.update_audios_ro));
            textPDF.setText(getContext().getString(R.string.update_pdfs_ro));
        } else if (Utils.language == Language.RU) {
            Utils.appBarTitleString = getString(R.string.menu_updates_ru);
            textAll.setText(getContext().getString(R.string.update_all_ru));
            textHymns.setText(getContext().getString(R.string.update_hymns_ru));
            textAudio.setText(getContext().getString(R.string.update_audios_ru));
            textPDF.setText(getContext().getString(R.string.update_pdfs_ru));
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragemnt_update, menu);
        currentState = menu.findItem(R.id.currentState);
        if (Utils.language == Language.RO) {
            currentState.setTitle(getContext().getString(R.string.curent_sttate_ro));
        } else if (Utils.language == Language.RU) {
            currentState.setTitle(getContext().getString(R.string.curent_sttate_ru));
        }
        if (!NetworkUtils.hasActiveNetworkConnection(getContext())) {
            currentState.setIcon(getContext().getDrawable(R.drawable.outline_wifi_off_white_24));
        } else {
            new VerifyForUpdate(getContext(), getActivity(), downloadAll, downloadHymns, downloadAudio, downloadPDF,
                    iconDAll, iconDHymns, iconDAudio, iconDPDF, currentState).execute();
        }
    }

    public static void setBtnsOnClickListener(Context context, FragmentActivity activity) {
        downloadAll.setOnClickListener(v -> {
            new UpdateAllFilesTask(context, activity).execute();
            downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
            iconDAll.setVisibility(View.GONE);
            currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
            if (iconDHymns.getVisibility() == View.VISIBLE) {
                downloadHymns.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                iconDHymns.setVisibility(View.GONE);
            }
            if (iconDAudio.getVisibility() == View.VISIBLE) {
                downloadAudio.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                iconDAudio.setVisibility(View.GONE);
            }
            if (iconDPDF.getVisibility() == View.VISIBLE) {
                downloadPDF.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                iconDPDF.setVisibility(View.GONE);
            }
        });
        downloadHymns.setOnClickListener(v -> {
            new UpdateFilesTask(context, activity, Type.HYMN).execute();
            iconDHymns.setVisibility(View.GONE);
            downloadHymns.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
            if (needsToDownload == 1) {
                downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (currentState != null)
                    currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
            }
        });

        downloadAudio.setOnClickListener(v -> {
            new UpdateFilesTask(context, activity, Type.AUDIO).execute();
            iconDAudio.setVisibility(View.GONE);
            downloadAudio.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
            if (needsToDownload == 1) {
                downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (currentState != null)
                    currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
            }
        });
        downloadPDF.setOnClickListener(v -> {
            new UpdateFilesTask(context, activity, Type.PDF).execute();
            iconDPDF.setVisibility(View.GONE);
            downloadPDF.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
            if (needsToDownload == 1) {
                downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (currentState != null)
                    currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
            }
        });
    }
}
