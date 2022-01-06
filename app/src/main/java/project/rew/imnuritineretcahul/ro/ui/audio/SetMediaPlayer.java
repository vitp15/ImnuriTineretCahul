package project.rew.imnuritineretcahul.ro.ui.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.io.File;

import project.rew.imnuritineretcahul.R;

import static project.rew.imnuritineretcahul.ro.ui.home.Utils.hymns;

public class SetMediaPlayer {
    public static final MediaPlayer[] mediaPlayer=new MediaPlayer[hymns.size()];

    public static void setMediaPlayer(Context context){
        File internalDir = context.getDir(context.getString(R.string.ro_internal_mp3_folder), Context.MODE_PRIVATE);
        File[] dirFilesm = internalDir.listFiles();
        for (int j=0;j<hymns.size();j++) {
            for (File dirFale:dirFilesm){
                String[] audio = dirFale.getName().split("\\.");
                if (audio[0].equals(String.valueOf(hymns.get(j).getId())))
                    hymns.get(j).setMediaPlayer(MediaPlayer.create(context.getApplicationContext(), Uri.parse(dirFale.toURI().toString())));
                    mediaPlayer[j] = hymns.get(j).getMediaPlayer();
            }

        }
    }
}
