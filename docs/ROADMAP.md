# ResetLife — Roadmap das próximas 10 versões

**Base atual:** `0.1.0` — tela **Hoje** com até três prioridades ativas, conclusão local e testes unitários.  
**Horizonte:** próximas 10 versões incrementais, de `0.2.0` a `1.1.0`.  
**Estratégia:** entregar primeiro o ciclo principal de reset antes de expandir módulos periféricos ou adicionar integrações externas.

---

## Como os builds são classificados

| Tipo | Duração-alvo | Propósito | Regra de escopo |
|---|---:|---|---|
| **Build menor** | 1 a 2 semanas | Uma capacidade bem delimitada, melhoria de fluxo ou endurecimento de qualidade. | No máximo um módulo principal afetado e sem expansão externa. |
| **Build grande** | 3 a 5 semanas | Um marco de produto completo, com domínio, persistência, interface, testes e validação manual. | Exige plano próprio, testes de regressão e checklist de release. |

> As durações são estimativas de planejamento, não prazos prometidos. A prioridade é entregar incrementos verificáveis e utilizáveis.

### Portões obrigatórios em todo build

- aderência a `docs/ESCOPO.md` e `AGENTS.md`;
- testes unitários novos para regras de domínio alteradas;
- execução de `:app:testDebugUnitTest`;
- geração de APK com `:app:assembleDebug`;
- funcionamento offline;
- nenhuma coleta, conta, sincronização ou integração externa adicionada sem decisão explícita;
- atualização de documentação e notas de versão.

### Portões adicionais para build grande

- plano técnico em `docs/plans/` antes da implementação;
- revisão de migrações e preservação de dados locais;
- teste manual documentado em aparelho ou emulador;
- revisão de acessibilidade nos fluxos afetados;
- teste de regressão do ciclo: **Hoje → plano → execução → revisão → retomada**.

---

## Visão consolidada

| Ordem | Versão | Porte | Marco | Dependência principal |
|---:|---:|---|---|---|
| 1 | `0.2.0` | Menor | Prioridades persistentes | `0.1.0` |
| 2 | `0.3.0` | Menor | Tarefas e projetos simples | `0.2.0` |
| 3 | `0.4.0` | Grande | Onboarding e Plano de Reset | `0.2.0`, `0.3.0` |
| 4 | `0.5.0` | Menor | Check-in e revisão semanal | `0.4.0` |
| 5 | `0.6.0` | Grande | Hábitos e rotinas | `0.2.0`, `0.4.0` |
| 6 | `0.7.0` | Menor | Organização do ambiente | `0.3.0`, `0.4.0` |
| 7 | `0.8.0` | Grande | Finanças essenciais | `0.2.0` |
| 8 | `0.9.0` | Menor | Foco Pomodoro e planejamento diário | `0.3.0`, `0.4.0` |
| 9 | `1.0.0` | Grande | Beta fechado e soberania dos dados | versões `0.2.0` a `0.9.0` |
| 10 | `1.1.0` | Menor | Estabilização pós-beta | `1.0.0` |

---

# Builds menores

## 1. Versão `0.2.0` — Prioridades persistentes

**Objetivo:** fazer a tela Hoje sobreviver ao fechamento do aplicativo, preservando prioridades e conclusões localmente.

### Inclui

- Room/SQLite para prioridades diárias;
- repositório local e casos de uso para criar, concluir e consultar prioridades;
- carregamento da data atual ao abrir a tela Hoje;
- tratamento de estado vazio, carregamento e erro local;
- migração inicial de banco e testes de DAO/repositório;
- remoção do estado de prioridades exclusivamente em memória.

### Não inclui

- recorrência;
- sincronização na nuvem;
- edição completa de tarefas;
- projetos ou subtarefas.

### Critérios de aceite

- uma prioridade criada continua disponível após encerrar e abrir o app;
- conclusão permanece registrada;
- o limite de três prioridades ativas continua válido após reiniciar;
- testes de domínio, DAO e repositório passam;
- nenhum dado depende de internet.

---

## 2. Versão `0.3.0` — Tarefas e projetos simples

**Objetivo:** transformar prioridades em uma visão mais ampla de pendências, sem criar um gerenciador corporativo complexo.

### Inclui

- tarefa com título, área da vida, nota, data opcional e duração estimada;
- estados: próxima ação, agendada, algum dia e concluída;
- projeto simples com objetivo e tarefas relacionadas;
- promoção de tarefa para uma das três prioridades do dia;
- reprogramação individual de tarefa;
- busca local por título;
- testes para transições de estado e limite diário.

### Não inclui

- colaboração;
- kanban;
- anexos;
- recorrência avançada;
- integração com calendário externo.

### Critérios de aceite

- usuário cria tarefa e projeto offline;
- tarefa pode ser promovida para Hoje respeitando a capacidade diária;
- tarefa reprogramada não perde título, nota ou vínculo de projeto;
- busca retorna somente resultados locais relevantes.

---

## 4. Versão `0.5.0` — Check-in e revisão semanal

**Objetivo:** permitir que o usuário perceba seu estado e feche a semana com uma próxima direção clara.

### Inclui

- check-in diário de humor, energia, sono percebido e estresse;
- nota curta opcional para o dia;
- calendário/histórico simples de check-ins;
- fluxo de revisão semanal com pendências, aprendizados e três prioridades da próxima semana;
- resumo semanal básico na tela Perfil;
- linguagem não clínica e avisos de limitação do produto.

### Não inclui

- diagnóstico de saúde;
- planos terapêuticos;
- compartilhamento de diário;
- integração com sensores, wearables ou Google Fit.

### Critérios de aceite

- check-in e revisão permanecem locais após reiniciar;
- usuário consegue revisar uma semana e definir as três prioridades seguintes;
- nenhuma tela apresenta recomendação médica ou psicológica como fato profissional.

---

## 6. Versão `0.7.0` — Organização do ambiente

**Objetivo:** adicionar microações para reduzir desordem física sem criar um módulo complexo de inventário.

### Inclui

- listas por espaço: quarto, banheiro, cozinha, sala, documentos e digital;
- tarefas sugeridas de 5, 15 e 30 minutos;
- listas personalizadas;
- itens de descarte/doação;
- registro simples de última organização de cada espaço;
- conexão opcional de uma microtarefa com Hoje.

### Não inclui

- fotos de ambientes;
- inventário doméstico;
- marketplace ou integração com serviços de coleta/doação;
- geolocalização.

### Critérios de aceite

- usuário conclui uma microtarefa e visualiza a última organização do espaço;
- listas personalizadas funcionam offline;
- tarefas de ambiente podem ser priorizadas sem ultrapassar o limite diário.

---

## 8. Versão `0.9.0` — Foco Pomodoro e planejamento diário

**Objetivo:** oferecer uma sessão de foco simples, privada e orientada à ação, conectada às prioridades reais do dia — sem gamificação, pressão ou rastreamento externo.

### Inclui

- temporizador Pomodoro local com ciclo padrão de 25 minutos de foco e 5 minutos de pausa;
- configuração opcional de duração de foco, pausa curta e pausa longa;
- iniciar, pausar, retomar, encerrar ou cancelar uma sessão;
- cálculo de tempo baseado em marca temporal para manter a contagem correta após o app ficar em segundo plano;
- vínculo opcional de uma sessão a uma tarefa ou prioridade ativa;
- registro local de sessões concluídas e total de foco do dia;
- aviso local opcional ao terminar foco ou pausa;
- lembrete diário opcional de planejamento;
- replanejamento do dia: manter, reprogramar ou arquivar prioridade pendente;
- testes para transição de estado do timer, cálculo de tempo, configuração e vínculo com tarefa/prioridade.

### Não inclui

- push notifications de servidor;
- bloqueio de outros aplicativos;
- controle de ponto ou horas faturáveis;
- salas de foco, colaboração ou ranking;
- streaks, mensagens de culpa ou recompensas punitivas;
- sincronização de sessões entre dispositivos.

### Critérios de aceite

- usuário inicia, pausa, retoma e encerra um Pomodoro sem conexão com a internet;
- tempo restante permanece correto depois que o app vai para segundo plano e retorna;
- sessão concluída pode ser associada opcionalmente a uma tarefa ou prioridade;
- preferências de duração são validadas e persistem localmente;
- aviso de término é opt-in e pode ser desligado;
- usuário consegue replanejar prioridades não concluídas sem perder o histórico.

### Risco e contenção

O Pomodoro deve apoiar foco, não gerar ansiedade. A interface prioriza uma sessão por vez, sem placares, contagens públicas ou linguagem de produtividade compulsória.

---

## 10. Versão `1.1.0` — Estabilização pós-beta

**Objetivo:** corrigir os problemas reais encontrados no beta fechado sem expandir o escopo do produto.

### Inclui

- correções priorizadas por impacto e frequência;
- melhorias de clareza de interface a partir de feedback observado;
- ajustes de desempenho e consumo de bateria;
- revisão de acessibilidade em telas reportadas;
- atualização de testes de regressão;
- notas de versão transparentes.

### Não inclui

- novas integrações externas;
- novos módulos grandes;
- funcionalidades que não tenham sido validadas no beta.

### Critérios de aceite

- nenhum erro crítico conhecido bloqueia o ciclo principal;
- regressões críticas possuem teste automatizado;
- problemas reportados no beta têm estado documentado: corrigido, não reproduzido ou planejado.

---

> Antes do build grande `0.4.0`, a linha `0.3.x` terá nove incrementos menores focados exclusivamente em UX/UI. Consulte [`ROADMAP-UX-UI-0.3x.md`](ROADMAP-UX-UI-0.3x.md).

# Builds grandes

## 3. Versão `0.4.0` — Onboarding e Plano de Reset

**Objetivo:** entregar o núcleo da proposta do ResetLife: diagnosticar o momento atual e gerar um plano de reorganização editável.

### Inclui

- onboarding com explicação de privacidade/offline;
- autoavaliação de rotina, tarefas, casa, saúde, finanças, relações e bem-estar;
- escolha de até três áreas prioritárias;
- disponibilidade diária: 10, 20, 30 ou 45+ minutos;
- seleção de plano de 7, 14 ou 30 dias;
- regras determinísticas para gerar ações iniciais editáveis;
- metas por área, marcos semanais, pausa, retomada e encerramento;
- progresso do plano visível na tela Hoje;
- testes de geração, edição e retomada do plano.

### Não inclui

- IA generativa;
- diagnóstico clínico;
- recomendações personalizadas baseadas em dados externos;
- sincronização de planos entre dispositivos.

### Critérios de aceite

- um novo usuário conclui o onboarding em até cinco minutos;
- o plano gerado possui ações pequenas e editáveis;
- o usuário pode pausar, retomar ou encerrar o plano sem apagar histórico;
- o plano alimenta a tela Hoje sem exceder três prioridades;
- fluxo testado manualmente em aparelho/emulador e coberto por testes de domínio.

### Risco e contenção

O plano não pode se tornar genérico demais nem prescritivo. Começar com poucas regras transparentes e validar linguagem com usuários antes de aumentar sugestões.

---

## 5. Versão `0.6.0` — Hábitos e rotinas

**Objetivo:** sustentar a reorganização com comportamentos mínimos e uma visão semanal de consistência.

### Inclui

- hábito diário ou em dias da semana;
- metas binárias e quantitativas simples;
- registro em um toque;
- pausa, arquivamento e retomada;
- histórico semanal e tendência simples;
- hábitos-base opcionais: água, sono, movimento, organização rápida e planejamento;
- exibição de hábitos relevantes na tela Hoje;
- testes de frequência, metas, pausa e cálculo semanal.

### Não inclui

- ranking;
- streaks punitivos;
- desafios sociais;
- prescrição de exercícios, dieta ou medicação.

### Critérios de aceite

- hábito pode ser criado, registrado e pausado offline;
- a visão semanal diferencia dias previstos, concluídos e não previstos;
- dias perdidos não apagam histórico nem disparam linguagem de falha;
- hábitos exibidos em Hoje respeitam as preferências do usuário.

### Risco e contenção

Evitar sobrecarregar a tela Hoje. Começar com poucos hábitos visíveis e permitir que o usuário escolha quais aparecem no painel.

---

## 7. Versão `0.8.0` — Finanças essenciais

**Objetivo:** oferecer clareza financeira inicial sem transformar o ResetLife em um banco ou sistema contábil.

### Inclui

- contas/carteiras locais;
- receita, despesa e transferência;
- categorias editáveis;
- vencimento opcional;
- saldo manual por conta;
- visão de gastos do mês e por categoria;
- limite mensal por categoria;
- alertas apenas dentro do aplicativo para limites próximos ou ultrapassados;
- exportação de transações em CSV;
- testes de cálculos, transferências, limites e exportação.

### Não inclui

- Open Finance;
- importação automática de fatura;
- pagamentos;
- crédito, investimentos, impostos ou aconselhamento financeiro individualizado.

### Critérios de aceite

- transações persistem localmente e afetam os saldos esperados;
- transferência não altera o saldo consolidado;
- CSV exportado contém cabeçalho, categorias e valores corretos;
- limites não usam tom de culpa e não requerem internet;
- módulo validado com dados fictícios e testes de cálculo.

### Risco e contenção

Dados financeiros são sensíveis. Não registrar valores em logs, não usar analytics por padrão e revisar o fluxo de exclusão/exportação antes do beta.

---

## 9. Versão `1.0.0` — Beta fechado e soberania dos dados

**Objetivo:** consolidar o MVP para teste fechado com usuários reais, dados locais protegidos e fluxo de recuperação verificável.

### Inclui

- backup local em JSON;
- importação validada com confirmação explícita de mesclagem/substituição;
- exportação CSV de finanças;
- exclusão total de dados com confirmação inequívoca;
- bloqueio por biometria/PIN quando disponível;
- revisão de tema claro/escuro, escala de fonte, contraste e leitor de tela;
- ícone, screenshots, política de privacidade e material para Google Play;
- plano de testes manuais em aparelhos reais;
- distribuição em teste fechado na Google Play;
- coleta de feedback opt-in e anonimizada somente se a política estiver definida.

### Não inclui

- login obrigatório;
- nuvem;
- sincronização;
- publicidade;
- venda ou compartilhamento de dados;
- publicação aberta sem conclusão satisfatória do beta fechado.

### Critérios de aceite

- backup exportado pode ser importado em instalação limpa sem perda silenciosa;
- exclusão remove todos os dados locais confirmadamente;
- app funciona sem internet em todos os fluxos do MVP;
- fluxos críticos passam em teste manual documentado;
- não há crash crítico conhecido antes da distribuição fechada;
- build release assinado e checklist de Play Store concluídos apenas quando houver credenciais e autorização explícita de Gustavo.

### Risco e contenção

A publicação e a assinatura de release são ações externas. Preparar os artefatos e checklists, mas só publicar ou usar credenciais após autorização explícita.

---

## Dependências e ordem de execução

```text
0.1.0 Fundação atual
  └─ 0.2.0 Persistência
      ├─ 0.3.0 Tarefas e projetos
      │   └─ 0.4.0 Onboarding e Plano de Reset
      │       ├─ 0.5.0 Check-in e revisão semanal
      │       ├─ 0.6.0 Hábitos e rotinas
      │       └─ 0.7.0 Organização do ambiente
      ├─ 0.8.0 Finanças essenciais
      └─ 0.9.0 Foco Pomodoro e planejamento diário
          (conectado a 0.3.0 tarefas e 0.4.0 plano)
          
0.5.0 + 0.6.0 + 0.7.0 + 0.8.0 + 0.9.0
  └─ 1.0.0 Beta fechado e soberania dos dados
      └─ 1.1.0 Estabilização pós-beta
```

---

## Ordem de prioridade se o escopo precisar ser reduzido

1. `0.2.0` — persistência;
2. `0.3.0` — tarefas/projetos simples;
3. `0.4.0` — onboarding e plano de reset;
4. `0.5.0` — check-in e revisão semanal;
5. `0.6.0` — hábitos;
6. `1.0.0` — privacidade, backup e beta;
7. módulos de ambiente, finanças e foco Pomodoro conforme validação de uso.

Essa ordem preserva o diferencial do produto: ajudar alguém a sair da sobrecarga, executar poucas ações relevantes e retomar o controle sem depender de internet.