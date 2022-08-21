package project.rew.imnuritineretcahul.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.items.hymns.Hymn;

public class Utils {

    public static List<Hymn> hymns_ro = new ArrayList<>();
    public static List<Hymn> hymns_ru = new ArrayList<>();
    public static List<String> savedHymnsRo = new ArrayList<>();
    public static List<String> savedHymnsRu = new ArrayList<>();
    public static List<String> dosentDownloadCorectly = new ArrayList<>();
    public static List<Hymn> savedHymns_Ro = new ArrayList<>();
    public static List<Hymn> savedHymns_Ru = new ArrayList<>();
    public static Language language;
    public static boolean isFirst = true;
    public static ImageView saved;
    public static TextView appBarTitle;
    public static String appBarTitleString;
    public static boolean audioList = false;
    public static TextView hymn_title_to_edit;
    public static ConstraintLayout constraintLayout;
    public static boolean ifDownloadBrokedChangeListItem = false;
    public static boolean needsToNotify = false;


    public static void deleteFile(Context context, String forDelete, Type type) {
        String folder = null;
        if (language == Language.RO) {
            if (type == Type.AUDIO)
                folder = context.getString(R.string.ro_internal_mp3_folder);
            else if (type == Type.PDF)
                folder = context.getString(R.string.ro_internal_pdf_folder);
            else if (type == Type.HYMN)
                folder = context.getString(R.string.ro_internal_hymns_folder);
        } else if (language == Language.RU) {
            if (type == Type.AUDIO)
                folder = context.getString(R.string.ru_internal_mp3_folder);
            else if (type == Type.PDF)
                folder = context.getString(R.string.ru_internal_pdf_folder);
            else if (type == Type.HYMN)
                folder = context.getString(R.string.ru_internal_hymns_folder);
        }
        File internalDir = context.getDir(folder, Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        for (File dirFile : dirFiles) {
            try {
                int count = 0;
                if (type == Type.AUDIO) {
                    String[] fileName = dirFile.getName().split("\\.");
                    String fileNametoDelete = fileName[0];
                    if (fileNametoDelete.equals(forDelete)) {
                        DeleteRecursive(dirFile);
                        count++;
                    }
                    if (count == 2) break;
                } else if (type != Type.AUDIO) {
                    String[] fileName = dirFile.getName().split("\\.");
                    String fileNametoDelete = "";
                    if (fileName.length > 0)
                        fileNametoDelete = fileName[0];
                    if (fileNametoDelete.equals(forDelete)) {
                        DeleteRecursive(dirFile);
                        break;
                    }
                }
                Utils.loadHymns(context);
            } catch (Exception e) {

            }
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

    public static boolean downloadSingleFile(Context context, FTPClient ftpClient,
                                             String remoteFilePath, String savePath,
                                             ProgressDialog progressDialog) throws IOException {
        File downloadFile = new File(savePath);
        if (dosentDownloadCorectly == null)
            dosentDownloadCorectly = new ArrayList<>();
        String savePatchToAdd;
        if (downloadFile.getName().split("\\.")[1].equals("txt"))
            savePatchToAdd = savePath.substring(0, savePath.length() - downloadFile.getName().length() - 1);
        else savePatchToAdd = savePath;
        dosentDownloadCorectly.add(savePatchToAdd);
        PrefConfig.saveNotDownloadCorectly(context, dosentDownloadCorectly);
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
                progressDialog.setProgress(progres);
                outputStream.write(data, 0, count);
            }
            dosentDownloadCorectly.remove(savePatchToAdd);
            PrefConfig.saveNotDownloadCorectly(context, dosentDownloadCorectly);
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


    public static void DeleteRecursive(File fileOrDirectory) {
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
        String color = context.getString(R.string.text_color_hc);
        String background = context.getString(R.string.background_hmn_canvas);
        try {
            File internalDir = null;
            if (language == Language.RO)
                internalDir = context.getDir(context.getString(R.string.ro_internal_hymns_folder), Context.MODE_PRIVATE);
            else if (language == Language.RU)
                internalDir = context.getDir(context.getString(R.string.ru_internal_hymns_folder), Context.MODE_PRIVATE);
            File[] dirFiles = internalDir.listFiles();
            assert dirFiles != null;
            if (dirFiles.length != 0) {
                for (File dirFile : dirFiles) {
                    String[] hymn = dirFile.getName().split(" - ");
                    String[] hymnIdAndCategory = hymn[0].split("\\.");
                    if (nr == Integer.parseInt(hymnIdAndCategory[0])) {
                        filename = chordsFlag ?
                                dirFile.getAbsolutePath() + File.separator + "_" + nr + context.getString(R.string.settings_hymn_extension) :
                                dirFile.getAbsolutePath() + File.separator + nr + context.getString(R.string.settings_hymn_extension);
                    }
                }
            }
            File file = new File(filename);
            if (!file.exists()) {
                if (language == Language.RO)
                    if (chordsFlag)
                        contentBuilder.append("<body background-color=\"" + background + "\" text=\"" + color + "\">" +
                                "<span style=\"font-family:"
                                + context.getResources().getString(R.string.hymn_font) + "\">" +
                                "<p>" + context.getString(R.string.chords_absent_ro) + "</p></span></body>");
                    else
                        contentBuilder.append("<body background-color=\"" + background + "\" text=\"" + color + "\">" +
                                "<span style=\"font-family:"
                                + context.getResources().getString(R.string.hymn_font) + "\">" +
                                "<p>" + context.getString(R.string.hymn_words_absent_ro) + "</p></span></body>");
                else if (language == Language.RU)
                    if (chordsFlag)
                        contentBuilder.append("<body background-color=\"" + background + "\" text=\"" + color + "\">" +
                                "<span style=\"font-family:"
                                + context.getResources().getString(R.string.hymn_font) + "\">" +
                                "<p>" + context.getString(R.string.chords_absent_ru) + "</p></span></body>");
                    else
                        contentBuilder.append("<body background-color=\"" + background + "\" text=\"" + color + "\">" +
                                "<span style=\"font-family:"
                                + context.getResources().getString(R.string.hymn_font) + "\">" +
                                "<p>" + context.getString(R.string.hymn_words_absent_ru) + "</p></sapn></body>");
            } else {
                BufferedReader in = new BufferedReader(new FileReader(filename));
                String str;
                while ((str = in.readLine()) != null) {
                    contentBuilder.append(str);
                }
                in.close();
            }
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return "<body background-color=\"" + background + "\" text=\"" + color + "\">" +
                "<span style=\"font-family:"
                + context.getResources().getString(R.string.hymn_font) + "\">" +
                contentBuilder.toString() + "</span></body>";
    }

    public static void loadHymns(@NotNull Context context) {
        File internalDir = null;
        if (language == Language.RO)
            internalDir = context.getDir(context.getString(R.string.ro_internal_hymns_folder), Context.MODE_PRIVATE);
        else if (language == Language.RU)
            internalDir = context.getDir(context.getString(R.string.ru_internal_hymns_folder), Context.MODE_PRIVATE);
        File[] dirFiles = internalDir.listFiles();
        assert dirFiles != null;
        Arrays.sort(dirFiles);
        if (language == Language.RO) {
            if (dirFiles.length != 0) {
                hymns_ro.clear();
                for (File dirFile : dirFiles) {
                    String[] hymn = dirFile.getName().split(" - ");
                    String[] id = hymn[0].split("\\.");

                    Hymn hymn_h = new Hymn(Integer.parseInt(id[0]), hymn[2]);
                    String[] categories = hymn[1].split("\\.");
                    List<String> categoryes = new ArrayList<>();
                    for (String s : categories)
                        categoryes.add(s);
                    hymn_h.setCategoryes(categoryes);
                    hymn_h.setSaved(false);

                    setAudio(context, hymn_h);
                    setPDF(context, hymn_h);

                    for (String s : savedHymnsRo) {
                        if (s.equals(id[0])) {
                            hymn_h.setSaved(true);
                            break;
                        }
                    }
                    hymns_ro.add(hymn_h);
                }
            }
            Collections.sort(hymns_ro, Hymn.HymnComparator);
            for (int i = 0; i < hymns_ro.size(); i++) {
                Hymn hymn = hymns_ro.get(i);
                hymn.setNr(i + 1);
            }
        } else if (language == Language.RU) {
            if (dirFiles.length != 0) {
                hymns_ru.clear();
                for (File dirFile : dirFiles) {
                    String[] hymn = dirFile.getName().split(" - ");
                    String[] id = hymn[0].split("\\.");
                    Hymn hymn_h = new Hymn(Integer.parseInt(id[0]), hymn[2]);
                    String[] categories = hymn[1].split("\\.");
                    List<String> categoryes = new ArrayList<>();
                    for (String s : categories)
                        categoryes.add(s);
                    hymn_h.setCategoryes(categoryes);
                    hymn_h.setSaved(false);
                    for (String s : savedHymnsRo) {
                        if (s.equals(id[0])) {
                            hymn_h.setSaved(true);
                            break;
                        }
                    }
                    hymns_ru.add(hymn_h);
                }
            }
            Collections.sort(hymns_ru, Hymn.HymnComparator);
            for (int i = 0; i < hymns_ru.size(); i++) {
                Hymn hymn = hymns_ru.get(i);
                hymn.setNr(i + 1);
            }
        }
    }

    public static void setAudio(Context context, Hymn hymn_h) {
        if (language == Language.RO) {
            File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (File dirFale : dirFilesm) {
                String[] audio = dirFale.getName().split("\\.");
                if (audio[0].equals(String.valueOf(hymn_h.getId())) && audio[2].equals("mp3")) {
                    hymn_h.setUriForMediaPlayer(dirFale.toURI().toString());
                }
                if (audio[0].equals(String.valueOf(hymn_h.getId())) &&
                        (audio[2].equals("png") || audio[2].equals("jpg") || audio[2].equals("jpeg"))) {
                    hymn_h.setUriForImgInAudio(dirFale.toURI().toString());
                }
                if (hymn_h.getUriForImgInAudio() != null && hymn_h.getUriForMediaPlayer() != null)
                    break;
            }
        } else if (language == Language.RU) {
            File internalDir = context.getDir(context.getString(R.string.ru_internal_mp3_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (File dirFale : dirFilesm) {
                String[] audio = dirFale.getName().split("\\.");
                if (audio[0].equals(String.valueOf(hymn_h.getId())) && audio[2].equals("mp3")) {
                    hymn_h.setUriForMediaPlayer(dirFale.toURI().toString());
                    break;
                } else if (audio[0].equals(String.valueOf(hymn_h.getId())) &&
                        (audio[2].equals("png") || audio[2].equals("jpg") || audio[2].equals("jpeg"))) {
                    hymn_h.setUriForImgInAudio(dirFale.toURI().toString());
                    break;
                }
            }
        }
    }

    public static void setPDF(Context context, Hymn hymn_h) {
        if (language == Language.RO) {
            File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (File dirFale : dirFilesm) {
                String[] pdf = dirFale.getName().split("\\.");
                if (pdf[0].equals(String.valueOf(hymn_h.getId()))) {
                    hymn_h.setPdfView(dirFale);
                    break;
                }
            }
        } else if (language == Language.RU) {
            File internalDir = context.getDir(context.getString(R.string.ru_internal_pdf_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (File dirFale : dirFilesm) {
                String[] pdf = dirFale.getName().split("\\.");
                if (pdf[0].equals(String.valueOf(hymn_h.getId()))) {
                    hymn_h.setPdfView(dirFale);
                    break;
                }
            }
        }
    }

    public static void loadHymnsSaved() {
        if (language == Language.RO) {
            savedHymns_Ro.clear();
            for (Hymn hymn : hymns_ro) {
                if (hymn.isSaved()) savedHymns_Ro.add(hymn);
            }
        } else if (language == Language.RU) {
            savedHymns_Ru.clear();
            for (Hymn hymn : hymns_ru) {
                if (hymn.isSaved()) savedHymns_Ru.add(hymn);
            }
        }
    }

    public static void addInSaved(Context context, String id) {
        if (language == Language.RO) {
            boolean exist = false;
            for (String s : savedHymnsRo)
                if (s.equals(id)) {
                    exist = true;
                    break;
                }
            if (!exist) {
                savedHymnsRo.add(id);
                PrefConfig.saveHymnsinPreferedRo(context, Utils.savedHymnsRo);
            }
        } else if (language == Language.RU) {
            boolean exist = false;
            for (String s : savedHymnsRu)
                if (s.equals(id)) {
                    exist = true;
                    break;
                }
            if (!exist) {
                savedHymnsRu.add(id);
                PrefConfig.saveHymnsinPreferedRu(context, Utils.savedHymnsRu);
            }
        }
    }

    public static void deleteFromSaved(Context context, String id) {
        if (language == Language.RO)
            for (String s : savedHymnsRo) {
                if (s.equals(id)) {
                    savedHymnsRo.remove(id);
                    PrefConfig.saveHymnsinPreferedRo(context, Utils.savedHymnsRo);
                    break;
                }
            }
        else if (language == Language.RU)
            for (String s : savedHymnsRu) {
                if (s.equals(id)) {
                    savedHymnsRu.remove(id);
                    PrefConfig.saveHymnsinPreferedRu(context, Utils.savedHymnsRu);
                    break;
                }
            }
    }
}
