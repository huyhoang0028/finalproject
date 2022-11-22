package vn.edu.greenwich.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.greenwich.myapplication.Fragment.AddExpenseFragment;
import vn.edu.greenwich.myapplication.adapter.Adapter;
import vn.edu.greenwich.myapplication.adapter.MyAdapter;
import vn.edu.greenwich.myapplication.database.DbHelper;
import vn.edu.greenwich.myapplication.database.ExpenseTable;
import vn.edu.greenwich.myapplication.database.StaffDao;
import vn.edu.greenwich.myapplication.database.StaffTable;
import vn.edu.greenwich.myapplication.dialog.DatePickerFragment;
import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;

public class UpdateActivity extends AppCompatActivity implements DatePickerFragment.FragmentListener{

    Staff _staff;
    StaffDao _db;
    MyAdapter myAdapter;
    Adapter adapter;
    RecyclerView recyclerView;
    private ArrayList<Expense> expenseList;

    EditText name_input, tripName_input,destination_input,
    date_input, description_input;
    RadioGroup riskAssessment_input;
    RadioButton radioButton;
    LinearLayout hide;
    View show;

    Button btn_update, btn_delete, btn_addExpense;
    ArrayList<Staff> staffList;
    String id, name, tripName, destination, date, riskAssessment,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        staffList = new ArrayList<>();
        Staff staff = new Staff();
        _db = new StaffDao(UpdateActivity.this);



        name_input = findViewById(R.id.name_input);
        tripName_input = findViewById(R.id.business_trip_name_input);
        destination_input = findViewById(R.id.destination_input);
        date_input = findViewById(R.id.date_input);
        riskAssessment_input = findViewById(R.id.riskAssessment_input);
        riskAssessment_input.getCheckedRadioButtonId();
        description_input =findViewById(R.id.description_input);
        recyclerView = findViewById(R.id.recyclerViewExpense);
        hide = (LinearLayout) findViewById(R.id.hide);
        show = (View) findViewById(R.id.show);
        expenseList = new ArrayList<>();

        date_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDateDialog(event);
                return false;
            }
        });


        //First we call this
        getAndSetIntentData();
        storeDataInArrays();


        adapter = new Adapter(UpdateActivity.this,this,expenseList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateActivity.this));


        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_addExpense = findViewById(R.id.btn_addExpense);


        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                StaffDao staffDao = new StaffDao(UpdateActivity.this);
                staff.setId(Integer.valueOf(id));
                staff.setName(name_input.getText().toString().trim());
                staff.setDestination(destination_input.getText().toString().trim());
                staff.setDate(date_input.getText().toString().trim());
                staff.setBusiness_trip_name(tripName_input.getText().toString().trim());
                staff.setDescription(description_input.getText().toString().trim());

                int checkedButtonId = riskAssessment_input.getCheckedRadioButtonId();

                String riskAssessment = "";
                if (checkedButtonId != -1) {
                    radioButton = findViewById(checkedButtonId);

                    riskAssessment = radioButton.getText().toString();
                }
                staff.setRiskAssessment(riskAssessment);

                staffDao.updateStaff(staff);
            }

        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
        btn_addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddExpenseFragment(Integer.parseInt(id)).show(getSupportFragmentManager(), null);
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide.setVisibility(View.GONE);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide.setVisibility(View.VISIBLE);
            }
        });
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getSupportFragmentManager(), null);
            return true;
        }
        return false;
    }




    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("destination") && getIntent().hasExtra("date")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            destination = getIntent().getStringExtra("destination");
            date = getIntent().getStringExtra("date");
            tripName = getIntent().getStringExtra("tripName");
            riskAssessment = getIntent().getStringExtra("riskAssessment");
            description = getIntent().getStringExtra("description");

            //Setting Intent Data
            name_input.setText(name);
            destination_input.setText(destination);
            date_input.setText(date);
            tripName_input.setText(tripName);
            description_input.setText(description);
            riskAssessment_input.setOnCheckedChangeListener(RadioGroup::check);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StaffDao staffDao = new StaffDao(UpdateActivity.this);
                staffDao.deleteOneRow(id);
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
    protected void showAddExpenseFragment() {
        new AddExpenseFragment(_staff.getId()).show(getSupportFragmentManager(), null);
    }

    public void storeDataInArrays() {
        StaffDao staffDao = new StaffDao(UpdateActivity.this);
        Cursor cursor = staffDao.readAllDataExpenseById(Integer.parseInt(id));
        if(cursor.getCount() == 0){
            //empty_imageview.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.VISIBLE);
        }else{
            expenseList.clear();
            while (cursor.moveToNext()){
                Expense expense = new Expense();
                expense.setId(cursor.getColumnIndexOrThrow(ExpenseTable.ID_COL));
                expense.setCost(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COST_COL))));
                expense.setAdditional_Comment(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.ADDITIONAL_COMMENT_COL)));
                expense.setType_of_cost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.TYPE_OF_COST_COL)));
                expense.setTimeOfCost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.TIME_OF_COST_COL)));
                expense.setDateOfCost(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.DATE_OF_COST_COL)));
                expense.setStaffId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseTable.STAFF_ID_COL)));
                expenseList.add(expense);
            }
            //empty_imageview.setVisibility(View.GONE);
        }

    }
    @Override
    public void sendFromDatePickerFragment(String date) {
        date_input.setText(date);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
