# Tarefas e projetos simples — Build 0.3.0

> **Para Hermes:** executar em fatias verticais, com teste RED → GREEN para domínio, repositório, migração e ViewModel.

**Objetivo:** entregar a versão `0.3.0` com tarefas e projetos locais simples, vinculáveis entre si, pesquisáveis e integrados à capacidade de prioridades da tela Hoje.

**Arquitetura:** ampliar o Room da versão 1 para a versão 2 com tabelas `projects` e `tasks`, usando auto-migration para as novas tabelas. O domínio terá modelos independentes do Android e estados explícitos para a tela Organizar. A navegação terá dois destinos: Hoje e Organizar.

**Tecnologias:** Kotlin, Room 2.6.1, AutoMigration, Compose Material 3, ViewModel/StateFlow, JUnit 4 e testes instrumentados AndroidX.

---

## Escopo fechado desta versão

### Incluído

- projeto com título e objetivo;
- tarefa com título, nota, data opcional, duração estimada e projeto opcional;
- estados: próxima ação, agendada, algum dia e concluída;
- filtro local por título;
- conclusão e reabertura de tarefa;
- promoção de uma tarefa para as prioridades de Hoje, respeitando o limite de três;
- persistência Room e migração de banco v1 → v2;
- tela Organizar e navegação Hoje/Organizar;
- funcionamento offline.

### Fora do escopo

- colaboração;
- kanban;
- anexos;
- recorrência avançada;
- calendário externo;
- sincronização ou backend.

---

## Task 1: Atualizar versão e criar o teste RED do domínio

**Arquivos:**
- Modificar: `app/build.gradle.kts`
- Criar: `app/src/main/java/br/com/resetlife/domain/organize/Task.kt`
- Criar: `app/src/main/java/br/com/resetlife/domain/organize/Project.kt`
- Criar: `app/src/test/java/br/com/resetlife/domain/organize/TaskTest.kt`

**Verificação:** teste falha por tipos de tarefa/projeto ausentes; `versionCode=3` e `versionName=0.3.0`.

---

## Task 2: Implementar regras de tarefa e projeto

**Regras:**

- título vazio é rejeitado;
- tarefa nova inicia como `NEXT_ACTION`;
- concluir muda para `COMPLETED`;
- reabrir retorna para `NEXT_ACTION`;
- status preserva projeto, nota, prazo e duração;
- filtro ignora diferenças de maiúsculas/minúsculas e espaços externos.

**Arquivos:**
- Modificar: `app/src/main/java/br/com/resetlife/domain/organize/Task.kt`
- Modificar: `app/src/main/java/br/com/resetlife/domain/organize/Project.kt`
- Modificar: `app/src/test/java/br/com/resetlife/domain/organize/TaskTest.kt`

**Verificação:** testes de domínio passam.

---

## Task 3: Criar entidades, DAOs e auto-migration Room v2

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/data/local/organize/ProjectEntity.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/local/organize/TaskEntity.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/local/organize/ProjectDao.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/local/organize/TaskDao.kt`
- Modificar: `app/src/main/java/br/com/resetlife/data/local/ResetLifeDatabase.kt`

**Verificação:** banco passa da versão 1 para 2; schema `2.json` é exportado; tabelas novas não alteram as prioridades existentes.

---

## Task 4: Implementar repositórios e testes de persistência

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/data/organize/ProjectRepository.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/organize/TaskRepository.kt`
- Criar: `app/src/test/java/br/com/resetlife/data/organize/TaskRepositoryTest.kt`
- Modificar: `app/src/main/java/br/com/resetlife/ResetLifeApplication.kt`

**Verificação:** criar, observar, editar status, buscar e relacionar tarefa/projeto funcionam no contrato local.

---

## Task 5: Testar migração no Android

**Arquivos:**
- Criar: `app/src/androidTest/java/br/com/resetlife/data/local/ResetLifeDatabaseMigrationTest.kt`
- Modificar: `app/build.gradle.kts` com dependências AndroidX Test/Room Testing

**Verificação:** `./gradlew :app:connectedDebugAndroidTest` executa a migração em emulador e confirma a preservação das prioridades da versão 1.

---

## Task 6: Implementar OrganizeViewModel com TDD

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeViewModel.kt`
- Criar: `app/src/test/java/br/com/resetlife/presentation/organize/OrganizeViewModelTest.kt`

**Comportamentos:**

- criar projeto e tarefa;
- selecionar projeto para nova tarefa;
- buscar por título;
- concluir/reabrir;
- expor carregamento, vazio e erro;
- promover tarefa para Hoje apenas quando houver vaga.

**Verificação:** testes unitários passam com fake stores e dispatcher controlado.

---

## Task 7: Criar tela Organizar e navegação

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`
- Criar/modificar: `app/src/main/java/br/com/resetlife/presentation/AppScreen.kt`
- Modificar: `app/src/main/java/br/com/resetlife/MainActivity.kt`
- Modificar: `app/src/main/res/values/strings.xml`

**Verificação manual:**

- aba Hoje mantém o fluxo de prioridades;
- aba Organizar cria projeto e tarefa;
- tarefa aparece vinculada ao projeto;
- busca filtra localmente;
- promoção respeita o limite diário;
- conclusão e reabertura permanecem após reinício.

---

## Task 8: Documentação, APK e release

**Arquivos:**
- Modificar: `README.md`
- Criar: `docs/releases/0.3.0.md`
- Modificar: `docs/ROADMAP.md` apenas se necessário para o status da entrega

**Comandos:**

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug :app:assembleAndroidTest
./gradlew :app:connectedDebugAndroidTest
```

**Critérios de aceite:**

- [ ] `versionCode=3` e `versionName=0.3.0`;
- [ ] projeto e tarefa persistem localmente;
- [ ] tarefa pode ser vinculada a projeto;
- [ ] busca local funciona;
- [ ] estados de tarefa funcionam;
- [ ] promoção para Hoje respeita três prioridades;
- [ ] migração v1 → v2 preserva prioridades;
- [ ] testes unitários passam;
- [ ] APK principal e APK instrumentado existem;
- [ ] validação conectada executa no emulador disponível;
- [ ] documentação registra limitações reais.
