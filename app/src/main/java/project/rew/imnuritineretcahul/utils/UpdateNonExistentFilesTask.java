package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;


public class UpdateNonExistentFilesTask extends AsyncTask<String, String, String> {
    private Context context;
    private Type type;
    private Language language;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String server = "ftpupload.net";
    private int port = 21;
    private String user = "epiz_30672048";
    private String pass = "wiejPSD0VHtsYx";
    private String ftpPatch;
    private String internalPatch;
    private int totalItems = 0, curentItem = 0;
    private double procentPerHymn;


    public UpdateNonExistentFilesTask(Context context, FragmentActivity fragmentActivity,
                                      Type type, Language language) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.type = type;
        this.language = language;
        UpdateFilesTask.total = 0;
        UpdateFilesTask.fileSize = 0;
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
            File[] dirFiles = internalDir.listFiles();
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, ftpPatch);
                deleteFiles(subFiles);
                ftpClient.logout();
                ftpClient.disconnect();
                int i = 0;
                for (FTPFile ftpFile : subFiles) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFiles) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        UpdateFilesTask.fileSize += ftpFile.getSize();
                        i++;
                    }
                }
                totalItems = i;
                procentPerHymn = 1 / (double) totalItems * 100;
                FTPFile[] subFilesforDownald = new FTPFile[i];
                int j = 0;
                for (FTPFile ftpFile : subFiles) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFiles) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        subFilesforDownald[j] = ftpFile;
                        j++;
                    }
                }
                downaldAll(ftpClient, subFilesforDownald, true, ftpPatch, internalDir.getAbsolutePath());
                if (language == Language.RO)
                    progressDialog.setMessage("Finisarea operatiei...");
                else if (language == Language.RU)
                    progressDialog.setMessage("Finishing...");
            } catch (Exception ex) {
                ex.printStackTrace();
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

    private void downaldAll(FTPClient ftpClient, FTPFile[] subFiles, boolean main, String dirToList, String dirSave) {
        try {
            if (!main) {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                subFiles = Utils.getDirectoryFiles(ftpClient, dirToList);
                ftpClient.logout();
                ftpClient.disconnect();
            }
            String remoteFilePatch, savePatch;
            for (FTPFile fileFtp : subFiles) {
                if (fileFtp.getName().equals(".") || fileFtp.getName().equals("..")) {
                    continue;
                }
                if (type != Type.HYMN)
                    curentItem++;
                if (fileFtp.isDirectory()) {
                    if (type == Type.HYMN) {
                        curentItem++;
                    }
                    if (language == Language.RO) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage("Se descarcă imnul  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    } else if (language == Language.RU) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage("Se descarcă imnul rus  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    }
                    File newDir = new File(dirSave + File.separator + fileFtp.getName());
                    newDir.mkdirs();
                    downaldAll(ftpClient, subFiles, false, dirToList + File.separator + fileFtp.getName(),
                            newDir.getAbsolutePath()); //because to can to set corectly the progress
                    if (type == Type.HYMN) {
                        int procent = (int) (curentItem * procentPerHymn);
                        progressDialog.setProgress(procent);
                    }
                } else {
                    remoteFilePatch = dirToList + File.separator + fileFtp.getName();
                    savePatch = dirSave + File.separator + fileFtp.getName();
                    if (language == Language.RO) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage("Se descarcă imnul  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.AUDIO) {
                            progressDialog.setMessage("Se descarcă audio  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.PDF) {
                            progressDialog.setMessage("Se descarcă fișierul pdf  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    } else if (language == Language.RU) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage("Se descarcă imnul rus  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.AUDIO) {
                            progressDialog.setMessage("Se descarcă audio rus  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.PDF) {
                            progressDialog.setMessage("Se descarcă pdf rus  " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    }
                    updateItem(remoteFilePatch, savePatch);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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