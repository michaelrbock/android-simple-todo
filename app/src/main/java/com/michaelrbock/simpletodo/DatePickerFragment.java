package com.michaelrbock.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by michaelbock on 7/7/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerDialogListener {
        public void onFinishDatePicker(Long date);
    }

    private DatePickerDialogListener listener;

    public DatePickerFragment() {
        // Empty constructor is required for DialogFragment.
    }

    public static DatePickerFragment newInstance(DatePickerDialogListener listener, Long date) {
        DatePickerFragment frag =  new DatePickerFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putLong("todo_date", date);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window.
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent.
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Long todoDate = getArguments().getLong("todo_date", 0L);
        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        if (todoDate == 0L) {
            date = calendar.getTime();
        } else {
            date = new Date(todoDate);
        }
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        listener.onFinishDatePicker(date.getTime());
    }
}
