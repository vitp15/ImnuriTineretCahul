package project.rew.imnuritineretcahul.ro.ui.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.ro.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.NetworkUtils;

import static project.rew.imnuritineretcahul.ro.ui.home.Utils.hymns;

public class Utils {

    public static void deleteAudio(Context context, String forDelete) {
        File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        for (File dirFile : dirFiles) {
            if (dirFile.getName().equals(forDelete)) DeleteRecursive(dirFile);
        }
    }

    public static void updateAudio(Context context, FragmentActivity fragmentActivity) {
        File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);


        if (!NetworkUtils.hasActiveNetworkConnection(context)) {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet), Toast.LENGTH_SHORT).show());
            return;
        }

        String server = "ftpupload.net";
        int port = 21;
        String user = "epiz_30672048";
        String pass = "wiejPSD0VHtsYx";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            //Delete directory taht are deleted from server or that name are deleted
            FTPFile[] subFiles = getDirectoryFiles(ftpClient, fragmentActivity.getString(R.string.ro_external_mp3_folder));

            File[] dirFiles = internalDir.listFiles();
            for (File dirFile : dirFiles) {
                boolean exist = false;
                for (int i = 0; i < subFiles.length; i++) {
                    if (dirFile.getName().equals(subFiles[i].getName())) {
                        exist = true;
                    }
                }
                if (!exist) DeleteRecursive(dirFile);
            }
//finish directory deleting if it is needed
            Utils.downloadDirectory(ftpClient, fragmentActivity.getString(R.string.ro_external_mp3_folder), "", internalDir.getAbsolutePath());

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }
    }

    public static void updateAudio(Context context, FragmentActivity fragmentActivity, String file) {
        File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);


        if (!NetworkUtils.hasActiveNetworkConnection(context)) {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.settings_connect_to_internet), Toast.LENGTH_SHORT).show());
            return;
        }

        String server = "ftpupload.net";
        int port = 21;
        String user = "epiz_30672048";
        String pass = "wiejPSD0VHtsYx";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();


            Utils.downloadDirectory(context, fragmentActivity, ftpClient, fragmentActivity.getString(R.string.ro_external_mp3_folder), "", internalDir.getAbsolutePath(), file);

            ftpClient.logout();
            ftpClient.disconnect();
            File[] dirFiles = internalDir.listFiles();
            for (File dirFile : dirFiles) System.out.println(dirFile.getName().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }
    }

    /**
     * * Download a whole directory from a FTP server.
     * * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * * @param parentDir Path of the parent directory of the current directory being
     * * downloaded.
     * * @param currentDir Path of the current directory being downloaded.
     * * @param saveDir path of directory where the whole remote directory will be
     * * downloaded and saved.
     * * @throws IOException if any network or IO error occurred.
     */
    public static void downloadDirectory(FTPClient ftpClient, String parentDir, String currentDir, String saveDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
        FTPFile[] subFiles = getDirectoryFiles(ftpClient, dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + File.separator
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                String newDirPath = saveDir + File.separator + currentFileName;

                if (currentDir.equals("")) {
                    newDirPath = saveDir + File.separator
                            + currentFileName;
                }

                if (aFile.isDirectory()) {
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        System.out.println("CREATED the directory: " + newDirPath);
                    } else {
                        System.out.println("COULD NOT create the directory: " + newDirPath);
                    }
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir + File.separator + currentFileName);
                } else {
                    boolean success = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (success) {
                        System.out.println("DOWNLOADED the file: " + filePath);
                    } else {
                        System.out.println("COULD NOT download the file: "
                                + filePath);
                    }
                }
            }
        }
    }


    public static void downloadDirectory(Context context, FragmentActivity fragmentActivity, FTPClient ftpClient, String parentDir, String currentDir, String saveDir, String file) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
        FTPFile[] subFiles = getDirectoryFiles(ftpClient, dirToList);

        if (subFiles != null && subFiles.length > 0) {
            boolean exist = false;
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.toString().equals(file)) {
                    exist = true;
                    if (currentFileName.equals(".") || currentFileName.equals("..")) {
                        continue;
                    }
                    String filePath = parentDir + "/" + currentDir + File.separator
                            + currentFileName;
                    if (currentDir.equals("")) {
                        filePath = parentDir + "/" + currentFileName;
                    }

                    String newDirPath = saveDir + File.separator + currentFileName;

                    if (currentDir.equals("")) {
                        newDirPath = saveDir + File.separator
                                + currentFileName;
                    }

                    if (aFile.isDirectory()) {
                        File newDir = new File(newDirPath);
                        boolean created = newDir.mkdirs();
                        if (created) {
                            System.out.println("CREATED the directory: " + newDirPath);
                        } else {
                            System.out.println("COULD NOT create the directory: " + newDirPath);
                        }
                        downloadDirectory(context, fragmentActivity, ftpClient, dirToList, currentFileName,
                                saveDir + File.separator + currentFileName, file);
                    } else {
                        boolean success = downloadSingleFile(ftpClient, filePath,
                                newDirPath);
                        if (success) {
                            System.out.println("DOWNLOADED the file: " + filePath);
                        } else {
                            System.out.println("COULD NOT download the file: "
                                    + filePath);
                        }
                    }
                }
            }
            if (!exist)
                fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Ne pare rău acest fișier audio nu există pe server", Toast.LENGTH_SHORT).show());
        }
    }


    private static FTPFile[] getDirectoryFiles(FTPClient ftp, String dirPath) throws IOException {
        String cwd = ftp.printWorkingDirectory();
        ftp.changeWorkingDirectory(dirPath);
        FTPFile[] files = ftp.listFiles();
        ftp.changeWorkingDirectory(cwd);
        return files;
    }

    /**
     * Download a single file from the FTP server
     *
     * @param ftpClient      an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param remoteFilePath path of the file on the server
     * @param savePath       path of directory where the file will be stored
     * @return true if the file was downloaded successfully, false otherwise
     * @throws IOException if any network or IO error occurred.
     */
    public static boolean downloadSingleFile(FTPClient ftpClient,
                                             String remoteFilePath, String savePath) throws IOException {
        File downloadFile = new File(savePath);
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    private static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                DeleteRecursive(child);
            }
        fileOrDirectory.delete();
    }
}
