package vn.edu.greenwich.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import vn.edu.greenwich.myapplication.database.DbHelper;
import vn.edu.greenwich.myapplication.database.StaffDao;
import vn.edu.greenwich.myapplication.model.Backup;
import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;


public class SettingActivity extends AppCompatActivity {
    protected StaffDao _db;
    protected Button settingBackup, settingResetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Setting");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _db = new StaffDao(this);

        settingBackup = findViewById(R.id.settingBackup);
        settingResetDatabase = findViewById(R.id.settingResetDatabase);

        settingBackup.setOnClickListener(v -> backup());
        settingResetDatabase.setOnClickListener(v -> resetDatabase());
    }

    protected void backup() {

        ArrayList<Staff> trips = _db.getStaffList(null,null,false);
        ArrayList<Expense> expenses = _db.getExpenseList(null,null,false);

        if (null != trips  && 0 < trips.size() && null != expenses && 0 < expenses.size()) {
            String deviceName = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

            Backup backup = new Backup(new Date(), deviceName, trips, expenses);

            FirebaseFirestore.getInstance().collection(Backup.BackupEntry.TABLE_BACKUP_NAME)
                    .add(backup)
                    .addOnSuccessListener(document -> {
                        Toast.makeText(this, "backup_success", Toast.LENGTH_SHORT).show();
                        Log.d("backup_firestore", document.getId());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,"backup_fail", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        } else {
            Toast.makeText(this, "empty_list", Toast.LENGTH_SHORT).show();
        }
    }



    protected void resetDatabase() {
        _db.reset();

        Toast.makeText(this, "Reset database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}