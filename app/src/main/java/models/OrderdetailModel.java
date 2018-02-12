package models;

import java.io.Serializable;

/**
 * Created by welcome on 23-08-2016.
 */
public class OrderdetailModel implements Serializable
{

    String SR;
    String name;
    String quantity;
    String price;
    String color;
    String type;
    String exchange_msg;
    String exchange;
    String image;
    String return_btn;
    String productID;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public OrderdetailModel(String SR, String name, String quantity, String price, String color, String type, String exchange_msg, String exchange, String image, String return_btn, String productID) {
        this.SR = SR;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.color = color;

        this.type = type;
        this.exchange_msg = exchange_msg;
        this.exchange = exchange;
        this.image = image;
        this.return_btn = return_btn;
        this.productID = productID;
    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getExchange_msg() {
        return exchange_msg;
    }

    public void setExchange_msg(String exchange_msg) {
        this.exchange_msg = exchange_msg;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReturn_btn() {
        return return_btn;
    }

    public void setReturn_btn(String return_btn) {
        this.return_btn = return_btn;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
