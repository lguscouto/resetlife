# Arquitetura de informação e navegação — Build 0.3.2

> **Para Hermes:** executar as tarefas em ordem, preservando dados e regras de negócio da versão 0.3.1.

**Objetivo:** tornar a navegação entre Hoje e Organizar mais explícita, semântica e previsível, com estado de aba preservado e hierarquia clara de títulos.

**Arquitetura:** substituir o índice inteiro `0/1` por destinos nomeados e uma barra de navegação reutilizável. Cada destino terá rótulo e descrição de acessibilidade provenientes de recursos PT-BR. As telas continuam usando os ViewModels e repositórios existentes.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, StateFlow, Android API 29+.

---

## Task 1: Criar destinos nomeados e teste de seleção

**Arquivos:**

- Criar: `app/src/main/java/br/com/resetlife/presentation/navigation/ResetLifeDestination.kt`
- Criar: `app/src/test/java/br/com/resetlife/presentation/navigation/ResetLifeDestinationTest.kt`
- Modificar: `app/build.gradle.kts`

**Objetivo:** eliminar índices mágicos e documentar os dois destinos atuais.

**Verificação:** teste confirma que Hoje e Organizar têm chaves únicas e não alteram a ordem pública da navegação.

---

## Task 2: Criar componente de navegação semântica

**Arquivos:**

- Criar: `app/src/main/java/br/com/resetlife/presentation/navigation/ResetLifeNavigationBar.kt`
- Modificar: `app/src/main/res/values/strings.xml`

**Inclui:**

- ícones/símbolos visuais consistentes;
- rótulos sempre visíveis;
- `contentDescription` para leitores de tela;
- indicador de destino selecionado usando tokens do tema;
- strings PT-BR para rótulo e descrição.

**Verificação:** componente não conhece ViewModel nem regras de negócio e possui preview.

---

## Task 3: Refatorar `ResetLifeApp` para destinos e estado preservado

**Arquivos:**

- Modificar: `app/src/main/java/br/com/resetlife/presentation/AppScreen.kt`

**Passos:**

1. Usar `ResetLifeDestination` em vez de `mutableIntStateOf(0)`.
2. Preservar a aba ao recriar a composição usando `rememberSaveable`.
3. Usar `ResetLifeNavigationBar`.
4. Manter os dois ViewModels fora do `if` para preservar rascunhos e dados carregados.
5. Tratar o destino selecionado sem alterar os callbacks existentes.

**Verificação:** trocar de aba não perde campos digitados, seleção de projeto ou busca.

---

## Task 4: Ajustar títulos e hierarquia das telas

**Arquivos:**

- Modificar: `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`
- Modificar: `app/src/main/res/values/strings.xml`

**Inclui:**

- TopAppBar com título da tela atual, não apenas o nome da marca;
- tela Hoje com título de contexto e subtítulo orientado à ação;
- tela Organizar com separação clara entre Projetos, Nova tarefa e Pendências;
- strings de acessibilidade e retorno do Android;
- nenhuma funcionalidade nova de negócio.

**Verificação:** a finalidade de cada destino é compreensível sem abrir o código ou consultar documentação.

---

## Task 5: Atualizar versão, testes e release note

**Arquivos:**

- Modificar: `app/build.gradle.kts` para `versionCode = 5` e `versionName = "0.3.2"`;
- Modificar: `README.md`;
- Criar: `docs/releases/0.3.2.md`.

**Comandos:**

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug :app:assembleAndroidTest
./gradlew :app:connectedDebugAndroidTest
```

**Critérios de aceite:**

- [ ] versão `0.3.2` e `versionCode = 5` confirmados;
- [ ] destinos nomeados substituem índices mágicos;
- [ ] navegação tem rótulo e descrição acessível;
- [ ] aba selecionada é preservada na recriação da Activity;
- [ ] Hoje e Organizar têm títulos e hierarquia claros;
- [ ] regras de negócio e persistência continuam inalteradas;
- [ ] testes unitários passam;
- [ ] APK debug e APK instrumentado são gerados;
- [ ] pipeline conectado passa no emulador disponível;
- [ ] documentação registra limitações reais.
