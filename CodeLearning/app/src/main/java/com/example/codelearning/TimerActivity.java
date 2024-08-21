package com.example.codelearning;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimerActivity extends AppCompatActivity {

    private TextView timerValue;
    private TextView hintText;
    private EditText studyTimeInput;
    private EditText breakTimeInput;
    private Spinner cycleSpinner;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private int cycleCount = 0;
    private int totalCycles;
    private boolean isTimerRunning = false;
    private boolean isPaused = false;
    private long timeLeftInMillis;
    private boolean isBreakTime = false;
    private boolean isStudyTimeFirstPhase = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerValue = findViewById(R.id.timer_value);
        hintText = findViewById(R.id.hint_text);
        studyTimeInput = findViewById(R.id.study_time_input);
        breakTimeInput = findViewById(R.id.break_time_input);
        cycleSpinner = findViewById(R.id.cycle_spinner);
        startButton = findViewById(R.id.start_button);

        // 設置 Spinner 的選項
        Integer[] cycles = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cycles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cycleSpinner.setAdapter(adapter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    if (studyTimeInput.getText().toString().isEmpty() ||
                            breakTimeInput.getText().toString().isEmpty()) {
                        Toast.makeText(TimerActivity.this, "請輸入所有值！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isPaused) { // 新的計時開始
                            int studyTime = Integer.parseInt(studyTimeInput.getText().toString()) * 60 * 1000;
                            int breakTime = Integer.parseInt(breakTimeInput.getText().toString()) * 60 * 1000;
                            totalCycles = (Integer) cycleSpinner.getSelectedItem();
                            timeLeftInMillis = studyTime;
                            isBreakTime = false;
                            isStudyTimeFirstPhase = true;
                        }
                        startCycle();
                        startButton.setText("暫停");
                        isTimerRunning = true;
                        isPaused = false;
                    }
                } else {
                    pauseTimer();
                }
            }
        });
    }

    private void startCycle() {
        updateHintText();
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                timerValue.setText(String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                if (isStudyTimeFirstPhase) {
                    timeLeftInMillis = Integer.parseInt(breakTimeInput.getText().toString()) * 60 * 1000;
                    isBreakTime = true;
                    isStudyTimeFirstPhase = false;
                } else if (isBreakTime) {
                    timeLeftInMillis = Integer.parseInt(studyTimeInput.getText().toString()) * 60 * 1000;
                    isBreakTime = false;
                    if (cycleCount < totalCycles) {
                        cycleCount++;
                    }
                } else {
                    if (cycleCount < totalCycles) {
                        timeLeftInMillis = Integer.parseInt(breakTimeInput.getText().toString()) * 60 * 1000;
                        isBreakTime = true;
                    } else {
                        timerValue.setText("完成所有輪次！");
                        startButton.setText("開始計時");
                        isTimerRunning = false;
                        isPaused = false;
                        return;
                    }
                }
                startCycle();
            }
        }.start();
    }

    private void updateHintText() {
        if (isBreakTime) {
            hintText.setText("休息時間");
        } else {
            hintText.setText("學習時間");
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        isPaused = true;
        startButton.setText("繼續計時");
    }
}