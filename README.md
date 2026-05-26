# EcoMeta - Sustentabilidade Urbana & Gamificação

O **EcoMeta** é um aplicativo nativo Android desenvolvido para incentivar a mobilidade sustentável. Ele monitora os deslocamentos diários do usuário e calcula a redução da pegada de carbono ($CO_2$) ao optar por meios de transporte limpos.

## 🚀 Tecnologias Utilizadas

- **Linguagem**: Java 11 (POO Clássica)
- **Interface**: XML (Material Design 3 - ConstraintLayout, MaterialCardView)
- **Navegação**: Navigation Component (Fragment-based)
- **Arquitetura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Backend**: [Firebase](https://firebase.google.com/) (Authentication & Firestore)
- **Build System**: Gradle (Kotlin DSL) com Version Catalogs (`libs.versions.toml`)

## 🏗️ Estrutura do Projeto

```text
com.example.ecometa
├── model/           # Classes POJO e modelos do Firestore
├── repository/      # Camada de abstração de dados e Callbacks
├── viewmodel/       # Lógica de negócio e LiveData
└── ui/
    ├── fragment/    # Telas do aplicativo (Home, Histórico, etc)
    └── adapter/     # Adapters para RecyclerView (Histórico)
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
*Desenvolvido em Java/XML focado em estabilidade e performance nativa.*
