package com.example.miauraculo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReadingActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private boolean isProcessing = false;
    private ImageView mascot, card1, card2, card3;
    private FrameLayout bubble;
    private TextView txtMessage;
    private int currentStep = 0;

    private String tipoLeitura;
    private String[] leiturasSalvas = new String[3];

    private List<Integer> deckIndices;

    private final int[] cardDrawables = {
            R.drawable.tarot_louco, R.drawable.tarot_mago, R.drawable.tarot_sacerdotisa,
            R.drawable.tarot_imperatriz, R.drawable.tarot_imperador, R.drawable.tarot_hierofante,
            R.drawable.tarot_enamorados, R.drawable.tarot_carro, R.drawable.tarot_forca,
            R.drawable.tarot_eremita, R.drawable.tarot_fortuna, R.drawable.tarot_justica,
            R.drawable.tarot_enforcado, R.drawable.tarot_morte, R.drawable.tarot_temperanca,
            R.drawable.tarot_diabo, R.drawable.tarot_torre, R.drawable.tarot_estrela,
            R.drawable.tarot_lua, R.drawable.tarot_sol, R.drawable.tarot_julgamento,
            R.drawable.tarot_mundo
    };
    private final String[] cardNames = {
            "O Louco", "O Mago", "A Sacerdotisa", "A Imperatriz", "O Imperador",
            "O Hierofante", "Os Enamorados", "O Carro", "A Força", "O Eremita",
            "A Roda da Fortuna", "A Justiça", "O Enforcado", "A Morte", "A Temperança",
            "O Diabo", "A Torre", "A Estrela", "A Lua", "O Sol",
            "O Julgamento", "O Mundo"
    };
    private final String MISTRAL_API_KEY = BuildConfig.MISTRAL_API_KEY;
    private MediaPlayer bgMusic;
    private MediaPlayer flipSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        tipoLeitura = getIntent().getStringExtra("TIPO_LEITURA");

        mascot = findViewById(R.id.mascotMiauraculo);
        card1 = findViewById(R.id.imgCard1);
        card2 = findViewById(R.id.imgCard2);
        card3 = findViewById(R.id.imgCard3);
        bubble = findViewById(R.id.bubbleContainer);
        txtMessage = findViewById(R.id.txtMascotMessage);

        disableBackButton();
        setupDeck();
        startMysticAnimations();
        retrieveReadingType();
        setupAudio();

        findViewById(android.R.id.content).setOnClickListener(v -> handleScreenClick());

        card1.setOnClickListener(v -> reLerCarta(0));
        card2.setOnClickListener(v -> reLerCarta(1));
        card3.setOnClickListener(v -> reLerCarta(2));

        mascot.setOnClickListener(v -> {
            if (currentStep >= 4 && !isProcessing) {
                isProcessing = true;
                setMascotText("Os astros falaram. Agora é com você, humano. Miau!");
                new Handler(Looper.getMainLooper()).postDelayed(this::animateMascotExit, 2000);
            }
        });
    }

    private String getSubtitulo(String tipo, int stepIndex) {
        if (tipo == null) return "Passado";

        switch (tipo.toLowerCase().replace("õ", "o").replace("ã", "a")) {
            case "carreira":
                return stepIndex == 0 ? "Situação Atual" : (stepIndex == 1 ? "Ação Recomendada" : "Resultado Esperado");
            case "decisoes":
                return stepIndex == 0 ? "Prós" : (stepIndex == 1 ? "Contras" : "Conselho");
            case "autoconhecimento":
                return stepIndex == 0 ? "Mente" : (stepIndex == 1 ? "Corpo" : "Espírito");
            case "padrao":
            default:
                return stepIndex == 0 ? "Passado" : (stepIndex == 1 ? "Presente" : "Futuro");
        }
    }

    private void reLerCarta(int index) {
        if (currentStep >= 4 && leiturasSalvas[index] != null) {
            setMascotText(leiturasSalvas[index]);
        }
    }

    private void disableBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }

    private void setupAudio() {
        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        if (bgMusic != null) {
            bgMusic.setLooping(true);
            bgMusic.start();
        }
        flipSound = MediaPlayer.create(this, R.raw.card_reveal);
    }

    private void setupDeck() {
        deckIndices = new ArrayList<>();
        for (int i = 0; i < cardDrawables.length; i++) {
            deckIndices.add(i);
        }
        Collections.shuffle(deckIndices);
    }

    private void handleScreenClick() {
        if (isProcessing) return;

        if (currentStep == 0) {
            revealCard(card1, deckIndices.get(0), 0);
            currentStep++;
        } else if (currentStep == 1) {
            revealCard(card2, deckIndices.get(1), 1);
            currentStep++;
        } else if (currentStep == 2) {
            revealCard(card3, deckIndices.get(2), 2);
            currentStep++;
        } else if (currentStep == 3) {
            setMascotText("As cartas foram tiradas! Clique nelas para reler as mensagens, ou clique em mim para encerrar. Miau!");
            currentStep++;
        } else if (currentStep >= 4) {
            setMascotText("As cartas foram tiradas! Clique nelas para reler as mensagens, ou clique em mim para encerrar. Miau!");
        }
    }

    private void revealCard(ImageView card, int cardIndex, int stepIndex) {
        isProcessing = true;
        String nomeDaCarta = cardNames[cardIndex];

        if (flipSound != null) {
            flipSound.seekTo(0);
            flipSound.start();
        }

        card.animate().rotationY(90f).setDuration(300).withEndAction(() -> {
            card.setImageResource(cardDrawables[cardIndex]);

            setMascotText("Miau... interpretando " + nomeDaCarta + "...");
            card.animate().rotationY(0f).setDuration(900).start();

            String subtitulo = getSubtitulo(tipoLeitura, stepIndex);

            String seuPrompt = "Você é o Miauráculo, um gato místico. " +
                    "Leitura de " + tipoLeitura + " com foco em: '" + subtitulo + "'. Carta: " + nomeDaCarta + ". " +
                    "Explique o significado dessa carta para essa posição específica da leitura de forma mística e acolhedora. " +
                    "Regras estritas: " +
                    "1. Responda em um único parágrafo. " +
                    "2. Use no máximo 3 a 4 frases curtas. " +
                    "3. Inclua um leve toque felino no final.";

            mistralCall(seuPrompt, stepIndex);
        }).start();
    }

    private void mistralCall(String prompt, int stepIndex) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "open-mistral-nemo");

            JSONArray messages = new JSONArray();
            JSONObject messageNode = new JSONObject();
            messageNode.put("role", "user");
            messageNode.put("content", prompt);
            messages.put(messageNode);

            jsonBody.put("messages", messages);

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://api.mistral.ai/v1/chat/completions")
                    .addHeader("Authorization", "Bearer " + MISTRAL_API_KEY)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        setMascotText("Miau, minha bola de cristal falhou (Erro de conexão). Tente novamente.");
                        isProcessing = false;
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);

                            String llmReply = jsonObject.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            String subtitulo = getSubtitulo(tipoLeitura, stepIndex);
                            String textoFinal = "<b>" + subtitulo + "</b><br><br>" + llmReply;

                            leiturasSalvas[stepIndex] = textoFinal;

                            runOnUiThread(() -> {
                                setMascotText(textoFinal);
                                isProcessing = false;
                            });

                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                setMascotText("Miau... As cartas estão confusas (Erro ao ler os dados).");
                                isProcessing = false;
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            setMascotText("Miau! Os astros negaram a resposta. (Erro na API: " + response.code() + ")");
                            isProcessing = false;
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            isProcessing = false;
        }
    }

    private void startMysticAnimations() {
        mascot.animate()
                .translationY(0)
                .setDuration(1000)
                .setStartDelay(500)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
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

        animateFloatingCard(card1, 0);
        animateFloatingCard(card2, 400);
        animateFloatingCard(card3, 800);
    }

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
        if (tipoLeitura != null && !tipoLeitura.isEmpty()) {
            setMascotText("Miau meu consagrado! Preparando as cartas para: " + tipoLeitura + ". Revele a carta clicando na tela para começar.");
        }
    }

    public void setMascotText(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtMessage.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtMessage.setText(Html.fromHtml(message));
        }
    }

    private void animateMascotExit() {
        bubble.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    mascot.animate()
                            .translationY(1500f)
                            .setDuration(800)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .withEndAction(this::finish)
                            .start();
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic != null) {
            if (bgMusic.isPlaying()) {
                bgMusic.stop();
            }
            bgMusic.release();
            bgMusic = null;
        }
        if (flipSound != null) {
            flipSound.release();
            flipSound = null;
        }
    }
}