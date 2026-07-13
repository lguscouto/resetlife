# ResetLife — Escopo do Produto

**Versão:** 0.1 — definição inicial  
**Plataforma inicial:** Android 10+  
**Estado:** escopo base para MVP

---

## 1. Visão do produto

**ResetLife** é um aplicativo Android de organização pessoal para pessoas que se sentem sobrecarregadas, desorganizadas ou paralisadas por muitas pendências. O produto transforma a intenção de “dar um reset na vida” em um processo simples e executável: diagnosticar o momento atual, escolher poucas prioridades, organizar rotinas e acompanhar progresso sem culpa.

O app não será apenas uma lista de tarefas. Ele será um **sistema pessoal de reorganização**, reunindo áreas práticas da vida em uma experiência única, privada e orientada à ação diária.

### Proposta de valor

> Em poucos minutos por dia, o usuário entende o que importa agora, executa o próximo passo e recupera controle sobre sua rotina, dinheiro, saúde e ambiente.

### Diferenciais do ResetLife

- **Plano de reinício guiado:** diagnóstico inicial e plano de 7, 14 ou 30 dias.
- **Foco no próximo passo:** reduz listas gigantes em ações pequenas e priorizadas.
- **Visão integrada da vida:** rotina, tarefas, hábitos, finanças básicas, autocuidado e organização do ambiente.
- **Privacidade por padrão:** uso completo offline; conta e sincronização não são obrigatórias no MVP.
- **Sem tom punitivo:** progresso baseado em consistência e retomada, não em sequências perfeitas.

---

## 2. Problema que o aplicativo resolve

O usuário-alvo normalmente vive uma combinação de situações:

- muitas tarefas, compromissos e preocupações dispersas;
- dificuldade de decidir por onde começar;
- hábitos irregulares e baixa constância;
- sensação de descontrole financeiro, físico, doméstico ou emocional;
- uso de ferramentas isoladas que não oferecem uma visão prática do todo;
- abandono de aplicativos excessivamente complexos, rígidos ou cheios de notificações.

O ResetLife deve diminuir a carga cognitiva. A função central não é registrar tudo: é **ajudar a escolher e concluir o que importa hoje**.

---

## 3. Público-alvo e personas

### Público primário

Adultos de 18 a 45 anos, usuários de Android, que querem reorganizar a vida prática e estabelecer uma rotina sustentável. Não exige conhecimento prévio de produtividade ou finanças.

### Persona 1 — “Quero retomar o controle”

- Tem pendências pessoais, profissionais e domésticas acumuladas.
- Já tentou listas e planners, mas abandona após alguns dias.
- Precisa de um começo claro e de vitórias rápidas.

### Persona 2 — “Quero consolidar bons hábitos”

- Já possui alguma rotina, mas é inconsistente.
- Quer acompanhar hábitos, tarefas e autocuidado sem trocar entre vários apps.
- Valoriza gráficos simples e evolução semanal.

### Persona 3 — “Quero me organizar sem expor meus dados”

- Deseja registrar informações pessoais e financeiras básicas.
- Prefere funcionamento offline e controle de exportação.
- Pode utilizar bloqueio biométrico no aparelho.

---

## 4. Objetivos mensuráveis do MVP

O MVP será considerado funcional quando permitir que um novo usuário:

1. conclua o onboarding e gere um plano de reset em até **5 minutos**;
2. registre áreas prioritárias e tarefas iniciais;
3. visualize uma agenda diária com no máximo **3 prioridades principais**;
4. crie e acompanhe hábitos simples;
5. registre entradas e saídas financeiras básicas;
6. registre uma checagem diária de bem-estar;
7. acompanhe a evolução semanal e reinicie um dia ruim sem perder o histórico;
8. use o app integralmente sem criar conta e sem conexão com a internet.

### Métricas de produto para versão beta

- taxa de conclusão do onboarding;
- percentual de usuários que conclui ao menos uma ação no primeiro dia;
- retenção D7 e D30;
- média de check-ins semanais por usuário ativo;
- média de prioridades concluídas por semana;
- quantidade de planos de reset iniciados e concluídos.

A coleta analítica deverá ser **opt-in**, anonimizada e claramente explicada. O MVP pode iniciar sem analytics externo.

---

## 5. Princípios de produto

1. **Ação antes de registro:** cada tela deve aproximar o usuário de uma decisão ou conclusão.
2. **Poucas prioridades:** o Hoje não pode incentivar listas intermináveis.
3. **Retomada sem culpa:** dias perdidos não quebram o produto nem apagam progresso.
4. **Offline primeiro:** dados essenciais funcionam localmente, sem cadastro obrigatório.
5. **Simplicidade progressiva:** recursos avançados só aparecem quando agregarem valor.
6. **Dados do usuário pertencem ao usuário:** exportação e exclusão local precisam ser claras.
7. **Saúde sem prescrição:** o app organiza autocuidado, mas não substitui atendimento médico, psicológico ou financeiro profissional.

---

## 6. Estrutura funcional do MVP

### 6.1 Onboarding e diagnóstico de reset

**Objetivo:** transformar a sensação vaga de desorganização em um plano inicial viável.

**Fluxo:**

1. Boas-vindas e explicação de privacidade/offline.
2. Usuário avalia de 0 a 10 as áreas: rotina, tarefas, casa, saúde, finanças, relações e bem-estar.
3. Usuário escolhe até 3 áreas prioritárias.
4. Usuário define disponibilidade diária aproximada: 10, 20, 30 ou 45+ minutos.
5. Usuário escolhe duração do plano: 7, 14 ou 30 dias.
6. O app gera uma estrutura inicial editável com pequenas ações e hábitos-base.
7. Usuário confirma e chega à tela **Hoje**.

**Critérios de aceite:**

- o usuário pode pular perguntas não essenciais;
- nenhuma recomendação depende de dados médicos ou financeiros sensíveis;
- o plano gerado é sempre editável;
- o fluxo pode ser repetido sem apagar o histórico anterior.

### 6.2 Tela Hoje — painel diário

**Objetivo:** ser a tela principal e responder: “o que devo fazer agora?”

**Conteúdo:**

- saudação e data;
- check-in rápido de bem-estar;
- bloco “Minhas 3 prioridades”;
- próximo compromisso ou tarefa agendada;
- hábitos do dia;
- progresso do plano de reset;
- botão de captura rápida para nova tarefa, nota ou gasto;
- opção “Replanejar meu dia”.

**Regras:**

- máximo de 3 tarefas marcadas como prioridade por dia;
- tarefas não concluídas podem ser reprogramadas, mantidas ou arquivadas;
- não há punição automática por atrasos;
- o painel deve abrir em menos de 2 segundos em aparelhos intermediários.

### 6.3 Plano de Reset

**Objetivo:** orientar uma fase concentrada de reorganização.

**Funções:**

- criação de planos de 7, 14 ou 30 dias;
- metas por área de vida;
- ações diárias sugeridas e totalmente editáveis;
- percentual de progresso;
- marcos semanais;
- resumo final com conquistas e itens ainda abertos;
- possibilidade de pausar, retomar, encerrar ou iniciar novo plano.

**Exemplos de ações sugeridas:**

- listar contas e vencimentos da semana;
- descartar ou organizar uma categoria pequena da casa;
- agendar uma pendência importante;
- preparar uma rotina de sono;
- fazer uma caminhada curta;
- entrar em contato com uma pessoa importante.

As sugestões devem ser genéricas e opcionais; não usar recomendações clínicas, dietas prescritas ou aconselhamento terapêutico.

### 6.4 Tarefas e projetos pessoais

**Objetivo:** organizar pendências sem transformar o aplicativo em um gerenciador corporativo complexo.

**Funções do MVP:**

- criar tarefa com título, área da vida, data opcional, duração estimada, prioridade e nota;
- criar subtarefas;
- classificar como “próxima ação”, “agendada”, “algum dia” ou “concluída”;
- criar projetos simples com objetivo e tarefas relacionadas;
- captura rápida na tela Hoje;
- busca local;
- reprogramar tarefas vencidas em lote ou individualmente.

**Fora do MVP:** colaboração, anexos pesados, kanban complexo, automações externas e recorrência avançada.

### 6.5 Rotinas e hábitos

**Objetivo:** reforçar comportamentos mínimos que sustentam a reorganização.

**Funções do MVP:**

- criar hábito com frequência diária ou dias da semana;
- definir meta binária (feito/não feito) ou quantitativa simples;
- registrar conclusão em um toque;
- visualizar semana atual e histórico básico;
- permitir pausar ou arquivar hábitos;
- oferecer hábitos-base opcionais durante o onboarding: água, sono, movimento, organização rápida e planejamento.

**Regra de experiência:** mostrar consistência semanal, mas evitar linguagem de “falha”, “quebra” ou punição por sequência perdida.

### 6.6 Organização financeira essencial

**Objetivo:** oferecer clareza financeira inicial, não substituir um sistema contábil completo.

**Funções do MVP:**

- cadastrar contas/carteiras locais (ex.: dinheiro, banco, cartão);
- registrar receita, despesa e transferência;
- usar categorias simples e editáveis;
- registrar vencimento opcional;
- visualizar saldo manual por conta, gastos do mês e gastos por categoria;
- criar limite mensal por categoria;
- alertar dentro do aplicativo quando um limite estiver próximo ou ultrapassado;
- exportar dados em CSV.

**Fora do MVP:** conexão bancária/Open Finance, importação automática de fatura, investimentos, impostos, crédito, pagamentos ou aconselhamento financeiro.

### 6.7 Bem-estar e diário breve

**Objetivo:** permitir percepção de padrões sem tentar diagnosticar condições de saúde mental ou física.

**Funções do MVP:**

- check-in diário: humor, energia, sono percebido e nível de estresse em escala simples;
- campo opcional “o que ajudaria hoje?”;
- diário em texto livre;
- histórico em calendário;
- visão semanal de tendências pessoais;
- tela de apoio com contatos que o usuário pode cadastrar para ajuda pessoal.

**Segurança:** a tela deve deixar claro que o aplicativo não é atendimento de emergência. Em futuro lançamento, poderá oferecer atalhos de contato configuráveis, sem afirmar serviços locais sem validação por país/região.

### 6.8 Organização do ambiente

**Objetivo:** reduzir desordem física através de microações concluíveis.

**Funções do MVP:**

- checklist por espaço: quarto, banheiro, cozinha, sala, documentos e digital;
- tarefas de 5, 15 e 30 minutos;
- lista de descarte/doação;
- registro simples de “última organização”;
- criação de listas próprias.

### 6.9 Revisão semanal

**Objetivo:** fechar ciclos, extrair aprendizados e planejar a próxima semana.

**Perguntas do fluxo:**

- o que foi concluído?
- o que ficou pendente e ainda importa?
- o que dificultou a semana?
- quais são as 3 prioridades da próxima semana?
- qual hábito merece ajuste?

**Resultado:** uma visão resumida de tarefas, hábitos, check-ins e finanças, seguida por um plano semanal editável.

### 6.10 Configurações, privacidade e dados

**Funções obrigatórias:**

- tema claro, escuro e seguir sistema;
- idioma inicial PT-BR, com arquitetura preparada para localização futura;
- bloqueio por biometria/PIN, quando disponível no aparelho;
- exportar dados locais em JSON e módulos financeiros em CSV;
- importar backup JSON com confirmação e explicação de substituição/mesclagem;
- apagar dados locais com confirmação explícita;
- preferências de notificações;
- política de privacidade acessível dentro do app.

---

## 7. Navegação proposta

A navegação inferior terá cinco destinos principais:

| Aba | Propósito |
|---|---|
| **Hoje** | Prioridades, hábitos e check-in diário. |
| **Plano** | Plano de reset ativo, metas e marcos. |
| **Organizar** | Tarefas, projetos e organização de ambiente. |
| **Vida** | Hábitos, finanças e bem-estar. |
| **Perfil** | Revisão semanal, dados, privacidade e configurações. |

A ação flutuante `+` deverá permitir captura rápida de tarefa, gasto ou nota em qualquer tela principal.

---

## 8. Direção de UX/UI

### Personalidade visual

- calma, limpa, adulta e acolhedora;
- objetiva, sem infantilização ou estética de jogo;
- sensação de recomeço e clareza, não de pressão;
- interface com bastante espaço em branco, hierarquia forte e microinterações discretas.

### Paleta inicial

| Uso | Cor | HEX |
|---|---:|---:|
| Primária — renovação | Verde petróleo | `#1F6B5B` |
| Secundária — ação | Azul profundo | `#245A8D` |
| Destaque positivo | Verde suave | `#A7D7C5` |
| Atenção | Âmbar | `#D99028` |
| Fundo claro | Névoa | `#F7F8F6` |
| Texto principal | Grafite | `#202522` |
| Fundo escuro | Carvão | `#141816` |

### Tipografia sugerida

- **Inter** para interface, números e dados;
- **DM Sans** como alternativa caso seja necessário um tom mais humano;
- seguir escalas e componentes do Material Design 3, sem personalizações que prejudiquem acessibilidade.

### Acessibilidade mínima

- contraste compatível com WCAG AA;
- fonte escalável pelo sistema;
- alvos de toque de pelo menos 48 dp;
- conteúdo descritivo para leitor de tela;
- informações importantes não dependem apenas de cor;
- suporte a tema escuro desde o MVP.

---

## 9. Notificações

Notificações são opcionais e devem ser configuráveis por módulo.

### Permitidas no MVP

- lembrete diário de planejamento ou check-in;
- lembrete de hábito;
- lembrete de tarefa agendada;
- convite para revisão semanal;
- alerta local de vencimento ou limite financeiro.

### Regras

- padrão inicial conservador: no máximo uma notificação diária, exceto tarefas com horário definido;
- horários configuráveis;
- desativação simples por módulo e global;
- não usar notificações de pressão, culpa ou streaks.

---

## 10. Requisitos não funcionais

### Privacidade e segurança

- dados armazenados localmente em banco criptografável/seguro, conforme viabilidade da plataforma;
- nenhuma credencial ou dado sensível em logs;
- biometria implementada usando APIs oficiais do Android;
- backups e exportações exigem confirmação explícita;
- se houver nuvem futuramente, sincronização deve ser opt-in e protegida por autenticação forte;
- não integrar anúncios comportamentais ou venda de dados.

### Desempenho e compatibilidade

- Android 10 (API 29) ou superior;
- modo offline para todos os fluxos do MVP;
- primeiro carregamento e tela Hoje responsivos em dispositivo intermediário;
- banco preparado para milhares de registros sem degradação perceptível;
- consumo de bateria baixo; tarefas de fundo apenas quando necessárias.

### Qualidade

- validação de formulários e prevenção de perda acidental de dados;
- testes unitários para regras de domínio;
- testes de interface para onboarding, criação/conclusão de tarefa, hábito, gasto e backup;
- suporte a rotação de tela e restauração de estado;
- tratamento claro de erros de importação/exportação.

---

## 11. Arquitetura técnica recomendada

A recomendação é desenvolver o MVP como aplicativo Android nativo, privilegiando estabilidade offline e manutenção de longo prazo.

| Camada | Escolha recomendada | Justificativa |
|---|---|---|
| Linguagem | Kotlin | Padrão moderno do Android e segurança de tipos. |
| Interface | Jetpack Compose + Material 3 | UI declarativa, consistente e acessível. |
| Arquitetura | Clean Architecture pragmática + MVVM | Separa regras de negócio, dados e telas sem complexidade excessiva. |
| Persistência | Room (SQLite) | Dados offline confiáveis e consultas estruturadas. |
| Preferências | DataStore | Preferências e configurações seguras/substitutas do SharedPreferences. |
| Injeção de dependência | Hilt | Organização de dependências e testabilidade. |
| Navegação | Navigation Compose | Fluxo explícito entre telas. |
| Trabalho em segundo plano | WorkManager | Lembretes, backup local e tarefas resilientes. |
| Notificações | APIs nativas Android | Controle e permissões adequados. |
| Gráficos | Compose Canvas ou Vico | Gráficos simples, locais e leves. |
| Testes | JUnit, Turbine, MockK e Compose UI Test | Cobertura de regras e principais jornadas. |

### Entidades principais

- `UserProfile`
- `LifeArea`
- `ResetPlan`
- `ResetPlanDay`
- `Task`
- `Project`
- `Habit`
- `HabitLog`
- `FinancialAccount`
- `Transaction`
- `Budget`
- `WellbeingCheckIn`
- `JournalEntry`
- `SpaceChecklist`
- `WeeklyReview`
- `AppSettings`

### Decisões arquiteturais

- Cada módulo possui regras próprias, mas pode alimentar a tela Hoje.
- O domínio não deve depender da interface ou do banco de dados.
- A lógica de sugestão inicial do plano será determinística e baseada em regras simples; IA generativa não faz parte do MVP.
- Sincronização em nuvem não será incluída até que a segurança, custos e política de privacidade estejam definidos.

---

## 12. Fora do escopo do MVP

Para manter o produto lançável, os itens abaixo ficam explicitamente fora da primeira versão:

- iOS, web e desktop;
- login obrigatório e rede social;
- chat com IA, terapia por IA ou diagnóstico de saúde;
- integração bancária/Open Finance;
- pagamentos, compras, assinaturas ou marketplace;
- integração com Google Calendar, Google Fit, wearables ou apps de terceiros;
- colaboração familiar/equipe;
- gamificação baseada em ranking, punição ou exposição;
- rastreamento de localização;
- aconselhamento médico, psicológico, jurídico ou financeiro individualizado;
- recursos de alimentação, treino ou medicação detalhados;
- upload de documentos e mídia pesada.

Esses itens só devem ser avaliados após validação do ciclo principal: **diagnóstico → plano → execução diária → revisão → retomada**.

---

## 13. Roadmap por fases

> O planejamento detalhado das próximas 10 versões, com classificação entre builds grandes e menores, está em [`ROADMAP.md`](ROADMAP.md).

### Fase 0 — Descoberta e protótipo (1 a 2 semanas)

- consolidar este escopo;
- entrevistar de 5 a 10 usuários do público-alvo;
- criar fluxo navegável de onboarding, Hoje e Plano;
- validar linguagem, número de áreas e limite de prioridades;
- definir nome final, ícone e identidade visual inicial.

**Saída:** protótipo clicável e decisões registradas.

### Fase 1 — Fundação técnica (1 a 2 semanas)

- configurar projeto Android, arquitetura e design system;
- configurar Room, DataStore, navegação e testes;
- criar banco, migrações iniciais e camada de backup;
- implementar tema claro/escuro e acessibilidade base.

**Saída:** aplicativo inicial navegável, testável e persistente offline.

### Fase 2 — Núcleo de reset (3 a 4 semanas)

- onboarding e diagnóstico;
- plano de reset;
- tela Hoje;
- tarefas, projetos simples e prioridades;
- hábitos e check-in de bem-estar;
- revisão semanal básica.

**Saída:** MVP alfa utilizável para teste pessoal diário.

### Fase 3 — Vida prática (2 a 3 semanas)

- organização de ambiente;
- finanças essenciais;
- notificações locais;
- exportação/importação;
- biometria e controles de privacidade.

**Saída:** beta fechado com todos os módulos do MVP.

### Fase 4 — Beta, qualidade e publicação (2 a 4 semanas)

- testes em aparelhos reais;
- correções de estabilidade e desempenho;
- revisão de acessibilidade e política de privacidade;
- material de loja, ícone, screenshots e trilha de distribuição;
- publicação em teste fechado na Google Play.

**Saída:** versão candidata a lançamento.

---

## 14. Critérios de aceite do MVP

O MVP estará pronto para beta fechado quando:

- [ ] o onboarding cria um plano editável de 7, 14 ou 30 dias;
- [ ] a tela Hoje consolida prioridades, hábitos, check-in e progresso do plano;
- [ ] o usuário cria, edita, conclui, arquiva e reprograma tarefas;
- [ ] o limite de três prioridades diárias é respeitado;
- [ ] hábitos podem ser registrados e vistos por semana;
- [ ] check-ins e diário podem ser salvos e consultados;
- [ ] receitas, despesas, categorias e limites podem ser registrados localmente;
- [ ] os dados persistem após fechar e reabrir o app;
- [ ] há modo claro, escuro e controles básicos de acessibilidade;
- [ ] lembretes locais respeitam preferências do usuário;
- [ ] backup JSON e exportação CSV funcionam em testes reais;
- [ ] exclusão de dados exige confirmação e funciona integralmente;
- [ ] os fluxos críticos possuem testes automatizados e testes manuais documentados;
- [ ] o app funciona sem internet e sem cadastro.

---

## 15. Riscos e mitigação

| Risco | Impacto | Mitigação |
|---|---|---|
| Escopo excessivo | Alto | Priorizar o ciclo de reset e manter itens de fora do MVP bloqueados. |
| Baixa adesão após onboarding | Alto | Plano inicial curto, editável e focado em vitórias no primeiro dia. |
| Interface virar lista de funções | Alto | Tela Hoje como ponto central e limite explícito de prioridades. |
| Dados sensíveis | Alto | Offline por padrão, biometria, exportação consciente e ausência de ads. |
| Notificações irritantes | Médio | Opt-in, horário configurável e frequência conservadora. |
| Complexidade financeira | Médio | Manter somente registro e visualização básicos no MVP. |
| Sugestões inadequadas de saúde | Alto | Linguagem não clínica, sugestões opcionais e avisos de limitação. |
| Sincronização prematura | Médio | Não incluir nuvem antes de validar segurança e necessidade real. |

---

## 16. Próximas decisões necessárias antes do desenvolvimento

1. Confirmar se o primeiro lançamento será exclusivamente em português brasileiro.
2. Definir se o nome de marca será `ResetLife`, `Reset Life` ou outra variação para loja e domínio.
3. Definir se o app será gratuito, freemium ou pago — sem implementar monetização no MVP.
4. Criar protótipo das três jornadas principais: onboarding, Hoje e plano de reset.
5. Transformar este escopo em backlog priorizado e plano técnico de implementação.

---

## Resumo executivo

O ResetLife será um aplicativo Android **offline-first** que conduz o usuário por um plano de reorganização de vida e o apoia diariamente com prioridades, tarefas, hábitos, finanças essenciais, bem-estar e organização do ambiente. O MVP precisa provar que o usuário consegue sair da sobrecarga para uma rotina mais controlada — não que o aplicativo consiga reunir todos os recursos de produtividade existentes.
