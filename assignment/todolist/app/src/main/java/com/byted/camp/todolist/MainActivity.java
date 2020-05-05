package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;
    private TodoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new TodoDbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        Log.d("debug","1");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        LinkedList<Note> linkedList = new LinkedList<>();


        Log.d("debug","5");

        if(db == null) {

            Log.d("debug","3");
            return linkedList;
        }

        String sortOrder = TodoContract.TodoList.COLUMN_PRIORITY + " DESC";
        Cursor cursor = db.query(
                TodoContract.TodoList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );


        Log.d("debug","4");

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoList._ID));
            Note newNote = new Note(itemId);
            newNote.setContent(cursor.getString(cursor.getColumnIndex(TodoContract.TodoList.COLUMN_EVENT)));
            newNote.setDate(new Date(cursor.getLong(cursor.getColumnIndex(TodoContract.TodoList.COLUMN_DATE))));
            newNote.setState(State.from(cursor.getInt(cursor.getColumnIndex(TodoContract.TodoList.COLUMN_STATE))));
            newNote.setPriority(Priority.from(cursor.getInt(cursor.getColumnIndex(TodoContract.TodoList.COLUMN_PRIORITY))));
            linkedList.add(newNote);
        }

        db.close();

        Log.d("debug","2");
        return linkedList;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        String[] noteID = {String.valueOf(note.id)};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null) {
            if(db.delete(
                    TodoContract.TodoList.TABLE_NAME,
                    TodoContract.TodoList._ID + "=?",
                    noteID
            ) > 0){
                notesAdapter.refresh(loadNotesFromDatabase());
            }
        }
        db.close();
    }

    private void updateNode(Note note) {
        // TODO 更新数据
        String[] noteID = {String.valueOf(note.id)};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null) {
            ContentValues values = new ContentValues();
            values.put(TodoContract.TodoList.COLUMN_STATE, note.getState().intValue);
            if (db.update(TodoContract.TodoList.TABLE_NAME,
                    values,
                    TodoContract.TodoList._ID + "=?",
                    noteID
            ) > 0) {
                notesAdapter.refresh(loadNotesFromDatabase());
            }
        }
        db.close();
    }

}
