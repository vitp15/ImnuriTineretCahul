package project.rew.imnuritineretcahul.items.categories;

import android.graphics.drawable.Drawable;

import java.util.List;

import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.items.hymns.Hymn;
import project.rew.imnuritineretcahul.utils.Utils;

public class Category {
    String id, title;
    Drawable Img;
    List<Hymn> hymns;

    public Category(String id, String title, Drawable img, List<Hymn> hymns) {
        this.id = id;
        this.title = title;
        Img = img;
        this.hymns = hymns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImg() {
        return Img;
    }

    public void setImg(Drawable img) {
        Img = img;
    }

    public List<Hymn> getHymns() {
        return hymns;
    }

    public void setHymns(List<Hymn> hymns) {
        this.hymns = hymns;
    }
}
