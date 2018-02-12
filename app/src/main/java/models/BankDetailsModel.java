package models;

import java.io.Serializable;

/**
 * Created by welcome on 23-08-2016.
 */
public class BankDetailsModel implements Serializable
{


    String name;
    String account_no;
    String branch;
    String ifsc_code;
    String branch_code;
    String account_type;
    String address;

    public BankDetailsModel(String name, String account_no, String branch, String ifsc_code, String branch_code, String account_type, String address) {



        this. name=name;
        this. account_no=account_no;
        this. branch=branch;
        this. ifsc_code=ifsc_code;
        this. branch_code=branch_code;
        this. account_type=account_type;
        this. address=address;
      }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
