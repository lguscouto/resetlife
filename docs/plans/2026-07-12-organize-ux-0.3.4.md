# Redesign da jornada Organizar — Build 0.3.4

> **Para Hermes:** executar as tarefas em ordem, preservando as regras de domínio, a persistência Room e os contratos existentes dos callbacks.

**Objetivo:** tornar a criação e revisão de projetos/tarefas mais simples que uma lista genérica, destacando o próximo passo e reduzindo a densidade inicial.

**Arquitetura:** manter `OrganizeViewModel` como fonte de estado e adicionar apenas propriedades derivadas para separar tarefas abertas e concluídas. A tela Compose usará superfícies agrupadas e formulários recolhíveis, sem criar nova navegação, entidade ou dependência.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, StateFlow, Room existente e JUnit.

---

### Task 1: Cobrir a separação visual de tarefas abertas e concluídas

**Arquivos:**
- Criar: `app/src/test/java/br/com/resetlife/presentation/organize/OrganizeUiStateTest.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeViewModel.kt`

**Implementação:** adicionar `filteredOpenTasks` e `filteredCompletedTasks` como propriedades derivadas de `filteredTasks`, preservando a busca existente e sem duplicar dados.

**Validação:** executar o teste específico e confirmar `BUILD SUCCESSFUL`.

### Task 2: Separar e recolher formulários

**Arquivo:** `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`

**Implementação:**
- manter `Criar tarefa` aberto inicialmente para priorizar a ação mais comum;
- manter `Criar projeto` recolhido inicialmente;
- preservar abertura/fechamento com `rememberSaveable`;
- agrupar título obrigatório, seleção de projeto e detalhes opcionais;
- destacar campos obrigatórios com `*` e manter notas, prazo e duração como opcionais;
- usar superfícies e botões em largura total para reduzir a sensação de formulário longo.

### Task 3: Tornar a relação tarefa/projeto e a promoção explícitas

**Arquivos:**
- `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`
- `app/src/main/res/values/strings.xml`

**Implementação:**
- mostrar `Projeto: ...` em um seletor de largura total;
- mostrar projeto, prazo e duração como metadados da tarefa;
- substituir o botão ambíguo `Hoje` por `Adicionar a Hoje` em `OutlinedButton`;
- manter checkbox exclusivamente para concluir/reabrir a tarefa;
- separar tarefas em `Abertas` e `Concluídas`;
- usar superfície atenuada e estado textual para tarefas concluídas;
- diferenciar vazio inicial de busca sem resultado.

### Task 4: Atualizar versão e documentação

**Arquivos:**
- `app/build.gradle.kts`
- `README.md`
- Criar: `docs/releases/0.3.4.md`

**Implementação:** alterar para `versionCode = 7` e `versionName = "0.3.4"`, registrar o plano, o APK e as limitações de validação.

### Task 5: Verificação final

Executar:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleAndroidTest :app:connectedDebugAndroidTest --console=plain
```

Validar também:

- tamanho dos dois APKs;
- metadata do pacote;
- instalação e abertura no `Pixel_8`;
- criação de tarefa sem trocar de tela;
- presença do projeto selecionado e metadados;
- ação `Adicionar a Hoje` distinta do checkbox;
- separação entre tarefas abertas e concluídas;
- `git diff --check` e ausência de placeholders na documentação.
