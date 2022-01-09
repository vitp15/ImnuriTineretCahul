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


public class UpdateFilesTask extends AsyncTask<String, String, String> {
    private Context context;
    private String result;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String file;
    private String server = "ftpupload.net";
    private int port = 21;
    private String user = "epiz_30672048";
    private String pass = "wiejPSD0VHtsYx";

    public UpdateFilesTask(Context context, FragmentActivity fragmentActivity, String file) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setMessage("Se pregăteștete");
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Anulare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        FTPClient ftpClient = new FTPClient();
        if (!NetworkUtils.hasActiveNetworkConnection(context)) {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet), Toast.LENGTH_SHORT).show());
        } else {
            File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
            if (file.equals("all")) {
                downaldAll(ftpClient, fragmentActivity.getString(R.string.ro_external_pdf_folder), internalDir.getAbsolutePath());
            } else {
                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] subFiles = Utils.getDirectoryFiles(ftpClient, fragmentActivity.getString(R.string.ro_external_pdf_folder));
                    deleteFiles(subFiles);
                    ftpClient.logout();
                    ftpClient.disconnect();
                    boolean exist = false;
                    for (FTPFile fileFtp : subFiles) {
                        if (fileFtp.getName().equals(file)) {
                            exist = true;
                            updateItem(fileFtp,
                                    fragmentActivity.getString(R.string.ro_external_pdf_folder)
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
            deleteFiles(subFiles);
            ftpClient.logout();
            ftpClient.disconnect();
            String remoteFilePatch, savePatch;
            for (FTPFile fileFtp : subFiles) {
                if (fileFtp.getName().equals(".") || fileFtp.getName().equals("..")) {
                    continue;
                }
                if (fileFtp.isDirectory()) {
                    File newDir = new File(dirSave + File.separator + fileFtp.getName());
                    boolean created = newDir.mkdirs();
                    if (created)
                        System.out.println("Created the Directory " + newDir);
                    else
                        System.out.println("Cannot create the directory " + newDir);
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
        File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
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