package project.rew.imnuritineretcahul.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefConfig {
    private static final String MY_PREFERENCE_NAME = "project.materiale.imnuritineret";
    private static final String PROGRESS_SAVE = "progress_save";
    private static final String PREFERED_HYMNS_SAVE_RO = "prefered_hymns_save_ro";
    private static final String PREFERED_HYMNS_SAVE_RU = "prefered_hymns_save_ru";
    private static final String LANGUAGE_SAVE = "language_save";
    private static final String NOT_DOWNLOAD_CORECLY = "not_download_corecly";

    public static void SaveSBprogress(Context context, int sb_progress) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PROGRESS_SAVE, sb_progress);
        editor.apply();
    }

    public static int load_saved_progress(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PROGRESS_SAVE, 30);
    }

    public static void SaveLanguage(Context context, String language) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LANGUAGE_SAVE, language);
        editor.apply();
    }

    public static String load_saved_language(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getString(LANGUAGE_SAVE, "RO");
    }

    public static void saveHymnsinPreferedRo(Context context, List<String> ids) {
        Gson ids_gson = new Gson();
        String ids_string = ids_gson.toJson(ids);
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREFERED_HYMNS_SAVE_RO, ids_string);
        editor.apply();
    }

    public static List<String> load_saved_list_of_hymns_ro(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String jsonString = pref.getString(PREFERED_HYMNS_SAVE_RO, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> list = gson.fromJson(jsonString, type);
        return list;
    }

    public static void saveHymnsinPreferedRu(Context context, List<String> ids) {
        Gson ids_gson = new Gson();
        String ids_string = ids_gson.toJson(ids);
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREFERED_HYMNS_SAVE_RU, ids_string);
        editor.apply();
    }

    public static List<String> load_saved_list_of_hymns_ru(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String jsonString = pref.getString(PREFERED_HYMNS_SAVE_RU, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> list = gson.fromJson(jsonString, type);
        return list;
    }

    public static void saveNotDownloadCorectly(Context context, List<String> notDownloadCorectly) {
        try {
            Gson ids_gson = new Gson();
            String ids_string = ids_gson.toJson(notDownloadCorectly);
            SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(NOT_DOWNLOAD_CORECLY, ids_string);
            editor.apply();
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static List<String> load_not_download_corectly(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String jsonString = pref.getString(NOT_DOWNLOAD_CORECLY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> list = gson.fromJson(jsonString, type);
        return list;
    }
}
