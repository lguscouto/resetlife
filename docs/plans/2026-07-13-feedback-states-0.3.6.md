# ResetLife `0.3.6` — Feedback e estados de operação

> **Para o Hermes:** implementar este plano no repositório ResetLife respeitando `AGENTS.md` e o ciclo RED → GREEN → REFACTOR.

**Objetivo:** tornar explícitos os estados de carregamento, vazio, erro e salvamento nas jornadas Hoje e Organizar, com tentativa novamente para falhas de persistência.

**Arquitetura:** manter a regra de negócio e Room inalterados. Os ViewModels continuam donos do estado de tela e passam a expor erro de carregamento, operação recuperável e retry; os composables apenas renderizam esses estados. A mensagem reutilizável de feedback receberá uma ação opcional.

**Stack:** Kotlin, ViewModel, StateFlow, coroutines, Jetpack Compose Material 3 e JUnit.

---

## Escopo

- diferenciar carregamento, ausência de dados e falha de armazenamento;
- mostrar indicador consistente durante carregamento e salvamento;
- oferecer `Tentar novamente` em falhas de leitura ou persistência;
- preservar rascunhos quando uma gravação falhar;
- manter botões desabilitados durante operações pendentes;
- aplicar microinteração não bloqueante na entrada/alteração de mensagens;
- não expor stack trace, caminhos locais ou detalhes internos ao usuário.

## Arquivos previstos

- Modificar `app/src/main/java/br/com/resetlife/presentation/today/TodayViewModel.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeViewModel.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/AppScreen.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/components/ResetLifeComponents.kt`;
- Modificar `app/src/main/res/values/strings.xml`;
- Ajustar testes de estado e ViewModels correspondentes;
- Atualizar `app/build.gradle.kts`, `README.md` e criar `docs/releases/0.3.6.md`.

## Sequência TDD

1. Criar testes para `canRetry`, erro recuperável e retry após falha de persistência.
2. Executar os testes direcionados e confirmar falha pelo comportamento ausente.
3. Implementar o estado e as operações de retry nos ViewModels.
4. Executar novamente os testes direcionados e confirmar sucesso.
5. Implementar a apresentação dos estados e a ação visual de retry.
6. Executar suíte unitária, build debug, APK instrumentado e testes conectados.
7. Instalar o APK no `Pixel_8`, validar loading/vazio/erro/retry e verificar logcat.
8. Atualizar release e publicar com commit convencional e `git push origin HEAD`.

## Critérios de aceite

- loading não é confundido com lista vazia;
- erro de persistência exibe ação `Tentar novamente`;
- retry funciona sem apagar o rascunho;
- ações em andamento não aceitam segundo toque;
- mensagens de sucesso e erro são claras em PT-BR;
- nenhum stack trace ou caminho interno aparece na UI;
- testes, build e validação no emulador passam.
