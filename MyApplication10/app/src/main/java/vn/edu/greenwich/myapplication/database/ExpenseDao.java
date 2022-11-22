package vn.edu.greenwich.myapplication.database;

import static vn.edu.greenwich.myapplication.database.ExpenseTable.TABLE_NAME_EXPENSE;
import static vn.edu.greenwich.myapplication.database.StaffTable.TABLE_NAME_STAFF;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import androidx.annotation.Nullable;

import java.util.ArrayList;

import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;

public class ExpenseDao{
    private Context context;
    protected DbHelper db;
    protected SQLiteDatabase dbWrite, dbRead;






    public ExpenseDao() {
    }

    public ExpenseDao(Context context) {
        db = new DbHelper(context);

        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    //-------------------------------------------------------------------------------------------
    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
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



    public Cursor readAllDataExpense() {
        String query = "SELECT * FROM " + TABLE_NAME_EXPENSE;
        SQLiteDatabase db = this.dbWrite;

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public ArrayList<Expense> getAllExpense() {
        SQLiteDatabase db = this.dbRead;
        ArrayList<Expense> expenseModalArrayList = new ArrayList<>();
        Cursor cursorExpense  = null;
        if (db != null) {
            cursorExpense = db.rawQuery("SELECT * FROM " + TABLE_NAME_EXPENSE, null);
            if (cursorExpense.moveToFirst()) {
                do {
                    expenseModalArrayList.add(new Expense(cursorExpense.getColumnIndex("id"),
                            cursorExpense.getColumnName(Integer.parseInt("typeOfCost")),
                            cursorExpense.getColumnIndex("cost"),
                            cursorExpense.getString(4),
                            cursorExpense.getString(5),
                            cursorExpense.getString(6),
                            cursorExpense.getString(7),
                            cursorExpense.getInt(8)));
                } while (cursorExpense.moveToNext());
            }
            cursorExpense.close();
        }
        return expenseModalArrayList;
    }
}
