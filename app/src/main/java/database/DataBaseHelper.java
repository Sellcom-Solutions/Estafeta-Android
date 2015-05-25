package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import database.models.Customer;
import database.models.CustomerWorkPlan;
import database.models.Evidence;
import database.models.EvidenceBrandProduct;
import database.models.EvidenceProduct;
import database.models.Module;
import database.models.Note;
import database.models.Order;
import database.models.OrderDetail;
import database.models.OrderStatus;
import database.models.PaymentType;
import database.models.People;
import database.models.Product;
import database.models.ProductBrand;
import database.models.ProductCategory;
import database.models.ProductPic;
import database.models.Receipt;
import database.models.Receiving;
import database.models.ReceivingItem;
import database.models.RecordActions;
import database.models.Sale;
import database.models.SaleItem;
import database.models.Supplier;
import database.models.User;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tracker.db";

    // NOTE: carefully update onUpgrade() when bumping database versions to make
    // sure user data is saved.

    private static final int VER_1            = 1;  // 1.1
    private static final int DATABASE_VERSION = VER_1;
    public static final int NOT_UPDATE        = -1;
    private final Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ User.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"username varchar(250) NOT NULL,"
                +"password varchar(250) NOT NULL,"
                +"profile_id integer NOT NULL,"
                +"people_id integer NOT NULL,"
                +"FOREIGN KEY (profile_id) REFERENCES profile (id),"
                +"FOREIGN KEY (people_id) REFERENCES profile (id))");

        db.execSQL("CREATE TABLE "+ Module.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY,"
                +"name varchar(250) NOT NULL)");

        db.execSQL("CREATE TABLE "+ Note.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"description text NOT NULL,"
                +"user_id integer NOT NULL,"
                +"timestamp varchar(250) NOT NULL,"
                +"FOREIGN KEY (user_id) REFERENCES user (id))");

        db.execSQL("CREATE TABLE "+ RecordActions.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"data text NOT NULL,"
                +"name_webservice varchar(250) NOT NULL,"
                +"user_id integer NOT NULL,"
                +"FOREIGN KEY (user_id) REFERENCES user (id))");

        db.execSQL("CREATE TABLE "+Product.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"barcode varchar(250),"
                +"name varchar(250) NOT NULL,"
                +"description text,"
                +"unit_price integer,"
                +"cost_price integer,"
                +"tax float,"
                +"stock integer,"
                +"stock_central integer,"
                +"product_category_id integer NOT NULL,"
                +"product_brand_id integer NOT NULL,"
                +"FOREIGN KEY (product_category_id) REFERENCES product_category (id),"
                +"FOREIGN KEY (product_brand_id) REFERENCES product_brand (id))");

        db.execSQL("CREATE TABLE "+ ProductBrand.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"name varchar(250) NOT NULL)");

        db.execSQL("CREATE TABLE "+ ProductCategory.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"name varchar(250) NOT NULL,"
                +"product_category_id integer,"
                +"FOREIGN KEY (product_category_id) REFERENCES product_category (id))");

        db.execSQL("CREATE TABLE "+ ProductPic.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"source text NOT NULL,"
                +"product_id integer NOT NULL,"
                +"main integer NOT NULL,"
                +"FOREIGN KEY (product_id) REFERENCES product (id))");

        db.execSQL("CREATE TABLE "+ Customer.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"account integer NOT NULL,"
                +"taxable integer NOT NULL,"
                +"people_id integer NOT NULL,"
                +"FOREIGN KEY (people_id) REFERENCES people (id))");

        db.execSQL("CREATE TABLE "+ PaymentType.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY,"
                +"name varchar(250) NOT NULL)");

        db.execSQL("CREATE TABLE "+ Receipt.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY,"
                +"image text NOT NULL)");

        db.execSQL("CREATE TABLE "+ People.TABLE +" (" +
                "    id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    first_name varchar(250) NOT NULL," +
                "    last_name varchar(250) NOT NULL," +
                "    email varchar(250) NOT NULL," +
                "    rfc varchar(250)," +
                "    phone_number varchar(250)," +
                "    address_1 varchar(250)," +
                "    address_2 varchar(250)," +
                "    city varchar(250)," +
                "    state varchar(250)," +
                "    country varchar(250)," +
                "    comments varchar(250)," +
                "    zip_code integer," +
                "    photo blob" +
                ")");

        db.execSQL("CREATE TABLE "+ Receiving.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"timestamp datetime DEFAULT (datetime('now','localtime')),"
                +"payment_type_id integer NOT NULL,"
                +"session_id integer NOT NULL,"
                +"supplier_id integer NOT NULL,"
                +"receipt_id integer NOT NULL,"
                +"FOREIGN KEY (payment_type_id) REFERENCES payment_type (id),"
                +"FOREIGN KEY (session_id) REFERENCES session (id),"
                +"FOREIGN KEY (supplier_id) REFERENCES supplier (id),"
                +"FOREIGN KEY (receipt_id) REFERENCES receipt (id))");

        db.execSQL("CREATE TABLE "+ ReceivingItem.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"receiving_id integer NOT NULL,"
                +"product_id integer NOT NULL,"
                +"quantity integer NOT NULL,"
                +"item_cost double,"
                +"FOREIGN KEY (receiving_id) REFERENCES receiving (id),"
                +"FOREIGN KEY (product_id) REFERENCES product (id))");

        db.execSQL("CREATE TABLE "+ Sale.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"timestamp datetime DEFAULT (datetime('now','localtime')),"
                +"payment_type_id integer NOT NULL,"
                +"session_id integer NOT NULL,"
                +"customer_id integer NOT NULL,"
                +"receipt_id integer NOT NULL,"
                +"FOREIGN KEY (payment_type_id) REFERENCES payment_type (id),"
                +"FOREIGN KEY (session_id) REFERENCES session (id),"
                +"FOREIGN KEY (customer_id) REFERENCES customer (id),"
                +"FOREIGN KEY (receipt_id) REFERENCES receipt (id))");

        db.execSQL("CREATE TABLE "+ SaleItem.TABLE +" (" +
                "id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "quantity integer NOT NULL," +
                "price_unit double NOT NULL," +
                "discount_percent float," +
                "sale_id integer NOT NULL," +
                "product_id integer NOT NULL," +
                "FOREIGN KEY (sale_id) REFERENCES sale (id)," +
                "FOREIGN KEY (product_id) REFERENCES product (id))");

        //Table of evidence module
        db.execSQL("CREATE TABLE "+ Evidence.TABLE +" (" +
                "id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(250) NOT NULL)");

        db.execSQL("CREATE TABLE "+ EvidenceBrandProduct.TABLE +" (" +
                "id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(250) NOT NULL," +
                "id_evidence integer NOT NULL," +
                "FOREIGN KEY (id_evidence) REFERENCES evidence (id))");


        db.execSQL("CREATE TABLE "+ EvidenceProduct.TABLE +" (" +
                "id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(250) NOT NULL," +
                "image_product varchar(250) NOT NULL," +
                "id_product_brand integer NOT NULL, " +
                "comment varchar(250) NOT NULL, " +
                "FOREIGN KEY (id_product_brand) REFERENCES evidence_brand_product (id))");

        //.....

        db.execSQL("CREATE TABLE "+ Supplier.TABLE +" (" +
                "id integer NOT NULL PRIMARY KEY," +
                "company_name varchar(250) NOT NULL," +
                "account integer NOT NULL," +
                "people_id integer NOT NULL," +
                "FOREIGN KEY (people_id) REFERENCES people (id))");

        db.execSQL("CREATE TABLE "+ Order.TABLE+" ("
                +"id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +"order_date datetime DEFAULT (datetime('now','localtime')),"
                +"required_date datetime NOT NULL,"
                +"shipped_date datetime NOT NULL,"
                +"customer_id integer NOT NULL,"
                +"session_id integer NOT NULL,"
                +"order_status_id integer NOT NULL,"
                +"receipt_id integer NOT NULL,"
                +"FOREIGN KEY (customer_id) REFERENCES customer (id),"
                +"FOREIGN KEY (order_status_id) REFERENCES order_status (id),"
                +"FOREIGN KEY (session_id) REFERENCES session (id),"
                +"FOREIGN KEY (receipt_id) REFERENCES receipt (id))");

        db.execSQL("CREATE TABLE "+ OrderDetail.TABLE +" (" +
                "    id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    order_id integer NOT NULL," +
                "    product_id integer NOT NULL," +
                "    unit_price float NOT NULL," +
                "    quantity integer NOT NULL," +
                "    discount float NOT NULL," +
                "    FOREIGN KEY (order_id) REFERENCES orders (id)," +
                "    FOREIGN KEY (product_id) REFERENCES product (id))");

        db.execSQL("CREATE TABLE "+ OrderStatus.TABLE +" (" +
                "    id integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    status varchar(250) NOT NULL)");

        /*
        * Para pruebas --------------------------------------------------------------------------
        * *
        db.execSQL("CREATE TABLE "+ CustomerWorkPlan.TABLE + " (" +
                "id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "customer_name varchar(250) NOT NULL,"+
                "cp integer NOT NULL,"+
                "country varchar(50) NOT NULL,"+
                "city varchar(50) NOT NULL,"+
                "state varchar(50) NOT NULL,"+
                "address varchar(250) NOT NULL,"+
                "workplan_date datetime NOT NULL,"+
                "openingTime varchar(50) NOT NULL,"+
                "closingTime varchar(50) NOT NULL)");

        ContentValues cv = new ContentValues();
        cv.put("customer", "COPAG, S.A. DE C.V.");
        cv.put("cp","56342");
        cv.put("country","MÉXICO");
        cv.put("city","HUIXQUILUCAN");
        cv.put("state","DF");
        cv.put("address","LÁZARO CÁRDENAS No. 2305 INT. F7 Y F8 COL. LAS TORRES");
        cv.put("workplan_date","2015-01-12");
        cv.put("openingTime","10:00:00");
        cv.put("closingTime","11:00:00");

        DataBaseAdapter.getDB(mContext).insert(CustomerWorkPlan.TABLE, null, cv);

        ContentValues cv1 = new ContentValues();
        cv1.put("customer", "RAGAZA, S.A. DE C.V.");
        cv1.put("cp","57362");
        cv1.put("country","MÉXICO");
        cv1.put("city","CUAJIMALPA");
        cv1.put("state","DF");
        cv1.put("address","AV. TABASCO No. 1082 COL. EL FARO");
        cv1.put("workplan_date","2015-01-13");
        cv1.put("openingTime","9:00:00");
        cv1.put("closingTime","12:00:00");

        DataBaseAdapter.getDB(mContext).insert(CustomerWorkPlan.TABLE, null, cv1);

        ContentValues cv2 = new ContentValues();
        cv2.put("customer", "COCA-COLA, S.A. DE C.V.");
        cv2.put("cp","63729");
        cv2.put("country","MÉXICO");
        cv2.put("city","CUERNAVACA");
        cv2.put("state","MORELOS");
        cv2.put("address","MIGUEL HIDALGO No. 6251 COL. CONSTITUCIÓN");
        cv2.put("workplan_date","2015-01-14");
        cv2.put("openingTime","8:00:00");
        cv2.put("closingTime","10:00:00");

        DataBaseAdapter.getDB(mContext).insert(CustomerWorkPlan.TABLE, null, cv2);

        ContentValues cv3 = new ContentValues();
        cv3.put("customer", "COCA-COLA, S.A. DE C.V.");
        cv3.put("cp","63729");
        cv3.put("country","MÉXICO");
        cv3.put("city","JALAPA");
        cv3.put("state","VERACRUZ");
        cv3.put("address","BANDERAS No. 827 COL. EL MIRADOR");
        cv3.put("workplan_date","2015-01-15");
        cv3.put("openingTime","7:00:00");
        cv3.put("closingTime","11:00:00");

        DataBaseAdapter.getDB(mContext).insert(CustomerWorkPlan.TABLE, null, cv3);

        /*
        *   ------------------------------------------------------------------------------------
        * */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // NOTE: This switch statement is designed to handle cascading database
        // updates, starting at the current version and falling through to all
        // future upgrade cases. Only use "break;" when you want to drop and
        // recreate the entire database.
        switch (oldVersion) {
            case VER_1:
                Log.i("DATABASE", "Database Ver " + oldVersion);
        }

        if (oldVersion != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Customer.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Module.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PaymentType.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + People.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ProductBrand.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ProductCategory.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ProductPic.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Receipt.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Receiving.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ReceivingItem.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RecordActions.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Sale.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SaleItem.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Supplier.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);

        /*
        * Para pruebas --------------------------------------------------------------------------
        * */
            db.execSQL("DROP TABLE IF EXISTS " + CustomerWorkPlan.TABLE);
        /*
        *            --------------------------------------------------------------------------
        * */

            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
