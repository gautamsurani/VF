package models;

import java.io.Serializable;

/**
 * Created by welcome on 25-10-2016.
 */
public class DashBoardBannersData implements Serializable {
    String  image;

    public    String sr;
    public    String catID;
    public    String subcat;
    public    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DashBoardBannersData(String sr, String image, String catID, String subcat,String thisname) {

        this.sr=sr;
        this.image=image;
        this. catID=catID;
        this. subcat=subcat;
this.name=thisname;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
