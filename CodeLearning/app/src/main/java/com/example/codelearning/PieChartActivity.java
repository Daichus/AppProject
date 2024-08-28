package com.example.codelearning;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Description; // 確認這行引用的是 MPAndroidChart 的 Description 類別

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_piechart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PieChart pieChart = findViewById(R.id.chart);

        ArrayList<PieEntry> entiers = new ArrayList<>();
        entiers.add(new PieEntry(80f,"答對率"));
        entiers.add(new PieEntry(20f,"答錯率"));
//        entiers.add(new PieEntry(75f,"English"));
//        entiers.add(new PieEntry(60f,"IT"));

        PieDataSet pieDataSet = new PieDataSet(entiers, "Subjects");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(20f);  // 設置 PieEntry 的字型大小

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // 創建並設置描述
        Description description = new Description(); // 正確的建構方式
        description.setText("答對：8 題  答錯：2 題  你的排名：第十名  "); // 設置描述文字
        description.setTextSize(20f);    // 設置描述文字的字型大小
        description.setTextColor(Color.BLACK); // 設置描述文字的顏色
        pieChart.setDescription(description);  // 將描述文字設定給 PieChart

        pieChart.animateY(1000);
        pieChart.invalidate();
    }

}