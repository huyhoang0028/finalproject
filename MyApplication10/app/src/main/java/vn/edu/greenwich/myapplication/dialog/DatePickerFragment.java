package vn.edu.greenwich.myapplication.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import vn.edu.greenwich.myapplication.UpdateActivity;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public DatePickerFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = "";

        ++month;

        date += day < 10 ? "0" + day : day;
        date += "/";
        date += month < 10 ? "0" + month : month;
        date += "/";
        date += year;

        if (getParentFragment() != null)
        {FragmentListener listen = (FragmentListener) getParentFragment();
            listen.sendFromDatePickerFragment(date);}

        if (getActivity() != null) {
            FragmentListener listen = (FragmentListener) getActivity();
            listen.sendFromDatePickerFragment(date);
        }






        dismiss();
    }

    public interface FragmentListener {
        void sendFromDatePickerFragment(String date);
    }


}