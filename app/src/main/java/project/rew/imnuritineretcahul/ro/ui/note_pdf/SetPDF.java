package project.rew.imnuritineretcahul.ro.ui.note_pdf;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import project.rew.imnuritineretcahul.R;

import static project.rew.imnuritineretcahul.ro.ui.home.Utils.hymns;

public class SetPDF {
    public static void setPDF(Context context){
        File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
        File[] dirFilesm = internalDir.listFiles();
        for (int j=0;j<hymns.size();j++) {
            for (File dirFale:dirFilesm){
                String[] pdf = dirFale.getName().split("\\.");
                if (pdf[0].equals(String.valueOf(hymns.get(j).getId())))
                    hymns.get(j).setPdfView(dirFale);
            }

        }
    }
}
