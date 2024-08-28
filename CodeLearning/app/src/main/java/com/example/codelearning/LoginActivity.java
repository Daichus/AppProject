package com.example.codelearning;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

                // 驗證登錄並獲取用戶 ID
                long userid = validateLogin(username, password);

                if (userid != -1) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("_ID", userid);  // 傳遞用戶 ID
                    intent.putExtra("username", username);  // 傳遞用戶名
                    startActivity(intent);
                    finish(); // 可選，防止用戶按返回鍵回到登錄頁面
                } else {
                    Toast.makeText(LoginActivity.this, "登入失敗，請檢查您的用戶名和密碼", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private long validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {UserDatabaseHelper.COLUMN_ID};
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                UserDatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        long userid = -1; // 默認為 -1，表示用戶不存在
        if (cursor.moveToFirst()) {
            userid = cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_ID));
        }

        cursor.close();
        db.close();
        return userid;
    }
}
