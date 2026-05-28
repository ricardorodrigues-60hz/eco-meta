# EcoMeta 🌿
### Transformando sustentabilidade em metas reais e gamificadas.

**EcoMeta** é um aplicativo Android nativo desenvolvido para ajudar usuários a monitorarem sua pegada de carbono através do registro de deslocamentos diários. Ao incentivar o uso de meios de transporte sustentáveis (como caminhada e bicicleta), o app transforma hábitos ecológicos em uma experiência gamificada com níveis, recompensas e rankings competitivos.

---

## 🚀 Stack Tecnológico
*   **Linguagem:** Java
*   **Arquitetura:** MVVM (Model-View-ViewModel)
*   **Banco de Dados:** Firebase Firestore (Tempo real)
*   **Navegação:** Jetpack Navigation Component
*   **Interface:** Material Design 3 (Material Components)
*   **Injeção/Gerenciamento de Estado:** LiveData & ViewModelProvider
*   **Autenticação:** Firebase Auth

---

## ✨ Funcionalidades Principais
*   📊 **Dashboard de Impacto:** Visualize seu nível de progresso, EcoPoints acumulados e total de CO₂ poupado.
*   🚲 **Registro de Trajetos:** Interface intuitiva para registrar deslocamentos via caminhada, bicicleta, ônibus ou metrô.
*   📜 **Histórico Detalhado:** Acompanhe todos os seus trajetos passados com cálculos dinâmicos de distância e economia de carbono.
*   🏆 **Ranking Global:** Compare seu desempenho com outros usuários da comunidade EcoMeta.
*   🎯 **Desafios e Conquistas:** Supere metas específicas para desbloquear selos e ganhar bônus de pontos.
*   ☀️ **Interface Light Exclusive:** Design otimizado para clareza e frescor visual, focado na identidade da marca.

---

## 🛠️ Como rodar o projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/eco-meta.git
    ```
2.  **Abra no Android Studio:**
    Certifique-se de estar usando a versão Flamingo ou superior.
3.  **Configure o Firebase:**
    - Crie um projeto no [Console do Firebase](https://console.firebase.google.com/).
    - Adicione um app Android com o package name `com.example.ecometa`.
    - Baixe o arquivo `google-services.json` e coloque-o na pasta `app/`.
    - Ative o **Firestore Database** e o **Anonymous Authentication** (ou Email/Password).
4.  **Execute o App:**
    Selecione um emulador (API 30+) ou dispositivo físico e clique em **Run**.

---

## 👤 Desenvolvedor
Desenvolvido como um projeto focado em sustentabilidade e boas práticas de arquitetura Android.

---
© 2026 EcoMeta Team. Todos os direitos reservados.
