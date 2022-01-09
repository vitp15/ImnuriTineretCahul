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


public class UpdateFilesTask extends AsyncTask<String, String, String> {
    private Context context;
    private Type type;
    private Language language;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String file = "all";
    private String server = "ftpupload.net";
    private int port = 21;
    private String user = "epiz_30672048";
    private String pass = "wiejPSD0VHtsYx";
    private String ftpPatch;
    private String internalPatch;

    public UpdateFilesTask(Context context, FragmentActivity fragmentActivity, String file,
                           Type type, Language language) {
        this.context = context;
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

    public UpdateFilesTask(Context context, FragmentActivity fragmentActivity,
                           Type type, Language language) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
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
        progressDialog.setMessage("Se pregăteștete");
        /*progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Anulare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });*/
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        FTPClient ftpClient = new FTPClient();
        if (!NetworkUtils.hasActiveNetworkConnection(context)) {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet), Toast.LENGTH_SHORT).show());
        } else {
            File internalDir = context.getDir(internalPatch, Context.MODE_PRIVATE);
            if (file.equals("all")) {
                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, ftpPatch);
                    deleteFiles(subFiles);
                    ftpClient.logout();
                    ftpClient.disconnect();
                    downaldAll(ftpClient, ftpPatch, internalDir.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
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
                            updateItem(fileFtp,
                                    ftpPatch
                                            + File.separator + fileFtp.getName(),
                                    internalDir.getAbsolutePath() + File.separator + fileFtp.getName());
                            break;
                        }
                    }
                    if (!exist)
                        fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Ne pare rău, fișierul nu există pe server", Toast.LENGTH_SHORT).show());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    private void downaldAll(FTPClient ftpClient, String dirToList, String dirSave) {
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, dirToList);
            ftpClient.logout();
            ftpClient.disconnect();
            String remoteFilePatch, savePatch;
            for (FTPFile fileFtp : subFiles) {
                if (fileFtp.getName().equals(".") || fileFtp.getName().equals("..")) {
                    continue;
                }
                if (fileFtp.isDirectory()) {
                    File newDir = new File(dirSave + File.separator + fileFtp.getName());
                    newDir.mkdirs();
                    downaldAll(ftpClient, dirToList + File.separator + fileFtp.getName(),
                            newDir.getAbsolutePath());
                } else {
                    remoteFilePatch = dirToList + File.separator + fileFtp.getName();
                    savePatch = dirSave + File.separator + fileFtp.getName();
                    updateItem(fileFtp, remoteFilePatch, savePatch);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateItem(FTPFile fileFTP, String remoteFilePatch, String savePatch) {
        long fileSize = fileFTP.getSize();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            Utils.downloadSingleFile(ftpClient, remoteFilePatch, savePatch, progressDialog, fileSize);

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