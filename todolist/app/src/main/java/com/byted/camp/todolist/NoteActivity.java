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
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.sql.Date;
import java.sql.SQLClientInfoException;
import java.text.SimpleDateFormat;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
//    TodoDbHelper todoDbHelper;
//    SQLiteDatabase db;

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
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

    @Override
    protected void onDestroy() {
        //todoDbHelper.close();
        super.onDestroy();

    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        TodoDbHelper todoDbHelper = new TodoDbHelper(editText.getContext());
        SQLiteDatabase db = todoDbHelper.getWritableDatabase();

        ContentValues values  = new ContentValues();
        //Note note = new Note(Long.parseLong(TodoContract.Todo.COLUMN_NAME_ID));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss//&#x83b7;&#x53d6;&#x5f53;&#x524d;&#x65f6;&#x95f4;
        Date date = new Date(System.currentTimeMillis());
        //long date = new Date().getTime();

        values.put(TodoContract.Todo.COLUMN_NAME_DATE,simpleDateFormat.format(date));
        values.put(TodoContract.Todo.COLUMN_NAME_STATE,0);
        values.put(TodoContract.Todo.COLUMN_NAME_CONTENT,content);

        long newRowId = db.insert(TodoContract.Todo.TABLE_NAME,null,values);
        if(newRowId != -1) {
            System.out.println("success");
            return true;
        }
        else
            return false;

    }
}
