package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elliot on 21/10/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME= "ads.db";
    public static final String TABLE_NAME= "user_crednetials.db";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_crednetials (id INTEGER PRIMARY KEY AUTO_INCREMENT,email TEXT , passcode TEXT, company_id TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_crednetials (id INTEGER AUTO_INCREMENT,email TEXT , passcode TEXT, company_id TEXT);");
    }
}
