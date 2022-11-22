package vn.edu.greenwich.myapplication.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import vn.edu.greenwich.myapplication.model.Staff;

public class StaffTable {
    public static final String TABLE_NAME_STAFF = "staff";

    public static final String ID_COL = "id";
    public static final String NAME_COL = "name";
    public static final String TRIPNAME_COL = "tripName";
    public static final String DESTINATION_COL = "destination";
    public static final String DATE_COL = "date";
    public static final String RISK_ASSESSMENT_COL = "riskAssessment";
    public static final String DESCRIPTION_COL = "description";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STAFF + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME_COL + " TEXT,"
            + TRIPNAME_COL + " TEXT,"
            + DESTINATION_COL + " TEXT,"
            + DATE_COL + " TEXT,"
            + RISK_ASSESSMENT_COL + " TEXT,"
            + DESCRIPTION_COL + " TEXT)";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_STAFF;

}
