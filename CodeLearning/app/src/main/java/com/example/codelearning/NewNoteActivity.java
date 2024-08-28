package com.example.codelearning;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView noteLv;
    private ArrayList<Note> notes;
    private ArrayAdapter<Note> noteAdapter;
    private Button actionButton;

    private NoteDatabaseHelper dbHelper;
    private long currentUserId;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> categories;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);  // 使用共用布局

        noteLv = findViewById(R.id.noteLv);
        actionButton = findViewById(R.id.newNote);

        notes = new ArrayList<>();
        currentUserId = getIntent().getLongExtra("_ID", -1);

        if (currentUserId == -1) {
            // 如果用戶 ID 無效，處理錯誤或重新導向到登入頁面
            Toast.makeText(this, "用戶未登入", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dbHelper = new NoteDatabaseHelper(this, currentUserId);

        noteAdapter = new storeAdapter(this, notes);
        noteLv.setAdapter(noteAdapter);

        // 初始化下拉式選單和分類資料
        categorySpinner = findViewById(R.id.categorySpinner);
        loadCategories(); // 載入分類

        // 在這裡呼叫 loadAllNotes() 方法來載入所有筆記
        loadAllNotes();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        noteLv.setOnItemClickListener((parent, view, position, id) -> {
            if (notes.isEmpty()) {
                Toast.makeText(NewNoteActivity.this, "請新增筆記！", Toast.LENGTH_SHORT).show();
            } else {
                Note selectedNote = notes.get(position);
                Toast.makeText(NewNoteActivity.this, "編輯筆記： " + selectedNote.getName(), Toast.LENGTH_SHORT).show();
                editNote(position, selectedNote);
            }
        });

        noteLv.setOnItemLongClickListener((adapterView, view, itemIndex, l) -> {
            // 顯示刪除確認對話框
            new AlertDialog.Builder(NewNoteActivity.this)
                    .setTitle("刪除筆記")
                    .setMessage("確定要刪除這個筆記嗎？")
                    .setPositiveButton("確定", (dialog, which) -> {
                        // 從資料庫中刪除筆記
                        Note noteToDelete = notes.get(itemIndex);
                        deleteNoteFromDatabase(noteToDelete);
                        notes.remove(itemIndex);
                        refreshPartListView(NewNoteActivity.this);
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        });

        intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getData() != null && o.getResultCode() == Activity.RESULT_OK) {
                            String action = o.getData().getStringExtra("ACTION");

                            String newCategory = o.getData().getStringExtra("CATEGORY");
                            String newName = o.getData().getStringExtra("NAME");
                            String newContent = o.getData().getStringExtra("CONTENT");

                            // 驗證分類名稱、筆記名稱和內容是否為空
                            if (newCategory == null || newCategory.isEmpty() ||
                                    newName == null || newName.isEmpty() ||
                                    newContent == null || newContent.isEmpty()) {
                                Toast.makeText(NewNoteActivity.this, "分類名稱、筆記名稱和內容不能為空！", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (action.equals("new")) {
                                insertNoteIntoDatabase(newCategory, newName, newContent);
                                loadAllNotes(); // 重新加载所有笔记，确保刷新视图
                                loadCategories(); // 重新加载分类，确保下拉选单更新
                            } else if (action.equals("edit")) {
                                int index = o.getData().getIntExtra("INDEX", 0);

                                // 更新資料庫中的筆記
                                updateNoteInDatabase(notes.get(index), newCategory, newName, newContent);

                                notes.get(index).setCategory(newCategory);
                                notes.get(index).setName(newName);
                                notes.get(index).setContent(newContent);
                                refreshPartListView(NewNoteActivity.this); // 刷新列表视图
                            }

                            // 確保編輯或新增筆記後，下拉選單顯示「全部分類」
                            categorySpinner.setSelection(0);
                            loadAllNotes(); // 顯示全部分類的筆記
                        }
                    }
                });

        refreshPartListView(this); // 初次加载时刷新列表
    }

    private void loadCategories() {
        // 從資料庫中獲取所有分類
        categories = dbHelper.getAllCategories(currentUserId);

        // 添加一個顯示全部分類的固定選項
        categories.add(0, "全部分類");

        // 創建適配器
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 設置適配器
        categorySpinner.setAdapter(categoryAdapter);

        // 設置 Spinner 的選擇事件監聽器
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根據選中的分類篩選筆記
                String selectedCategory = categories.get(position);
                if (selectedCategory.equals("全部分類")) {
                    loadAllNotes();  // 顯示全部分類
                } else {
                    filterNotesByCategory(selectedCategory);  // 顯示指定分類
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未選擇時顯示所有筆記
                loadAllNotes();
            }
        });
    }

    public void addNote() {
        Intent intent = new Intent(this, NotesActivity.class);
        //設定一個bundle來放資料
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "new");

        //利用intent攜帶bundle資料
        intent.putExtras(bundle);
        intentActivityResultLauncher.launch(intent);
    }

    private void editNote(int index, Note note) {
        Intent intent = new Intent(this, NotesActivity.class);
        //設定一個bundle來放資料
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "edit");
        bundle.putString("CATEGORY", note.getCategory());
        bundle.putString("NAME", note.getName());
        bundle.putString("CONTENT", note.getContent());
        bundle.putInt("INDEX", index);

        //利用intent攜帶bundle資料
        intent.putExtras(bundle);
        intentActivityResultLauncher.launch(intent);
    }

    private void insertNoteIntoDatabase(String category, String name, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_CATEGORY, category);
        values.put(NoteDatabaseHelper.COLUMN_NAME, name);
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, content);
        values.put(NoteDatabaseHelper.COLUMN_ID, currentUserId); // 關聯到當前用戶

        long newRowId = db.insert(NoteDatabaseHelper.TABLE_NOTES, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "筆記新增成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "新增筆記時出錯！", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNoteInDatabase(Note note, String newCategory, String newName, String newContent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_CATEGORY, newCategory);
        values.put(NoteDatabaseHelper.COLUMN_NAME, newName);
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, newContent);

        db.update(NoteDatabaseHelper.TABLE_NOTES, values,
                NoteDatabaseHelper.COLUMN_ID + " = ? AND " + NoteDatabaseHelper.COLUMN_CATEGORY + " = ? AND " + NoteDatabaseHelper.COLUMN_NAME + " = ?",
                new String[]{String.valueOf(currentUserId), note.getCategory(), note.getName()});
    }

    private void deleteNoteFromDatabase(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NoteDatabaseHelper.TABLE_NOTES,
                NoteDatabaseHelper.COLUMN_ID + " = ? AND " + NoteDatabaseHelper.COLUMN_CATEGORY + " = ? AND " + NoteDatabaseHelper.COLUMN_NAME + " = ?",
                new String[]{String.valueOf(currentUserId), note.getCategory(), note.getName()});
    }

    private void filterNotesByCategory(String category) {
        notes.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NoteDatabaseHelper.TABLE_NOTES, null,
                NoteDatabaseHelper.COLUMN_CATEGORY + " = ? AND " + NoteDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{category, String.valueOf(currentUserId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_NAME));
                String content = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_CONTENT));
                notes.add(new Note(category, name, content));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        refreshPartListView(this);
    }

    private void loadAllNotes() {
        notes.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(NoteDatabaseHelper.TABLE_NOTES, null,
                NoteDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(currentUserId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_CATEGORY));
                String name = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_NAME));
                String content = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_CONTENT));
                notes.add(new Note(category, name, content));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        refreshPartListView(this);
    }

    private void refreshPartListView(Context context) {
        if (notes.isEmpty()) {
            ArrayList<String> empty = new ArrayList<>();
            empty.add("目前無任何筆記！");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, empty);
            noteLv.setAdapter(adapter);
            noteLv.setOnItemClickListener((parent, view, position, id) ->
                    Toast.makeText(context, "請新增筆記！", Toast.LENGTH_SHORT).show()
            );
        } else {
            noteAdapter.notifyDataSetChanged();
            noteLv.setAdapter(noteAdapter);
            noteLv.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Note item = notes.get(i);
        String category = item.getCategory();
        String name = item.getName();
        String content = item.getContent();

        Intent intent = new Intent(this, NotesActivity.class);
        //設定一個bundle來放資料
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "edit");
        bundle.putString("CATEGORY", category);
        bundle.putString("NAME", name);
        bundle.putString("CONTENT", content);
        bundle.putInt("INDEX", i);

        //利用intent攜帶bundle資料
        intent.putExtras(bundle);
        intentActivityResultLauncher.launch(intent);
    }
}
