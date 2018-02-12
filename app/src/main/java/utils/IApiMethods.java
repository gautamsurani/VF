package utils;


import com.vedicfashions.shopping.Api_Model;

import retrofit.http.GET;
import retrofit.http.Query;

/*
 * Created by welcome on 09-04-2016.
 */
public interface IApiMethods {
    @GET("/index.php")
    Api_Model CheckStatus(
            @Query("view") String view,
            @Query("userID") String userID,
            @Query("societyID") String societyID
    );

    @GET("/index.php")
    Api_Model GetDashBoardData(
            @Query("view") String view,
            @Query("custID") String custID,
            @Query("token") String token,
            @Query("deviceID") String deviceID
    );

    @GET("/index.php")
    Api_Model GetStartScreen(
            @Query("view") String view
    );

    @GET("/index.php")
    Api_Model GetAllNOtification(
            @Query("view") String key,
            @Query("userID") String userID
    );

    @GET("/index.php")
    Api_Model GetLogin(
            @Query("view") String view,
            @Query("page") String page,
            @Query("user") String user,
            @Query("pass") String pass
    );

    @GET("/index.php")
    Api_Model GetSocialLogin(
            @Query("view") String view,
            @Query("page") String page,
            @Query("user") String user,
            @Query("auth") String auth
    );

    @GET("/index.php")
    Api_Model GetSignUp(
            @Query("view") String view,
            @Query("sname") String sname,
            @Query("semail") String semail,
            @Query("spass") String spass,
            @Query("sphone") String sphone,
            @Query("referral_code") String referral_code
    );

    @GET("/index.php")
    Api_Model AfetrOtpVerify(
            @Query("view") String view,
            @Query("page") String page,
            @Query("get_mobile") String get_mobile,
            @Query("ID") String ID
    );

    @GET("/index.php")
    Api_Model ResendOTPApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("type") String type,
            @Query("get_mobile") String get_mobile
    );

    @GET("/index.php")
    Api_Model postRestPasswor(
            @Query("view") String view,
            @Query("page") String page,
            @Query("user") String user,
            @Query("ID") String ID,
            @Query("new_pass") String new_pass
    );

    @GET("/index.php")
    Api_Model AddHelp(
            @Query("view") String view,
            @Query("name") String name,
            @Query("email") String email,
            @Query("contact_no") String contact_no,
            @Query("message") String message
    );

    @GET("/index.php")
    Api_Model SellUs(
            @Query("view") String view,
            @Query("name") String name,
            @Query("cemail") String cemail,
            @Query("cname") String cname,
            @Query("phone") String phone,
            @Query("vat") String vat,
            @Query("pan") String pan,
            @Query("msg") String msg
    );

    @GET("/index.php")
    Api_Model referFriend(
            @Query("view") String view,
            @Query("userID") String userID
    );

    @GET("/index.php")
    Api_Model RferDetail(
            @Query("view") String view
    );

    @GET("/index.php")
    Api_Model postForgot(
            @Query("view") String view,
            @Query("page") String page,
            @Query("user") String user
    );

    @GET("/index.php")
    Api_Model GetCategoryAPI(
            @Query("view") String view
    );


    @GET("/index.php")
    Api_Model SubGetCategoryAPI(
            @Query("view") String view,
            @Query("catID") String catID
    );

    @GET("/index.php")
    Api_Model GetProductDetails(
            @Query("view") String view,
            @Query("productID") String productID,
            @Query("custID") String custID
    );


    @GET("/index.php")
    Api_Model GetProductListAPI(
            @Query("view") String view,
            @Query("catID") String catID,
            @Query("pagecode") String pagecode,
            @Query("type") String type
    );


    @GET("/index.php")
    Api_Model GetProductListAPIFromSearch(
            @Query("view") String view,
            @Query("search") String search,
            @Query("pagecode") String pagecode,
            @Query("type") String type
    );


    @GET("/index.php")
    Api_Model GetProductListAPIBrand(
            @Query("view") String view,
            @Query("brandID") String brandID,
            @Query("pagecode") String pagecode,
            @Query("type") String type
    );


    @GET("/index.php")
    Api_Model AccountDeactiveAPI(
            @Query("view") String view,
            @Query("catID") String catID,
            @Query("pagecode") String pagecode
    );

    @GET("/index.php")
    Api_Model GEtAddressAPI(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID
    );

    @GET("/index.php")
    Api_Model GEtContactUsAPI(
            @Query("view") String view

    );

    @GET("/index.php")
    Api_Model GEtMyOrdersAPI(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,

            @Query("pagecode") String pagecode

    );

    @GET("/index.php")
    Api_Model GetBankDetails(
            @Query("view") String view

    );

    @GET("/index.php")
    Api_Model AddAdrressGEt(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,
            @Query("cname") String cname,
            @Query("cphone") String cphone,
            @Query("caddress1") String caddress1,
            @Query("caddress2") String caddress2,
            @Query("carea") String carea,
            @Query("ccity") String ccity,
            @Query("cstate") String cstate,
            @Query("cpincode") String cpincode,
            @Query("caddressID") String caddressID

    );

    @GET("/index.php")
    Api_Model GetStateAPI(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID


    );

    @GET("/index.php")
    Api_Model DeteAddressAPI(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,
            @Query("caddressID") String caddressID


    );


    @GET("/index.php")
    Api_Model AccountDeactiveAPI(
            @Query("view") String view,
            @Query("custID") String custID


    );

    @GET("/index.php")
    Api_Model GetRelatedProduct(
            @Query("view") String view,
            @Query("catID") String catID

    );

    @GET("/index.php")
    Api_Model ChangePIncodeAsyntask(
            @Query("view") String view,
            @Query("post") String post,
            @Query("productID") String productID,

            @Query("custID") String custID

    );

    @GET("/index.php")
    Api_Model ChangeEmailAsyntask(
            @Query("view") String view,
            @Query("custID") String custID,
            @Query("email") String email

    );

    @GET("/index.php")
    Api_Model AddtoCartApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,
            @Query("productID") String productID,
            @Query("priceID") String priceID,
            @Query("productQuantity") String productQuantity,
            @Query("productColor") String productColor

    );

    @GET("/index.php")
    Api_Model GetCartDataApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID

    );


    @GET("/index.php")
    Api_Model RemoveCartApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,

            @Query("productID") String productID,
            @Query("priceID") String priceID,
            @Query("cartID") String cartID


    );

    @GET("/index.php")
    Api_Model cHANGEqUENTITYCartApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,

            @Query("productID") String productID,
            @Query("priceID") String priceID,
            @Query("productQuantity") String productQuantity,
            @Query("cartID") String cartID


    );

    @GET("/index.php")
    Api_Model getOrderDetailsApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,
            @Query("orderID") String orderID
    );

    @GET("/index.php")
    Api_Model OrderShippingApi(
            @Query("view") String view,
            @Query("page") String page,
            @Query("custID") String custID,
            @Query("addressID") String addressID,
            @Query("items") String items,
            @Query("subtotal") String subtotal
    );

    @GET("/index.php")
    Api_Model GetPaymentDetails(
            @Query("view") String view,
            @Query("custID") String custID,
            @Query("addressID") String addressID,
            @Query("items") String items,
            @Query("subtotal") String subtotal,
            @Query("shipping") String shipping,
            @Query("payment") String payment,
            @Query("disID") String disID,
            @Query("disValue") String disValue,
            @Query("wallet_used") String wallet_used,
            @Query("wallet_use_value") String wallet_use_value,
            @Query("super_wallet_use_value") String super_wallet_use_value,
            @Query("cart_total") String cart_total
    );

    @GET("/index.php")
    Api_Model GetSearchDataApi(
            @Query("view") String view
    );

    @GET("/index.php")
    Api_Model GetBootomData(
            @Query("view") String view,
            @Query("custID") String custID
    );

    @GET("/index.php")
    Api_Model GetSearchDataApi(
            @Query("view") String view,
            @Query("coupon_code") String coupon_code,
            @Query("subtotal") String subtotal,
            @Query("custID") String custID
    );

    @GET("/index.php")
    Api_Model getWalletHistory(
            @Query("view") String key,
            @Query("userID") String userID,
            @Query("userPhone") String userPhone,
            @Query("pagecode") String pagecode
    );

    @GET("/index.php")
    Api_Model getNewCategory(
            @Query("view") String key
    );

    @GET("/index.php")
    Api_Model AddWalletMoney(
            @Query("view") String view,
            @Query("page") String page,
            @Query("userID") String userID,
            @Query("userPhone") String userPhone,
            @Query("amount") String amount,
            @Query("type") String type
    );
}