package com.example.codelearning;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        // 讀取 CSV 檔案並將其內容顯示在 Spinner 中
        Spinner spinner = findViewById(R.id.spinner);
        List<String> rankingList = readCsvFileFromAssets();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rankingList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // 從 assets 讀取 CSV 文件並返回內容作為列表
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
                    // 將Title和Description合併後加入data列表中
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