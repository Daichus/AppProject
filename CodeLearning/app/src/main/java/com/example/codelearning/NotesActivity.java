package com.example.codelearning;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    String category,name, content, action;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle = this.getIntent().getExtras();
        action = bundle.getString("ACTION");
        if (bundle != null && action.equals("edit")){
            category = String.format(bundle.getString("CATEGORY"));
            name = String.format(bundle.getString("NAME"));
            content = String.format(bundle.getString("CONTENT"));
            index = bundle.getInt("INDEX");


            EditText tvCategory = (EditText)findViewById(R.id.newCategory);
            tvCategory.setText(category);
            EditText tvName = (EditText)findViewById(R.id.newName);
            tvName.setText(name);
            TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.contentTextInputLayout);
            textInputLayout.getEditText().setText(content);
        }
    }

    public void ClickSave(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String titleText = action.equals("edit") ? "確定修改？" : "確定新增？";
        String contentText = action.equals("edit") ? "確定修改內容並儲存？" : "確定內容並儲存？";

        dialog.setTitle(titleText);
        dialog.setMessage(contentText);
        dialog.setCancelable(true);
        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String category = ((EditText)findViewById(R.id.newCategory)).getText().toString();
                String name = ((EditText)findViewById(R.id.newName)).getText().toString();
                TextInputLayout textInputLayout = findViewById(R.id.contentTextInputLayout);
                String content = textInputLayout.getEditText().getText().toString();

                Intent intent = new Intent();
                intent.putExtra("NAME", name);
                intent.putExtra("CONTENT", content);
                intent.putExtra("CATEGORY", category);
                intent.putExtra("ACTION", action);

                if(action.equals("edit")){
                    intent.putExtra("INDEX", index);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        dialog.setNeutralButton("取消", null);
        dialog.show();
    }



    public void ClickGiveUp(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("確定放棄？");
        dialog.setMessage("確定放棄並返回主畫面？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {finish();}
        });
        dialog.setNeutralButton("取消",null);
        dialog.show();
    }
}