package project.rew.imnuritineretcahul.items.note_pdf;

import android.content.Context;

import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.utils.Utils;


public class SetPDF {

    public static void setPDF(Context context) {
        if (Utils.language==Language.RO) {
            File internalDir = context.getDir(context.getString(R.string.ro_internal_pdf_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (int j = 0; j < Utils.hymns_ro.size(); j++) {
                for (File dirFale : dirFilesm) {
                    String[] pdf = dirFale.getName().split("\\.");
                    if (pdf[0].equals(String.valueOf(Utils.hymns_ro.get(j).getId())))
                        Utils.hymns_ro.get(j).setPdfView(dirFale);
                }

            }
        }
        else if (Utils.language==Language.RU){
            File internalDir = context.getDir(context.getString(R.string.ru_internal_pdf_folder), Context.MODE_PRIVATE);
            File[] dirFilesm = internalDir.listFiles();
            for (int j = 0; j < Utils.hymns_ru.size(); j++) {
                for (File dirFale : dirFilesm) {
                    String[] pdf = dirFale.getName().split("\\.");
                    if (pdf[0].equals(String.valueOf(Utils.hymns_ru.get(j).getId())))
                        Utils.hymns_ru.get(j).setPdfView(dirFale);
                }

            }
        }
    }
}
