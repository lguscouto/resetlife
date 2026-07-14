# ResetLife Roadmap 0.11 → 0.20 — Melhorias de UI

> **For Hermes:** Usar `subagent-driven-development` para implementar versão a versão, com dois estágios de revisão (spec → qualidade). Cada versão é independente e commitável.

**Goal:** Elevar retenção e paridade de mercado do ResetLife implementando widgets, heatmap de streak, tema escuro, celebrações, cores/categorias, hábitos negativos, backup JSON, lembretes locais, estados vazios acessíveis e tela de detalhe do hábito — sem quebrar o princípio offline-first/privado.

**Architecture:** Padrão atual do app (domínio isolado + MVVM + StateFlow + Room). Novidades: (1) `SettingsRepository` (DataStore) para preferências de UI (tema, modo-streak); (2) widgets via Jetpack Glance; (3) notificações via `AlarmManager`/`WorkManager` locais; (4) telas/detalhes novos como destinations filhos dos hubs existentes. Manter `strings.xml` em PT-BR, nada hardcoded, ícones `material-icons-extended`.

**Tech Stack:** Kotlin, Jetpack Compose (Material3), Room, DataStore, Glance (widgets), Android WorkManager/AlarmManager, kotlinx-coroutines-flow.

---

## Índice de versões

| Versão | Foco | Prioridade |
|---|---|---|
| 0.11 | Widgets (Hoje + Hábitos) na home | 🔴 Alta |
| 0.12 | Heatmap de streak (calendário mensal do hábito) | 🔴 Alta |
| 0.13 | Modo escuro / troca de tema | 🔴 Alta |
| 0.14 | Mensagens de recompensa ao concluir | 🔴 Alta |
| 0.15 | Cores por hábito/categoria | 🟡 Média |
| 0.16 | Hábitos negativos (a reduzir) | 🟡 Média |
| 0.17 | Backup/exportação JSON | 🟡 Média |
| 0.18 | Lembretes locais (notificações) | 🟡 Média |
| 0.19 | Estados vazios + acessibilidade (contentDescription) | 🟢 Baixa |
| 0.20 | Tela de detalhe do hábito + modo sem pressão de streak | 🟢 Baixa |

Regra por versão: `versionCode` incrementa +1 a partir de 24 (0.10.0); `versionName` segue; ao final, `assembleDebug` + `testDebugUnitTest` + validação em emulador (Pixel_8) + release notes `docs/releases/0.XX.0.md` + `README.md` + commit/push convencional.

---

## 0.11 — Widgets (Hoje + Hábitos)

**Objective:** Permitir logging rápido sem abrir o app (maior gap de retenção vs Streaks/Habi).

**Files:**
- Create: `app/src/main/java/br/com/resetlife/widget/TodayGlanceWidget.kt`
- Create: `app/src/main/java/br/com/resetlife/widget/HabitsGlanceWidget.kt`
- Create: `app/src/main/res/xml/today_widget_info.xml`, `app/src/main/res/xml/habits_widget_info.xml`
- Modify: `app/src/main/AndroidManifest.xml` (receivers + metadata)
- Modify: `app/build.gradle.kts` (dependência `androidx.glance:glance-appwidget`)
- Modify: `res/values/strings.xml` (labels PT-BR)

**Tasks:**
1. Adicionar dependência Glance em `app/build.gradle.kts` (`androidx.glance:glance-appwidget:1.1.0` + `androidx.glance:glance-material3`). Build verde.
2. Criar `TodayGlanceWidget.kt`: `GlanceAppWidget` que lê as 3 prioridades do `PriorityStore` e renderiza botão de concluir (action via `ActionCallback` que chama `todayViewModel.completePriority`).
3. Criar `habits_widget_info.xml` + `HabitsGlanceWidget.kt`: lista até 4 hábitos do dia com checkbox (action conclui via `HabitRepository`).
4. Registrar receivers no `AndroidManifest.xml` com `android:name`, `android:exported="false"`, `<intent-filter><action android:name="android.appwidget.action.APPWIDGET_UPDATE"/></intent-filter>` e `<meta-data android:name="android.appwidget.provider" android:resource="@xml/..."/>`.
5. Adicionar strings `widget_today_title`, `widget_habits_title`, `widget_empty`.
6. `./gradlew :app:assembleDebug` OK; instalar no emulador; adicionar widget via `adb shell cmd appwidget ...` ou validar manualmente; checar que concluir no widget reflete no app (DB Room compartilhado).
7. Commit `feat: 0.11 widgets Hoje e Habitos`.

---

## 0.12 — Heatmap de streak (calendário mensal do hábito)

**Objective:** Visualizar consistência (padrão de mercado ausente no ResetLife, que só mostra número `diasSeguidos`).

**Files:**
- Create: `presentation/habit/HabitDetailScreen.kt` (destino filho de Hábits)
- Create: `presentation/habit/components/StreakCalendar.kt` (composable grid 7x5)
- Modify: `presentation/habit/HabitScreen.kt` (card do hábito navega para detalhe)
- Modify: `domain/habit/HabitModels.kt` (já tem histórico? se não, `List<LocalDate> concluidos`)
- Modify: `data/habit/HabitRepository.kt` (`observeCompletions(habitId)`)
- Modify: `res/values/strings.xml`

**Tasks:**
1. Garantir modelo de conclusão diária (`HabitCompletionEntity` com `date`). Se faltar, criar + AutoMigration (version+1).
2. Criar `StreakCalendar.kt`: grid de 35 células, cor verde se concluído, cinza se não; legenda.
3. Criar `HabitDetailScreen.kt`: título, streak atual, `StreakCalendar`, taxa de conclusão (concluídos/total dias desde criação).
4. `HabitScreen.kt`: card do hábito vira `clickable { navController.navigate(HabitDetail(id)) }`.
5. Adicionar strings `habit_detail_title`, `habit_streak_label`, `habit_completion_rate`.
6. Teste unitário: `StreakCalendar` recebe lista de datas → conta células verdes corretas (teste de lógica de cálculo em ViewModel ou função pura).
7. Build + emulador: abrir detalhe de um hábito com histórico → calendário renderiza.
8. Commit `feat: 0.12 heatmap de streak no detalhe do habito`.

---

## 0.13 — Modo escuro / troca de tema

**Objective:** Paridade com Streaks (78 temas) e conforto noturno; usar `SettingsRepository` (DataStore).

**Files:**
- Create: `data/settings/SettingsRepository.kt` (DataStore `user_prefs`; chave `dark_mode: Boolean`)
- Create: `data/settings/UserPreferencesSerializer.kt` (ou usar `booleanPreferencesKey`)
- Modify: `ResetLifeApplication.kt` (expor `settingsStore`)
- Modify: `presentation/theme/Theme.kt` (`ResetLifeTheme(darkTheme: Boolean)`)
- Modify: `presentation/AppScreen.kt` (coleta `settingsStore.darkMode` e passa ao tema)
- Modify: `presentation/profile/ProfileScreen.kt` (toggle "Modo escuro" + talvez 2 accents)
- Modify: `res/values/strings.xml`

**Tasks:**
1. Criar `SettingsRepository` com `dataStore` e `val darkMode: Flow<Boolean>` + `suspend fun setDarkMode(v)`.
2. `Theme.kt`: `MaterialTheme(colorScheme = if (darkTheme) darkScheme else lightScheme)` (scheme teal existente adaptado).
3. `AppScreen.kt`: `val isDark by application.settingsStore.darkMode.collectAsState(initial = isSystemInDarkTheme())`; passar a `ResetLifeTheme`.
4. `ProfileScreen.kt`: `Switch` "Modo escuro" ligado a `viewModel.toggleDarkMode()` (Factory recebe `SettingsRepository`).
5. Strings `settings_dark_mode`, `settings_title`.
6. Teste: `SettingsRepository` (in-memory DataStore em test) salva/relê `darkMode`.
7. Build + emulador: toggle no Perfil muda tema imediatamente e persiste após reiniciar app.
8. Commit `feat: 0.13 modo escuro com DataStore`.

---

## 0.14 — Mensagens de recompensa ao concluir

**Objective:** Retenção via reforço positivo (padrão "You're doing great!" do case study).

**Files:**
- Modify: `presentation/today/TodayViewModel.kt` (`completePriority`/`completeEnvironmentSuggestion` emitem evento de celebração)
- Modify: `presentation/today/TodayScreen.kt` (observa `uiState.celebration` → `Snackbar` motivacional)
- Modify: `presentation/habit/HabitViewModel.kt` (mesmo p/ hábito)
- Create: `domain/rewards/RewardMessages.kt` (lista PT-BR: "Sequência de X dias!", "Mandando bem!", etc.)
- Modify: `res/values/strings.xml`

**Tasks:**
1. Criar `RewardMessages.kt`: `fun forStreak(days: Int): String` retorna frase conforme marco (3/7/14/30).
2. `TodayViewModel`: ao concluir, `mutableUiState` ganha `celebration: String?`; `TodayScreen` mostra `Snackbar` e limpa após `LaunchedEffect`.
3. `HabitViewModel`: ao concluir hábito, emite celebração com streak atual.
4. Strings `reward_streak_3`, `reward_streak_7`, `reward_done`, `reward_generic`.
5. Teste unitário: `forStreak(7)` retorna frase de 7 dias; ViewModel emite celebração ao concluir.
6. Build + emulador: concluir prioridade → snackbar aparece.
7. Commit `feat: 0.14 mensagens de recompensa`.

---

## 0.15 — Cores por hábito/categoria

**Objective:** Categorização por cor (UX research: ajuda escaneabilidade e WCAG).

**Files:**
- Modify: `domain/habit/HabitModels.kt` (`colorHex: String?`)
- Modify: `data/habit/HabitEntities.kt` (coluna `color_hex`)
- Modify: `data/habit/HabitMappers.kt`
- Modify: `presentation/habit/HabitScreen.kt` + `HabitDetailScreen.kt` (usar cor no card/calendário)
- Modify: `presentation/habit/components/HabitDialog.kt` (seletor de cor)
- Modify: `res/values/strings.xml`, `res/values/colors.xml` (paleta)

**Tasks:**
1. Adicionar `colorHex` ao modelo/entity/mapper + AutoMigration.
2. `HabitDialog.kt`: `Chip`/`Dropdown` com 6 cores da paleta (`ResetLifeColors`).
3. `HabitScreen.kt`: `AccessCard`/item usa `colorHex` como accent (fallback teal).
4. `HabitDetailScreen.kt`: `StreakCalendar` usa cor do hábito nas células concluídas.
5. Strings `habit_color_label`.
6. Teste: mapper round-trip `colorHex`; ViewModel salva cor.
7. Build + emulador: criar hábito com cor → reflete em lista e detalhe.
8. Commit `feat: 0.15 cores por habito`.

---

## 0.16 — Hábitos negativos (a reduzir)

**Objective:** Paridade com Streaks (ex: "sem celular após 21h").

**Files:**
- Modify: `domain/habit/HabitModels.kt` (`type: HabitType = POSITIVE | NEGATIVE`)
- Modify: `data/habit/HabitEntities.kt` (coluna `type`)
- Modify: `presentation/habit/HabitScreen.kt` (ícone diferente p/ negativo; streak conta dias "sem fazer")
- Modify: `presentation/habit/components/HabitDialog.kt` (radio POSITIVE/NEGATIVE)
- Modify: `res/values/strings.xml`

**Tasks:**
1. `enum class HabitType { POSITIVE, NEGATIVE }` + coluna + mapper + AutoMigration.
2. `HabitDialog.kt`: `RadioButton` POSITIVE/NEGATIVE.
3. Lógica de streak: positivo = dias concluídos seguidos; negativo = dias sem ocorrência seguidos (concluir = "não fiz hoje", mantém sequência).
4. `HabitScreen.kt`: ícone `Block`/`DoNotDisturb` para negativo; label "Evitar".
5. Strings `habit_type_positive`, `habit_type_negative`.
6. Teste: `forStreak` e contagem distinta por tipo.
7. Build + emulador: criar hábito negativo, concluir aparece como "evitado hoje".
8. Commit `feat: 0.16 habitos negativos`.

---

## 0.17 — Backup/exportação JSON

**Objective:** Diferencial de privacidade (Super Productivity): dados portáteis, sem lock-in.

**Files:**
- Create: `data/backup/BackupExporter.kt` (lê todos os repos → JSON)
- Create: `data/backup/BackupImporter.kt` (parse + upsert)
- Create: `presentation/profile/BackupScreen.kt` (botões Exportar/Importar via SAF `ActivityResultContracts.CreateDocument`/`OpenDocument`)
- Modify: `presentation/profile/ProfileScreen.kt` (entrada "Backup")
- Modify: `AndroidManifest.xml` (permissão `READ/WRITE` não necessária com SAF)
- Modify: `res/values/strings.xml`

**Tasks:**
1. `BackupExporter.exportAll(): JSONObject` agrega spaces, tasks, habits, completions, priorities, lists, checkins.
2. `BackupImporter.import(json)` valida schema `version` e faz upsert (não duplicar por id).
3. `BackupScreen.kt`: botão Exportar → `CreateDocument("application/json")` salva em local escolhido; Importar → `OpenDocument` lê e aplica.
4. Strings `backup_export`, `backup_import`, `backup_success`, `backup_error`.
5. Teste unitário: export→import round-trip preserva contagem de entidades (usar repos em memória).
6. Build + emulador: exportar, limpar DB, importar, confirmar dados restauradas.
7. Commit `feat: 0.17 backup JSON export/import`.

---

## 0.18 — Lembretes locais (notificações)

**Objective:** Apps de hábito dependem de lembrete; manter 100% local (sem servidor).

**Files:**
- Create: `data/reminder/ReminderScheduler.kt` (usa `AlarmManager` exato + `BroadcastReceiver` → `Notification`)
- Modify: `domain/habit/HabitModels.kt` (`reminderTime: LocalTime?`)
- Modify: `presentation/habit/components/HabitDialog.kt` (`TimePicker` opcional)
- Modify: `AndroidManifest.xml` (`RECEIVE_BOOT_COMPLETED` + receiver de alarme)
- Modify: `res/values/strings.xml`, `res/values/strings.xml` (canal notificação)

**Tasks:**
1. `ReminderScheduler.schedule(habitId, time)` com `AlarmManager.setExactAndAllowWhileIdle`; `BootReceiver` reagenda após reboot.
2. `ReminderReceiver` posta `Notification` (canal "Lembretes" criado em `Application.onCreate`).
3. `HabitDialog.kt`: `TimePicker` "Lembrar às" (opcional, default off).
4. Strings `habit_reminder`, `reminder_channel_name`.
5. Teste: `ReminderScheduler` (fake `AlarmManager`) agenda intent correto.
6. Build + emulador: criar hábito com lembrete, adiantar relógio/disparar receiver manual → notificação aparece.
7. Commit `feat: 0.18 lembretes locais`.

---

## 0.19 — Estados vazios + acessibilidade

**Objective:** Microcopy acionável no primeiro uso e `contentDescription` em todos os elementos de navegação (dump anterior mostrou "Hoje" sem content-desc).

**Files:**
- Modify: `presentation/today/TodayScreen.kt`, `HabitScreen.kt`, `LifeScreen.kt`, `CustomListsScreen.kt`, `ProfileScreen.kt` (empty states)
- Modify: `presentation/navigation/ResetLifeNavigationBar.kt` (`contentDescription` nos `Icon`)
- Modify: `res/values/strings.xml`

**Tasks:**
1. `ResetLifeNavigationBar.kt`: cada `NavigationBarItem` recebe `contentDescription = dest.label` (já tem `label` em `strings`).
2. Cada tela: quando lista vazia, mostrar `EmptyState` (ícone + texto guia + botão de ação), ex: `today_empty` = "Toque em + para adicionar sua primeira prioridade."
3. Strings `today_empty`, `habits_empty`, `lists_empty`, `env_empty`, `wellbeing_empty`.
4. Teste: composable preview/teste de `EmptyState` renderiza texto.
5. Build + emulador: `uiautomator dump` confirma `content-desc` não vazio na barra e textos de empty state.
6. Commit `feat: 0.19 estados vazios e acessibilidade`.

---

## 0.20 — Tela de detalhe + modo sem pressão de streak

**Objective:** Fechar retenção (detalhe já iniciado em 0.12) e oferecer modo estilo Finch (sem zerar ao falhar).

**Files:**
- Modify: `data/settings/SettingsRepository.kt` (`noStreakPressure: Boolean`)
- Modify: `domain/habit/HabitModels.kt` / lógica de streak (respeitar flag)
- Modify: `presentation/habit/HabitDetailScreen.kt` (mostra histórico + toggle "não zerar ao falhar")
- Modify: `presentation/profile/ProfileScreen.kt` (config global opcional)
- Modify: `res/values/strings.xml`

**Tasks:**
1. `SettingsRepository.noStreakPressure` Flow + setter.
2. Cálculo de streak: se flag on, dias perdidos não zerem o contador (mantém melhor sequência histórica); UI mostra "melhor sequência" em vez de "atual".
3. `HabitDetailScreen.kt`: seletor por hábito (ou global em Perfil) "Modo sem pressão".
4. Strings `streak_mode_soft`, `streak_best`, `settings_soft_streak`.
5. Teste: cálculo de streak com flag retorna melhor sequência, não zera.
6. Build + emulador: alternar modo e ver contador não zerar ao pular dia.
7. Commit `feat: 0.20 detalhe do habito + modo sem pressao`.

---

## Riscos, tradeoffs e open questions

- **Glance vs AppWidgetProvider clássico:** Glance é o padrão Compose, mas exige `glance-appwidget` e pode ter quirks em API 36. Fallback: AppWidgetProvider com `RemoteViews` se Glance falhar no emulador.
- **Heatmap precisa de tabela de conclusões diárias:** verificar se `HabitCompletionEntity` já existe; se não, criar na 0.12 (AutoMigration cuidadosa — Room exige schema exportado em `schemas/`).
- **Tema escuro:** manter contraste WCAG; testar em teal claro/escuro.
- **Notificações em API 36:** `setExactAndAllowWhileIdle` pode exigir `SCHEDULE_EXACT_ALARM` permission (runtime) — tratar gracefully.
- **Backup import:** definir `schemaVersion` no JSON e rejeitar incompatível para não corromper DB.
- **Modo sem pressão:** decidir se é global (Perfil) ou por hábito; plano assume global por simplicidade, revisitável.

## Validação global por versão

```bash
export JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
export ANDROID_HOME='C:\Users\gustavo\AppData\Local\Android\Sdk'
export ANDROID_SDK_ROOT="$ANDROID_HOME"
./gradlew :app:assembleDebug --console=plain      # BUILD SUCCESSFUL
./gradlew :app:testDebugUnitTest --console=plain   # todos verdes
# emulador Pixel_8: instalar, validar via uiautomator dump + python (overview do sistema contorna com home/launch)
```
