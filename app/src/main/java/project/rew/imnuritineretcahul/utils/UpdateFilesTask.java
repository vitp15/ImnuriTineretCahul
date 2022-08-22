package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
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


public class UpdateFilesTask extends AsyncTask<String, String, String> {
    private Context context;
    private Type type;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private String server;
    private int port;
    private String user;
    private String pass;
    private String ftpPatchRO, ftpPatchRU;
    private String internalPatchRO, internalPatchRU;
    private int totalItems = 0, curentItem;
    private boolean onlyImgInAudio = false;
    public static long fileSize, total;

    public UpdateFilesTask(Context context, FragmentActivity fragmentActivity,
                           Type type) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.type = type;
        port = Integer.parseInt(context.getString(R.string.port));
        server = context.getString(R.string.server);
        user = context.getString(R.string.user);
        pass = context.getString(R.string.password);
        curentItem = 0;
        total = 0;
        fileSize = 0;

        if (type == Type.HYMN) {
            internalPatchRO = context.getString(R.string.ro_internal_hymns_folder);
            ftpPatchRO = fragmentActivity.getString(R.string.ro_external_hymns_folder);
            internalPatchRU = context.getString(R.string.ru_internal_hymns_folder);
            ftpPatchRU = fragmentActivity.getString(R.string.ru_external_hymns_folder);
        } else if (type == Type.AUDIO) {
            internalPatchRO = context.getString(R.string.ro_internal_mp3_folder);
            ftpPatchRO = fragmentActivity.getString(R.string.ro_external_mp3_folder);
            internalPatchRU = context.getString(R.string.ru_internal_mp3_folder);
            ftpPatchRU = fragmentActivity.getString(R.string.ru_external_mp3_folder);
        } else if (type == Type.PDF) {
            internalPatchRO = context.getString(R.string.ro_internal_pdf_folder);
            ftpPatchRO = fragmentActivity.getString(R.string.ro_external_pdf_folder);
            internalPatchRU = context.getString(R.string.ru_internal_pdf_folder);
            ftpPatchRU = fragmentActivity.getString(R.string.ru_external_pdf_folder);
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
        } else {
            File internalDirRO = context.getDir(internalPatchRO, Context.MODE_PRIVATE);
            File internalDirRU = context.getDir(internalPatchRU, Context.MODE_PRIVATE);
            File[] dirFilesRO = internalDirRO.listFiles();
            File[] dirFilesRU = internalDirRU.listFiles();
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                FTPFile[] subFilesRO = Utils.getDirectoryFiles(ftpClient, ftpPatchRO);
                FTPFile[] subFilesRU = Utils.getDirectoryFiles(ftpClient, ftpPatchRU);
                deleteFiles(subFilesRO, internalDirRO);
                deleteFiles(subFilesRU, internalDirRU);
                int iRO = 0, aRO = 0, iRU = 0, aRU = 0;
                for (FTPFile ftpFile : subFilesRO) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesRO) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        if (type == Type.HYMN && ftpFile.isDirectory()) {
                            FTPFile[] subFilesFromHymn = Utils.getDirectoryFiles(ftpClient, ftpPatchRO + "/" + ftpFile.getName());
                            if (subFilesFromHymn != null)
                                for (FTPFile ftpFileFromHymn : subFilesFromHymn) {
                                    if (ftpFileFromHymn.getName().equals(".") || ftpFileFromHymn.getName().equals("..")) {
                                        continue;
                                    }
                                    fileSize += ftpFileFromHymn.getSize();
                                }
                        } else if (type == Type.AUDIO || type == Type.PDF)
                            fileSize += ftpFile.getSize();
                        if (type != Type.AUDIO || ftpFile.getName().split("\\.")[2].equals("mp3"))
                            iRO++;
                        else aRO++;
                    }
                }
                for (FTPFile ftpFile : subFilesRU) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        continue;
                    }
                    boolean exist = false;
                    for (File fileDir : dirFilesRU) {
                        if (ftpFile.getName().equals(fileDir.getName())) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        if (type == Type.HYMN && ftpFile.isDirectory()) {
                            FTPFile[] subFilesFromHymn = Utils.getDirectoryFiles(ftpClient, ftpPatchRU + "/" + ftpFile.getName());
                            if (subFilesFromHymn != null)
                                for (FTPFile ftpFileFromHymn : subFilesFromHymn) {
                                    if (ftpFileFromHymn.getName().equals(".") || ftpFileFromHymn.getName().equals("..")) {
                                        continue;
                                    }
                                    fileSize += ftpFileFromHymn.getSize();
                                }
                        } else if (type == Type.AUDIO || type == Type.PDF)
                            fileSize += ftpFile.getSize();
                        if (type != Type.AUDIO || ftpFile.getName().split("\\.")[2].equals("mp3"))
                            iRU++;
                        else aRU++;
                    }
                }
                ftpClient.logout();
                ftpClient.disconnect();
                totalItems = iRO + iRU;
                if (totalItems == 0 && (aRO != 0 || aRU != 0) && type == Type.AUDIO) {
                    totalItems = aRO + aRU;
                    onlyImgInAudio = true;
                }
                FTPFile[] subFilesforDownaldRO = new FTPFile[iRO + aRO];
                FTPFile[] subFilesforDownaldRU = new FTPFile[iRU + aRU];
                if (totalItems != 0) {
                    int jRO = 0, jRU = 0;
                    for (FTPFile ftpFile : subFilesRO) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesRO) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldRO[jRO] = ftpFile;
                            jRO++;
                        }
                    }
                    for (FTPFile ftpFile : subFilesRU) {
                        if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                            continue;
                        }
                        boolean exist = false;
                        for (File fileDir : dirFilesRU) {
                            if (ftpFile.getName().equals(fileDir.getName())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            subFilesforDownaldRU[jRU] = ftpFile;
                            jRU++;
                        }
                    }
                }
                if (totalItems == 0) {
                    Thread thread = new Thread() {
                        public void run() {
                            Looper.prepare();
                            if (Utils.language == Language.RO) {
                                if (type == Type.HYMN)
                                    Toast.makeText(context, R.string.all_hymns_are_updated_ro, Toast.LENGTH_SHORT).show();
                                if (type == Type.AUDIO)
                                    Toast.makeText(context, R.string.all_audio_are_updated_ro, Toast.LENGTH_SHORT).show();
                                if (type == Type.PDF)
                                    Toast.makeText(context, R.string.all_pdf_are_updated_ro, Toast.LENGTH_SHORT).show();
                            } else if (Utils.language == Language.RU) {
                                if (type == Type.HYMN)
                                    Toast.makeText(context, R.string.all_hymns_are_updated_ru, Toast.LENGTH_SHORT).show();
                                if (type == Type.AUDIO)
                                    Toast.makeText(context, R.string.all_audio_are_updated_ru, Toast.LENGTH_SHORT).show();
                                if (type == Type.PDF)
                                    Toast.makeText(context, R.string.all_pdf_are_updated_ru, Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        }
                    };
                    thread.start();
                }
                if (totalItems != 0) {
                    downaldAll(ftpClient, subFilesforDownaldRO, true, ftpPatchRO, internalDirRO.getAbsolutePath());
                    downaldAll(ftpClient, subFilesforDownaldRU, true, ftpPatchRU, internalDirRU.getAbsolutePath());
                }
                if (Utils.language == Language.RO)
                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.cancel_operation_update_ro)));
                else if (Utils.language == Language.RU)
                    fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.cancel_operation_update_ru)));
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
                if (type == Type.PDF)
                    curentItem++;
                if (type == Type.AUDIO && fileFtp.getName().split("\\.")[2].equals("mp3") || type == Type.AUDIO && onlyImgInAudio)
                    curentItem++;
                if (fileFtp.isDirectory()) {
                    if (type == Type.HYMN) {
                        curentItem++;
                    }
                    if (Utils.language == Language.RO) {
                        if (type == Type.HYMN) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        }
                    } else if (Utils.language == Language.RU) {
                        if (type == Type.HYMN) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        }
                    }
                    File newDir = new File(dirSave + File.separator + fileFtp.getName());
                    newDir.mkdirs();
                    downaldAll(ftpClient, subFiles, false, dirToList + File.separator + fileFtp.getName(),
                            newDir.getAbsolutePath()); //because to can to set corectly the progress
                } else {
                    remoteFilePatch = dirToList + File.separator + fileFtp.getName();
                    savePatch = dirSave + File.separator + fileFtp.getName();
                    if (Utils.language == Language.RO) {
                        if (type == Type.HYMN) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        } else if (type == Type.AUDIO) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        } else if (type == Type.PDF) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ro) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        }
                    } else if (Utils.language == Language.RU) {
                        if (type == Type.HYMN) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_hymn_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        } else if (type == Type.AUDIO) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_audio_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
                        } else if (type == Type.PDF) {
                            fragmentActivity.runOnUiThread(() -> progressDialog.setMessage(fragmentActivity.getString(R.string.downald_pdf_ru) + "   " +
                                    String.valueOf(curentItem) + " / " + String.valueOf(totalItems)));
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