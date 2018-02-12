package models;

import java.io.Serializable;

/**
 * Created by welcome on 24-10-2016.
 */
public class DashBoardNewProductData implements Serializable {

    String productID;
    String name;
    String image;
    String discount;
    String price;
    String mrp;
    String sold_out;

    public String getSold_out() {
        return sold_out;
    }

    public void setSold_out(String sold_out) {
        this.sold_out = sold_out;
    }

    public DashBoardNewProductData(String productID, String name, String image, String discount, String price, String mrp, String sold_out) {

        this. productID=productID;
        this. name=name;
        this. image=image;
        this. discount=discount;
        this. price=price;
        this. mrp=mrp;
        this. sold_out=sold_out;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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