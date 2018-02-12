package com.vedicfashions.shopping;

import java.util.List;

/**
 * Created by welcome on 09-04-2016.
 */
public class Api_Model {

    public String message;
    public String message1;
    public String super_wallet_info;

    public String cat1;
    public String cat2;
    public String cat3;
    public String cat4;
    public String cat5;
    public String cat6;


    public String cat1_id;
    public String cat2_id;
    public String cat3_id;
    public String cat4_id;
    public String cat5_id;
    public String cat6_id;


    public String walletBal;
    public String super_walletBal;
    public List<transction_data> transction_data;


    public String payu_wallet_fail;
    public String payu_wallet_success;
    public List<add_money_data> add_money_data;


    public String msgcode;
    public String offer_show;
    public String result;
    public String bank_trans;
    public String type;
    public String cod;
    public String Discount;
    public String disID;
    public String otp;
    public String pincode;
    public String super_wallet;
    public String Vedic_wallet;
    public String subtotal;
    public String discount;
    public String version;
    public String delivery;
    public String flag;
    public String email;
    public int product_count;
    public String msg;

    public String wallet;
    public String user_super_wallet;
    public String super_use_amount;
    public String super_wallet_msg;
    public String time;
    public String amount;
    public String shipping;
    public String item;
    public String status;
    public String exchange_amount;

    public String cart_count;
    public String total;

    public String image;
    public String text;
    public List<user_detail> user_detail;
    public List<detail> detail;
    public List<offer_list> offer_list;
    public List<sale_offer_list> sale_offer_list;
    public List<share_data> share_data;
    public List<about> about;
    public List<category_list> category_list;
    public List<image_list> image_list;
    public List<subcategory_list> subcategory_list;
    public List<category_list_new> category_list_new;

    public List<product_list> product_list;
    public List<customer_detail> customer_detail;
    public List<product> product;
    public List<address_list> address_list;
    public List<banners> banners;
    public List<new_product> new_product;
    public List<best_product> best_product;
    public List<offers> offers;
    public List<order_list> order_list;
    public List<related_product> related_product;
    public List<cart> cart;
    public List<item_list> item_list;
    public List<address> address;
    public List<search_list> search_list;

    public List<contact> contact;
    public List<list> list;
    public List<online_payment_data> online_payment_data;

    public List<bank_data> bank_data;
    public List<brands> brands;
    public List<msg_values> msg_values;

    public List<pincode_msg> pincode_msg;

    public List<fuser_detail> fuser_detail;

    public List<cat1_product> cat1_product;
    public List<cat2_product> cat2_product;
    public List<cat3_product> cat3_product;
    public List<cat4_product> cat4_product;
    public List<cat5_product> cat5_product;
    public List<cat6_product> cat6_product;


    public class user_detail {
        String ID;
        String phone;
        String otp;

    }

    public class share_data {
        String image;
        String message;
        String share_image;
        String ref_key;
        String you_get;
        String you_friend_get;
    }

    public class transction_data {
        String OrderID;
        String Remark;
        String symbol;
        String Amount;
        String type;
        String wallet_type;
        String TransactionDate;
    }


    public class add_money_data {
        String userID;
        String product_info;
        String amount;
        String first_name;
        String email;
        String trans_id;
        String payu_key;
        String payment_hash;
        String vas_for_mobile_sdk_hash;
        String payment_related_details_for_mobile_sdk_hash;
        String udf1;
        String udf2;
        String payu_wallet_fail;
        String payu_wallet_success;

    }

    public class about {
        String image;
        String text;
        String facebook_link;
        String google_link;
        String linkdin_link;
        String twitter_link;
        String insta_link;

    }

    public class offer_list {
        String offer_ID;
        String name;
        String buttonID;
        String subcat;
        String title;
        String image;
        String message;
        String added_on;
        String product_button;
        String cat_button;
        String shre_msg;
    }

    public class sale_offer_list {
        String offer_ID;
        String name;
        String buttonID;
        String subcat;
        String title;
        String image;
        String message;
        String added_on;
        String product_button;
        String cat_button;
        String shre_msg;
    }

    public class fuser_detail {
        String ID;
        String phone;
        String otp;

    }

    public class online_payment_data {
        String custID;
        String product_info;
        String amount;
        String first_name;
        String email;
        String trans_id;
        String udf1;
        String udf2;
        String payu_key;
        String payment_hash;
        String vas_for_mobile_sdk_hash;
        String payment_related_details_for_mobile_sdk_hash;
    }

    public class detail {
        String userID;
        String name;
        String email;
        String phone;
        String userimage;
    }

    public class category_list {
        String catID;
        String name;
        String icon;
        String subcat;
    }

    public class image_list {
        String sr;
        String image;
    }


    public class product_list {
        String productID;
        String name;
        String image;
        String discount;
        String price;
        String mrp;
        String sold_out;
        String qty_message;
    }

    public class msg_values {
        String type;
        String msg;
    }

    public class pincode_msg {
        String type;
        String msg;
    }

    public class customer_detail {
        String ID;
        String name;
        String email;
        String phone;
        String image;
    }


    public class list {
        String SR;
        String name;
    }

    public class subcategory_list {

        String catID;
        String name;

        List<subsubcategory_list> subsubcategory_list;

        public class subsubcategory_list {
            String Sr, agenda;
        }
    }

    public class category_list_new {

        public String catID;
        public String name;
        public String icon;
        public String subcat;
        public List<subcat_list> subcat_list;

        public class subcat_list {
            public String catID, name, icon;
        }
    }

    public class product {
        String productID;
        String name;
        String model;
        String sold_out;
        String description;
        String qty_message;

        List<image_list> image_list;

        class image_list {
            String sr;
            String small_image;
            String large_image;
        }

        List<price_list> price_list;

        class price_list {
            String sr;
            String priceID;
            String type;
            String price;
            String mrp;
            String colors;
            String color_option;
            String size_option;
            String discount;
        }
    }

    public class address_list {
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

    }

    public class order_list {
        String SR;
        String orderID;
        String date;
        String status;
        String amount;
    }

    public class bank_data {
        String name;
        String account_no;
        String branch;
        String ifsc_code;
        String branch_code;
        String account_type;
        String address;
    }


    public class banners {
        public String sr;
        public String image;
        public String catID;
        public String name;
        public String subcat;
    }

    public class new_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;
    }

    public class best_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;
    }

    public class offers {
        public String sr;
        public String image;
        public String catID;
        public String subcat;
        public String name;
    }

    public class brands {
        public String sr;
        public String image;
        public String brandID;
        public String name;
    }


    public class related_product {
        String productID;
        String name;
        String image;
        String discount;
        String price;
        String mrp;
        String sold_out;
    }

    public class cart {
        String cartID;
        String productID;
        String priceID;
        String name;
        String model;
        String image;
        String price;
        String quantity;
        String color;
        String mrp;
        String maxqty;
    }


    public class item_list {
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
    }

    public class address {
        String name;
        String email;
        String phone;
        String address;
    }

    public class search_list {
        public String productID;
        public String name;
        public String type;
    }

    public class contact {
        public String email;
        public String phone;
        public String website;
        public String call;
        public String address_1;
        public String message;
    }

    public class cat1_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;

    }

    public class cat2_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;

    }

    public class cat3_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;

    }

    public class cat4_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;

    }

    public class cat5_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;
    }

    public class cat6_product {
        public String productID;
        public String name;
        public String image;
        public String discount;
        public String price;
        public String mrp;
        public String sold_out;
    }
}
