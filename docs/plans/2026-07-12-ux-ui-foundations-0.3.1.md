# UX/UI Foundations — Build 0.3.1

> **Para Hermes:** executar as tarefas em ordem, preservando o comportamento de negócio da versão 0.3.0.

**Objetivo:** criar um design system mínimo para o ResetLife e aplicar seus tokens às telas Hoje, Organizar e navegação sem adicionar funcionalidades de domínio.

**Arquitetura:** centralizar cores, tipografia, espaçamento, formas e elevação no tema Compose. Criar componentes visuais pequenos e reutilizáveis para títulos de seção, superfícies, mensagens e ações. Refatorar apenas a camada de apresentação.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Android API 29+.

---

## Escopo fechado

### Incluído

- tokens de cor, tipografia, espaçamento, formas e elevação;
- tema claro e escuro coerentes;
- componentes ResetLife para superfície, título de seção, badge e estado vazio/erro;
- aplicação nas telas Hoje, Organizar e navegação inferior;
- previews dos componentes em tema claro e escuro;
- incremento de versão para `0.3.1` / `versionCode = 4`.

### Não incluído

- novas entidades, tabelas ou regras de negócio;
- mudanças no Room;
- onboarding, Plano de Reset, hábitos, finanças ou Pomodoro;
- alteração de textos funcionais além de ajustes de clareza visual;
- dependências externas de ícones ou bibliotecas de design.

---

## Task 1: Criar tokens visuais

**Arquivos:**

- Criar: `app/src/main/java/br/com/resetlife/presentation/theme/ResetLifeTokens.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/theme/ResetLifeTheme.kt`
- Modificar: `app/build.gradle.kts`

**Tokens:**

- espaçamento `xs`, `sm`, `md`, `lg`, `xl`;
- raios de forma para card, campo e botão;
- cores semânticas para foco, sucesso, alerta, erro e superfície;
- tipografia para título de tela, seção, corpo, legenda e números;
- elevação padrão de superfície.

**Verificação:** o tema compila e oferece os tokens sem quebrar o build atual.

---

## Task 2: Criar componentes visuais reutilizáveis

**Arquivos:**

- Criar: `app/src/main/java/br/com/resetlife/presentation/components/ResetLifeSurface.kt`
- Criar: `app/src/main/java/br/com/resetlife/presentation/components/ResetLifeSectionHeader.kt`
- Criar: `app/src/main/java/br/com/resetlife/presentation/components/ResetLifeMessage.kt`
- Criar: `app/src/main/java/br/com/resetlife/presentation/components/ResetLifePreview.kt`

**Componentes:**

- superfície com raio, borda e elevação padronizados;
- título de seção com hierarquia e ação opcional;
- mensagem de vazio, erro, sucesso e informação;
- preview combinando tema claro e escuro.

**Verificação:** componentes têm preview e não conhecem regras de domínio.

---

## Task 3: Refatorar a navegação e as superfícies principais

**Arquivos:**

- Modificar: `app/src/main/java/br/com/resetlife/presentation/AppScreen.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt`
- Modificar: `app/src/main/java/br/com/resetlife/presentation/organize/OrganizeScreen.kt`

**Passos:**

1. Substituir números usados como ícones de navegação por símbolos visuais acessíveis e rótulos claros.
2. Aplicar tokens de espaçamento e superfícies aos blocos principais.
3. Padronizar títulos de tela e títulos de seção.
4. Diferenciar visualmente conteúdo ativo, concluído, vazio e erro.
5. Preservar todos os callbacks e modelos de estado existentes.

**Verificação:** criação, conclusão, busca e promoção continuam com o mesmo comportamento.

---

## Task 4: Refinar estados e acessibilidade visual

**Arquivos:**

- Modificar: `TodayScreen.kt`
- Modificar: `OrganizeScreen.kt`
- Modificar: `strings.xml`

**Passos:**

- aplicar contraste semântico para erro, sucesso e informação;
- garantir que superfícies não sejam a única indicação de estado;
- adicionar `contentDescription` somente onde o ícone não tiver rótulo textual;
- manter alvos de toque mínimos de 48 dp;
- revisar visual com fonte ampliada e tema escuro;
- preservar mensagens de erro e vazio já existentes.

**Verificação:** não há texto cortado nos previews e os elementos principais continuam identificáveis sem depender apenas da cor.

---

## Task 5: Atualizar versão e documentação

**Arquivos:**

- Modificar: `app/build.gradle.kts`
- Modificar: `README.md`
- Criar: `docs/releases/0.3.1.md`

**Verificação:**

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
./gradlew :app:assembleAndroidTest
```

### Critérios de aceite

- [ ] `versionCode = 4` e `versionName = "0.3.1"`;
- [ ] design tokens centralizados no tema;
- [ ] componentes visuais reutilizáveis criados;
- [ ] Hoje, Organizar e navegação usam os tokens;
- [ ] tema claro e escuro compilam;
- [ ] previews dos componentes existem;
- [ ] regras de negócio continuam sem alteração;
- [ ] testes unitários passam;
- [ ] APK debug é gerado;
- [ ] documentação registra o que mudou e limitações reais.
