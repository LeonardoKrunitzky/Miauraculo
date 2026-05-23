package com.example.miauraculo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReadingActivity extends AppCompatActivity {

    private ImageView mascot, card1, card2, card3;
    private FrameLayout bubble;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        mascot = findViewById(R.id.mascotMiauraculo);
        card1 = findViewById(R.id.imgCard1);
        card2 = findViewById(R.id.imgCard2);
        card3 = findViewById(R.id.imgCard3);
        bubble = findViewById(R.id.bubbleContainer);
        txtMessage = findViewById(R.id.txtMascotMessage);

        startMysticAnimations();
        retrieveReadingType();
    }

    private void startMysticAnimations() {
        // 1. Mascote subindo suavemente do canto
        mascot.animate()
                .translationY(0)
                .setDuration(1000)
                .setStartDelay(500)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    // 2. Balão de fala aparece
                    bubble.setScaleX(0);
                    bubble.setScaleY(0);
                    bubble.setAlpha(1f);
                    bubble.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(500)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .start();
                })
                .start();

        // 3. Efeito de flutuação nas três cartas com tempos desencontrados
        animateFloatingCard(card1, 0);
        animateFloatingCard(card2, 400); // 400ms de atraso
        animateFloatingCard(card3, 800); // 800ms de atraso
    }

    /**
     * Aplica um efeito de flutuação vertical infinito em uma View.
     *
     * @param card  A View (carta) que será animada.
     * @param delay Tempo de espera antes de iniciar a animação (em milissegundos).
     */
    private void animateFloatingCard(View card, long delay) {
        ObjectAnimator floatingAnim = ObjectAnimator.ofFloat(card, "translationY", -20f, 20f);
        floatingAnim.setDuration(2000);
        floatingAnim.setStartDelay(delay);
        floatingAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatingAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatingAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        floatingAnim.start();
    }

    private void retrieveReadingType() {
        String tipoLeitura = getIntent().getStringExtra("TIPO_LEITURA");

        if (tipoLeitura != null && !tipoLeitura.isEmpty()) {
            setMascotText("Miau meu consagrado! Preparando as cartas para: " + tipoLeitura + ". Revele a carta clicando na tela para começar.");
        }
    }

    public void setMascotText(String message) {
        txtMessage.setText(message);
    }
}