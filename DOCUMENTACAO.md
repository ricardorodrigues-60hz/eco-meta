# Documentação Técnica: Projeto EcoMeta

## 1. Visão Geral
O EcoMeta é uma plataforma móvel voltada para a conscientização ambiental. O objetivo técnico foi construir um sistema escalável e reativo que pudesse processar dados de deslocamento e converter em métricas de sustentabilidade (CO₂ poupado) e gamificação (EcoPoints).

## 2. Arquitetura (MVVM)
O projeto segue rigorosamente o padrão **Model-View-ViewModel**, garantindo a separação de responsabilidades e facilitando a manutenção.

### 🧩 Model
Camada de dados pura que representa as entidades do negócio e as tabelas do Firestore.
*   `Usuario.java`: Armazena perfil, nível e pontos totais.
*   `Atividade.java`: Representa um trajeto (distância, tipo de transporte, CO₂ evitado).
*   `Desafio.java` / `DesafioStatus.java`: Define as metas e o estado de conquista do usuário.

### 📁 Repository
Ponto único de comunicação com serviços externos.
*   `AutenticacaoRepository.java`: Centraliza todas as chamadas ao Firebase Firestore e Auth, fornecendo Callbacks para a ViewModel.

### ⚙️ ViewModel
Gerencia a lógica de negócio e mantém o estado da UI de forma independente do ciclo de vida das Activities.
*   `EcoMetaViewModel.java`: O "cérebro" do app. Observa o Repository e expõe dados via `LiveData` para que as Views reajam automaticamente a mudanças no banco de dados.

### 📱 View (Fragment/Activity)
Responsável apenas por exibir a interface e enviar eventos do usuário para a ViewModel.
*   `MainActivity.java`: Host principal da navegação.
*   `HomeFragment.java`, `HistoryFragment.java`, etc.: Consomem os LiveDatas da ViewModel.

### 🔗 Adapter
Ponte entre os dados (Listas) e as Views complexas (RecyclerView).
*   `AtividadeAdapter.java`: Renderiza os itens de histórico com lógica de data.
*   `RankingAdapter.java`: Gerencia a exibição do pódio e medalhas.
*   `ChallengesAdapter.java`: Controla visualmente o estado de bloqueio/conquista dos desafios.

## 3. Mapeamento de Telas

### 🏠 Home (Dashboard)
*   **Fragment:** `HomeFragment.java`
*   **ViewModel:** `EcoMetaViewModel.java`
*   **Descrição:** Exibe o progresso do nível do usuário, cards de estatísticas rápidas e o botão principal para registro de novas atividades. Utiliza `ShapeableImageView` para fotos circulares.

### 📜 Histórico
*   **Fragment:** `HistoryFragment.java`
*   **Descrição:** Lista cronológica de trajetos. Possui um cabeçalho dinâmico que soma em tempo real os trajetos, kms e CO₂ de toda a lista vinculada ao usuário.

### 🏆 Ranking
*   **Fragment:** `RankingFragment.java`
*   **Descrição:** Gamificação competitiva. Implementa um design de pódio visual para os TOP 3 usuários e uma lista geral com badges de medalhas automáticas.

### 🎯 Desafios
*   **Fragment:** `ChallengesFragment.java`
*   **Descrição:** Sistema de conquistas. Exibe desafios disponíveis e concluídos, alterando cores e ícones conforme o progresso do usuário no Firestore.

### 🚲 Registrar Trajeto
*   **Fragment:** `RegistrarAtividadeFragment.java`
*   **Descrição:** Interface amigável de seleção por cards. Converte a entrada do usuário em dados de sustentabilidade e os persiste no Firebase.

---
*Documentação gerada em 26 de Maio de 2026.*
