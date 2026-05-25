# EcoMeta - Sustentabilidade Urbana & Gamificação

O **EcoMeta** é um aplicativo nativo Android desenvolvido para incentivar a mobilidade sustentável. Ele monitora os deslocamentos diários do usuário e calcula a redução da pegada de carbono ($CO_2$) ao optar por meios de transporte limpos.

## 🚀 Tecnologias Utilizadas

- **Linguagem**: [Kotlin 2.1.10](https://kotlinlang.org/)
- **Interface**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
- **Navegação**: Jetpack Navigation Component
- **Arquitetura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Backend**: [Firebase](https://firebase.google.com/) (Authentication & Firestore)
- **Build System**: Gradle (Kotlin DSL) com Version Catalogs (`libs.versions.toml`)

## 🏗️ Estrutura do Projeto

```text
com.example.ecometa
├── model/           # Data classes e modelos do Firestore
├── repository/      # Camada de abstração de dados (Firebase)
├── viewmodel/       # Lógica de negócio e estado da UI
└── ui/
    ├── theme/       # Design System (Cores, Tipografia, Shapes)
    ├── components/  # Componentes reutilizáveis (EcoCard, EcoButton, etc)
    └── screens/     # Telas do aplicativo (Home, Ranking, etc)
```

## ⚙️ Configuração do Firebase

Para compilar e rodar o projeto, siga os passos abaixo:

1. Acesse o [Firebase Console](https://console.firebase.google.com/).
2. Crie um novo projeto chamado `EcoMeta`.
3. Adicione um app Android com o package name `com.example.ecometa`.
4. Faça o download do arquivo `google-services.json`.
5. Cole o arquivo no diretório: `app/`.
6. No console do Firebase:
    - Ative o **Firebase Authentication** (E-mail/Senha).
    - Ative o **Cloud Firestore** em modo de teste ou produção.

## 🌿 Regra de Cálculo Ambiental

A fórmula principal utilizada para o cálculo do impacto é:
$$E = d \times (f_{carro} - f_{escolhido})$$

Onde:
- **E**: Emissão evitada em kg de $CO_2$.
- **d**: Distância percorrida em km.
- **f_carro**: 0.120 (fator fixo do carro a combustão).
- **f_escolhido**: Bicicleta (0.0), Caminhada (0.0), Ônibus (0.030), Metrô (0.040).

---
*Desenvolvido como um projeto de impacto ambiental positivo.*
