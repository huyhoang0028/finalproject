package vn.edu.greenwich.myapplication.database;

public class ExpenseTable {

    public static final String TABLE_NAME_EXPENSE = "expense";

    public static final String ID_COL = "id";
    public static final String TYPE_OF_COST_COL = "typeOfCost";
    public static final String COST_COL = "cost";
    public static final String DATE_OF_COST_COL = "dateOfCost";
    public static final String TIME_OF_COST_COL = "timeOfCost";
    public static final String ADDITIONAL_COMMENT_COL = "additionalComment";
    public static final String STAFF_ID_COL = "staffID";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_EXPENSE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TYPE_OF_COST_COL + " TEXT NOT NULL," +
                    COST_COL + " TEXT NOT NULL," +
                    DATE_OF_COST_COL + " TEXT NOT NULL," +
                    TIME_OF_COST_COL + " INTEGER NOT NULL," +
                    ADDITIONAL_COMMENT_COL + " INTEGER NOT NULL," +
                    STAFF_ID_COL + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + STAFF_ID_COL + ") " +
                    "REFERENCES " + StaffTable.TABLE_NAME_STAFF + "(" + StaffTable.ID_COL + "))";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_EXPENSE;


}
