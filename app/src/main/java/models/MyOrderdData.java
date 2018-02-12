package models;

import java.io.Serializable;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyOrderdData implements Serializable {

    String SR;
    String orderID;
    String date;
    String status;
    String amount;

    public MyOrderdData(String sr, String orderID, String date, String status, String amount) {

        this. SR=sr;
        this. orderID=orderID;
        this. date=date;
        this. status=status;
        this. amount=amount;

    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
