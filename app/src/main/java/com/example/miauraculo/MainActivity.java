package com.example.miauraculo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private AutoCompleteTextView dropdown;
    private TextInputLayout dropdownLayout;
    private MaterialButton btnProximo;
    private TextView tvEnergiaStatus;

    private int targetRetries;
    private int currentAttempts = 0;
    private boolean handInside = false;
    private boolean isEnergyRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropdown = findViewById(R.id.tipoLeituraDropdown);
        dropdownLayout = findViewById(R.id.dropdownLayout);
        btnProximo = findViewById(R.id.btnProximo);
        tvEnergiaStatus = findViewById(R.id.tvEnergiaStatus);

        setupSensor();
        setupDropdown();
        setupButton();
        animateUI();
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    private void setupRetryTrick() {
        targetRetries = new Random().nextInt(3) + 1;
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
        tvEnergiaStatus.setText("Aproxime a mão do topo da tela para ler sua energia...");
        tvEnergiaStatus.setTextColor(Color.parseColor("#FFEB3B"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnergyRead = false;
        currentAttempts = 0;
        handInside = false;
        setupRetryTrick();
        lockInterface();

        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (proximitySensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_PROXIMITY || isEnergyRead) return;

        float distance = event.values[0];
        boolean isClose = distance < Math.min(proximitySensor.getMaximumRange(), 5.0f);

        if (isClose && !handInside) {
            handInside = true;
            handleProximityTrigger();
        } else if (!isClose && handInside) {
            handInside = false;
            handleProximityRelease();
        }
    }

    /**
     * Handles the logic when the hand is detected near the sensor.
     */
    private void handleProximityTrigger() {
        if (currentAttempts < targetRetries) {
            currentAttempts++;
            tvEnergiaStatus.setText("Energia recebida. Aproxime a mão novamente!");
            tvEnergiaStatus.setTextColor(Color.parseColor("#FF9800"));
        } else {
            unlockInterface();
        }
    }


    private void handleProximityRelease() {
        if (currentAttempts < targetRetries) {
            tvEnergiaStatus.setText("Flutuação de energia detectada, porém está muito negativa. Afaste a mão para tentar novamente.");
            tvEnergiaStatus.setTextColor(Color.parseColor("#FFEB3B")); // Amarelo
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }


    private void unlockInterface() {
        isEnergyRead = true;
        sensorManager.unregisterListener(this);

        tvEnergiaStatus.setText("Energia positiva coletada! Escolha a leitura.");
        tvEnergiaStatus.setTextColor(Color.parseColor("#A5D6A7"));

        dropdownLayout.setEnabled(true);
        btnProximo.setEnabled(true);
    }
}