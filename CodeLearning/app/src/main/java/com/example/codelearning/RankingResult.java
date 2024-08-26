package com.example.codelearning;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RankingResult extends AppCompatActivity {

    private TextView tvRankingNo1, tvRankingNo2, tvRankingNo3, tvRankingNo4, tvRankingNo5, tvYourRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvRankingNo1 = findViewById(R.id.tv_rankingNo1);
        tvRankingNo2 = findViewById(R.id.tv_rankingNo2);
        tvRankingNo3 = findViewById(R.id.tv_rankingNo3);
        tvRankingNo4 = findViewById(R.id.tv_rankingNo4);
        tvRankingNo5 = findViewById(R.id.tv_rankingNo5);
        tvYourRanking = findViewById(R.id.tv_yourRanking);

        Spinner spinner = findViewById(R.id.spinner);
        List<String> rankingList = readCsvFileFromAssets();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rankingList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 設置 Spinner 的選項選擇監聽器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根據選擇的選項更新 TextView 的內容
                if (position == 0) { // 假設這是第一個選項
                    tvRankingNo1.setText("王一明");
                    tvRankingNo2.setText("王二明");
                    tvRankingNo3.setText("王三明");
                    tvRankingNo4.setText("王四明");
                    tvRankingNo5.setText("王五明");
                    tvYourRanking.setText("六");
                }
                // 根據其他選項做類似的處理
                 else if (position == 1) {
                    tvRankingNo1.setText("王一明");
                    tvRankingNo2.setText("王五明");
                    tvRankingNo3.setText("王九明");
                    tvRankingNo4.setText("王七明");
                    tvRankingNo5.setText("王二明");
                    tvYourRanking.setText("十");
                 }
                // 根據其他選項做類似的處理
                else if (position == 2) {
                    tvRankingNo1.setText("王二明");
                    tvRankingNo2.setText("王四明");
                    tvRankingNo3.setText("王八明");
                    tvRankingNo4.setText("王一明");
                    tvRankingNo5.setText("王六明");
                    tvYourRanking.setText("十五");
                }
                // 根據其他選項做類似的處理
                else if (position == 3) {
                    tvRankingNo1.setText("王七明");
                    tvRankingNo2.setText("王八明");
                    tvRankingNo3.setText("王二明");
                    tvRankingNo4.setText("王一明");
                    tvRankingNo5.setText("王三明");
                    tvYourRanking.setText("二六");
                }
                else {
                    tvRankingNo1.setText("王五明");
                    tvRankingNo2.setText("王一明");
                    tvRankingNo3.setText("王四明");
                    tvRankingNo4.setText("王九明");
                    tvRankingNo5.setText("王十明");
                    tvYourRanking.setText("十三");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 當沒有選擇時可以選擇處理，或者保持空實現
            }
        });
    }

    private List<String> readCsvFileFromAssets() {
        List<String> data = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("units.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length > 1) {
                    data.add("CH." + tokens[0] + " - " + tokens[1]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
