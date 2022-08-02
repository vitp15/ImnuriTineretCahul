package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;


public class DownloadSingleFileTask extends AsyncTask<String, String, String> {
    private Context context;
    private Type type;
    private Language language;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String file;
    private String server;
    private int port;
    private String user;
    private String pass;
    private String ftpPatch;
    private String internalPatch;


    public DownloadSingleFileTask(Context context, FragmentActivity fragmentActivity, String file,
                                  Type type, Language language) {
        this.context = context;
        port = Integer.parseInt(context.getString(R.string.port));
        server = context.getString(R.string.server);
        user = context.getString(R.string.user);
        pass = context.getString(R.string.password);
        this.fragmentActivity = fragmentActivity;
        this.file = file;
        this.type = type;
        this.language = language;
        if (language == Language.RO) {
            if (type == Type.HYMN) {
                internalPatch = context.getString(R.string.ro_internal_hymns_folder);
                ftpPatch = fragmentActivity.getString(R.string.ro_external_hymns_folder);
            } else if (type == Type.AUDIO) {
                internalPatch = context.getString(R.string.ro_internal_mp3_folder);
                ftpPatch = fragmentActivity.getString(R.string.ro_external_mp3_folder);
            } else if (type == Type.PDF) {
                internalPatch = context.getString(R.string.ro_internal_pdf_folder);
                ftpPatch = fragmentActivity.getString(R.string.ro_external_pdf_folder);
            }
        } else if (language == Language.RU) {
            if (type == Type.HYMN) {
                internalPatch = context.getString(R.string.ru_internal_hymns_folder);
                ftpPatch = fragmentActivity.getString(R.string.ru_external_hymns_folder);
            } else if (type == Type.AUDIO) {
                internalPatch = context.getString(R.string.ru_internal_mp3_folder);
                ftpPatch = fragmentActivity.getString(R.string.ru_external_mp3_folder);
            } else if (type == Type.PDF) {
                internalPatch = context.getString(R.string.ru_internal_pdf_folder);
                ftpPatch = fragmentActivity.getString(R.string.ru_external_pdf_folder);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        if (language == Language.RO) {
            progressDialog.setMessage(fragmentActivity.getString(R.string.ready_ro));
            /*progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, fragmentActivity.getString(R.string.cancel_ro), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.setMessage(fragmentActivity.getString(R.string.canceling_msg_ro));
                }
            });*/
        } else if (language == Language.RU) {
            progressDialog.setMessage(fragmentActivity.getString(R.string.ready_ru));
            /*progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, fragmentActivity.getString(R.string.cancel_ru), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.setMessage(fragmentActivity.getString(R.string.canceling_msg_ru));
                }
            });*/
        }

        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        FTPClient ftpClient = new FTPClient();
        if (!NetworkUtils.hasActiveNetworkConnection(context)) {
            if (language == Language.RO)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show());
            if (language == Language.RU)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show());
        } else {
            File internalDir = context.getDir(internalPatch, Context.MODE_PRIVATE);
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, ftpPatch);
                deleteFiles(subFiles);
                ftpClient.logout();
                ftpClient.disconnect();
                boolean exist = false;
                for (FTPFile fileFtp : subFiles) {
                    if (fileFtp.getName().equals(file)) {
                        exist = true;
                        if (language == Language.RO) {
                            if (type == Type.HYMN) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ro));
                            } else if (type == Type.AUDIO) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ro));
                            } else if (type == Type.PDF) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ro));
                            }
                        } else if (language == Language.RU) {
                            if (type == Type.HYMN) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ru));
                            } else if (type == Type.AUDIO) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ru));
                            } else if (type == Type.PDF) {
                                progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ru));
                            }
                        }
                        updateItem(ftpPatch
                                        + File.separator + fileFtp.getName(),
                                internalDir.getAbsolutePath() + File.separator + fileFtp.getName());
                        break;
                    }
                }
                if (!exist) {
                    if (language == Language.RO)
                        fragmentActivity.runOnUiThread(() -> Toast.makeText(context, R.string.dont_exist_ro, Toast.LENGTH_SHORT).show());
                    if (language == Language.RU)
                        fragmentActivity.runOnUiThread(() -> Toast.makeText(context, R.string.dont_exist_ru, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                progressDialog.dismiss();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        progressDialog.setMessage(text[0]);
    }

    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        fragmentActivity.recreate();
    }

    private void updateItem(String remoteFilePatch, String savePatch) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            Utils.downloadSingleFile(ftpClient, remoteFilePatch, savePatch, progressDialog, type);

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void deleteFiles(FTPFile[] subFiles) {
        File internalDir = context.getDir(internalPatch, Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        for (File dirFile : dirFiles) {
            boolean exist = false;
            for (FTPFile fileFtp : subFiles) {
                if (dirFile.getName().equals(fileFtp.getName())) {
                    exist = true;
                }
            }
            if (!exist) Utils.DeleteRecursive(dirFile);
        }
    }

}