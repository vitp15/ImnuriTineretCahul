package project.rew.imnuritineretcahul.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {
    private static final String MY_PREFERENCE_NAME = "project.materiale.imnuritineret";
    private static final String PROGRESS_SAVE ="progress_save";
    public static void SaveSBprogress(Context context, int sb_progress) {
        SharedPreferences pref=context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt(PROGRESS_SAVE,sb_progress);
        editor.apply();
    }
    public static int load_saved_progress(Context context){
        SharedPreferences pref=context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        return pref.getInt(PROGRESS_SAVE,30);
    }
}
