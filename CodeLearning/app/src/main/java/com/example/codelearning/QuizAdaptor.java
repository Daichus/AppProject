package com.example.codelearning;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class QuizAdaptor extends ArrayAdapter<Quiz> {
    public QuizAdaptor(@NonNull Context context, ArrayList<Quiz> quiz) {
        super(context,0,quiz);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Quiz currentQuiz = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.questionlayout, parent, false);
        }

        TextView quizContent = convertView.findViewById(R.id.quizContent);
        RadioButton rb1 = convertView.findViewById(R.id.option1);
        RadioButton rb2 = convertView.findViewById(R.id.option2);
        RadioButton rb3 = convertView.findViewById(R.id.option3);
        RadioButton rb4 = convertView.findViewById(R.id.option4);
        Button submitBtn = convertView.findViewById(R.id.submitBtn);
        Button restartBtn = convertView.findViewById(R.id.restartBtn);
        TextView testResult = convertView.findViewById(R.id.testResult);


        quizContent.setText(currentQuiz.getQuizContent());
        rb1.setText(currentQuiz.getOpt1());
        rb2.setText(currentQuiz.getOpt2());
        rb3.setText(currentQuiz.getOpt3());
        rb4.setText(currentQuiz.getOpt4());

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                testResult.setText("");
                testResult.setTextColor(Color.BLACK);
            }
        });

        // 判斷哪一個 RadioButton 被選中,與答案做對比,答案可透過調用currentQuiz的getAnswer取得
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected = "";
                if (rb1.isChecked()) {
                    selected = "1";
                } else if (rb2.isChecked()) {
                    selected = "2";
                } else if (rb3.isChecked()) {
                    selected = "3";
                } else if (rb4.isChecked()) {
                    selected = "4";
                }
                if(selected.equals(currentQuiz.getAnswer())) {
                    testResult.setText("答對了!");
                    testResult.setTextColor(Color.GREEN);
                } else {
                    testResult.setText("錯了!");
                    testResult.setTextColor(Color.RED);
                }
            }
        });

        return convertView;

    }

}
