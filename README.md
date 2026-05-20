# 🔮🐈‍⬛ Miauráculo

Aplicativo mobile Android nativo (Java) que simula um oráculo místico guiado por um gato. O app realiza tiragens de cartas e utiliza a API da MistralAI para gerar interpretações dinâmicas baseadas na categoria de leitura escolhida pelo usuário.

## 🚀 Funcionalidades e Fluxo de Uso

### Interação Inicial
* O usuário aproxima a mão do celular (implementado com um delay/cálculo randômico de 3x para simular a "leitura de energia").

### Tipos de Leitura (Sorteio de 3 cartas)
* **Padrão / Vida:** Passado | Presente | Futuro
* **Carreira / Projetos:** Situação atual | Ação recomendada | Resultado esperado
* **Decisões:** Prós | Contras | Conselho
* **Autoconhecimento:** Mente | Corpo | Espírito

### Sorteio e Análise (Loop de 3 etapas)
1. O app sorteia uma carta e exibe na tela.
2. Ao clicar na carta, os dados (**Carta** + **Tipo de Leitura**) são enviados para a API da **MistralAI**.
3. O mascote "Miauráculo" aparece na tela explicando o significado em um balão de diálogo.
4. O processo se repete até que 3 cartas tenham sido sorteadas.

### Encerramento
* Após a 3ª carta, o Miauráculo finaliza com a frase: *"Os astros falaram. Agora é com você, humano. Miau!"*, e o app retorna à tela inicial.

## 🛠️ Tecnologias

* **Plataforma:** Android (Mobile)
* **Linguagem:** Java
* **Integração de IA:** API MistralAI (para processamento de linguagem natural e interpretação das cartas)

## ⚙️ Pré-requisitos e Setup

* Android Studio
* SDK do Android atualizado
* Chave de API da MistralAI configurada no arquivo de variáveis de ambiente (`local.properties`).
