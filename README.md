# ResetLife

Aplicativo Android **offline-first** para ajudar pessoas a reorganizarem a vida por meio de planos de reset, prioridades diárias, hábitos, bem-estar, finanças essenciais e revisão semanal.

O build `0.3.5` reduz o atrito dos formulários da jornada Organizar, com teclado/foco orientados, validação inline e datas exibidas no formato brasileiro `DD/MM/AAAA`.

## Estado atual

- projeto Android nativo em Kotlin + Jetpack Compose;
- versão atual: `0.3.5` (`versionCode = 8`);
- suporte mínimo: Android 10 (API 29);
- tela **Organizar** com formulários recolhíveis para criar projeto e tarefa;
- tarefa priorizada como ação inicial e projeto separado visualmente;
- campos obrigatórios marcados com `*` e detalhes opcionais agrupados;
- projeto relacionado destacado como seleção explícita;
- metadados de tarefa para projeto, prazo e duração;
- tarefas abertas e concluídas em seções distintas;
- conclusão identificada por checkbox, estado textual e superfície atenuada;
- ação `Adicionar a Hoje` separada visualmente de concluir tarefa;
- estados vazios diferentes para lista vazia e busca sem resultado;
- prazos aceitos e exibidos na interface como `DD/MM/AAAA`, com conversão interna compatível para armazenamento;
- máscara automática para entrada numérica de datas;
- validações inline para título, prazo e duração sem apagar o rascunho;
- ações de teclado `Next`, `Done` e `Search`, com foco sequencial e proteção contra duplo envio;
- confirmação visual após criação de projeto, tarefa e prioridade;
- tela **Hoje** com contexto, capacidade e ação principal de próximo passo;
- prioridades ativas separadas visualmente das concluídas;
- estado vazio orientado ao próximo passo;
- feedback semântico para prioridades pendentes e concluídas;
- teclado e barra inferior considerados no layout da tela Hoje;
- destinos de navegação nomeados para Hoje e Organizar;
- barra inferior com rótulos, indicador de seleção e descrições acessíveis;
- aba selecionada preservada durante recriação da composição;
- botão voltar do Android retorna de Organizar para Hoje antes de fechar o app;
- design tokens e componentes visuais reutilizáveis;
- tema claro e escuro refinados;
- dados persistidos com Room/SQLite;
- testes unitários e teste instrumentado de migração;

> O aplicativo funciona sem conta e sem internet. A sincronização em nuvem não faz parte desta versão.

## Pré-requisitos

- Android Studio com JDK integrado;
- Android SDK Platform 35;
- dispositivo ou emulador Android com API 29 ou superior, para executar o aplicativo.

## Como abrir

1. Abra a pasta do projeto no Android Studio.
2. Confirme o Android SDK local na tela de configuração do IDE.
3. Aguarde a sincronização do Gradle.
4. Selecione um emulador ou aparelho Android e execute a configuração `app`.

## Como compilar e testar pelo terminal

No Git Bash do Windows, com Android Studio instalado no caminho padrão:

```bash
export JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
export ANDROID_HOME='C:\Users\gustavo\AppData\Local\Android\Sdk'
export ANDROID_SDK_ROOT="$ANDROID_HOME"

./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
./gradlew :app:assembleAndroidTest
./gradlew :app:connectedDebugAndroidTest
```

O APK de depuração é produzido em:

```text
app/build/outputs/apk/debug/app-debug.apk
```

O APK do source set de testes instrumentados é produzido em:

```text
app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
```

O schema exportado pelo Room fica em:

```text
app/schemas/br.com.resetlife.data.local.ResetLifeDatabase/1.json
app/schemas/br.com.resetlife.data.local.ResetLifeDatabase/2.json
```

A validação realizada na versão `0.3.5` instalou o APK no emulador `Pixel_8`, confirmou erros inline de data/duração, corrigiu o formulário sem perder os campos, criou uma tarefa com prazo `15/07/2026`, verificou a exibição brasileira do prazo, confirmou a mensagem `Tarefa criada.` e preservou um rascunho ao alternar entre Hoje e Organizar.

## Documentação

- [Escopo do produto](docs/ESCOPO.md)
- [Roadmap das próximas 10 versões](docs/ROADMAP.md)
- [Roadmap UX/UI das versões 0.3.1 a 0.3.9](docs/ROADMAP-UX-UI-0.3x.md)
- [Plano do primeiro incremento](docs/plans/2026-07-12-prioridades-diarias.md)
- [Plano de persistência da versão 0.2.0](docs/plans/2026-07-12-persistencia-prioridades.md)
- [Plano da versão 0.3.0](docs/plans/2026-07-12-tarefas-projetos.md)
- [Plano UX/UI da versão 0.3.1](docs/plans/2026-07-12-ux-ui-foundations-0.3.1.md)
- [Plano UX/UI da versão 0.3.2](docs/plans/2026-07-12-information-architecture-0.3.2.md)
- [Plano UX/UI da versão 0.3.3](docs/plans/2026-07-12-today-ux-0.3.3.md)
- [Plano UX/UI da versão 0.3.4](docs/plans/2026-07-12-organize-ux-0.3.4.md)
- [Plano UX/UI da versão 0.3.5](docs/plans/2026-07-12-forms-validation-0.3.5.md)
- [Notas de release 0.2.0](docs/releases/0.2.0.md)
- [Notas de release 0.3.0](docs/releases/0.3.0.md)
- [Notas de release 0.3.1](docs/releases/0.3.1.md)
- [Notas de release 0.3.2](docs/releases/0.3.2.md)
- [Notas de release 0.3.3](docs/releases/0.3.3.md)
- [Notas de release 0.3.4](docs/releases/0.3.4.md)
- [Notas de release 0.3.5](docs/releases/0.3.5.md)
- [Regras de trabalho do repositório](AGENTS.md)

## Princípios do produto

- ação antes de registro;
- no máximo três prioridades diárias;
- retomada sem culpa;
- privacidade e funcionamento offline por padrão;
- sem diagnóstico, prescrição médica, psicológica ou financeira.
