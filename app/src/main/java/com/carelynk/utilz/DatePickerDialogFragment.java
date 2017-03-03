package com.carelynk.utilz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private boolean cancelDialog = false;
    private int year;
    private int month;
    private int day;
    private OnDateSelection onDateSelection;

    public DatePickerDialogFragment(OnDateSelection onDateSelection) {
            this.onDateSelection = onDateSelection;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return datePickerDialog;
    }

    public void setDatePickerDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        cancelDialog = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!cancelDialog) {
            String mDatePick = AppUtils.formattedDate("yyyy MM dd", "dd MMMM yyyy", "" + year + " " + (month + 1) + " " + day);
            onDateSelection.onDateSelect(mDatePick);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDatePickerDate(year, monthOfYear, dayOfMonth);
    }

    public interface OnDateSelection{
        void onDateSelect(String date);
    }
}
