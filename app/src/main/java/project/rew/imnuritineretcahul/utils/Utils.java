package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;

public class Utils {

    public static List<Hymn> hymns_ro = new ArrayList<>();


    public static void deleteFile(Context context, String forDelete, String folder) {

        File internalDir = context.getDir(folder, Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        for (File dirFile : dirFiles) {
            if (dirFile.getName().equals(forDelete)) DeleteRecursive(dirFile);
        }
    }


    protected static FTPFile[] getDirectoryFiles(FTPClient ftp, String dirPath) throws IOException {
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
                                             String remoteFilePath, String savePath,
                                             ProgressDialog progressDialog, Type type) throws IOException {
        File downloadFile = new File(savePath);
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        InputStream input = new BufferedInputStream(ftpClient.retrieveFileStream(remoteFilePath));
        int count = 0;
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                UpdateFilesTask.total += count;
                int progres = (int) ((UpdateFilesTask.total * 100) / UpdateFilesTask.fileSize);
                if (type != Type.HYMN)
                    progressDialog.setProgress(progres);
                outputStream.write(data, 0, count);
            }
            return true;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            input.close();
        }
        return false;
    }


    protected static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                DeleteRecursive(child);
            }
        fileOrDirectory.delete();
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

    public static void loadHymns(@NotNull Context context, String internalFolder) {
        File internalDir = context.getDir(internalFolder, Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        assert dirFiles != null;
        Arrays.sort(dirFiles);
        if (dirFiles.length != 0) {
            hymns_ro.clear();
            for (File dirFile : dirFiles) {
                String[] hymn = dirFile.getName().split(" - ");
                hymns_ro.add(new Hymn(Integer.parseInt(hymn[0]), hymn[1]));
            }
        }
        Collections.sort(hymns_ro, Hymn.HymnComparator);
        for (int i = 0; i < hymns_ro.size(); i++) {
            Hymn hymn = hymns_ro.get(i);
            hymn.setNr(i + 1);
        }
    }

}
