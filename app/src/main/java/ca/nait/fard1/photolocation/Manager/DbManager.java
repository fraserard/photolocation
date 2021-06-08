package ca.nait.fard1.photolocation.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Creates database and manages transactions.
 */
public class DbManager extends SQLiteOpenHelper {

    static final String DB_NAME = "PhotoLocation.db";
    static final int DB_VERSION = 6;

    //TABLE - LOCATION
    static final String T_LOCATION = "Location";
    //COLUMNS - LOCATION
    static final String C_ID = BaseColumns._ID; //PK
    static final String C_LOCATION_NAME = "location_name";

    //TABLE - PHOTO
    static final String T_PHOTO = "Photo";
    //COLUMNS - PHOTO
    //static final String C_ID = BaseColumns._ID; //PK
    static final String C_LOCATION_ID = "location_id"; //FK
    static final String C_URI = "uri";
    static final String C_LAT = "latitude";
    static final String C_LON = "longitude";
    static final String C_FSTOP = "fstop";
    static final String C_EXPOSURE_TIME = "exposure_time";
    static final String C_ISO = "iso";
    static final String C_FOCAL_LENGTH = "focal_length";
    static final String C_MAKE = "make";
    static final String C_MODEL = "model";
    static final String C_DATE_TAKEN = "date_take";

    //Singleton
    private static DbManager instance;
    public static synchronized DbManager getInstance(Context context) {
        if (instance == null) {
            instance = new DbManager(context.getApplicationContext());
        }
        return instance;
    }

    private DbManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s integer primary key autoincrement, %s text) ",
                T_LOCATION, C_ID, C_LOCATION_NAME);
        db.execSQL(sql);

        sql = String.format("create table %s (%s integer primary key autoincrement, %s integer, %s text, %s real, %s text, %s numeric, %s text, %s numeric, %s text, %s text, %s text, %s text) ",
                                    T_PHOTO, C_ID, C_LOCATION_ID, C_URI, C_LAT, C_LON, C_FSTOP, C_EXPOSURE_TIME, C_ISO, C_FOCAL_LENGTH, C_MAKE, C_MODEL, C_DATE_TAKEN);
        db.execSQL(sql);

        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(DbManager.C_LOCATION_NAME, "Edmonton");
        db.insertOrThrow(DbManager.T_LOCATION, null, values);
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + T_PHOTO);
        db.execSQL("drop table if exists " + T_LOCATION);

        onCreate(db);
    }
}
