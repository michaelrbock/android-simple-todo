package com.michaelrbock.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;

    SQLiteDatabase db;

    ArrayList<TodoModel> todoModels;
    TodoModelAdapter aToDoAdapter;
    ListView lvItems;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        populateArrayItemsAdapterAndListView();

        etNewItem = (EditText) findViewById(R.id.etNewItem);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                onDeleteItem(position);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditView(position);
            }
        });
    }

    public void launchEditView(int position) {
        Intent editToDoIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editToDoIntent.putExtra("todo_text", todoModels.get(position).text);
        editToDoIntent.putExtra("todo_position", position);
        startActivityForResult(editToDoIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newTodoText = data.getExtras().getString("todo_text");
            int position = data.getExtras().getInt("todo_position");
            todoModels.get(position).text = newTodoText;
            aToDoAdapter.notifyDataSetChanged();

            cupboard().withDatabase(db).put(todoModels.get(position));
        }
    }

    private void populateArrayItemsAdapterAndListView() {
        readItemsFromDb();
        aToDoAdapter = new TodoModelAdapter(this, todoModels);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
    }

    public void onAddItem(View view) {
        TodoModel newTodo = new TodoModel(
                etNewItem.getText().toString(),
                System.currentTimeMillis(),
                "");
        aToDoAdapter.add(newTodo);
        etNewItem.setText("");

        cupboard().withDatabase(db).put(newTodo);
    }

    private void onDeleteItem(int position) {
        TodoModel toDelete = todoModels.remove(position);
        aToDoAdapter.notifyDataSetChanged();

        cupboard().withDatabase(db).delete(toDelete);
    }

    private void readItemsFromDb() {
        todoModels = new ArrayList<TodoModel>();
        Cursor todoCursor = cupboard().withDatabase(db).query(TodoModel.class).getCursor();
        try {
            QueryResultIterable<TodoModel> itr =
                    cupboard().withCursor(todoCursor).iterate(TodoModel.class);
            for (TodoModel todo : itr) {
                todoModels.add(todo);
            }
        } finally {
            todoCursor.close();
        }
    }
}
