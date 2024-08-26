package com.example.codelearning;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MemberInfoActivity extends AppCompatActivity {

    private TextView emailTextView, nameTextView, idTextView, codeTextView, birthdayTextView;
    private Button logoutButton, deleteButton;

    private UserDatabaseHelper dbHelper;
    private String username;  // 用來保存當前使用者名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        dbHelper = new UserDatabaseHelper(this);

        idTextView = findViewById(R.id.id);
        nameTextView = findViewById(R.id.name);
        codeTextView = findViewById(R.id.code);
        emailTextView = findViewById(R.id.email);
        birthdayTextView = findViewById(R.id.birthday);
        logoutButton = findViewById(R.id.logout_button);
        deleteButton = findViewById(R.id.delete_button);

        username = getIntent().getStringExtra("username");  // 獲取傳遞過來的用戶名
        if (username != null) {
            loadMemberInfo(username);
        } else {
            Toast.makeText(this, "未找到用戶資料", Toast.LENGTH_SHORT).show();
            finish();  // 如果沒有找到用戶名，關閉當前Activity
        }

        // 設置 TextView 點擊事件，讓用戶編輯資料
        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("使用者名稱", UserDatabaseHelper.COLUMN_USERNAME);
            }
        });

        codeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("密碼", UserDatabaseHelper.COLUMN_PASSWORD);
            }
        });

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("信箱", UserDatabaseHelper.COLUMN_EMAIL);
            }
        });

        birthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("生日", UserDatabaseHelper.COLUMN_BIRTHDAY);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除用戶的 Session 或登錄信息，具體取決於你如何保存這些信息
                // 例如，你可以清除 SharedPreferences 中的登錄狀態
                getSharedPreferences("userPrefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();

                // 回到登入畫面
                Intent intent = new Intent(MemberInfoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();  // 結束當前活動
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteUser();
            }
        });
    }

    private void loadMemberInfo(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                UserDatabaseHelper.COLUMN_ID,
                UserDatabaseHelper.COLUMN_USERNAME,
                UserDatabaseHelper.COLUMN_PASSWORD,
                UserDatabaseHelper.COLUMN_EMAIL,
                UserDatabaseHelper.COLUMN_BIRTHDAY
        };
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_USERNAME));
            String code = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_EMAIL));
            String birthday = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_BIRTHDAY));

            idTextView.setText("ID： " + id);
            nameTextView.setText("使用者名稱： " + name);
            codeTextView.setText("密碼： " + code);
            emailTextView.setText("信箱： " + email);
            birthdayTextView.setText("生日： " + birthday);
        } else {
            Toast.makeText(this, "未找到用戶資料!!!!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    private void editField(String field, final String columnName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改" + field);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newValue = input.getText().toString();
                updateMemberInfo(columnName, newValue);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateMemberInfo(String columnName, String newValue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnName, newValue);

        // 根據用戶名更新該用戶的資料
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        int count = db.update(UserDatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            loadMemberInfo(username);  // 更新後重新加載資料
        } else {
            Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void confirmDeleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("刪除用戶")
                .setMessage("你確定要刪除這個用戶嗎？這個操作無法撤銷。")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteUser();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 根據用戶名刪除該用戶的資料
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        int deletedRows = db.delete(UserDatabaseHelper.TABLE_USERS, selection, selectionArgs);
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(this, "用戶已刪除", Toast.LENGTH_SHORT).show();

            // 清除用戶的 Session 或登錄信息
            getSharedPreferences("userPrefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // 跳轉到登入畫面
            Intent intent = new Intent(MemberInfoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();  // 結束當前活動
        } else {
            Toast.makeText(this, "刪除失敗", Toast.LENGTH_SHORT).show();
        }
    }

}
