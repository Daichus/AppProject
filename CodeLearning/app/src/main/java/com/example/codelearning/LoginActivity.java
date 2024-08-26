package com.example.codelearning;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDatabaseHelper(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                long currentUserId = dbHelper.authenticateUser(username, password);
                if (currentUserId != -1) {
                    // 登入成功，啟動 NewNoteActivity 並傳遞用戶 ID
                    Intent intent = new Intent(LoginActivity.this, NewNoteActivity.class);
                    intent.putExtra("_ID", currentUserId);
                    startActivity(intent);

                    // 可能要結束登入 Activity
                    finish();
                } else {
                    // 登入失敗，顯示錯誤消息
                    Toast.makeText(LoginActivity.this, "登入失敗，請檢查用戶名和密碼", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {UserDatabaseHelper.COLUMN_ID};
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                UserDatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}
