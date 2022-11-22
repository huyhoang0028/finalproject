package vn.edu.greenwich.myapplication.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "data";
    private static final int DB_VERSION = 2;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(StaffTable.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(ExpenseTable.SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(StaffTable.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(ExpenseTable.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
