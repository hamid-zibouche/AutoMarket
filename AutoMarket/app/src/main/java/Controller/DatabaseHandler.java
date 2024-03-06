package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import Model.USER;
import Utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler( Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Utils.TABLE_NAME
                + "(" +Utils.KEY_ID +"INTEGER PRIMARY KEY," +
               Utils.KEY_USERNAME +"TEXT," + Utils.KEY_EMAIL +"STRING," +
                Utils.KEY_PHONE +"INTEGER," +Utils.KEY_PASSWORD +"STRING" + ")";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS " + Utils.DATABASE_NAME);
onCreate(db);
    }

    public void addUSER(USER user){
     SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_USERNAME, user.getUsername());
        contentValues.put(Utils.KEY_EMAIL, user.getEmail());
        contentValues.put(Utils.KEY_PASSWORD, user.getPassword());
        contentValues.put(Utils.KEY_PHONE, user.getPhone());


        database.insert(Utils.TABLE_NAME , null, contentValues);
        database.close();
    }



}




