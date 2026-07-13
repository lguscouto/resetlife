# Persistência de prioridades — Build 0.2.0

> **Para Hermes:** executar as tarefas em ordem, seguindo TDD para o domínio, repositório e estado da tela.

**Objetivo:** entregar a versão `0.2.0` do ResetLife com prioridades persistentes localmente após fechar e reabrir o aplicativo.

**Arquitetura:** Room/SQLite será a fonte de dados da tela Hoje. A regra de limite e conclusão permanece no domínio, enquanto um repositório traduz entidades Room para `PriorityItem`. O ViewModel observará apenas o dia atual e exporá estados de carregamento e erro para a UI.

**Tecnologias:** Kotlin, Room 2.6.x, Kotlin Coroutines/Flow, ViewModel, Jetpack Compose, JUnit 4, Android API 29+.

---

## Task 1: Preparar Room e incrementar a versão do aplicativo

**Objetivo:** adicionar o compilador Room, dependências e versão `0.2.0` sem alterar o comportamento da tela.

**Arquivos:**
- Modificar: `build.gradle.kts`
- Modificar: `app/build.gradle.kts`
- Modificar: `README.md`

**Passos:**

1. Adicionar o plugin de processamento Kotlin KAPT.
2. Adicionar `room-runtime`, `room-ktx` e `room-compiler` na versão compatível.
3. Configurar `kapt.correctErrorTypes = true`.
4. Alterar `versionCode` para `2` e `versionName` para `0.2.0`.
5. Executar uma tarefa Gradle de compilação antes do código de produção da persistência.

**Verificação:** Gradle resolve Room e o app atual continua compilando.

---

## Task 2: Definir o contrato de persistência com teste RED

**Objetivo:** definir o comportamento de salvar e observar prioridades do dia.

**Arquivos:**
- Criar: `app/src/test/java/br/com/resetlife/data/today/PriorityRepositoryTest.kt`

**Passos:**

1. Criar um fake DAO mínimo no teste.
2. Escrever teste para salvar uma prioridade e recuperá-la pelo dia atual.
3. Escrever teste para mapear conclusão e manter a ordem de criação.
4. Executar o teste específico e confirmar falha por tipos/repositorio ausentes.

**Verificação:** falha por ausência do contrato/implementação, não por falha de Gradle ou Android SDK.

---

## Task 3: Criar entidade e DAO Room

**Objetivo:** representar prioridades no SQLite e expor observação por dia.

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/data/local/today/PriorityEntity.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/local/today/PriorityDao.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/local/ResetLifeDatabase.kt`

**Contrato:**

- chave estável da prioridade;
- título normalizado;
- `isCompleted`;
- `dayKey` ISO-8601 (`yyyy-MM-dd`);
- `createdAt` para preservar a ordem;
- `Flow<List<PriorityEntity>>` filtrado por `dayKey`;
- inserção e atualização idempotentes.

**Verificação:** classes geradas pelo Room compilam e o schema inicial é criado.

---

## Task 4: Implementar o repositório e fazer o teste GREEN

**Objetivo:** encapsular o armazenamento e converter entidades para o domínio.

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/data/today/PriorityRepository.kt`
- Modificar: `app/src/test/java/br/com/resetlife/data/today/PriorityRepositoryTest.kt`

**Passos:**

1. Criar `PriorityRepository` com observação, adição e conclusão.
2. Usar `Clock` injetável para derivar o dia atual em vez de chamar o relógio diretamente no teste.
3. Mapear `PriorityEntity` para `PriorityItem` e vice-versa.
4. Executar o teste específico e depois a suíte unitária.

**Verificação:** o teste do repositório passa e dados de outro dia não aparecem no Hoje.

---

## Task 5: Testar a migração inicial e o banco

**Objetivo:** proteger a criação do banco e a estratégia de versão.

**Arquivos:**
- Criar: `app/src/androidTest/java/br/com/resetlife/data/local/ResetLifeDatabaseTest.kt`
- Criar: `app/schemas/` quando exigido pelo exportSchema
- Modificar: `app/build.gradle.kts`

**Passos:**

1. Configurar exportação de schema do Room.
2. Criar banco em memória no teste instrumentado.
3. Confirmar inserção, leitura por dia e atualização de conclusão.
4. Confirmar que uma nova versão de schema possui migração explícita ou política segura definida.

**Verificação:** executar o teste instrumentado se houver dispositivo/emulador disponível; caso não haja, compilar o source set instrumentado e registrar a limitação.

---

## Task 6: Refatorar o ViewModel para o repositório persistente

**Objetivo:** remover o armazenamento principal em memória e expor estados de carregamento/erro.

**Arquivos:**
- Criar/modificar: `app/src/main/java/br/com/resetlife/presentation/today/TodayViewModel.kt`
- Modificar: `app/src/test/java/br/com/resetlife/presentation/today/TodayViewModelTest.kt`

**Estados mínimos:**

- `isLoading` durante a primeira observação;
- `priorities` do dia atual;
- `feedback` para limite, título vazio e erro de persistência;
- `hasStorageError` ou estado equivalente com mensagem não sensível.

**Passos:**

1. Injetar `PriorityRepository` e `Clock`/dia quando necessário.
2. Observar o `Flow` no `viewModelScope`.
3. Delegar inclusão e conclusão ao repositório após validar a regra de domínio.
4. Adicionar teste para o fluxo carregado e para erro de armazenamento.
5. Executar RED → GREEN → refatoração.

**Verificação:** fechar/reabrir o ViewModel não apaga o contrato de dados quando o mesmo repositório é reutilizado.

---

## Task 7: Conectar o banco real à Activity e tratar estados na UI

**Objetivo:** usar Room no app real e mostrar carregamento, vazio e erro de forma clara.

**Arquivos:**
- Criar: `app/src/main/java/br/com/resetlife/ResetLifeApplication.kt`
- Criar: `app/src/main/java/br/com/resetlife/data/ServiceLocator.kt` ou equivalente simples
- Criar: `app/src/main/java/br/com/resetlife/presentation/today/TodayViewModelFactory.kt`
- Modificar: `app/src/main/AndroidManifest.xml`
- Modificar: `app/src/main/java/br/com/resetlife/MainActivity.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`
- Modificar: `app/src/main/res/values/strings.xml`

**Passos:**

1. Criar uma instância única de `ResetLifeDatabase` por processo.
2. Construir o repositório Room e fornecer o ViewModel via factory.
3. Renderizar indicador de carregamento e estado vazio.
4. Mostrar erro sem expor stack trace, caminho ou dados privados.
5. Preservar tema, acessibilidade e limite de três prioridades.

**Verificação:** após reiniciar o aplicativo, uma prioridade criada permanece visível e uma prioridade concluída continua concluída.

---

## Task 8: Atualizar documentação e validar o APK 0.2.0

**Objetivo:** documentar o novo comportamento e gerar o artefato de release de desenvolvimento.

**Arquivos:**
- Modificar: `README.md`
- Modificar: `docs/ROADMAP.md` para registrar o estado da versão
- Criar: `docs/releases/0.2.0.md`

**Comandos:**

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
./gradlew :app:assembleAndroidTest
```

**Critérios de aceite:**

- [ ] `versionCode = 2` e `versionName = "0.2.0"`;
- [ ] prioridades persistem depois de fechar e reabrir o app;
- [ ] conclusão persiste;
- [ ] prioridades de outro dia não aparecem no Hoje;
- [ ] limite de três prioridades permanece válido após reinício;
- [ ] Room cria banco sem internet;
- [ ] estados de carregamento, vazio e erro estão tratados;
- [ ] testes unitários passam;
- [ ] APK debug e source set instrumentado compilam;
- [ ] nenhuma credencial, dado pessoal ou banco local é versionado.
