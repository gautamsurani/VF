package models;

import java.io.Serializable;

/**
 * Created by welcome on 15-12-2016.
 */
public class DashBoardBrandData implements Serializable {
    public    String sr;
    public   String image;
    public    String brandID;
    public    String name;

    public DashBoardBrandData(String sr, String image, String brandID, String name) {

             this. sr=sr;
        this.  image=image;
        this.  brandID=brandID;
        this.  name=name;
      }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
