package project.rew.imnuritineretcahul.items.hymns;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;


public class Hymn {
    private int id, category;
    private int nr;
    private String title;
    private String UriForMediaPlayer;
    private String UriForImgInAudio;
    private File pdfView;
    private boolean saved;

    public Hymn(int id, String title) {
        byte[] bytes = title.getBytes(StandardCharsets.ISO_8859_1);
        String decoding_title = new String(bytes, StandardCharsets.UTF_8);
        this.id = id;
        this.title = decoding_title;
    }

    public File getPdfView() {
        return pdfView;
    }

    public void setPdfView(File pdfView) {
        this.pdfView = pdfView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getUriForMediaPlayer() {
        return UriForMediaPlayer;
    }

    public void setUriForMediaPlayer(String uriForMediaPlayer) {
        UriForMediaPlayer = uriForMediaPlayer;
    }

    public String getUriForImgInAudio() {
        return UriForImgInAudio;
    }

    public void setUriForImgInAudio(String uriForImgInAudio) {
        UriForImgInAudio = uriForImgInAudio;
    }

    public static final Comparator<Hymn> HymnComparator = new Comparator<Hymn>() {
        @Override
        public int compare(Hymn h1, Hymn h2) {
            return h1.getTitle().compareToIgnoreCase(h2.getTitle());
        }
    };

    @Override
    public String toString() {
        return nr + " " + title;
    }
}
