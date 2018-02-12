package models;

import java.io.Serializable;

/**
 * Created by welcome on 14-10-2016.
 */
public class CartData implements Serializable {

    String productID;
    String priceID;
    String name;
    String model;
    String image;
    String price;
    String quantity;
    String color;
    String mrp;
String cartID;
    public  String maxQty;

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPriceID() {
        return priceID;
    }

    public void setPriceID(String priceID) {
        this.priceID = priceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public CartData(String productID, String priceID, String name,
                    String model, String image, String price, String quantity,
                    String color,String thismrp,String ThiscartID,String Thisqty ) {


        this. productID=productID;
        this.  priceID=priceID;
        this.  name=name;
        this.  model=model;
        this.  image=image;
        this.  price=price;
        this.  quantity=quantity;
        this.  color=color;
        this.mrp=thismrp;
this.cartID=ThiscartID;
        this.maxQty=Thisqty;
       }
}
