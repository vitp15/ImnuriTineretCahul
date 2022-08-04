package project.rew.imnuritineretcahul.items.audio;

import java.util.ArrayList;
import java.util.List;

import project.rew.imnuritineretcahul.items.hymns.Hymn;

public class HymnsAudioRealTime {

    public static List<Hymn> curentList = new ArrayList<>();
    public static int position;

    public static void setCurentPosition(int position, List<Hymn> curentList, Hymn hymn) {
        HymnsAudioRealTime.curentList = curentList;
        if (curentList.get(position) == hymn)
            HymnsAudioRealTime.position = position;
        else {
            for (int i = 0; i < curentList.size(); i++) {
                if (curentList.get(i) == hymn) {
                    HymnsAudioRealTime.position = i;
                    break;
                }
            }
        }
    }

    public static void goToNextSong() {
        if (position == curentList.size() - 1) position = 0;
        else
            position++;
        if (curentList.get(position).getUriForMediaPlayer() == null)
            goToNextSong();
    }

    public static void goToPreviousSong() {
        if (position == 0) position = curentList.size() - 1;
        else
            position--;
        if (curentList.get(position).getUriForMediaPlayer() == null)
            goToPreviousSong();
    }

    public static Hymn getHymnToPlay() {
        return curentList.get(position);
    }
}
