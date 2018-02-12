package models;

import java.io.Serializable;

/**
 * Created by welcome on 17-08-2016.
 */
public class SearchModel implements Serializable {
    String productID;
    String name;
    String type;

    public SearchModel(String productID, String name, String type) {
        this.productID = productID;
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
