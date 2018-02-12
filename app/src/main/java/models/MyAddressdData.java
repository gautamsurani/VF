package models;

import java.io.Serializable;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyAddressdData implements Serializable {

    String SR;
    String addID;

    String name;
    String phone;
    String address1;
    String address2;
    String area;
    String city;
    String state;
    String pincode;

    public MyAddressdData(String sr, String addID, String name,
                          String phone, String address1, String address2,
                          String area, String city, String state, String pincode) {

        this. SR=sr;
        this. addID=addID;

        this. name=name;
        this. phone=phone;
        this. address1=address1;
        this. address2=address2;
        this. area=area;
        this. city=city;
        this. state=state;
        this. pincode=pincode;

    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getAddID() {
        return addID;
    }

    public void setAddID(String addID) {
        this.addID = addID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
