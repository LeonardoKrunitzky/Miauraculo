package com.example.miauraculo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView dropdown;
    private TextInputLayout dropdownLayout;
    private MaterialButton btnProximo;
    private TextView tvEnergiaStatus;
    private ImageView ivTouchArea;

    private boolean isEnergyRead = false;
    private boolean isPressing = false;
    private int energyLevel = 0;
    private final int MAX_ENERGY = 100;
    private float currentTouchPressure = 0f;

    private final Handler chargeHandler = new Handler(Looper.getMainLooper());
    private Runnable chargeRunnable;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropdown = findViewById(R.id.tipoLeituraDropdown);
        dropdownLayout = findViewById(R.id.dropdownLayout);
        btnProximo = findViewById(R.id.btnProximo);
        tvEnergiaStatus = findViewById(R.id.tvEnergiaStatus);
        ivTouchArea = findViewById(R.id.ivTouchArea);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setupTouchSensor();
        setupDropdown();
        setupButton();
        animateUI();
    }

    private void vibrateDevice(long duration) {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(duration);
            }
        }
    }

    private void setupTouchSensor() {
        ivTouchArea.setOnTouchListener((v, event) -> {
            if (isEnergyRead) return false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isPressing = true;
                    currentTouchPressure = event.getPressure();
                    vibrateDevice(50);
                    startCharging();
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    currentTouchPressure = event.getPressure();
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isPressing = false;
                    stopCharging();
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    return true;
            }
            return false;
        });
    }

    private void startCharging() {
        tvEnergiaStatus.setText("Canalizando energia... Mantenha pressionado!");
        tvEnergiaStatus.setTextColor(Color.parseColor("#FF9800"));

        chargeRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPressing) return;

                int chargeStep = 2 + (int) (currentTouchPressure * 15);
                energyLevel += chargeStep;

                if (energyLevel >= MAX_ENERGY) {
                    unlockInterface();
                } else {
                    chargeHandler.postDelayed(this, 100);
                }
            }
        };
        chargeHandler.post(chargeRunnable);
    }

    private void stopCharging() {
        chargeHandler.removeCallbacks(chargeRunnable);
        if (!isEnergyRead) {
            energyLevel = 0;
            tvEnergiaStatus.setText("Conexão perdida. Pressione novamente a pata para carregar.");
            tvEnergiaStatus.setTextColor(Color.parseColor("#FFEB3B"));
        }
    }

    private void setupDropdown() {
        String[] opcoesDeLeitura = {"Leitura Padrão", "Leitura de Carreira", "Leitura de Decisões", "Autoconhecimento"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_dropdown, opcoesDeLeitura);
        dropdown.setAdapter(adapter);
    }

    private void setupButton() {
        btnProximo.setOnClickListener(v -> {
            String tipoSelecionado = dropdown.getText().toString();
            Intent intent = new Intent(MainActivity.this, ReadingActivity.class);
            intent.putExtra("TIPO_LEITURA", tipoSelecionado);
            startActivity(intent);
        });
    }

    private void animateUI() {
        LinearLayout uiContainer = findViewById(R.id.uiContainer);
        uiContainer.setTranslationY(80f);
        uiContainer.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(800)
                .start();
    }

    private void lockInterface() {
        dropdownLayout.setEnabled(false);
        btnProximo.setEnabled(false);
        tvEnergiaStatus.setText("Pressione e segure a pata para ler sua energia...");
        tvEnergiaStatus.setTextColor(Color.parseColor("#FFEB3B"));
    }

    private void unlockInterface() {
        isEnergyRead = true;
        isPressing = false;
        chargeHandler.removeCallbacks(chargeRunnable);

        vibrateDevice(300);

        tvEnergiaStatus.setText("Energia positiva coletada! Escolha a leitura.");
        tvEnergiaStatus.setTextColor(Color.parseColor("#A5D6A7"));

        dropdownLayout.setEnabled(true);
        btnProximo.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnergyRead = false;
        energyLevel = 0;
        lockInterface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPressing = false;
        chargeHandler.removeCallbacks(chargeRunnable);
    }
}