package vn.edu.greenwich.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Backup implements Serializable {
    public class BackupEntry {
        public static final String TABLE_BACKUP_NAME = "Backup";
    }

    protected java.util.Date Date;
    protected String DeviceName;
    protected ArrayList<Staff> tripModal;
    protected ArrayList<Expense> expenseModal;



    public Backup(java.util.Date date, String deviceName, ArrayList<Staff> trips, ArrayList<Expense> expenses) {
        Date = date;
        DeviceName = deviceName;
        tripModal = trips;
        expenseModal = expenses;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setTrips(ArrayList<Staff> trips) {
        tripModal = trips;
    }

    public ArrayList<Staff> getTrips() {
        return tripModal;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        expenseModal = expenses;
    }

    public ArrayList<Expense> getExpenses() {
        return expenseModal;
    }
}