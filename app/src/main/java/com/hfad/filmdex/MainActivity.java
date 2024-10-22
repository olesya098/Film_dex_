package com.hfad.filmdex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        ProgressBar progressBar = findViewById(R.id.progressBar2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запускаем ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);

                // Имитация длительной операции
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 100; i++) {
                            try {
                                Thread.sleep(30); // Задержка для имитации работы
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.setProgress(i);
                        }

                        // Переход на MainActivity
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                                startActivity(intent);
                                finish(); // Закрываем текущую активность
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
