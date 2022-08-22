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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    static ProgressBar loading_all, loading_hymns, loading_audio, loading_pdf;
    public static boolean needsToDownloadH, needsToDownloadA, needsToDownloadP;

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
        loading_all = root.findViewById(R.id.loading_all);
        loading_hymns = root.findViewById(R.id.loading_hymns);
        loading_audio = root.findViewById(R.id.loading_audio);
        loading_pdf = root.findViewById(R.id.loading_pdf);

        loading_all.setVisibility(View.VISIBLE);
        loading_hymns.setVisibility(View.VISIBLE);
        loading_audio.setVisibility(View.VISIBLE);
        loading_pdf.setVisibility(View.VISIBLE);

        if (!NetworkUtils.hasActiveNetworkConnection(getContext())) {
            textAll.setTextColor(getContext().getColor(R.color.no_network));
            textHymns.setTextColor(getContext().getColor(R.color.no_network));
            textAudio.setTextColor(getContext().getColor(R.color.no_network));
            textPDF.setTextColor(getContext().getColor(R.color.no_network));
            loading_all.setVisibility(View.GONE);
            loading_hymns.setVisibility(View.GONE);
            loading_audio.setVisibility(View.GONE);
            loading_pdf.setVisibility(View.GONE);
            iconDAll.setVisibility(View.VISIBLE);
            iconDHymns.setVisibility(View.VISIBLE);
            iconDAudio.setVisibility(View.VISIBLE);
            iconDPDF.setVisibility(View.VISIBLE);
            iconDAll.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDHymns.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDAudio.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));
            iconDPDF.setImageDrawable(getContext().getDrawable(R.drawable.outline_rotate_left_gray_24));

            if (Utils.language == Language.RO) {
                downloadAll.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show();
                });
                downloadHymns.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show();
                });

                downloadAudio.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show();
                });

                downloadPDF.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show();
                });
            } else if (Utils.language == Language.RU) {
                downloadAll.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show();
                });
                downloadHymns.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show();
                });

                downloadAudio.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show();
                });

                downloadPDF.setOnClickListener(v -> {
                    Toast.makeText(getContext(), getContext().getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show();
                });
            }
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
                    iconDAll, iconDHymns, iconDAudio, iconDPDF, loading_all, loading_hymns, loading_audio, loading_pdf, currentState).execute();
        }
    }

    public static void setBtnsOnClickListener(Context context, FragmentActivity activity) {
        if (UpdatesFragment.needsToDownloadH || UpdatesFragment.needsToDownloadA || UpdatesFragment.needsToDownloadP) {
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
        } else {
            if (Utils.language == Language.RO) {
                downloadAll.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_elements_are_updated_ro), Toast.LENGTH_SHORT).show();
                });
            } else if (Utils.language == Language.RU) {
                downloadAll.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_elements_are_updated_ru), Toast.LENGTH_SHORT).show();
                });
            }
        }

        if (UpdatesFragment.needsToDownloadH) {
            downloadHymns.setOnClickListener(v -> {
                new UpdateFilesTask(context, activity, Type.HYMN).execute();
                iconDHymns.setVisibility(View.GONE);
                downloadHymns.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (!UpdatesFragment.needsToDownloadA && !UpdatesFragment.needsToDownloadP) {
                    downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                    if (currentState != null)
                        currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
                }
            });
        } else {
            if (Utils.language == Language.RO) {
                downloadHymns.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_hymns_are_updated_ro), Toast.LENGTH_SHORT).show();
                });
            } else if (Utils.language == Language.RU) {
                downloadHymns.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_hymns_are_updated_ru), Toast.LENGTH_SHORT).show();
                });
            }
        }

        if (UpdatesFragment.needsToDownloadA) {
            downloadAudio.setOnClickListener(v -> {
                new UpdateFilesTask(context, activity, Type.AUDIO).execute();
                iconDAudio.setVisibility(View.GONE);
                downloadAudio.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (!UpdatesFragment.needsToDownloadH && !UpdatesFragment.needsToDownloadP) {
                    downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                    if (currentState != null)
                        currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
                }
            });
        } else {
            if (Utils.language == Language.RO) {
                downloadAudio.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_audio_are_updated_ro), Toast.LENGTH_SHORT).show();
                });
            } else if (Utils.language == Language.RU) {
                downloadAudio.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_audio_are_updated_ru), Toast.LENGTH_SHORT).show();
                });
            }
        }

        if (UpdatesFragment.needsToDownloadP) {
            downloadPDF.setOnClickListener(v -> {
                new UpdateFilesTask(context, activity, Type.PDF).execute();
                iconDPDF.setVisibility(View.GONE);
                downloadPDF.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                if (!UpdatesFragment.needsToDownloadA && !UpdatesFragment.needsToDownloadH) {
                    downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_progres1));
                    if (currentState != null)
                        currentState.setIcon(context.getDrawable(R.drawable.updates_all_progress));
                }
            });
        } else {
            if (Utils.language == Language.RO) {
                downloadPDF.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_pdf_are_updated_ro), Toast.LENGTH_SHORT).show();
                });
            } else if (Utils.language == Language.RU) {
                downloadPDF.setOnClickListener(v -> {
                    Toast.makeText(context, context.getString(R.string.all_pdf_are_updated_ru), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    public static void setBtnsOnErrorClickListener(Context context, FragmentActivity activity) {

        iconDAll.setVisibility(View.GONE);
        iconDHymns.setVisibility(View.GONE);
        iconDAudio.setVisibility(View.GONE);
        iconDPDF.setVisibility(View.GONE);

        loading_all.setVisibility(View.GONE);
        loading_hymns.setVisibility(View.GONE);
        loading_audio.setVisibility(View.GONE);
        loading_pdf.setVisibility(View.GONE);

        downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_error));
        downloadHymns.setImageDrawable(context.getDrawable(R.drawable.update_error));
        downloadAudio.setImageDrawable(context.getDrawable(R.drawable.update_error));
        downloadPDF.setImageDrawable(context.getDrawable(R.drawable.update_error));
        currentState.setIcon(context.getDrawable(R.drawable.updates_error_all));

        downloadAll.setOnClickListener(v -> {
            activity.recreate();
        });

        downloadHymns.setOnClickListener(v -> {
            activity.recreate();
        });

        downloadAudio.setOnClickListener(v -> {
            activity.recreate();
        });

        downloadPDF.setOnClickListener(v -> {
            activity.recreate();
        });

        currentState.setOnMenuItemClickListener(v -> {
            activity.recreate();
            return false;
        });
    }
}
