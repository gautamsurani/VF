package models;

import java.io.Serializable;

/**
 * Created by welcome on 15-10-2016.
 */
public class CategoryData implements Serializable {



    String catID;
    String name;
    String icon;
    String subcat;

    public CategoryData(String catID, String name, String icon, String subcat) {
         this. catID=catID;
        this. name=name;
        this. icon=icon;
        this. subcat=subcat;


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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
