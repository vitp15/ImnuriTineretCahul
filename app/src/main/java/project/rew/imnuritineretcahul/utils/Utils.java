package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.ro.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.NetworkUtils;

public class Utils {

    public static List<Hymn> hymns = new ArrayList<>();

    public static void deleteFile(Context context, String forDelete) {
        File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
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
                                             ProgressDialog progressDialog, long fileSize) throws IOException {
        File downloadFile = new File(savePath);
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        InputStream input = new BufferedInputStream(ftpClient.retrieveFileStream(remoteFilePath));
        int count;
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            byte data[] = new byte[1024];
            long total = 0;
            progressDialog.setMessage("Se descarcă");
            while ((count = input.read(data)) != -1) {
                total += count;
                progressDialog.setProgress('"' + (int) ((total * 100) / fileSize));
                outputStream.write(data, 0, count);
            }
            System.out.println("Downalded the file: " + savePath);
            outputStream.close();
            input.close();
            progressDialog.setMessage("Finising...");
            return true;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
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

}
