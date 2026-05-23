package com.example.miauraculo;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDropdown();
        setupButton();
        animateUI();
    }

    private void setupDropdown() {
        String[] opcoesDeLeitura = {"Leitura Padrão", "Leitura de Carreira", "Leitura de Decisões", "Autoconhecimento"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_dropdown,
                opcoesDeLeitura
        );

        AutoCompleteTextView dropdown = findViewById(R.id.tipoLeituraDropdown);
        dropdown.setAdapter(adapter);
    }

    private void setupButton() {
        MaterialButton btnProximo = findViewById(R.id.btnProximo);
        AutoCompleteTextView dropdown = findViewById(R.id.tipoLeituraDropdown);

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
}