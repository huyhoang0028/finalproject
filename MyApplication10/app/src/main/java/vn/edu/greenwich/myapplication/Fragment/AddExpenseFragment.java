package vn.edu.greenwich.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import vn.edu.greenwich.myapplication.R;
import vn.edu.greenwich.myapplication.database.StaffDao;
import vn.edu.greenwich.myapplication.dialog.DatePickerFragment;
import vn.edu.greenwich.myapplication.dialog.TimePickerFragment;
import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;

public class AddExpenseFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener{

    protected long _id;

    protected Staff staff;
    protected EditText addDate, addTime, addAdditionalComment,addCost;
    protected Spinner spinnerTypeOfCost;
    protected Button cancel,addExpense;
    protected StaffDao _db;
    protected Expense expense;

    public AddExpenseFragment() {
        _id = -1;
    }

    public AddExpenseFragment(long id) {
        _id = id;
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        addDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        addTime.setText(time);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new StaffDao(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        staff = new Staff();


        spinnerTypeOfCost = view.findViewById(R.id.spinnerTypeOfCost);
        addDate = view.findViewById(R.id.addDate);
        addTime = view.findViewById(R.id.addTime);
        addAdditionalComment = view.findViewById(R.id.addAdditionalComment);
        addCost = view.findViewById(R.id.expense);

        cancel = view.findViewById(R.id.cancel);
        addExpense = view.findViewById(R.id.addExpense);
        setTypeSpinner();

        addDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        addTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        cancel.setOnClickListener(v -> dismiss());
        addExpense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createExpense();
                dismiss();

            }
        });
        return view;

    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.label_typeOfCost,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfCost.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected void createExpense() {
        Expense expense = new Expense();
        expense.setStaffId(_id);
        expense.setType_of_cost(spinnerTypeOfCost.getSelectedItem().toString());
        expense.setCost(Double.parseDouble(addCost.getText().toString()));
        expense.setDateOfCost(addDate.getText().toString());
        expense.setTimeOfCost(addTime.getText().toString());
        expense.setAdditional_Comment(addAdditionalComment.getText().toString());
        _db.addExpense(expense);
    }
}