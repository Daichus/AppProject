package com.example.codelearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    private List<String[]> receivedQuizData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //接收從MainActivity中傳過來的數據,類型會是一個儲存字串陣列的List
        //unitTitle則是使用者選取的章節名稱,取得後直接將PracticeActivity中
        // 的標題View chaptorTitle設定為該章節名稱
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receivedQuizData = (List<String[]>) bundle.getSerializable("quizData");
            String unitTitle = bundle.getString("unitTitle");
            TextView chaptorTitle = findViewById(R.id.chaptorTitle);
            setQuiz(receivedQuizData);
            chaptorTitle.setText(unitTitle);
        } else {
            Log.e("PracticeActivity", "No data in Intent extras.");
        }
    }
    //這個方法用於初始化管理題目的Adaptor,每一個子View透過一個Quiz類別初始化,再套用到questionsView
    //從MainActivity中接收到數據,也就是之前已經處理成陣列的題目字串,接下來每一個字串都要用於初始化Quiz類別,
    //再將Quiz物件傳入quizsList的List中儲存,這是因為Adaptor需要List作為參數來初始化
    //最後一步是將List傳入QuizAdaptor,這個QuizAdaptor是我們自定義的布局管理器
    //quiz的屬性可以參照Quiz類中的代碼
    //參照QuizAdaptor中的代碼
    public void setQuiz (List<String[]> quizs) {
        ArrayList<Quiz> quizsList = new ArrayList<>();
        for (String [] quiz : quizs) {
            quizsList.add(new Quiz(quiz[1],quiz[2],quiz[3],quiz[4],quiz[5],quiz[6]));
        }
        QuizAdaptor quizAdaptor = new QuizAdaptor(this,quizsList);
        ListView questionsView = findViewById(R.id.questions);
        questionsView.setAdapter(quizAdaptor);
    }

    //離開當前頁面,暫訂回傳值為1的int
    public void leavePgn (View view) {
        Intent intent = new Intent();
        intent.putExtra("quizCompleted", 1);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}