package project.rew.imnuritineretcahul.items.categories;

import android.graphics.drawable.Drawable;

import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.utils.Utils;

public class Category {
    String id, titleRO, titleRU;
    Drawable ImgRO, ImgRU;

    public Category(String id, String titleRO,String titleRU, Drawable imgRO, Drawable imgRU) {
        this.id = id;
        this.titleRO = titleRO;
        this.titleRU = titleRU;
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
        if (Utils.language == Language.RO)
            return titleRO;
        else if (Utils.language == Language.RU)
            return titleRU;
        return "";
    }

    public void setTitleRO(String title) {
        this.titleRO = title;
    }

    public void setTitleRU(String title) {
        this.titleRU = title;
    }

    public Drawable getImg() {
        if (Utils.language == Language.RO)
            return ImgRO;
        else if (Utils.language == Language.RU)
            return ImgRU;
        return ImgRO;
    }

    public void setImgRO(Drawable imgRO) {
        ImgRO = imgRO;
    }

    public void setImgRU(Drawable imgRU) {
        ImgRU = imgRU;
    }
}
