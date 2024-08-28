package com.example.codelearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ChecklistActivity extends AppCompatActivity {
    String action, id= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checklist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = this.getIntent().getExtras();
        action = bundle.getString("ACTION");
        if(bundle != null && action.equals("edit")){
            String todo = String.format(bundle.getString("TODO"));
            id = String.format(bundle.getString("ID"));

            TextInputLayout target = (TextInputLayout) findViewById(R.id.target);
            target.getEditText().setText(todo);
        }

        Button newButton = (Button) findViewById(R.id.save);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.target);
                String target = String.valueOf(textInputLayout.getEditText().getText());
                Intent intent = new Intent();
                intent.putExtra("TODO", target);
                intent.putExtra("ACTION", action);
                if(!Objects.equals(id, "")){
                    intent.putExtra("ID", id);
                }

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}