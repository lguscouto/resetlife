# Prioridades diárias — Primeiro incremento de implementação

> **Para Hermes:** executar as tarefas em ordem, mantendo TDD para toda regra de domínio.

**Objetivo:** criar a fundação Android offline do ResetLife e a primeira jornada funcional da tela Hoje: adicionar prioridades, limitar a três por dia e concluir prioridades locais.

**Arquitetura:** aplicativo Android nativo em Kotlin e Jetpack Compose, com uma primeira camada de domínio independente do Android. A tela inicial mantém estado em memória neste incremento; persistência com Room entra no próximo incremento para não misturar fundação, domínio e banco em uma única entrega.

**Tecnologias:** Kotlin, Jetpack Compose, Material 3, ViewModel, StateFlow, JUnit 4, Gradle 8.13, Android Gradle Plugin 8.8.x, compileSdk 35, minSdk 29.

---

## Task 1: Inicializar repositório e projeto Android

**Objetivo:** criar um app Android compilável com uma única tela Compose.

**Arquivos:**
- Criar: `settings.gradle.kts`
- Criar: `build.gradle.kts`
- Criar: `gradle.properties`
- Criar: `gradlew`, `gradlew.bat`, `gradle/wrapper/*`
- Criar: `app/build.gradle.kts`
- Criar: `app/src/main/AndroidManifest.xml`
- Criar: `app/src/main/java/br/com/resetlife/MainActivity.kt`
- Criar: `app/src/main/res/values/{strings.xml,colors.xml,themes.xml}`
- Criar: `.gitignore`, `README.md`

**Passos:**
1. Inicializar o repositório Git.
2. Criar Gradle Wrapper na versão 8.13 usando a distribuição local já disponível.
3. Configurar Kotlin, Android Gradle Plugin, Compose e JUnit.
4. Criar `MainActivity` com tema Material 3 e conteúdo mínimo.
5. Executar `./gradlew.bat :app:assembleDebug`.

**Verificação esperada:** o comando termina com `BUILD SUCCESSFUL` e gera um APK debug.

---

## Task 2: Escrever o teste RED para o limite de prioridades

**Objetivo:** definir a regra central: um dia pode ter no máximo três prioridades ativas.

**Arquivos:**
- Criar: `app/src/test/java/br/com/resetlife/domain/today/DailyPrioritiesTest.kt`

**Passos:**
1. Criar o teste para adicionar três prioridades com sucesso.
2. Criar o teste para rejeitar a quarta prioridade.
3. Executar `./gradlew.bat :app:testDebugUnitTest --tests '*DailyPrioritiesTest'`.

**Verificação esperada:** falha porque `DailyPriorities` ainda não existe.

---

## Task 3: Implementar o domínio mínimo GREEN

**Objetivo:** implementar o agregado de prioridades diárias sem dependência Android.

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/domain/today/DailyPriorities.kt`

**Passos:**
1. Criar `PriorityItem` com id, título e estado de conclusão.
2. Criar `DailyPriorities` com `MAX_ACTIVE_PRIORITIES = 3`.
3. Implementar adição, rejeição do quarto item e conclusão por id.
4. Executar novamente o teste específico e depois toda a suíte unitária.

**Verificação esperada:** todos os testes passam.

---

## Task 4: Escrever o teste RED para a conclusão de prioridade

**Objetivo:** assegurar que concluir uma prioridade preserva o item e libera capacidade ativa.

**Arquivos:**
- Modificar: `app/src/test/java/br/com/resetlife/domain/today/DailyPrioritiesTest.kt`

**Passos:**
1. Escrever teste que conclui uma prioridade pelo id.
2. Escrever teste que permite adicionar uma nova prioridade após uma conclusão.
3. Executar o teste específico.

**Verificação esperada:** falha porque o comportamento ainda não foi implementado.

---

## Task 5: Completar o domínio e reexecutar a suíte

**Objetivo:** concluir a regra de capacidade diária e proteger casos de borda básicos.

**Arquivos:**
- Modificar: `app/src/main/java/br/com/resetlife/domain/today/DailyPriorities.kt`

**Passos:**
1. Implementar conclusão de item e recálculo de prioridades ativas.
2. Retornar resultado explícito ao tentar concluir id inexistente.
3. Executar teste específico e `./gradlew.bat :app:testDebugUnitTest`.

**Verificação esperada:** todos os testes unitários passam.

---

## Task 6: Integrar a regra na tela Hoje

**Objetivo:** entregar uma tela utilizável para adicionar e concluir até três prioridades.

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/presentation/today/TodayViewModel.kt`
- Criar: `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`
- Criar: `app/src/main/java/br/com/resetlife/presentation/theme/ResetLifeTheme.kt`
- Modificar: `app/src/main/java/br/com/resetlife/MainActivity.kt`
- Modificar: `app/src/main/res/values/strings.xml`

**Passos:**
1. Expor estado imutável da tela via `StateFlow`.
2. Permitir adicionar prioridade com título válido.
3. Desabilitar ação de adicionar quando houver três prioridades ativas.
4. Exibir contagem, lista, estado de conclusão e mensagem de erro acessível.
5. Usar strings PT-BR em recursos e Material 3.

**Verificação esperada:** o APK compila e a tela apresenta o fluxo sem depender de rede ou conta.

---

## Task 7: Validar artefato e documentar a fundação

**Objetivo:** confirmar que o incremento é reproduzível e atualizar o onboarding técnico do repositório.

**Arquivos:**
- Modificar: `README.md`

**Passos:**
1. Executar `./gradlew.bat :app:testDebugUnitTest`.
2. Executar `./gradlew.bat :app:assembleDebug`.
3. Confirmar existência de `app/build/outputs/apk/debug/app-debug.apk`.
4. Registrar no README os requisitos e os comandos exatos para compilar/testar.
5. Verificar `git diff --check` e `git status`.

**Verificação esperada:** testes verdes, APK gerado e diff sem erros de espaço em branco.

---

## Critérios de aceite deste incremento

- [ ] projeto Android abre e compila com Android 10+ como mínimo;
- [ ] tela Hoje mostra a marca ResetLife e explica as três prioridades do dia;
- [ ] usuário adiciona até três prioridades com título não vazio;
- [ ] quarta prioridade ativa é bloqueada por regra de domínio e feedback de UI;
- [ ] usuário conclui uma prioridade e pode adicionar outra;
- [ ] nenhuma funcionalidade depende de rede, conta, serviço externo ou permissão desnecessária;
- [ ] regras de domínio possuem testes executados em RED e GREEN;
- [ ] APK debug é realmente produzido.
