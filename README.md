# 🐱 Miauráculo

Miauráculo é um aplicativo Android inovador desenvolvido como projeto final de desenvolvimento mobile. A aplicação combina elementos interativos com inteligência artificial para criar uma experiência única de leitura energética.

## ✨ Sobre o Projeto

Miauráculo oferece uma experiência imersiva onde os usuários podem:
- **Carregar energia** através de um sensor de toque interativo
- **Selecionar tipos de leitura** diversos (Tarot, Astrologia, Cristais, etc.)
- **Receber interpretações** geradas por IA utilizando a API Mistral
- **Visualizar leituras personalizadas** em uma interface elegante

## 🚀 Funcionalidades Principais

### 🔋 Sistema de Energia Interativo
- Interface tátil imersiva para "carregar" energia
- Feedback visual e vibração em tempo real
- Sensor de pressão para controle natural da carga
- Animações fluidas e responsivas

### 📚 Tipos de Leitura Múltiplos
- Seleção dinâmica via dropdown menu
- Diferentes categorias de interpretação esotérica
- Interface adaptativa baseada na seleção

### 🤖 Integração com IA
- Integração com API Mistral para geração de texto
- Interpretações personalizadas e contextualizadas
- Processamento em background com feedback visual

### 📱 Interface Moderna
- Design Material Design 3
- Suporte a orientação retrato
- Componentes reutilizáveis e bem estruturados

## 🛠️ Requisitos Técnicos

### Ambiente
- **Android Studio** (versão recente)
- **SDK mínimo**: Android 7.0 (API 24)
- **SDK alvo**: Android 15 (API 36)
- **Java**: 11 ou superior

### Dependências Principais
```gradle
- AndroidX AppCompat
- AndroidX ConstraintLayout
- Material Design 3
- OkHttp3 4.12.0
- AndroidX Activity KTX
```

## 📦 Instalação

### 1. Clonar o Repositório
```bash
git clone https://github.com/LeonardoKrunitzky/Miauraculo.git
cd Miauraculo
```

### 2. Configurar Chave da API Mistral
Crie um arquivo `local.properties` na raiz do projeto:
```properties
MISTRAL_API_KEY="sua_chave_mistral_aqui"
```

### 3. Build e Execução
```bash
./gradlew build
./gradlew installDebug
```

Ou abra o projeto no Android Studio e execute normalmente.

## 📱 Como Usar

### Passo 1: Carregar Energia
1. Abra o aplicativo
2. Localize a área da "pata" na tela
3. Pressione e mantenha pressionado para carregar energia
4. Observe o nível de energia aumentar com base na pressão exercida

### Passo 2: Selecionar Tipo de Leitura
1. Após desbloquear a interface (100% de energia)
2. Selecione o tipo de leitura desejado no dropdown menu

### Passo 3: Obter Interpretação
1. Clique no botão "Próximo"
2. Aguarde enquanto a IA gera sua interpretação
3. Visualize a leitura na tela dedicada

## 🏗️ Estrutura do Projeto

```
Miauraculo/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/miauraculo/
│   │   │   │   ├── MainActivity.java          # Atividade principal com sensor de toque
│   │   │   │   ├── ReadingActivity.java       # Atividade de exibição de leitura
│   │   │   │   └── ...                        # Outras classes
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # Arquivos de layout XML
│   │   │   │   ├── drawable/                  # Recursos gráficos
│   │   │   │   ├── values/                    # Strings, cores, estilos
│   │   │   │   └── mipmap/                    # Ícones da aplicação
│   │   │   └── AndroidManifest.xml            # Configuração da aplicação
│   │   └── test/                              # Testes unitários
│   └── build.gradle.kts                       # Configuração do build
├── gradle/                                    # Scripts Gradle
├── settings.gradle.kts                        # Configurações do projeto
├── build.gradle.kts                           # Build raiz
└── README.md                                  # Este arquivo
```

## 🔑 Permissões Necessárias

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.VIBRATE" />
```

## 🎨 Principais Classes

### MainActivity.java
Gerencia a tela inicial com:
- Sistema de sensor de toque para carregar energia
- Controle de animações
- Integração com dropdown de seleção
- Feedback tátil e visual

### ReadingActivity.java
Exibe a leitura gerada por IA:
- Apresentação formatada da interpretação
- Navegação entre diferentes leituras
- Interface limpa e legível

## 🔗 Integração com API Mistral

O aplicativo utiliza a API Mistral AI para gerar interpretações. As requisições são feitas via OkHttp3 com:
- Autenticação via API Key
- Processamento assíncrono
- Tratamento de erros robusto

## 📊 Versão

- **Versão Atual**: 1.0
- **Código de Versão**: 1
- **Nome do Pacote**: `com.example.miauraculo`

## 🎓 Contexto Acadêmico

Este é um projeto final para conclusão do curso de **Desenvolvimento Mobile**. O projeto demonstra:
- ✅ Conhecimento de componentes Android
- ✅ Manipulação de eventos de toque
- ✅ Integração com APIs externas
- ✅ Design responsivo e user experience
- ✅ Gerenciamento de estado e threading
- ✅ Boas práticas de desenvolvimento Android

## 🤝 Contribuições

Como este é um projeto acadêmico, contribuições não são esperadas, mas feedback e sugestões são bem-vindos!

## 📝 Licença

Este projeto é disponibilizado sem licença específica. Para uso comercial, entre em contato com o desenvolvedor.

## 👤 Autor

**Leonardo Krunitzky**
- GitHub: [@LeonardoKrunitzky](https://github.com/LeonardoKrunitzky)

## 📚 Recursos e Referências

- [Documentação Android Official](https://developer.android.com/)
- [Material Design 3](https://m3.material.io/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Mistral AI API](https://docs.mistral.ai/)

---

Desenvolvido com ✨ e dedicação como projeto final de Desenvolvimento Mobile.
