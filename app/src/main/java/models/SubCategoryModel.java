package models;

/**
 * Created by welcome on 17-08-2016.
 */
public class SubCategoryModel
{

    // For Slider Image
    String strSliderImage = "";

    public String getStrSliderImage() {
        return strSliderImage;
    }

    public void setStrSliderImage(String strSliderImage) {
        this.strSliderImage = strSliderImage;
    }



    // For Header..
    String catId = "";
    String catName = "";

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }



    // For Child..

    String childcatId = "";
    String childcatName = "";


    public String getChildcatId() {
        return childcatId;
    }

    public void setChildcatId(String childcatId) {
        this.childcatId = childcatId;
    }

    public String getChildcatName() {
        return childcatName;
    }

    public void setChildcatName(String childcatName) {
        this.childcatName = childcatName;
    }



}
