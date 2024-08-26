package com.example.codelearning;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, emailEditText, birthdayEditText;
    private Button registerButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new UserDatabaseHelper(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        birthdayEditText = findViewById(R.id.birthday);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String birthday = birthdayEditText.getText().toString();

                if (validateInput(username, password, email, birthday)) {
                    if (isUsernameTaken(username)) {
                        Toast.makeText(RegisterActivity.this, "用戶名稱已被使用，請選擇其他名稱", Toast.LENGTH_SHORT).show();
                    } else {
                        saveUserToDatabase(username, password, email, birthday);
                        Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "請檢查輸入資料", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput(String username, String password, String email, String birthday) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
            return false;
        }

        // 檢查密碼是否包含至少一個字母和一個數字
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            Toast.makeText(this, "密碼必須包含至少一個字母和一個數字", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isUsernameTaken(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {UserDatabaseHelper.COLUMN_ID};
        String selection = UserDatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(UserDatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean usernameExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return usernameExists;
    }

    private void saveUserToDatabase(String username, String password, String email, String birthday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDatabaseHelper.COLUMN_USERNAME, username);
        values.put(UserDatabaseHelper.COLUMN_PASSWORD, password);
        values.put(UserDatabaseHelper.COLUMN_EMAIL, email);
        values.put(UserDatabaseHelper.COLUMN_BIRTHDAY, birthday);

        db.insert(UserDatabaseHelper.TABLE_USERS, null, values);
        db.close();
    }
}
