package vn.edu.greenwich.myapplication.database;


import static vn.edu.greenwich.myapplication.database.ExpenseTable.TABLE_NAME_EXPENSE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;

public class StaffDao {
    Context context;
    protected DbHelper db;
    protected SQLiteDatabase dbWrite, dbRead;


    public StaffDao() {
    }

    public StaffDao(Context context){
        db = new DbHelper(context);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();
    }

    public void close(){
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        db.onUpgrade(dbWrite, 0, 0);
    }



    public ContentValues getStaffValues(Staff staff){
        ContentValues values = new ContentValues();

        values.put(StaffTable.NAME_COL, staff.getName());
        values.put(StaffTable.TRIPNAME_COL, staff.getBusiness_trip_name());
        values.put(StaffTable.DESTINATION_COL, staff.getDestination());
        values.put(StaffTable.DATE_COL, staff.getDate());
        values.put(StaffTable.RISK_ASSESSMENT_COL, staff.getRiskAssessment());
        values.put(StaffTable.DESCRIPTION_COL, staff.getDescription());
        values.put(StaffTable.NAME_COL, staff.getName());
        values.put(StaffTable.NAME_COL, staff.getName());

        return values;
    }




    public long addStaff(Staff staff) {
        ContentValues values = getStaffValues(staff);

        return dbWrite.insert(StaffTable.TABLE_NAME_STAFF, null, values);
    }

    public long updateStaff(Staff staff){
        ContentValues values = getStaffValues(staff);

        String selection = StaffTable.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(staff.getId())};

        long result = dbWrite.update(StaffTable.TABLE_NAME_STAFF, values, selection, selectionArgs);
        if(result == -1 ){
            Toast.makeText(context,"Insert data failure",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Insert data successfully",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public long deleteOneRow(String id){

        String selection = StaffTable.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        long result =  dbWrite.delete(StaffTable.TABLE_NAME_STAFF, selection, selectionArgs);
        if(result == -1 ){
            Toast.makeText(context,"Insert data failure",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Insert data successfully",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public long deleteAllDate(){
        long result = dbWrite.delete(StaffTable.TABLE_NAME_STAFF, null, null);
        return result;
    }


    public Cursor readAllData(){
        String query = "SELECT * FROM " + StaffTable.TABLE_NAME_STAFF;
        SQLiteDatabase db = this.dbWrite;

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }


    public ArrayList<Staff> getStaffList(Staff trip, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (trip.getName() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + StaffTable.NAME_COL + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }

            if (trip.getDate() != null && !trip.getDate().trim().isEmpty()) {
                selection += " AND " + StaffTable.DATE_COL + " = ?";
                conditionList.add(trip.getDate());
            }


            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getStaffFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    protected ArrayList<Staff> getStaffFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Staff> list = new ArrayList<>();

            Cursor cursor = dbRead.query(StaffTable.TABLE_NAME_STAFF, columns, selection, selectionArgs, groupBy, having, orderBy);

            while (cursor.moveToNext()) {
                Staff StaffItem = new Staff();
                StaffItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StaffTable.ID_COL)));
                StaffItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.NAME_COL)));
                StaffItem.setBusiness_trip_name(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.TRIPNAME_COL)));
                StaffItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.DESTINATION_COL)));
                StaffItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.DESCRIPTION_COL)));
                StaffItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.DATE_COL)));
                StaffItem.setRiskAssessment(cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.RISK_ASSESSMENT_COL)));
                list.add(StaffItem);
            }
            cursor.close();

        return list;
    }


    //--------------------------------------------------------------


    public Staff getStaffById(long id) {
        String selection = StaffTable.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getStaffFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }





    public Cursor readAllDataExpenseById(int id ){
        String query = "SELECT * FROM " + ExpenseTable.TABLE_NAME_EXPENSE + " WHERE " + ExpenseTable.STAFF_ID_COL + "="+ id;
        Log.e("asdasd",String.valueOf(id));
        SQLiteDatabase db = this.dbWrite;

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    public long addExpense(Expense expense) {
        ContentValues values = getExpenseValues(expense);
        //
        return dbWrite.insert(ExpenseTable.TABLE_NAME_EXPENSE,null,values);
    }
    public ContentValues getExpenseValues(Expense expense){

        ContentValues values = new ContentValues();
        values.put(ExpenseTable.ID_COL, expense.getId());
        values.put(ExpenseTable.TYPE_OF_COST_COL, expense.getType_of_cost());
        values.put(ExpenseTable.COST_COL, expense.getCost());
        values.put(ExpenseTable.DATE_OF_COST_COL, expense.getDateOfCost());
        values.put(ExpenseTable.TIME_OF_COST_COL, expense.getTimeOfCost());
        values.put(ExpenseTable.ADDITIONAL_COMMENT_COL, expense.getAdditional_Comment());
        values.put(ExpenseTable.STAFF_ID_COL, expense.getStaffId());


        return values;
    }

    public ArrayList<Expense> getExpenseList(Expense expense, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (expense != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (expense.getDateOfCost() != null && !expense.getDateOfCost().trim().isEmpty()) {
                selection += " AND " + ExpenseTable.COST_COL + " LIKE ?";
                conditionList.add("%" + expense.getCost() + "%");
            }

            if (expense.getTimeOfCost() != null && !expense.getTimeOfCost().trim().isEmpty()) {
                selection += " AND " + ExpenseTable.COST_COL + " LIKE ?";
                conditionList.add("%" + expense.getCost() + "%");
            }



            if (expense.getStaffId() != -1) {
                selection += " AND " + ExpenseTable.STAFF_ID_COL + " = ?";
                conditionList.add(String.valueOf(expense.getStaffId()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getExpensetFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    protected ArrayList<Expense> getExpensetFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Expense> list = new ArrayList<>();

        if(dbRead!=null){
            Cursor cursor = dbRead.query(TABLE_NAME_EXPENSE, columns, selection, selectionArgs, groupBy, having, orderBy);

            while (cursor.moveToNext()) {
                Expense expenseItem = new Expense();

                expenseItem.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseTable.ID_COL)));
                expenseItem.setType_of_cost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.TYPE_OF_COST_COL)));
                expenseItem.setDateOfCost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.DATE_OF_COST_COL)));
                expenseItem.setTimeOfCost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.TIME_OF_COST_COL)));
                expenseItem.setAdditional_Comment(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.ADDITIONAL_COMMENT_COL)));
                expenseItem.setStaffId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseTable.STAFF_ID_COL)));
                expenseItem.setCost(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseTable.COST_COL)));

                list.add(expenseItem);
            }

            cursor.close();
        }

        return list;
    }
}



