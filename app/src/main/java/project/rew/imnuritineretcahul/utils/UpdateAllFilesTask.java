package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;


public class UpdateAllFilesTask extends AsyncTask<String, String, String> {
    private Context context;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String server;
    private int port;
    private String user;
    private String pass;
    private String ftpPatchHRO, ftpPatchHRU, ftpPatchARO, ftpPatchARU, ftpPatchPRO, ftpPatchPRU;
    private String internalPatchHRO, internalPatchHRU, internalPatchARO, internalPatchARU, internalPatchPRO, internalPatchPRU;
    private int totalItems = 0, curentItem;

    public UpdateAllFilesTask(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        port = Integer.parseInt(context.getString(R.string.port));
        server = context.getString(R.string.server);
        user = context.getString(R.string.user);
        pass = context.getString(R.string.password);
        curentItem = 0;
        UpdateFilesTask.total = 0;
        UpdateFilesTask.fileSize = 0;


        internalPatchHRO = context.getString(R.string.ro_internal_hymns_folder);
        ftpPatchHRO = fragmentActivity.getString(R.string.ro_external_hymns_folder);
        internalPatchHRU = context.getString(R.string.ru_internal_hymns_folder);
        ftpPatchHRU = fragmentActivity.getString(R.string.ru_external_hymns_folder);

        internalPatchARO = context.getString(R.string.ro_internal_mp3_folder);
        ftpPatchARO = fragmentActivity.getString(R.string.ro_external_mp3_folder);
        internalPatchARU = context.getString(R.string.ru_internal_mp3_folder);
        ftpPatchARU = fragmentActivity.getString(R.string.ru_external_mp3_folder);

        internalPatchPRO = context.getString(R.string.ro_internal_pdf_folder);
        ftpPatchPRO = fragmentActivity.getString(R.string.ro_external_pdf_folder);
        internalPatchPRU = context.getString(R.string.ru_internal_pdf_folder);
        ftpPatchPRU = fragmentActivity.getString(R.string.ru_external_pdf_folder);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        if (Utils.language == Language.RO) {
            progressDialog.setMessage(fragmentActivity.getString(R.string.ready_ro));
           /*progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, fragmentActivity.getString(R.string.cancel_ro), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.setMessage(fragmentActivity.getString(R.string.canceling_msg_ro));
                }
            });*/

        } else if (Utils.language == Language.RU) {
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
            if (Utils.language == Language.RO)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ro), Toast.LENGTH_SHORT).show());
            if (Utils.language == Language.RU)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet_ru), Toast.LENGTH_SHORT).show());
        } else {
            File internalDirHRO = context.getDir(internalPatchHRO, Context.MODE_PRIVATE);
            File internalDirHRU = context.getDir(internalPatchHRU, Context.MODE_PRIVATE);
            File internalDirARO = context.getDir(internalPatchARO, Context.MODE_PRIVATE);
            File internalDirARU = context.getDir(internalPatchARU, Context.MODE_PRIVATE);
            File internalDirPRO = context.getDir(internalPatchPRO, Context.MODE_PRIVATE);
            File internalDirPRU = context.getDir(internalPatchPRU, Context.MODE_PRIVATE);
            File[] dirFilesHRO = internalDirHRO.listFiles();
            File[] dirFilesHRU = internalDirHRU.listFiles();
            File[] dirFilesARO = internalDirARO.listFiles();
            File[] dirFilesARU = internalDirARU.listFiles();
            File[] dirFilesPRO = internalDirPRO.listFiles();
            File[] dirFilesPRU = internalDirPRU.listFiles();
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
                deleteFiles(subFilesHRO, internalDirHRO);
                deleteFiles(subFilesHRU, internalDirHRU);
                deleteFiles(subFilesARO, internalDirARO);
                deleteFiles(subFilesARU, internalDirARU);
                deleteFiles(subFilesPRO, internalDirPRO);
                deleteFiles(subFilesPRU, internalDirPRU);
                int iHRO = 0, iHRU = 0, iARO = 0, iARU = 0, iPRO = 0, iPRU = 0;
                for (FTPFile ftpFile : subFilesHRO) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesHRO) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        if (ftpFile.isDirectory()) {
                            FTPFile[] subFilesFromHymn = Utils.getDirectoryFiles(ftpClient, ftpPatchHRO + "/" + ftpFile.getName());
                            if (subFilesFromHymn != null)
                                for (FTPFile ftpFileFromHymn : subFilesFromHymn) {
                                    if (ftpFileFromHymn.getName().equals(".") || ftpFileFromHymn.getName().equals("..")) {
                                        continue;
                                    }
                                    UpdateFilesTask.fileSize += ftpFileFromHymn.getSize();
                                }
                        }
                        iHRO++;
                    }
                }
                for (FTPFile ftpFile : subFilesHRU) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesHRU) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        if (ftpFile.isDirectory()) {
                            FTPFile[] subFilesFromHymn = Utils.getDirectoryFiles(ftpClient, ftpPatchHRU + "/" + ftpFile.getName());
                            if (subFilesFromHymn != null)
                                for (FTPFile ftpFileFromHymn : subFilesFromHymn) {
                                    if (ftpFileFromHymn.getName().equals(".") || ftpFileFromHymn.getName().equals("..")) {
                                        continue;
                                    }
                                    UpdateFilesTask.fileSize += ftpFileFromHymn.getSize();
                                }
                        }
                        iHRU++;
                    }
                }
                ftpClient.logout();
                ftpClient.disconnect();
                for (FTPFile ftpFile : subFilesARO) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesARO) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        UpdateFilesTask.fileSize += ftpFile.getSize();
                        iARO++;
                    }
                }
                for (FTPFile ftpFile : subFilesARU) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesARU) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        UpdateFilesTask.fileSize += ftpFile.getSize();
                        iARU++;
                    }
                }
                for (FTPFile ftpFile : subFilesPRO) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesPRO) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        UpdateFilesTask.fileSize += ftpFile.getSize();
                        iPRO++;
                    }
                }
                for (FTPFile ftpFile : subFilesPRU) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesPRU) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        UpdateFilesTask.fileSize += ftpFile.getSize();
                        iPRU++;
                    }
                }
                totalItems = iHRO + iHRU + iARO + iARU + iPRO + iPRU;
                FTPFile[] subFilesforDownaldHRO = new FTPFile[iHRO];
                FTPFile[] subFilesforDownaldHRU = new FTPFile[iHRU];
                FTPFile[] subFilesforDownaldARO = new FTPFile[iARO];
                FTPFile[] subFilesforDownaldARU = new FTPFile[iARU];
                FTPFile[] subFilesforDownaldPRO = new FTPFile[iPRO];
                FTPFile[] subFilesforDownaldPRU = new FTPFile[iPRU];
                if (totalItems != 0) {
                    int jHRO = 0, jHRU = 0, jARO = 0, jARU = 0, jPRO = 0, jPRU = 0;
                    for (FTPFile ftpFile : subFilesHRO) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesHRO) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldHRO[jHRO] = ftpFile;
                            jHRO++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesHRU) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesHRU) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldHRU[jHRU] = ftpFile;
                            jHRU++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesARO) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesARO) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldARO[jARO] = ftpFile;
                            jARO++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesARU) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesARU) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldARU[jARU] = ftpFile;
                            jARU++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesPRO) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesPRO) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldPRO[jPRO] = ftpFile;
                            jPRO++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesPRU) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesPRU) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldPRU[jPRU] = ftpFile;
                            jPRU++;
                        }
                    }
                }
                if (totalItems == 0) {
                    Thread thread = new Thread() {
                        public void run() {
                            Looper.prepare();
                            if (Utils.language == Language.RO) {
                                Toast.makeText(context, R.string.all_elements_are_updated_ro, Toast.LENGTH_SHORT).show();
                            } else if (Utils.language == Language.RU) {
                                Toast.makeText(context, R.string.all_elements_are_updated_ru, Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        }
                    };
                    thread.start();
                }
                if (totalItems != 0) {
                    downaldAll(ftpClient, subFilesforDownaldHRO, true, ftpPatchHRO, internalDirHRO.getAbsolutePath(), Type.HYMN);
                    downaldAll(ftpClient, subFilesforDownaldHRU, true, ftpPatchHRU, internalDirHRU.getAbsolutePath(), Type.HYMN);
                    downaldAll(ftpClient, subFilesforDownaldARO, true, ftpPatchARO, internalDirARO.getAbsolutePath(), Type.AUDIO);
                    downaldAll(ftpClient, subFilesforDownaldARU, true, ftpPatchARU, internalDirARU.getAbsolutePath(), Type.AUDIO);
                    downaldAll(ftpClient, subFilesforDownaldPRO, true, ftpPatchPRO, internalDirPRO.getAbsolutePath(), Type.PDF);
                    downaldAll(ftpClient, subFilesforDownaldPRU, true, ftpPatchPRU, internalDirPRU.getAbsolutePath(), Type.PDF);
                }
                if (Utils.language == Language.RO)
                    progressDialog.setMessage(fragmentActivity.getString(R.string.cancel_operation_update_ro));
                else if (Utils.language == Language.RU)
                    progressDialog.setMessage(fragmentActivity.getString(R.string.cancel_operation_update_ru));
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
        Utils.loadHymns(context);
        progressDialog.dismiss();
        fragmentActivity.recreate();
    }

    private void downaldAll(FTPClient ftpClient, FTPFile[] subFiles, boolean main, String dirToList, String dirSave, Type type) {
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
                if (type == Type.PDF || type == Type.AUDIO)
                    curentItem++;
                if (fileFtp.isDirectory()) {
                    if (type == Type.HYMN) {
                        curentItem++;
                    }
                    if (Utils.language == Language.RO) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_hymn_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    } else if (Utils.language == Language.RU) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_hymn_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    }
                    File newDir = new File(dirSave + File.separator + fileFtp.getName());
                    newDir.mkdirs();
                    downaldAll(ftpClient, subFiles, false, dirToList + File.separator + fileFtp.getName(),
                            newDir.getAbsolutePath(), type);
                } else {
                    remoteFilePatch = dirToList + File.separator + fileFtp.getName();
                    savePatch = dirSave + File.separator + fileFtp.getName();
                    if (Utils.language == Language.RO) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_hymn_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.AUDIO) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_audio_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.PDF) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_pdf_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    } else if (Utils.language == Language.RU) {
                        if (type == Type.HYMN) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_hymn_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.AUDIO) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_audio_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        } else if (type == Type.PDF) {
                            progressDialog.setMessage(fragmentActivity.getString(R.string.downald_all_pdf_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems));
                        }
                    }
                    updateItem(remoteFilePatch, savePatch);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            progressDialog.dismiss();
        }
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
            ex.printStackTrace();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void deleteFiles(FTPFile[] subFiles, File internalDir) {
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
