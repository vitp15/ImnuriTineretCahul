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

public class Utils {

    public static void updateAudio(Context context, FragmentActivity fragmentActivity,boolean single) {
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

            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show());
            Utils.downloadDirectory(ftpClient, fragmentActivity.getString(R.string.ro_external_mp3_folder), "", internalDir.getAbsolutePath(),single);

            ftpClient.logout();
            ftpClient.disconnect();
            fragmentActivity.runOnUiThread(() -> Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show());
            File[] dirFiles = internalDir.listFiles();
            for (File dirFile:dirFiles) System.out.println(dirFile.getName().toString());
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
    public static void downloadDirectory(FTPClient ftpClient, String parentDir, String currentDir, String saveDir ,boolean single) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
        FTPFile[] subFiles = getDirectoryFiles(ftpClient, dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (single){
                        if (currentFileName.toString().equals("Pages.txt")){
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
                                    saveDir + File.separator + currentFileName, single);
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
                    }}
                    else{
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
                                        saveDir + File.separator + currentFileName, single);
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

    /**
     * @param nr         Numarul imnului
     * @param chordsFlag Flag daca sa caute cantarea cu acorduri
     * @param context    De unde se apeleaza metoda {@link #readContent(int, boolean, Context)}
     * @return Continutul cantarii din fisier local.
     */
    public static String readContent(int nr, boolean chordsFlag, Context context) {
        StringBuilder contentBuilder = new StringBuilder();
        String filename = "";

        try {
            File internalDir = context.getDir(context.getString(R.string.ro_internal_hymns_folder), Context.MODE_PRIVATE);
            File[] dirFiles = internalDir.listFiles();
            assert dirFiles != null;
            if (dirFiles.length != 0) {
                for (File dirFile : dirFiles) {
                    String[] hymn = dirFile.getName().split(" - ");
                    if (nr == Integer.parseInt(hymn[0])) {
                        filename = chordsFlag ?
                                dirFile.getAbsolutePath() + File.separator + "_" + nr + context.getString(R.string.settings_hymn_extension) :
                                dirFile.getAbsolutePath() + File.separator + nr + context.getString(R.string.settings_hymn_extension);
                    }
                }
            }
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return contentBuilder.toString();
    }

    private static void DeleteRecursive(File fileOrDirectory){
        if (fileOrDirectory.isDirectory())
            for (File child:fileOrDirectory.listFiles()){
                DeleteRecursive(child);
            }
        fileOrDirectory.delete();
    }
}
