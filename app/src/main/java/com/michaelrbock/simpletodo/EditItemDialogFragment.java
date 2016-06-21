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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by michaelbock on 6/20/16.
 */
public class EditItemDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener {

    private EditText etTodoText;
    private EditText etTodoDate;
    private EditText etTodoPriority;
    private Button btnSave;

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

        // Set up button.
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });

        // Get field from view.
        etTodoText = (EditText) view.findViewById(R.id.etTodoText);
        etTodoText.setOnEditorActionListener(this);
        etTodoDate = (EditText) view.findViewById(R.id.etTodoDate);
        etTodoPriority = (EditText) view.findViewById(R.id.etTodoPriority);

        // Fetch arguments from bundle and set text.
        String todoText = getArguments().getString("todo_text", "Todo Text");
        etTodoText.setText(todoText);
        Long todoDate = getArguments().getLong("todo_date", 0L);
        if (todoDate != 0L) {
            Date date = new Date(todoDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            etTodoDate.setText(sdf.format(date));
        }
        String todoPriority = getArguments().getString("todo_priority");
        etTodoPriority.setText(todoPriority);

        // Show soft keyboard automatically and request to focus to field.
        etTodoText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSave();
            return true;
        }
        return false;
    }

    public void onSave() {
        // Return input text back to activity through the implemented listener.
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        Long todoDate = 0L;
        if (!etTodoDate.getText().toString().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            try {
                Date date = (Date) sdf.parse(etTodoDate.getText().toString());
                todoDate = date.getTime();
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Error inputting date",
                        Toast.LENGTH_SHORT).show();
            }
        }
        listener.onFinishEditDialog(
                getArguments().getInt("todo_position"),
                etTodoText.getText().toString(),
                todoDate,
                etTodoPriority.getText().toString());

        // Close the dialog and return back to the parent activity.
        dismiss();
    }
}
