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

/**
 * Created by michaelbock on 6/20/16.
 */
public class EditItemDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener {

    private EditText etTodoText;
    private Button btnSave;

    public interface EditItemDialogListener {
        void onFinishEditDialog(int position, String todoText);
    }

    public EditItemDialogFragment() {
        // Empty constructor is required for DialogFragment.
    }

    public static EditItemDialogFragment newInstance(int position, String todoText) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putInt("todo_position", position);
        args.putString("todo_text", todoText);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
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

        // Fetch arguments from bundle and set text.
        String todoText = getArguments().getString("todo_text", "Todo Text");
        etTodoText.setText(todoText);

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
        listener.onFinishEditDialog(getArguments().getInt("todo_position"),
                etTodoText.getText().toString());

        // Close the dialog and return back to the parent activity.
        dismiss();
    }
}
