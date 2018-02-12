package dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by welcome on 12-10-2016.
 */
public class LocalVedicDB extends SQLiteOpenHelper {
    Context con;
    LocalVedicDB mLocalVedicDB;

    static String DB_DBNAME;

    final static String DB_TABLE_WISHLIST = "wish_list";

    final static String WISHLIST_productID = "productID", WISHLIST_name = "name", WISHLIST_image = "image", WISHLIST_price = "price", WISHLIST_mrp = "mrp";

    final static int DB_VERSION = 1;
    SQLiteDatabase sql;


    String StrQueryWishList = "create table " + DB_TABLE_WISHLIST + "(id INTEGER PRIMARY KEY AUTOINCREMENT,productID string" + ",name string,image string,price string,mrp string)";

    public LocalVedicDB(Context context, String localDbUserSocityIdName) {
        super(context, localDbUserSocityIdName, null, DB_VERSION);
        con = context;
        DB_DBNAME = localDbUserSocityIdName;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(StrQueryWishList);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + DB_TABLE_WISHLIST);

        onCreate(db);
    }

    public void OpenDB() {
        mLocalVedicDB = new LocalVedicDB(con, DB_DBNAME);

    }


    public void CloseDB() {
        sql.close();
    }

    public void InsertWishListData(String productID, String name, String image, String price, String mrp) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WISHLIST_productID, productID);
        cv.put(WISHLIST_name, name);
        cv.put(WISHLIST_image, image);
        cv.put(WISHLIST_price, price);
        cv.put(WISHLIST_mrp, mrp);
        sql.insert(DB_TABLE_WISHLIST, null, cv);
    }

    public Cursor ShowTableWishList() {
        sql = this.getReadableDatabase();
        String w[] = new String[]{"id", WISHLIST_productID, WISHLIST_name, WISHLIST_image, WISHLIST_price, WISHLIST_mrp};
        Cursor cn = sql.query(DB_TABLE_WISHLIST, w, null, null, null, null, null);
        return cn;
    }

    public void DeleteWishListByID(String productID) {
        try {

            sql = this.getReadableDatabase();
            sql.delete(DB_TABLE_WISHLIST, WISHLIST_productID + "='" + productID + "'", null);
        } catch (Exception e) {

        }
    }


/*
    public Cursor ShowTableNOTICEBoardPagination(int Pgae) {

        sql = this.getReadableDatabase();

        String w[] = new String[]{"id", NOTICE_ID, NOTICE_TITLE, NOTICE__IMAGE, NOTICE__DESCRIPTION
                , NOTICE_TIME, NOTICE_IMAGENAME, NOTICE_READ_UNREAD, NOTICE_TYPE, NOTICE_NUMBER, NOTICE_STATUS};
        Cursor cn = sql.query(DB_TABLE_NOTICEBOARD, w, null, null, null, null, "id" + " DESC", "" + Pgae);
        return cn;
    }




    public void UpdateAllNoticeData(String id, String title, String image, String desc, String time, String image_name,
                                    String type, String status, String number) {
        sql = this.getWritableDatabase();

        try {
            String strSQL = "UPDATE " + DB_TABLE_NOTICEBOARD +
                    " SET notice_title = '" + title + "' , " +
                    "      notice_image = '" + image + "' ," +
                    "       notice_description = '" + desc + "' ," +
                    "      notice_time = '" + time + "' ," +
                    "      notice_imageName = '" + image_name + "' ," +
                    "      notice_type = '" + type + "' ," +
                    "      notice_number = '" + number + "' ," +
                    "      notice_status = '" + status + "'     " +
                    "  WHERE notice_id = '" + id + "'";


            sql.execSQL(strSQL);
        } catch (Exception e) {
            Log.e("asd", e.getMessage());
        }
    }

    public void UpdateReadUnReadeNoticeBoard(String noticeId, String read) {
        sql = this.getWritableDatabase();

        try {
            String strSQL = "UPDATE " + DB_TABLE_NOTICEBOARD + " SET notice_read_unread = '" + read + "' WHERE notice_id = '" + noticeId + "'";

            sql.execSQL(strSQL);
        } catch (Exception e) {
            Log.e("asd", e.getMessage());
        }
    }

    public String GetReadCommandNoticeBoard(String noticeIdThis) {
        String strReadUnreadMAKe = "";
        try {
            String queryLablke = "SELECT * FROM " + DB_TABLE_NOTICEBOARD + " WHERE notice_id =" + noticeIdThis + "";
            Cursor cursor = sql.rawQuery(queryLablke, null);
            cursor.moveToFirst();
            strReadUnreadMAKe = cursor.getString(7);

        } catch (Exception e) {
            Utils.showToastShort(e.getMessage(), (Activity) con);

            return null;
        }

        return strReadUnreadMAKe;
    }
*/


}
