package com.michaelrbock.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    EditText etTodoText;
    Button saveButton;
    String todoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        todoText = getIntent().getStringExtra("todo_text");
        etTodoText = (EditText) findViewById(R.id.etTodoText);
        etTodoText.setText(todoText, TextView.BufferType.EDITABLE);
        etTodoText.setSelection(todoText.length());

        saveButton = (Button) findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
    }

    public void onSave() {
        Intent data = new Intent();
        data.putExtra("todo_text", etTodoText.getText().toString());
        data.putExtra("todo_position", getIntent().getIntExtra("todo_position", 0));
        setResult(RESULT_OK, data);
        finish();
    }
}
