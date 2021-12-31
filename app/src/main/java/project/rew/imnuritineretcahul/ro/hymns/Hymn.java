package project.rew.imnuritineretcahul.ro.hymns;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class Hymn {
    private int id;
    private int nr;
    private String title;

    public Hymn(int id, String title) {
        byte[] bytes = title.getBytes(StandardCharsets.ISO_8859_1);
        String decoding_title=new String(bytes, StandardCharsets.UTF_8);
        this.id = id;
        this.title = decoding_title;
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

    public static final Comparator<Hymn> HymnComparator = new Comparator<Hymn>() {
        @Override
        public int compare(Hymn h1, Hymn h2) {
            return h1.getTitle().compareToIgnoreCase(h2.getTitle());
        }
    };

    @Override
    public String toString() {
        return nr+" "+title;
    }
}
