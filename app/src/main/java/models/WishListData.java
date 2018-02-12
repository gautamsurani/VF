package models;

import java.io.Serializable;

/**
 * Created by welcome on 18-10-2016.
 */
public class WishListData implements Serializable {
    String productID;
    String name;
    String image;
    String price;
    String mrp;

    public WishListData(String string, String string1, String string2, String string3, String string4) {
        this. productID=string;
        this.  name=string1;
        this.  image=string2;
        this.  price=string3;
        this.  mrp=string4;


    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
