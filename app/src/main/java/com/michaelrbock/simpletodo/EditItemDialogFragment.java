package com.michaelrbock.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by michaelbock on 6/20/16.
 */
public class EditItemDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener, DatePickerFragment.DatePickerDialogListener {

    private EditText etTodoText;
    private String todoPriority;
    private Spinner spinTodoPriority;
    private Long todoDate;
    private Button btnTodoDate;
    private Button btnSave;

    final String[] priorityOptions =
            new String[]{"Priority", "Low (!)", "Medium (!!)", "High (!!!)"};

    public interface EditItemDialogListener {
        void onFinishEditDialog(int position, String todoText, Long date, String priority);
    }

    public EditItemDialogFragment() {
        // Empty constructor is required for DialogFragment.
    }

    public static EditItemDialogFragment newInstance(int position, String todoText, Long date,
                                                     String priority) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putInt("todo_position", position);
        args.putString("todo_text", todoText);
        args.putLong("todo_date", date);
        args.putString("todo_priority", priority);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window.
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent.
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up button click listeners.
        btnTodoDate = (Button) view.findViewById(R.id.btnTodoDate);
        btnTodoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });

        // Get fields from view.
        etTodoText = (EditText) view.findViewById(R.id.etTodoText);
        etTodoText.setOnEditorActionListener(this);
        spinTodoPriority = (Spinner) view.findViewById(R.id.spinTodoPriority);

        // Fetch arguments from bundle and set text.
        String todoText = getArguments().getString("todo_text", "Todo Text");
        etTodoText.setText(todoText);

        todoDate = getArguments().getLong("todo_date", 0L);
        updateDateText();

        todoPriority = getArguments().getString("todo_priority");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, priorityOptions);
        spinTodoPriority.setAdapter(arrayAdapter);
        spinTodoPriority.setSelection(
                arrayAdapter.getPosition(getPriorityShowString(todoPriority)));
        spinTodoPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onPriorityChosen(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing :)
            }
        });

        // Show soft keyboard automatically and request to focus to field.
        etTodoText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void updateDateText() {
        String todoDateString;
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        if (todoDate == 0L) {
            Calendar calendar = Calendar.getInstance();
            date = calendar.getTime();
        } else {
            date = new Date(todoDate);
        }
        todoDateString = "Due Date: " + sdf.format(date);
        btnTodoDate.setText(todoDateString);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSave();
            return true;
        }
        return false;
    }

    public void onPriorityChosen(int position) {
        todoPriority = getPriorityDataString(priorityOptions[position]);
    }

    public void onSave() {
        // Return input text back to activity through the implemented listener.
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(
                getArguments().getInt("todo_position"),
                etTodoText.getText().toString(),
                todoDate,
                todoPriority);

        // Close the dialog and return back to the parent activity.
        dismiss();
    }

    public void showDatePickerDialog() {
        DatePickerFragment dateFragment = DatePickerFragment.newInstance(this, todoDate);
        dateFragment.show(getFragmentManager(), "date_picker");
    }

    @Override
    public void onFinishDatePicker(Long date) {
        todoDate = date;
        updateDateText();
    }

    private String getPriorityShowString(String excalamations) {
        switch (excalamations) {
            case "!":
                return "Low (!)";
            case "!!":
                return "Medium (!!)";
            case "!!!":
                return "High (!!!)";
            default:
                return "Priority";
        }
    }

    private String getPriorityDataString(String showString) {
        switch (showString) {
            case "Low (!)":
                return "!";
            case "Medium (!!)":
                return "!!";
            case "High (!!!)":
                return "!!!";
            default:
                return "";
        }
    }
}
