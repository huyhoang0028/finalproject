package vn.edu.greenwich.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.edu.greenwich.myapplication.Fragment.AboutFragment;
import vn.edu.greenwich.myapplication.Fragment.DialogEnterData_Fragment;
import vn.edu.greenwich.myapplication.adapter.MyAdapter;
import vn.edu.greenwich.myapplication.database.ExpenseDao;
import vn.edu.greenwich.myapplication.database.StaffDao;
import vn.edu.greenwich.myapplication.dialog.DatePickerFragment;
import vn.edu.greenwich.myapplication.model.Backup;
import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;
import vn.edu.greenwich.myapplication.database.DbHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.FragmentListener {
    Context context;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ImageView empty_imageview;
    TextView no_data;
    DbHelper db;
    RadioButton radioButton;
    private ArrayList<Staff> staffList;
    Staff staff;
    EditText ip_name, ip_business_trip_name, ip_destination,
            ip_date, ip_description;
    Button btn_close, btn_send;
    RadioGroup ip_riskAssessment;
    DatabaseReference databaseUsers;
    FirebaseFirestore DB;
    protected StaffDao _db;
    private DrawerLayout drawerLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = FirebaseFirestore.getInstance();
        _db = new StaffDao(this);
        recyclerView = findViewById(R.id.recyclerView);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        databaseUsers = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new DbHelper(MainActivity.this);

        staffList = new ArrayList<>();
        staff = new Staff();
        storeDataInArrays();

        myAdapter = new MyAdapter(MainActivity.this,this,staffList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        //    Show dialog form to enter data

        FloatingActionButton btnOpenDialogCenter= (FloatingActionButton) findViewById(R.id.btn_float_add);
        btnOpenDialogCenter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DialogEnterData_Fragment.class);
                openEnterData(Gravity.CENTER);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        storeDataInArrays();
        myAdapter.notifyDataSetChanged();
        //customAdapter.notifyDataSetChanged();
    }

    //    Ham xu li cho function open dialog
    private void openEnterData(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_enter_data);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


//        Check su kien khi click ra ben ngoai cua dialog xem no no tat hay khong
        if(Gravity.BOTTOM== gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        ip_name = dialog.findViewById(R.id.name);
        ip_business_trip_name = dialog.findViewById(R.id.bussiness_trip_name);
        ip_destination = dialog.findViewById(R.id.destination);
        ip_date = dialog.findViewById(R.id.date);
        ip_date.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        ip_description = dialog.findViewById(R.id.description);
        btn_close = dialog.findViewById(R.id.btn_close);
        btn_send = dialog.findViewById(R.id.btn_send);

        ip_riskAssessment = dialog.findViewById(R.id.riskAssessment);



        btn_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
                // tat dialog di
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                StaffDao staffDao = new StaffDao(MainActivity.this);
                if (isValidForm()) {
                    staff.setName(ip_name.getText().toString().trim());
                    staff.setDestination(ip_destination.getText().toString().trim());
                    staff.setDate(ip_date.getText().toString().trim());
                    staff.setBusiness_trip_name(ip_business_trip_name.getText().toString().trim());
                    staff.setDescription(ip_description.getText().toString().trim());
                    int checkedButtonId = ip_riskAssessment.getCheckedRadioButtonId();

                    String riskAssessment = "";
                    if (checkedButtonId != -1) {
                        radioButton = dialog.findViewById(checkedButtonId);
                        riskAssessment = radioButton.getText().toString();
                    }
                    staff.setRiskAssessment(riskAssessment);
                    staffDao.addStaff(staff);

                    Toast.makeText(MainActivity.this,"Add information successfully",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"Add information failure",Toast.LENGTH_LONG).show();
                }

                storeDataInArrays();
                myAdapter.notifyDataSetChanged();

            }
        });
        dialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    protected boolean isValidForm(){
        boolean isValid = true;

        if (ip_name.getText().toString() == null || ip_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this,"name can't be null",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (ip_destination.getText().toString() == null || ip_destination.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this,"Destination can't be null",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (ip_date.getText().toString() == null || ip_date.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this,"Date can't be null",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    public void storeDataInArrays() {
        StaffDao staffDao = new StaffDao(MainActivity.this);
        Cursor cursor = staffDao.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            staffList.clear();
            while (cursor.moveToNext()){
                Staff staff = new Staff();
                staff.setId(Integer.valueOf(cursor.getString(0)));
                staff.setName(cursor.getString(1));
                staff.setBusiness_trip_name(cursor.getString(2));
                staff.setDestination(cursor.getString(3));
                staff.setDate(cursor.getString(4));
                staff.setRiskAssessment(cursor.getString(5));
                staff.setDescription(cursor.getString(6));
                staffList.add(staff);
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.nav_home:
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            case R.id.nav_inf:
                recyclerView.setVisibility(View.GONE);
            replaceFragment(new AboutFragment());break;
            case R.id.nav_setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
        }

        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem search = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        if(item.getItemId() == R.id.setting){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StaffDao staffDao = new StaffDao(MainActivity.this);
                staffDao.deleteAllDate();
                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
       if (drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
       } else {
           super.onBackPressed();
       }
    }
    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getSupportFragmentManager(), null);
            return true;
        }

        return false;
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        ip_date.setText(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}