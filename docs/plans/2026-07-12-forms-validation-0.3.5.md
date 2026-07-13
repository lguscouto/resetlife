# Formulários, teclado e prevenção de erros — Build 0.3.5

> **Para Hermes:** executar em ciclos TDD, preservando o escopo UX/UI e sem alterar banco ou entidades persistidas.

**Objetivo:** reduzir atrito na entrada, mostrar erros junto aos campos, preservar rascunhos e impedir registros duplicados por toques repetidos.

**Arquitetura:** manter validações e estado transitório nos ViewModels. A interface aceitará e exibirá datas exclusivamente em `DD/MM/AAAA`; antes de criar a tarefa, o valor será normalizado para ISO `AAAA-MM-DD` para preservar a persistência existente. Erros de campo serão representados no `OrganizeUiState`, enquanto feedbacks globais representarão sucesso, promoção e falha de armazenamento. A UI Compose receberá opções de teclado, ações IME, foco sequencial e botões desabilitados durante persistência.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, StateFlow, coroutines, `java.time.LocalDate` e JUnit.

---

### Task 1: Validar e normalizar prazo

**Arquivos:**
- Modificar: `app/src/test/java/br/com/resetlife/presentation/organize/OrganizeViewModelTest.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeViewModel.kt`

**Ciclo TDD:**
1. escrever teste para rejeitar data impossível preservando os campos;
2. executar e confirmar falha;
3. implementar erro inline `InvalidDate`;
4. escrever teste para aceitar `DD/MM/AAAA` e persistir `AAAA-MM-DD`;
5. executar RED/GREEN.

### Task 2: Validar campos sem apagar rascunho

**Arquivos:** os mesmos da Task 1.

**Ciclo TDD:**
- título de tarefa vazio gera erro junto ao título;
- duração inválida gera erro junto à duração;
- editar o campo correspondente limpa somente seu erro;
- nenhum erro invalida ou apaga os demais campos digitados.

### Task 3: Impedir salvamento duplicado

**Arquivos:**
- `OrganizeViewModel.kt`
- `OrganizeViewModelTest.kt`
- `TodayViewModel.kt`
- `TodayViewModelTest.kt`

**Ciclo TDD:** chamar duas vezes a ação antes de liberar a coroutine e confirmar somente uma gravação. Adicionar flags de salvamento, desabilitar a ação enquanto a operação está em andamento e limpar o rascunho apenas depois do sucesso.

### Task 4: Mostrar confirmação visual

**Arquivos:**
- `OrganizeViewModel.kt`
- `TodayViewModel.kt`
- `OrganizeScreen.kt`
- `TodayScreen.kt`
- `strings.xml`

**Implementação:** adicionar feedbacks de projeto, tarefa e prioridade salvos; exibi-los com tom de sucesso e manter falhas com tom de erro.

### Task 5: Melhorar teclado e foco

**Arquivo:** `OrganizeScreen.kt` e `TodayScreen.kt`.

**Implementação:**
- `ImeAction.Next` entre campos sequenciais;
- `ImeAction.Done` para salvar projeto, tarefa e prioridade;
- `KeyboardType.Number` com máscara automática `DD/MM/AAAA` para data e duração;
- `FocusRequester` para objetivo, nota, data e duração;
- `imePadding` e rolagem para evitar botão coberto;
- busca com `ImeAction.Search`;
- mensagens `supportingText` junto ao campo com `isError`.

### Task 6: Versão, documentação e validação final

**Arquivos:**
- `app/build.gradle.kts`
- `README.md`
- Criar: `docs/releases/0.3.5.md`

Alterar para `versionCode = 8` e `versionName = "0.3.5"`.

Executar:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleAndroidTest :app:connectedDebugAndroidTest --console=plain
```

Validar no emulador:

- erro de data inválida próximo ao campo;
- erro de duração inválida próximo ao campo;
- rascunho preservado ao corrigir erro e trocar de aba;
- data brasileira `DD/MM/AAAA` normalizada internamente sem alterar o formato apresentado;
- criação única após toque duplo;
- confirmação visual após salvar;
- APK instalado, atividade aberta e logcat sem falha fatal.
