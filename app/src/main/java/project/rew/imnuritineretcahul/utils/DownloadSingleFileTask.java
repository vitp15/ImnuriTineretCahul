package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;


public class DownloadSingleFileTask extends AsyncTask<String, String, String> {
    private Context context;
    private Type type;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String file;
    private String server;
    private int port;
    private String user;
    private String pass;
    private String ftpPatch;
    private String internalPatch;
    boolean brokedAfterStart;
    private Hymn hymn;


    public DownloadSingleFileTask(Context context, FragmentActivity fragmentActivity, Hymn hymn,
                                  Type type) {
        this.context = context;
        brokedAfterStart = false;
        port = Integer.parseInt(context.getString(R.string.port));
        server = context.getString(R.string.server);
        user = context.getString(R.string.user);
        pass = context.getString(R.string.password);
        UpdateFilesTask.fileSize = 0;
        UpdateFilesTask.total = 0;
        this.hymn = hymn;
        this.fragmentActivity = fragmentActivity;
        this.file = String.valueOf(hymn.getId());
        this.type = type;
        if (Utils.language == Language.RO) {
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
        } else if (Utils.language == Language.RU) {
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
        if (Utils.language == Language.RO) {
            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.ready_ro)));
            /*progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, fragmentActivity.getString(R.string.cancel_ro), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.setMessage(fragmentActivity.getString(R.string.canceling_msg_ro));
                }
            });*/
        } else if (Utils.language == Language.RU) {
            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.ready_ru)));
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
            if (Utils.language == Language.RO)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show());
            if (Utils.language == Language.RU)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show());
            brokedAfterStart = true;
        } else {
            File internalDir = context.getDir(internalPatch, Context.MODE_PRIVATE);
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, ftpPatch);
                ftpClient.logout();
                ftpClient.disconnect();
                boolean exist = false;
                int count = 0;
                List<String> toDownload = new ArrayList<>();
                for (FTPFile fileFtp : subFiles) {
                    if (fileFtp.getName().equals(".") || fileFtp.getName().equals("..")) continue;
                    if ((type == Type.HYMN && fileFtp.getName().split(" - ").length > 0 && fileFtp.getName().split("\\.").length != 0) ||
                            (type != Type.HYMN) && fileFtp.getName().split("\\.").length > 0)
                        if ((type == Type.HYMN && fileFtp.getName().split(" - ")[0].split("\\.")[0].equals(file)) ||
                                (type != Type.HYMN) && fileFtp.getName().split("\\.")[0].equals(file)) {
                            exist = true;
                            count++;
                            if (Utils.language == Language.RO) {
                                if (type == Type.HYMN) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ro)));
                                } else if (type == Type.AUDIO) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ro)));
                                } else if (type == Type.PDF) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ro)));
                                }
                            } else if (Utils.language == Language.RU) {
                                if (type == Type.HYMN) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ru)));
                                } else if (type == Type.AUDIO) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ru)));
                                } else if (type == Type.PDF) {
                                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ru)));
                                }
                            }
                            toDownload.add(fileFtp.getName());
                            UpdateFilesTask.fileSize += fileFtp.getSize();
                            if (type != Type.AUDIO || count == 2)
                                break;
                        }
                }
                if (!exist) {
                    brokedAfterStart = true;
                    if (Utils.language == Language.RO)
                        fragmentActivity.runOnUiThread(() -> Toast.makeText(context, R.string.dont_exist_ro, Toast.LENGTH_SHORT).show());
                    if (Utils.language == Language.RU)
                        fragmentActivity.runOnUiThread(() -> Toast.makeText(context, R.string.dont_exist_ru, Toast.LENGTH_SHORT).show());
                } else if (exist) {
                    for (String file : toDownload)
                        updateItem(ftpPatch
                                        + File.separator + file,
                                internalDir.getAbsolutePath() + File.separator + file);
                }
            } catch (Exception ex) {
                brokedAfterStart = true;
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
        try {
            Utils.dosentDownloadCorectly = PrefConfig.load_not_download_corectly(context);
            if (Utils.dosentDownloadCorectly != null)
                if (Utils.dosentDownloadCorectly.size() > 0 && Utils.ifDownloadBrokedChangeListItem) {
                    brokedAfterStart = true;
                }
            if (brokedAfterStart) {
                Utils.ifDownloadBrokedChangeListItem = false;
                Utils.constraintLayout.setBackground(context.getDrawable(R.drawable.hymn_nonexistent_list_press));
                Utils.hymn_title_to_edit.setTextColor(context.getResources().getColor(R.color.nonpressed_nonexis_contur));
                if (hymn.isSaved())
                    Utils.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_clicked));
                else
                    Utils.saved.setImageDrawable(context.getResources().getDrawable(R.drawable.nonexisting_save_nonclicked));
            }
            if (Utils.dosentDownloadCorectly != null && !Utils.dosentDownloadCorectly.isEmpty()) {
                for (String s : Utils.dosentDownloadCorectly) {
                    File file = new File(s);
                    Utils.DeleteRecursive(file);
                }
                Utils.dosentDownloadCorectly.clear();
                PrefConfig.saveNotDownloadCorectly(context, Utils.dosentDownloadCorectly);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        Utils.loadHymns(context);
        fragmentActivity.recreate();
    }

    private void updateItem(String remoteFilePatch, String savePatch) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            Utils.downloadSingleFile(context, ftpClient, remoteFilePatch, savePatch, progressDialog);

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception ex) {
            brokedAfterStart = true;
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