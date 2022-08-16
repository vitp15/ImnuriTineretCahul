package project.rew.imnuritineretcahul.items.categories;

import android.graphics.drawable.Drawable;

public class Category {
    String id, title;
    Drawable ImgRO, ImgRU;

    public Category(String id, String title, Drawable imgRO, Drawable imgRU) {
        this.id = id;
        this.title = title;
        ImgRO = imgRO;
        ImgRU = imgRU;
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

    public Drawable getImgRO() {
        return ImgRO;
    }

    public void setImgRO(Drawable imgRO) {
        ImgRO = imgRO;
    }

    public Drawable getImgRU() {
        return ImgRU;
    }

    public void setImgRU(Drawable imgRU) {
        ImgRU = imgRU;
    }
}
