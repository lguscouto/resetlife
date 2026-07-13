# ResetLife `0.3.7` — Acessibilidade das jornadas principais

> **Para o Hermes:** implementar este plano no repositório ResetLife respeitando `AGENTS.md` e o ciclo RED → GREEN → REFACTOR.

**Objetivo:** garantir que as jornadas Hoje e Organizar sejam utilizáveis com TalkBack, foco lógico, rótulos semânticos, áreas de toque de 48 dp, indicação de status além de cor e escalabilidade de fonte.

**Arquitetura:** manter domínio, Room, ViewModels e stores inalterados. As mudanças são exclusivamente na camada de apresentação (composables, semantics, spacing, strings).

---

## Escopo

- adicionar `contentDescription` e `stateDescription` em checkboxes de prioridade e tarefa;
- adicionar `contentDescription` no botão de promover tarefa;
- adicionar `contentDescription` nos botões de expandir/recolher formulários;
- adicionar indicação de status além de cor (ícone ou prefixo textual) em prioridades e tarefas concluídas;
- garantir áreas de toque de no mínimo 48 dp em todos os controles interativos;
- adicionar `semantics` live region nas mensagens de feedback para TalkBack;
- garantir que textos escaláveis não cortem títulos ou botões essenciais;
- revisar contraste dos estados principais;
- adicionar strings de acessibilidade em PT-BR.

## Arquivos previstos

- Modificar `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`;
- Modificar `app/src/main/java/br/com/resetlife/presentation/components/ResetLifeComponents.kt`;
- Modificar `app/src/main/res/values/strings.xml`;
- Atualizar `app/build.gradle.kts`, `README.md` e criar `docs/releases/0.3.7.md`.

## Critérios de aceite

- usuário consegue navegar, criar e concluir uma prioridade com TalkBack;
- usuário consegue criar e concluir uma tarefa com TalkBack;
- foco não desaparece após abrir menu, erro ou confirmação;
- texto ampliado não corta títulos ou botões essenciais;
- estados concluído, aberto, erro e selecionado têm indicação além da cor.