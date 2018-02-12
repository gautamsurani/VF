package models;

import java.io.Serializable;

/**
 * Created by welcome on 14-10-2016.
 */
public class StatelistModel implements Serializable {

    String SR;
    String name;

    public StatelistModel(String s) {
        name=s;
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
}
