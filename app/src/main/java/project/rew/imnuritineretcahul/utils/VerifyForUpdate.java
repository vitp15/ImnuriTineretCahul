package project.rew.imnuritineretcahul.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.fragments.UpdatesFragment;

public class VerifyForUpdate extends AsyncTask {

    Context context;
    FragmentActivity activity;
    ImageView downloadAll, downloadHymns, downloadAudio, downloadPDF, iconDAll, iconDHymns, iconDAudio, iconDPDF;
    ProgressBar loading_all, loading_hymns, loading_audio, loading_pdf;
    MenuItem currentState;
    private String server;
    private int port;
    private String user;
    private String pass;
    private String ftpPatchHRO, ftpPatchHRU, ftpPatchARO, ftpPatchARU, ftpPatchPRO, ftpPatchPRU;
    private String internalPatchHRO, internalPatchHRU, internalPatchARO, internalPatchARU, internalPatchPRO, internalPatchPRU;
    boolean errorOcured;


    public VerifyForUpdate(Context context, FragmentActivity activity, ImageView downloadAll, ImageView downloadHymns, ImageView downloadAudio, ImageView downloadPDF,
                           ImageView iconDAll, ImageView iconDHymns, ImageView iconDAudio, ImageView iconDPDF,
                           ProgressBar loading_all, ProgressBar loading_hymns, ProgressBar loading_audio, ProgressBar loading_pdf, MenuItem currentState) {
        this.context = context;
        this.activity = activity;
        this.downloadAll = downloadAll;
        this.downloadHymns = downloadHymns;
        this.downloadAudio = downloadAudio;
        this.downloadPDF = downloadPDF;
        this.iconDAll = iconDAll;
        this.iconDHymns = iconDHymns;
        this.iconDAudio = iconDAudio;
        this.iconDPDF = iconDPDF;
        this.currentState = currentState;
        this.loading_all = loading_all;
        this.loading_hymns = loading_hymns;
        this.loading_audio = loading_audio;
        this.loading_pdf = loading_pdf;
        port = Integer.parseInt(context.getString(R.string.port));
        server = context.getString(R.string.server);
        user = context.getString(R.string.user);
        pass = context.getString(R.string.password);
        UpdatesFragment.needsToDownloadH = false;
        UpdatesFragment.needsToDownloadA = false;
        UpdatesFragment.needsToDownloadP = false;
        errorOcured = false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        FTPClient ftpClient = new FTPClient();

        internalPatchHRO = context.getString(R.string.ro_internal_hymns_folder);
        ftpPatchHRO = context.getString(R.string.ro_external_hymns_folder);
        internalPatchHRU = context.getString(R.string.ru_internal_hymns_folder);
        ftpPatchHRU = context.getString(R.string.ru_external_hymns_folder);

        internalPatchARO = context.getString(R.string.ro_internal_mp3_folder);
        ftpPatchARO = context.getString(R.string.ro_external_mp3_folder);
        internalPatchARU = context.getString(R.string.ru_internal_mp3_folder);
        ftpPatchARU = context.getString(R.string.ru_external_mp3_folder);

        internalPatchPRO = context.getString(R.string.ro_internal_pdf_folder);
        ftpPatchPRO = context.getString(R.string.ro_external_pdf_folder);
        internalPatchPRU = context.getString(R.string.ru_internal_pdf_folder);
        ftpPatchPRU = context.getString(R.string.ru_external_pdf_folder);
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] subFilesHRO = Utils.getDirectoryFiles(ftpClient, ftpPatchHRO);
            FTPFile[] subFilesHRU = Utils.getDirectoryFiles(ftpClient, ftpPatchHRU);
            FTPFile[] subFilesARO = Utils.getDirectoryFiles(ftpClient, ftpPatchARO);
            FTPFile[] subFilesARU = Utils.getDirectoryFiles(ftpClient, ftpPatchARU);
            FTPFile[] subFilesPRO = Utils.getDirectoryFiles(ftpClient, ftpPatchPRO);
            FTPFile[] subFilesPRU = Utils.getDirectoryFiles(ftpClient, ftpPatchPRU);
            ftpClient.logout();
            ftpClient.disconnect();
            File internalDirHRO = context.getDir(internalPatchHRO, Context.MODE_PRIVATE);
            File internalDirHRU = context.getDir(internalPatchHRU, Context.MODE_PRIVATE);
            File internalDirARO = context.getDir(internalPatchARO, Context.MODE_PRIVATE);
            File internalDirARU = context.getDir(internalPatchARU, Context.MODE_PRIVATE);
            File internalDirPRO = context.getDir(internalPatchPRO, Context.MODE_PRIVATE);
            File internalDirPRU = context.getDir(internalPatchPRU, Context.MODE_PRIVATE);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    loading_hymns.setVisibility(View.GONE);
                    if (needsToUpdate(internalDirHRO, internalDirHRU, subFilesHRO, subFilesHRU)) {
                        iconDHymns.setVisibility(View.VISIBLE);
                        iconDHymns.setImageDrawable(context.getDrawable(R.drawable.outline_rotate_left_24));
                        UpdatesFragment.needsToDownloadH = true;
                    } else {
                        downloadHymns.setImageDrawable(context.getDrawable(R.drawable.update_done));
                        iconDHymns.setVisibility(View.GONE);
                    }

                    loading_audio.setVisibility(View.GONE);
                    if (needsToUpdate(internalDirARO, internalDirARU, subFilesARO, subFilesARU)) {
                        iconDAudio.setVisibility(View.VISIBLE);
                        iconDAudio.setImageDrawable(context.getDrawable(R.drawable.outline_rotate_left_24));
                        UpdatesFragment.needsToDownloadA = true;
                    } else {
                        downloadAudio.setImageDrawable(context.getDrawable(R.drawable.update_done));
                        iconDAudio.setVisibility(View.GONE);
                    }

                    loading_pdf.setVisibility(View.GONE);
                    if (needsToUpdate(internalDirPRO, internalDirPRU, subFilesPRO, subFilesPRU)) {
                        iconDPDF.setVisibility(View.VISIBLE);
                        iconDPDF.setImageDrawable(context.getDrawable(R.drawable.outline_rotate_left_24));
                        UpdatesFragment.needsToDownloadP = true;
                    } else {
                        downloadPDF.setImageDrawable(context.getDrawable(R.drawable.update_done));
                        iconDPDF.setVisibility(View.GONE);
                    }

                    loading_all.setVisibility(View.GONE);
                    if (UpdatesFragment.needsToDownloadH || UpdatesFragment.needsToDownloadA || UpdatesFragment.needsToDownloadP) {
                        iconDAll.setVisibility(View.VISIBLE);
                        iconDAll.setImageDrawable(context.getDrawable(R.drawable.outline_rotate_left_24));
                        currentState.setIcon(context.getDrawable(R.drawable.outline_rotate_left_white_24));
                    } else {
                        iconDAll.setVisibility(View.INVISIBLE);
                        downloadAll.setImageDrawable(context.getDrawable(R.drawable.update_done));
                        currentState.setIcon(context.getDrawable(R.drawable.updates_all_done));
                    }
                }
            });
        } catch (Exception e) {
            errorOcured = true;
            activity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show());
            return false;
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        if (errorOcured) {
            UpdatesFragment.setBtnsOnErrorClickListener(context, activity);
        } else {
            UpdatesFragment.setBtnsOnClickListener(context, activity);
        }
        super.onPostExecute(o);
    }

    private boolean needsToUpdate(File internalDirRO, File internalDirRU, FTPFile[] subFilesRO, FTPFile[] subFilesRU) {
        try {
            File[] dirFilesRO = internalDirRO.listFiles();
            File[] dirFilesRU = internalDirRU.listFiles();
            if (subFilesRO != null)
                if (subFilesRO.length > 0)
                    for (FTPFile ftpFile : subFilesRO) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        if (dirFilesRO != null)
                            if (dirFilesRO.length > 0)
                                for (File fileDir : dirFilesRO) {
                                    if (ftpFile.getName().equals(fileDir.getName())) {
                                        exist = true;
                                        break;
                                    }
                                }
                        if (!exist) {
                            return true;
                        }
                    }
            if (subFilesRU != null)
                if (subFilesRU.length > 0)
                    for (FTPFile ftpFile : subFilesRU) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        if (dirFilesRU != null)
                            if (dirFilesRU.length > 0)
                                for (File fileDir : dirFilesRU) {
                                    if (ftpFile.getName().equals(fileDir.getName())) {
                                        exist = true;
                                        break;
                                    }
                                }
                        if (!exist) {
                            return true;
                        }
                    }
            return false;
        } catch (Exception e) {
            errorOcured = true;
            activity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show());
            return false;
        }
    }
}
