# ResetLife — Roadmap UX/UI das versões `0.3.1` a `0.3.9`

**Base:** `0.3.0` — tarefas, projetos, tela Hoje, tela Organizar e navegação inicial.  
**Objetivo:** melhorar clareza, facilidade de uso, acessibilidade e consistência visual antes de iniciar o build grande `0.4.0` — Onboarding e Plano de Reset.  
**Natureza:** todos os incrementos são builds menores focados em UX/UI; não devem introduzir novos módulos de negócio.

---

## Princípio da sequência

Antes de expandir o produto, o ResetLife precisa ficar fácil de entender e agradável de usar. Esta sequência prioriza:

1. consistência visual;
2. clareza da arquitetura de informação;
3. melhoria das jornadas Hoje e Organizar;
4. formulários mais rápidos e tolerantes a erros;
5. feedback de sistema;
6. acessibilidade;
7. adaptação a diferentes telas e preferências;
8. polimento e validação com usuários.

### O que fica congelado durante `0.3.1`–`0.3.9`

- não adicionar onboarding;
- não adicionar Plano de Reset;
- não adicionar hábitos, bem-estar, finanças ou Pomodoro;
- não adicionar login, nuvem, integrações ou colaboração;
- não alterar regras de domínio sem necessidade de corrigir uma barreira de UX;
- não substituir dados, tabelas ou contratos de persistência sem justificativa.

---

## Portões obrigatórios de todos os builds UX/UI

- testes unitários existentes continuam passando;
- `./gradlew :app:assembleDebug` passa;
- validação manual no emulador `Pixel_8` ou aparelho equivalente;
- nenhuma tela nova deixa estados de carregamento, vazio ou erro sem tratamento;
- componentes continuam utilizáveis com fonte ampliada;
- textos visíveis permanecem em recursos de string PT-BR;
- nenhuma informação importante depende apenas de cor;
- registrar antes/depois e decisão de design no plano ou release note da versão;
- não introduzir dependência visual sem necessidade comprovada.

---

# Visão consolidada

| Versão | Foco | Resultado esperado |
|---|---|---|
| `0.3.1` | Fundamentos visuais | Design system mínimo consistente. |
| `0.3.2` | Arquitetura de informação | Usuário entende rapidamente onde está e o que pode fazer. |
| `0.3.3` | Jornada Hoje | Prioridades e próximo passo ficam imediatamente claros. |
| `0.3.4` | Jornada Organizar | Projetos e tarefas ficam mais fáceis de criar e revisar. |
| `0.3.5` | Formulários e entrada | Menos atrito para digitar, corrigir e salvar dados. |
| `0.3.6` | Feedback e estados | O app comunica carregamento, sucesso, erro e vazio com clareza. |
| `0.3.7` | Acessibilidade | Jornadas principais funcionam com recursos de acessibilidade do Android. |
| `0.3.8` | Adaptação visual | Experiência consistente em tema escuro, fontes grandes e telas diferentes. |
| `0.3.9` | Polimento e validação | Release candidate UX/UI pronto antes do `0.4.0`. |

---

## `0.3.1` — Fundamentos visuais e design system

**Objetivo:** substituir decisões visuais espalhadas por tokens e componentes reutilizáveis.

### Inclui

- consolidar paleta ResetLife, tipografia, espaçamentos, formas e elevação;
- criar tokens no `ResetLifeTheme`;
- padronizar botões primário, secundário e de ação discreta;
- padronizar campos, divisores, chips, mensagens e cards;
- definir estados visuais de prioridade, tarefa concluída e tarefa atrasada;
- revisar contraste das cores atuais;
- criar previews dos componentes principais em tema claro e escuro.

### Critérios de aceite

- telas Hoje e Organizar usam os mesmos tokens para cor, espaçamento e tipografia;
- não existem cores arbitrárias repetidas nos composables principais;
- componentes têm aparência consistente em estados normal, pressionado, desabilitado e erro;
- tema escuro não perde contraste nem hierarquia.

### Fora do escopo

- mudança de marca;
- animações complexas;
- nova funcionalidade de domínio.

---

## `0.3.2` — Arquitetura de informação e navegação

**Objetivo:** reduzir a dúvida sobre onde executar cada ação.

### Inclui

- revisar rótulos Hoje e Organizar;
- tornar a navegação inferior mais compreensível;
- padronizar títulos, subtítulos e hierarquia de cada tela;
- definir uma ação principal por tela;
- revisar a posição da captura de tarefa/projeto;
- diferenciar visualmente conteúdo principal, secundário e configurações locais;
- revisar retorno do Android e preservação da aba selecionada;
- adicionar estados de primeira utilização sem criar onboarding completo.

### Critérios de aceite

- usuário entende a finalidade de Hoje e Organizar sem instrução externa;
- a ação principal é identificável em até três segundos;
- trocar de aba não apaga texto digitado ou seleções válidas;
- voltar do sistema não fecha o app de forma inesperada nem perde a tela atual.

### Fora do escopo

- onboarding guiado da versão `0.4.0`;
- navegação para módulos ainda inexistentes.

---

## `0.3.3` — Redesign da jornada Hoje

**Objetivo:** fazer a tela Hoje responder imediatamente “o que importa agora?”.

### Inclui

- reorganizar a ordem visual: data/contexto, três prioridades, conclusão e próximo passo;
- reforçar a capacidade de três prioridades sem parecer uma punição;
- melhorar o estado vazio com uma ação direta;
- tornar a conclusão de prioridade mais evidente e reversível quando possível;
- reduzir ruído visual quando há prioridades concluídas;
- melhorar o texto de limite diário;
- revisar tamanhos de toque, espaçamento e leitura em uma mão;
- garantir que o conteúdo não fique escondido pelo teclado ou barra inferior.

### Critérios de aceite

- uma prioridade ativa é encontrada sem rolagem desnecessária em uma tela comum;
- o usuário identifica quantas vagas ainda existem;
- concluir uma prioridade produz feedback visual claro;
- o estado vazio oferece um próximo passo objetivo;
- a tela permanece legível com fonte do sistema ampliada.

### Métrica de validação

Em teste manual, uma pessoa deve conseguir criar e concluir uma prioridade sem explicação verbal do agente.

---

## `0.3.4` — Redesign da jornada Organizar

**Objetivo:** deixar a criação e revisão de tarefas/projetos mais simples que uma lista genérica.

### Inclui

- separar visualmente “criar projeto” e “criar tarefa”;
- reduzir a sensação de formulário longo;
- agrupar campos essenciais e opcionais;
- destacar projeto selecionado sem depender apenas do texto do botão;
- melhorar a leitura de tarefa com prazo, duração e projeto;
- diferenciar tarefas abertas e concluídas;
- melhorar o botão de promoção para Hoje e seu contexto;
- revisar a densidade da lista e o comportamento em listas longas.

### Critérios de aceite

- usuário identifica rapidamente quais campos são obrigatórios;
- tarefa criada aparece na lista sem precisar trocar de tela;
- relação entre tarefa e projeto é visualmente evidente;
- ação Hoje não é confundida com concluir tarefa;
- lista vazia explica como começar.

### Fora do escopo

- kanban;
- filtros avançados;
- agrupamento por múltiplos critérios.

---

## `0.3.5` — Formulários, teclado e prevenção de erros

**Objetivo:** reduzir o atrito de entrada e corrigir erros antes do salvamento.

### Inclui

- ordem de foco coerente entre campos;
- teclado adequado para data e duração;
- ação “Próximo” e “Concluir” no teclado quando aplicável;
- validação inline sem apagar conteúdo já digitado;
- mensagens de erro próximas ao campo causador;
- confirmação visual de salvamento;
- tratamento de data inválida no formato brasileiro `DD/MM/AAAA` e duração inválida;
- preservação de rascunho ao alternar de aba quando seguro;
- prevenção de duplo toque que crie registros repetidos.

### Critérios de aceite

- criar uma tarefa comum exige poucos toques e nenhuma correção manual de foco;
- o usuário consegue corrigir um campo inválido sem recomeçar o formulário;
- o teclado não cobre o botão da ação principal;
- salvar duas vezes rapidamente não cria duplicação;
- erros são compreensíveis em PT-BR.

---

## `0.3.6` — Feedback, carregamento, vazio e erro

**Objetivo:** fazer o sistema comunicar claramente o que está acontecendo.

### Inclui

- placeholders ou indicadores de carregamento consistentes;
- mensagens de estado vazio específicas por contexto;
- confirmação discreta de criação, conclusão e promoção;
- erros de persistência com ação de tentar novamente;
- desabilitação visual durante ações em andamento;
- mensagens para ausência de resultados na busca;
- tratamento de banco ocupado ou operação concorrente sem stack trace;
- microinterações curtas para inserção, conclusão e remoção visual.

### Critérios de aceite

- nenhum toque importante parece não ter funcionado;
- o usuário distingue “não há dados” de “ocorreu um erro”;
- operações repetidas não mostram feedback contraditório;
- animações não bloqueiam leitura, toque ou leitor de tela;
- falhas nunca expõem dados internos, caminhos de arquivo ou stack trace.

### Fora do escopo

- gamificação;
- contagem de pontos;
- sons ou vibração obrigatórios.

---

## `0.3.7` — Acessibilidade das jornadas principais

**Objetivo:** garantir que Hoje e Organizar sejam utilizáveis por mais pessoas e configurações de aparelho.

### Inclui

- revisão de descrições semânticas de botões, checkboxes e menus;
- ordem lógica de foco;
- rótulos para ícones e ações que não têm texto;
- contraste WCAG AA nos estados principais;
- suporte a escala de fonte do sistema;
- não depender apenas de cor para status;
- áreas de toque de pelo menos 48 dp;
- teste com TalkBack nas ações principais;
- revisão de mensagens para leitura por acessibilidade.

### Critérios de aceite

- usuário consegue navegar, criar e concluir uma prioridade com TalkBack;
- usuário consegue criar e concluir uma tarefa com TalkBack;
- foco não desaparece após abrir menu, erro ou confirmação;
- texto ampliado não corta títulos ou botões essenciais;
- estados concluído, aberto, erro e selecionado têm indicação além da cor.

---

## `0.3.8` — Tema, tamanhos de tela e adaptação

**Objetivo:** manter a experiência coerente em diferentes preferências e dispositivos Android.

### Inclui

- refinamento do tema escuro;
- suporte a fonte grande e display size ampliado;
- teste em portrait e landscape quando aplicável;
- uso correto de insets, barra de navegação e teclado;
- revisão em telas pequenas e grandes;
- adaptação de linhas, campos e botões sem overflow;
- suporte a modo de contraste elevado quando fornecido pelo sistema;
- revisão da aparência em API mínima e API atual do emulador.

### Critérios de aceite

- nenhuma tela principal apresenta texto ou botão cortado;
- tema claro e escuro mantêm hierarquia equivalente;
- conteúdo continua acessível depois de abrir o teclado;
- rotação ou recriação da Activity não perde dados persistidos;
- listas e formulários continuam utilizáveis em telas pequenas.

### Fora do escopo

- tablet dedicado;
- versão web;
- redesign específico para foldables.

---

## `0.3.9` — Polimento e release candidate UX/UI

**Objetivo:** consolidar os aprendizados dos oito builds anteriores e congelar a camada visual antes do `0.4.0`.

### Inclui

- auditoria visual completa de Hoje e Organizar;
- remoção de inconsistências, textos duplicados e estados esquecidos;
- revisão de performance percebida e tempo de abertura;
- revisão das jornadas: criar prioridade, concluir prioridade, criar projeto, criar tarefa, buscar, concluir e promover;
- teste de usabilidade com 5 a 10 pessoas, quando possível;
- registro dos problemas por severidade;
- correção de bloqueadores e problemas de alta severidade;
- baseline de screenshots para comparação futura;
- documentação dos componentes e decisões aprovadas;
- release note de fechamento da linha `0.3.x`.

### Critérios de aceite

- não há inconsistência crítica nas telas Hoje e Organizar;
- todos os portões UX/UI estão verdes;
- problemas não corrigidos possuem justificativa e versão de destino;
- build debug e testes instrumentados passam;
- principais jornadas podem ser executadas sem instrução externa;
- a camada visual está estável para receber o onboarding e o Plano de Reset do `0.4.0`.

### Regra de saída

Após `0.3.9`, novas mudanças visuais devem ser classificadas como correção crítica ou adiadas para não interromper o planejamento do `0.4.0`.

---

## Critérios de sucesso da linha `0.3.x`

Ao final da `0.3.9`, o ResetLife deverá:

- parecer um produto coerente, não um conjunto de telas independentes;
- permitir que um usuário novo entenda Hoje e Organizar sem tutorial;
- reduzir erros de preenchimento e dúvidas sobre o resultado das ações;
- funcionar com tema escuro, fonte ampliada e TalkBack nas jornadas principais;
- preservar todas as regras de negócio e dados da `0.3.0`;
- possuir uma base visual estável para o build grande `0.4.0`.
