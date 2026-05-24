# 🌿 EcoMeta - Monitoramento de Emissões de CO₂

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Database-Firebase%20Firestore-FFCA28?style=flat-square&logo=firebase&logoColor=black)
![Material Design 3](https://img.shields.io/badge/UI-Material%20Design%203-757575?style=flat-square&logo=materialdesign&logoColor=white)

O **EcoMeta** é uma aplicação móvel Android desenvolvida como projeto prático (P2) que visa combater as mudanças climáticas e incentivar a mobilidade urbana sustentável. Através do monitoramento em tempo real dos deslocamentos diários, o aplicativo quantifica a redução da pegada de carbono individual, transformando dados abstratos em métricas de impacto ecológico real de forma gamificada.

---

## 🚀 Sobre o Projeto

O setor de transportes urbanos é um dos principais responsáveis pela emissão de dióxido de carbono ($CO_2$) devido ao uso massivo de combustíveis fósseis. Embora a conscientização global esteja a crescer, as pessoas frequentemente enfrentam a falta de indicadores claros sobre o impacto ecológico de suas escolhas de locomoção. 

O EcoMeta atua diretamente nesta lacuna, operando como um ecossistema que converte trajetos ecológicos (caminhada, bicicleta, transporte público) em **EcoPoints** e métricas visíveis de $CO_2$ poupado, utilizando o **Firebase Firestore** para a persistência e sincronização instantânea dos dados entre os utilizadores.

---

## ✨ Funcionalidades Principais

* **Autenticação e Perfil Ecológico:** Cadastro e login seguros integrados ao Firebase Authentication. O perfil exibe o nível de evolução do utilizador (de "Semente" a "Guardião da Floresta") e a sua experiência (XP).
* **Registo de Deslocamentos:** Interface simples para inserir trajetos diários especificando o modal de transporte (Caminhada, Bicicleta, Ônibus, Metrô) e a distância em quilómetros.
* **Cálculo Automatizado de Carbono:** Algoritmo nativo que calcula dinamicamente os quilogramas de $CO_2$ evitados em comparação com um veículo a combustão padrão.
* **Central de Gamificação & Missões:** Desafios comunitários temporários (ex: "Semana Sem Carro") com metas e recompensas exclusivas em EcoPoints.
* **Galeria de Insígnias:** Um sistema de conquistas em formato de grelha (*Grid*) que desbloqueia medalhas visuais conforme os marcos de sustentabilidade são atingidos.
* **Ranking Social & Estatísticas:** Quadro de líderes (*Leaderboard*) interativo com pódio para promover a competição saudável, além de gráficos de colunas para análise mensal da evolução individual.

---

## 📐 Arquitetura & Algoritmo

A aplicação segue o padrão de desenvolvimento **MVVM (Model-View-ViewModel)** / **MVC**, garantindo desacoplamento e facilidade de manutenção:

* **View:** Interfaces construídas nativamente em arquivos **XML** utilizando os princípios modernos do *Material Design 3*.
* **ViewModel / Controller:** Lógica programada em **Java** para manipulação dos fluxos de dados, escuta de eventos e validações de input.
* **Model / Firebase:** Camada reativa conectada diretamente ao ecossistema Google Firebase para respostas assíncronas em tempo real.

### Fórmula de Cálculo Ambiental
A equação para obter a emissão de $CO_2$ evitada ($E$) baseia-se na diferença entre o fator de emissão de um automóvel convencional de tamanho médio e o transporte sustentável escolhido:

$$E = d 	imes (f_{carro} - f_{escolhido})$$

Onde:
* $E$ = Emissão total de $CO_2$ evitada em kg.
* $d$ = Distância do trajeto percorrido em km.
* $f_{carro}$ = Fator padrão de emissão de um carro a combustão ($ pprox 0.120 	ext{ kg/km}$).
* $f_{escolhido}$ = Fator do modal utilizado (Ex: Bicicleta = $0 	ext{ kg/km}$; Ônibus = $0.030 	ext{ kg/km}$).

---

## 🗄️ Modelagem da Base de Dados (Firebase Firestore)

A arquitetura do banco de dados NoSQL organiza-se em 4 coleções independentes estruturadas de forma lógica:

### 1. Coleção: `usuarios`
| Campo | Tipo de Dado | Descrição / Regra de Negócio |
| :--- | :--- | :--- |
| `id_user` | String | UID exclusivo gerado pelo Firebase Authentication. |
| `nome` | String | Nome completo ou nome de exibição do utilizador. |
| `email` | String | Endereço de e-mail associado à conta. |
| `eco_points` | Integer | Pontuação acumulada do utilizador na plataforma. |
| `total_co2_poupado` | Double | Soma histórica de todo o $CO_2$ evitado em kg. |
| `nivel` | String | Patamar atual na gamificação (ex: *Brotinho*). |

### 2. Coleção: `atividades`
| Campo | Tipo de Dado | Descrição / Regra de Negócio |
| :--- | :--- | :--- |
| `id_atividade` | String | ID alfanumérico gerado automaticamente pelo Firestore. |
| `user_id` | String | FK que associa a atividade ao `id_user` do criador. |
| `tipo_transporte` | String | Modal de transporte (Caminhada, Bicicleta, Ônibus, Metrô). |
| `distancia_km` | Double | Distância total do trajeto expressa em quilómetros. |
| `co2_evitado` | Double | Resultado da equação ambiental aplicada ao trajeto. |
| `data` | Timestamp | Carimbo de data/hora no momento do salvamento. |

### 3. Coleção: `desafios`
| Campo | Tipo de Dado | Descrição / Regra de Negócio |
| :--- | :--- | :--- |
| `id_desafio` | String | Identificador exclusivo do desafio global. |
| `titulo` | String | Nome curto da missão comunitária (ex: "Caminhante Verde"). |
| `descricao` | String | Regras e condições para a validação da meta. |
| `pontos_recompensa`| Integer | EcoPoints a serem creditados no perfil de quem concluir. |
| `data_limite` | Timestamp | Prazo final para a expiração do desafio. |

### 4. Coleção: `conquistas_usuario`
| Campo | Tipo de Dado | Descrição / Regra de Negócio |
| :--- | :--- | :--- |
| `id_conquista` | String | Chave primária do registo do marco alcançado. |
| `user_id` | String | FK vinculando ao utilizador premiado (`id_user`). |
| `titulo_conquista` | String | Nome descritivo da insígnia (ex: "Ciclista de Ferro"). |
| `data_desbloqueio` | Timestamp | Data exata em que os critérios foram atingidos. |

---

## 📱 Telas do Aplicativo (Estrutura da UI)

A interface foi modularizada de acordo com as seguintes definições visuais:
1.  **Tela de Login:** Autenticação limpa com o logótipo oficial do app, inputs modernos com floating labels e botões de ação e cadastro rápidos.
2.  **Tela Home (Dashboard):** Visualização do nível atual do utilizador via barra de progresso linear, cards de alto impacto com o placar de EcoPoints e botão proeminente para `Registrar Novo Trajeto`.
3.  **Tela de Histórico:** Lista cronológica vertical dos trajetos recuperados do Firestore, exibindo ícone do modal, quilometragem e o impacto de carbono de cada ação.
4.  **Tela de Desafios Ativos:** Cards horizontais interativos com barras de progresso percentual indicando o progresso rumo à conclusão das metas da comunidade.
5.  **Tela de Minhas Insígnias:** Grelha de exibição contendo medalhas coloridas (desbloqueadas) e em escala de cinza (bloqueadas) para incentivar o engajamento contínuo.
6.  **Tela de Ranking Social:** Aba social dedicada contendo o pódio com efeito de degrau para o Top 3 e a listagem vertical geral destacando a linha correspondente ao utilizador atual.
7.  **Tela de Estatísticas:** Painel focado em gráficos de colunas que exibe a flutuação do volume de $CO_2$ economizado nos últimos meses.

*Nota: Para adicionar imagens a esta seção, insira os arquivos correspondentes na pasta `/assets` e utilize o padrão Markdown: `![Nome da Tela](assets/nome_da_imagem.png)`.*

---

## ⚙️ Configuração e Instalação

### Pré-requisitos
* [Android Studio Bumblebee](https://developer.android.com/studio) ou superior.
* JDK 11+ configurado.
* Dispositivo Android ou Emulador executando API 26 (Android 8.0) ou superior.
* Uma conta no console do [Firebase](https://console.firebase.google.com/).

### Passo a Passo
1.  **Clonar o Repositório:**
    ```bash
    git clone https://github.com/ricardorodrigues-60hz/eco-meta.git
    ```
2.  **Configurar o Firebase:**
    * Crie um projeto no Console do Firebase.
    * Adicione um aplicativo Android ao projeto utilizando o package name `com.example.ecometa` (ou o definido no seu `build.gradle`).
    * Faça o download do arquivo `google-services.json` fornecido pelo Firebase.
    * Mova o arquivo `google-services.json` para dentro do diretório `/app` do seu projeto clonado.
3.  **Habilitar Serviços:**
    * No Console do Firebase, ative o **Authentication** (método E-mail/Senha).
    * Ative o **Cloud Firestore** em modo de teste ou configure as regras de leitura/escrita de segurança adequadas.
4.  **Compilar e Executar:**
    * Abra o projeto no Android Studio.
    * Aguarde a sincronização completa do Gradle.
    * Clique no botão **Run** (`Shift + F10`) selecionando o seu dispositivo ou emulador.

---

## 👥 Autores

* **Ricardo Florentino Rodrigues** - *Desenvolvimento & Modelagem de Dados*
* **Victor Willian Spontone** - *Desenvolvimento & Arquitetura da Interface*
