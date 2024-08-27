package com.example.codelearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivityResultLauncher<Intent> practiceActivityLauncher;
    private List<String> unitsId = new ArrayList<>();
    private List<String> unitsName = new ArrayList<>();
    private List<String[]> quizList1 = new ArrayList<>();
    private List<String[]> quizList2 = new ArrayList<>();
    private List<String[]> quizList3 = new ArrayList<>();
    private List<String[]> quizList4 = new ArrayList<>();
    private List<String[]> quizList5 = new ArrayList<>();
    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //onCreate執行時讀取units的csv檔案
        // 讀取到的units檔案存入unitsName和unitsList屬性,這就是章節名稱,用於初始化Spinner和傳遞章節名稱
        List<String> unitsList = readUnitsData();
        if(unitsList !=null && !unitsList.isEmpty()) {
            unitsName = unitsList;
            loadSpinnerData(unitsList);
        } else {
            Log.e("MainActivity", "Failed to load units data or data is empty");
        }
        readQuizData();
        practiceActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent practiceResultData = result.getData();
                            if(practiceResultData != null) {
                                int userPracticeResult = practiceResultData.getIntExtra("quizCompleted",0);
                            }
                        }
                    }
                });
        Spinner mainSpinner = findViewById(R.id.unitsSpinner);
        mainSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //從units csv檔中提取的單元名稱加入List中後,將做為初始化Spinner需用的資料
    private void loadSpinnerData(List<String> unitsList) {
        Spinner unitsSpinner = findViewById(R.id.unitsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitsSpinner.setAdapter(adapter);
    }
    //此方法會讀取位於assets項目下的units csv檔案,將內部units id與名稱存入一個List<String>並返還
    //Spinner 以此為數據源
    //每一行資料進行切割成陣列以後,取出為ID與單元名稱,ID將會被存入名為unitsId的屬性中備用
    private List<String> readUnitsData() {
        List<String>unitsList = new ArrayList<>();
        try {
            InputStream is = getAssets().open("units_202408232342_new.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                String unitId = columns[0];
                String unitName = columns[1].trim();
                unitsId.add(unitId);
                unitsList.add(unitName);
            }
            reader.close();
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading CSV file", e);
            return null;
        }
        return unitsList;
    }

    //讀取題目的csv檔案並處理為陣列,根據id放入不同的list中以供intent備用
    //陣列呈現狀態為:["id","question","option1","option2","option3","option4","answer","unit_id"]
    //quizList共有五個,對應不同的章節,因此需要對比每一個陣列的unit id,也就是陣列[7],以決定題目字串要放到哪一個quizList中
    private void readQuizData() {
        try {
            InputStream is = getAssets().open("quiz_202408232342_new.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String quizLine;
            reader.readLine();
            while ((quizLine = reader.readLine()) != null) {
                String[] columns = quizLine.split(",");
                String quizId = columns[7];

                switch (quizId) {
                    case "1":
                        quizList1.add(columns);
                        break;
                    case "2":
                        quizList2.add(columns);
                        break;
                    case "3":
                        quizList3.add(columns);
                        break;
                    case "4":
                        quizList4.add(columns);
                        break;
                    case "5":
                        quizList5.add(columns);
                        break;
                    default:
                        Log.e("readQuizData", "Unexpected quizId: " + quizId);
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e("readQuizData", "Error reading CSV file", e);
        }
    }


    //當使用者選取選項之一時,透過回傳選取view的位置,拿取相對應的題目quizList,和章節名稱
    //透過intent傳遞給PracticeActivity,PracticeActivity便可以設定選取到的章節名稱,和用quizList初始化題目的布局等
    //用Bundle包裝要傳遞的quizList,以及章節名
    @Override
    public void onItemSelected(AdapterView<?> unitSpinner, View spinnerView, int i, long l) {

        if (!isSpinnerInitialized) {
            isSpinnerInitialized = true;
            return;
        }
        String selectedUnitId = unitsId.get(i);
        String selectedUnitName = unitsName.get(i);
        List<String[]> selectedQuizData ;
        switch(selectedUnitId) {
            case "1":
                selectedQuizData = quizList1;
                break;
            case "2":
                selectedQuizData = quizList2;
                break;
            case "3":
                selectedQuizData = quizList3;
                break;
            case "4":
                selectedQuizData = quizList4;
                break;
            case "5":
                selectedQuizData = quizList5;
                break;
            default:
                Log.e("onItemSelected", "Unexpected Unit ID: " + selectedUnitId);
                return;
        }
        Intent intent = new Intent(this, PracticeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("quizData",(Serializable)selectedQuizData);
        bundle.putString("unitTitle",selectedUnitName);
        intent.putExtras(bundle);
        practiceActivityLauncher.launch(intent);
    }
}