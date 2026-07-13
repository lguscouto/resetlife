# AGENTS.md — ResetLife

Este arquivo orienta todo agente que trabalhar neste repositório. Leia-o integralmente antes de analisar, criar ou alterar qualquer código, documento ou configuração.

## Contexto e fonte de verdade

**Produto:** ResetLife  
**Plataforma inicial:** aplicativo Android nativo (Android 10 / API 29 ou superior)  
**Idioma inicial:** português brasileiro (PT-BR)  
**Documento de produto canônico:** [`docs/ESCOPO.md`](docs/ESCOPO.md)

O ResetLife ajuda pessoas sobrecarregadas a reorganizar a vida de forma prática. O ciclo central do produto é:

> diagnóstico → plano de reset → execução diária → revisão semanal → retomada

O app não é apenas uma lista de tarefas. Ele organiza prioridades, hábitos, tarefas, finanças básicas, bem-estar e ambiente, sempre reduzindo a carga cognitiva do usuário.

Se houver divergência entre um pedido novo e o escopo, não altere silenciosamente a direção do produto. Explique o impacto, proponha uma decisão e, após aprovação, atualize `docs/ESCOPO.md` junto da implementação afetada.

---

## Princípios obrigatórios de produto

1. **Ação antes de registro.** Cada tela deve ajudar o usuário a decidir ou concluir algo.
2. **Poucas prioridades.** A tela Hoje deve limitar o usuário a três prioridades principais por dia.
3. **Retomada sem culpa.** Não usar mecânicas de punição, linguagem de fracasso ou perda de histórico por dias não concluídos.
4. **Offline-first.** Todo fluxo do MVP deve funcionar sem conta e sem internet.
5. **Simplicidade progressiva.** Não introduzir complexidade ou configuração sem necessidade comprovada.
6. **Privacidade por padrão.** Dados pertencem ao usuário; exportação e exclusão precisam ser claras.
7. **Saúde sem prescrição.** O produto organiza autocuidado, mas não diagnostica, prescreve ou substitui profissionais médicos, psicológicos ou financeiros.
8. **Notificações respeitosas.** Devem ser opt-in, configuráveis e nunca pressionar ou culpabilizar o usuário.

---

## Escopo do MVP

### Módulos incluídos

- onboarding com diagnóstico de áreas da vida e plano editável de 7, 14 ou 30 dias;
- tela **Hoje** com prioridades, hábitos, check-in e progresso do plano;
- tarefas, subtarefas e projetos simples;
- hábitos e rotinas;
- check-in de bem-estar e diário breve;
- organização de ambiente por microtarefas;
- finanças essenciais: contas locais, receitas, despesas, transferências, categorias, limites e exportação CSV;
- revisão semanal;
- notificações locais opcionais;
- tema claro/escuro, acessibilidade básica, backup JSON, importação/exportação e exclusão de dados;
- bloqueio por biometria/PIN quando suportado pela plataforma.

### Fora do MVP — não implementar sem aprovação explícita

- iOS, web ou desktop;
- login obrigatório, rede social, colaboração ou gamificação competitiva;
- IA generativa, chat terapêutico ou recomendações clínicas;
- Open Finance, conexão bancária, investimentos, pagamentos ou compras;
- Google Calendar, Google Fit, wearables ou integrações externas;
- rastreamento de localização;
- anexos pesados, upload de documentos ou armazenamento em nuvem;
- sincronização em nuvem;
- anúncios comportamentais, venda de dados ou rastreamento invasivo.

---

## Direção técnica inicial

Quando o projeto Android for criado, adote estas escolhas, salvo decisão posterior documentada:

| Área | Padrão |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Arquitetura | Clean Architecture pragmática + MVVM |
| Persistência local | Room / SQLite |
| Preferências | DataStore |
| Navegação | Navigation Compose |
| Injeção de dependências | Hilt |
| Tarefas em segundo plano | WorkManager |
| Testes | JUnit, Turbine, MockK e Compose UI Test |

### Regras de arquitetura

- O domínio não depende de Android, Compose, Room ou detalhes de rede.
- Separar claramente apresentação, domínio e dados.
- Modelos de banco não devem vazar diretamente para a UI.
- Regras críticas devem ser testáveis sem emulador.
- Começar simples: não criar microserviços, camadas genéricas ou abstrações especulativas.
- Preferir dependências oficiais e estáveis; justificar qualquer biblioteca adicional.
- Não adicionar backend, autenticação ou nuvem enquanto a necessidade não estiver definida e aprovada.

### Entidades de domínio previstas

`UserProfile`, `LifeArea`, `ResetPlan`, `ResetPlanDay`, `Task`, `Project`, `Habit`, `HabitLog`, `FinancialAccount`, `Transaction`, `Budget`, `WellbeingCheckIn`, `JournalEntry`, `SpaceChecklist`, `WeeklyReview` e `AppSettings`.

Não crie todas as entidades antecipadamente: adicione-as somente quando o módulo correspondente estiver sendo implementado.

---

## Organização prevista do repositório

O projeto ainda pode não ter sido inicializado. Ao criá-lo, usar uma estrutura Android clara e previsível:

```text
.
├── AGENTS.md
├── README.md
├── docs/
│   ├── ESCOPO.md
│   ├── plans/
│   └── decisions/
├── app/
└── ... módulos Android somente quando necessários
```

Se a escala exigir modularização, prefira módulos de responsabilidade clara, por exemplo:

```text
:app
:core:common
:core:designsystem
:core:database
:core:testing
:feature:onboarding
:feature:today
:feature:resetplan
:feature:tasks
:feature:habits
:feature:wellbeing
:feature:finance
:feature:weeklyreview
```

Não modularize prematuramente. Para o primeiro incremento funcional, um módulo `app` bem organizado é aceitável.

---

## Convenções de desenvolvimento

### Kotlin e Compose

- Usar nomes claros e consistentes em inglês no código; conteúdo visível ao usuário em PT-BR deve vir de recursos de string.
- Não colocar textos de UI hardcoded em composables.
- Usar `StateFlow`/estado imutável para estado de tela.
- Tratar carregamento, vazio, sucesso e erro explicitamente quando o fluxo exigir.
- Composables devem ser pequenos, reutilizáveis e previsíveis.
- Não executar I/O, regras de negócio ou mutações diretamente no corpo de um composable.
- Usar `MaterialTheme` e tokens de design; não espalhar cores ou tamanhos literais pela UI.
- Não usar cor como única forma de comunicar estado.

### Dados e privacidade

- Dados do usuário são pessoais e potencialmente sensíveis.
- Nunca registrar conteúdo de diário, transações, notas, tokens, PINs ou dados biométricos em logs.
- Não enviar dados a serviços externos sem consentimento explícito e uma decisão arquitetural documentada.
- Para ações destrutivas, oferecer confirmação inequívoca e explicar o impacto.
- Importação de backup deve validar o arquivo antes de substituir ou mesclar dados.
- Exportações devem usar formatos abertos: JSON para backup e CSV para finanças.

### Qualidade e acessibilidade

- Suportar tema claro, escuro e preferência do sistema desde o início.
- Manter contraste compatível com WCAG AA, alvos de toque de pelo menos 48 dp e textos escaláveis.
- Incluir descrições acessíveis para ícones relevantes.
- Testar em aparelho/emulador com Android 10+ e ao menos uma versão Android recente.
- Não apresentar funcionalidades de saúde ou finanças como aconselhamento profissional.

---

## Processo obrigatório para mudanças

1. **Inspecionar antes de editar.** Ler os arquivos relacionados, a estrutura atual e os testes existentes.
2. **Confirmar o encaixe no escopo.** Verificar `docs/ESCOPO.md`; não expandir MVP por suposição.
3. **Planejar incrementos pequenos.** Para alterações não triviais, criar um plano em `docs/plans/AAAA-MM-DD-nome.md` com objetivo, arquivos, passos e validação.
4. **Implementar a menor solução completa.** Evitar stubs, TODOs sem contexto e código morto.
5. **Testar.** Criar/ajustar testes de domínio e executar os testes relevantes.
6. **Validar manualmente.** Para mudanças de UI, testar a jornada afetada em emulador ou aparelho quando o ambiente existir.
7. **Documentar.** Atualizar README, escopo, decisões ou plano quando a mudança afetar comportamento, arquitetura ou setup.
8. **Reportar fatos verificáveis.** Informar arquivos alterados, comandos executados e resultado real dos testes; nunca inventar validações.

### Test-first para regras de negócio

Para lógica de domínio, usar o ciclo:

1. escrever ou atualizar o teste que demonstra o comportamento esperado;
2. executar e confirmar que ele falha pelo motivo correto;
3. implementar o mínimo necessário;
4. executar novamente e confirmar que passa;
5. refatorar somente com testes verdes.

Exemplos de regras que exigem testes unitários: limite de três prioridades diárias, geração do plano de reset, reprogramação de tarefas, cálculos de orçamento, progresso de hábitos, importação/exportação e exclusão de dados.

---

## Critérios mínimos de aceite por funcionalidade

Uma funcionalidade só está pronta quando:

- atende a um requisito explícito do escopo ou de uma decisão aprovada;
- possui estados de interface relevantes e tratamento de entradas inválidas;
- preserva os dados existentes e não introduz perda silenciosa;
- contém testes para regras de domínio e casos limites relevantes;
- passa pelos testes e verificações aplicáveis, com resultado real registrado;
- funciona offline quando fizer parte do MVP;
- respeita acessibilidade, tema e privacidade;
- não introduz dependências ou permissões desnecessárias;
- documentação afetada foi atualizada.

---

## Git e alterações de arquivos

- Nunca apagar ou substituir dados importantes sem autorização explícita.
- Antes de mudanças amplas, verificar `git status`, diff e arquivos impactados.
- Preferir commits pequenos e sem mistura de refatoração não relacionada.
- Formato de commit recomendado: `tipo: resumo curto`.
  - Exemplos: `feat: add daily priorities`, `fix: preserve task on reschedule`, `test: cover budget limit calculation`, `docs: define reset plan flow`.
- Não versionar segredos, arquivos de build, chaves de assinatura, backups pessoais ou dados reais de usuários.
- Manter `.gitignore` adequado para Android, IDE e arquivos locais.

---

## Comunicação com Gustavo

- Responder em português brasileiro, de forma direta e objetiva.
- Antes de executar ações externas, irreversíveis ou que possam ter custo, pedir autorização explícita.
- Quando houver ambiguidade não crítica, aplicar a alternativa mais simples e declarar a suposição no resultado.
- Quando uma premissa estiver errada ou for arriscada, explicar objetivamente e sugerir uma alternativa melhor.
- Ao concluir, informar o que foi criado/alterado, como foi validado e limitações reais.

---

## Referências rápidas

- Escopo completo do produto: [`docs/ESCOPO.md`](docs/ESCOPO.md)
- Para decisões arquiteturais relevantes, criar um registro em `docs/decisions/` com contexto, decisão, alternativas e consequências.
- Para trabalho de implementação com mais de um incremento, criar um plano em `docs/plans/` antes de começar.
