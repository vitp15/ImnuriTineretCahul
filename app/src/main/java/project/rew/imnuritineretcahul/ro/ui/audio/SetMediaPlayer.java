package project.rew.imnuritineretcahul.ro.ui.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import java.io.File;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.utils.Utils;

public class SetMediaPlayer {
    public static final MediaPlayer[] mediaPlayer=new MediaPlayer[Utils.hymns_ro.size()];

    public static void setMediaPlayer(Context context){
        File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);
        File[] dirFilesm = internalDir.listFiles();
        for (int j = 0; j< Utils.hymns_ro.size(); j++) {
            for (File dirFale:dirFilesm){
                String[] audio = dirFale.getName().split("\\.");
                if (audio[0].equals(String.valueOf(Utils.hymns_ro.get(j).getId())))
                    Utils.hymns_ro.get(j).setMediaPlayer(MediaPlayer.create(context.getApplicationContext(), Uri.parse(dirFale.toURI().toString())));
                    mediaPlayer[j] = Utils.hymns_ro.get(j).getMediaPlayer();
            }

        }
    }
}
