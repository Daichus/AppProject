package com.example.codelearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> items;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        tvTitle = findViewById(R.id.tv_title);
        items = new ArrayList<>();

        // 讀取CSV檔案並顯示在ListView
        readCSVAndDisplay();
    }

    private void readCSVAndDisplay() {
        try {
            // 開啟CSV檔案
            InputStream inputStream = getAssets().open("units.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            // 讀取每一行資料
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length > 1) {
                    // 將Title和Description合併後加入items列表中
                    items.add("CH." + tokens[0] + " - " + tokens[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 將資料顯示在ListView中
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    // 點擊測驗按鈕時的處理
    public void goToTesting(View view) {
        tvTitle.setText("測驗"); // 修改TextView的文本為"測驗"
//        Intent intent = new Intent(this, TestingActivity.class); // 假設有一個TestingActivity
//        startActivity(intent);
    }

    // 點擊排行榜按鈕時的處理
    public void goToRanking(View view) {
        tvTitle.setText("排行榜"); // 修改TextView的文本為"排行榜"
//        Intent intent = new Intent(this, RankingActivity.class); // 假設有一個RankingActivity
//        startActivity(intent);
    }

    //點擊goToPieChart_Button去往圓餅圖畫面
    public void goToPieChart(View view) {
        Intent intent = new Intent(this, PieChartActivity.class);
        startActivity(intent);
    }

    //點擊goToRankingResult_Button去往排行榜畫面
    public void goToRankingResult(View view) {
        Intent intent = new Intent(this, RankingResult.class);
        startActivity(intent);
    }

}