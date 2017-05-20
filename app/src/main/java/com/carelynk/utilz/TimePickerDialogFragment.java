package com.carelynk.utilz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private boolean cancelDialog = false;
    private OnTimeSelection onTimeSelection;
    private int hour;
    private int min;

    public TimePickerDialogFragment(OnTimeSelection onTimeSelection) {
            this.onTimeSelection = onTimeSelection;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog datePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, false);//Yes 24 hour time
        //datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return datePickerDialog;
    }

    public void setDatePickerDate(int hour, int min) {
        this.hour = hour;
        this.min = min;
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
            onTimeSelection.onTimeSelect(""+hour+":"+min);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setDatePickerDate(hourOfDay, minute);
    }

    public interface OnTimeSelection{
        void onTimeSelect(String time);
    }
}
