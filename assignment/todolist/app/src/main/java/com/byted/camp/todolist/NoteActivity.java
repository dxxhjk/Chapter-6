package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private RadioGroup priorityRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);
        priorityRG = findViewById(R.id.RG_priority);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(), getPriority());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
    private Priority getPriority() {
        int rank = priorityRG.getCheckedRadioButtonId();
        switch (rank) {
            case R.id.btnRank1:
                return Priority.RANK1;
            case R.id.btnRank2:
                return Priority.RANK2;
            case R.id.btnRank3:
                return Priority.RANK3;
        }
        return Priority.RANK3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content, Priority priority) {
        // TODO 插入一条新数据，返回是否插入成功
        if (content == "") return false;

        TodoDbHelper dbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db != null) {
            ContentValues valus = new ContentValues();
            valus.put(TodoContract.TodoList.COLUMN_EVENT, content);
            valus.put(TodoContract.TodoList.COLUMN_STATE, State.TODO.intValue);
            valus.put(TodoContract.TodoList.COLUMN_DATE, System.currentTimeMillis());
            valus.put(TodoContract.TodoList.COLUMN_PRIORITY, priority.intValue);
            return db.insert(TodoContract.TodoList.TABLE_NAME, null, valus) != -1;
        }
        return false;
    }
}
