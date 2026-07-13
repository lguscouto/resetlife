# Plano 0.4.0 — Onboarding e Plano de Reset

**Objetivo:** entregar o núcleo da proposta do ResetLife: diagnosticar o momento atual e gerar um plano de reorganização editável.

## Escopo

| Item | Detalhes |
|---|---|
| onboarding | Explicação de offline/privacidade, autoavaliação de 7 áreas, priorizar 3, escolher duração (7/14/30 dias), receber plano inicial |
| plano de reset | Data de início, metas por área, marcos semanais, regras determinísticas (simples) |
| integração | Plano alimenta a tela Hoje (máx 3 prioridades) |
| dados | Persistidos localmente via Room, exportáveis via JSON |

## Arquivos afetados

- `app/src/main/java/br/com/resetlife/presentation/onboarding/OnboardingScreen.kt` (novo)
- `app/src/main/java/br/com/resetlife/presentation/onboarding/OnboardingViewModel.kt` (novo)
- `app/src/main/java/br/com/resetlife/domain/onboarding/OnboardingData.kt` (novo)
- `app/src/main/java/br/com/resetlife/domain/resetplan/ResetPlan.kt` (novo)
- `app/src/main/java/br/com/resetlife/data/onboarding/OnboardingDao.kt` (novo)
- `app/src/main/java/br/com/resetlife/presentation/today/TodayScreen.kt` (adaptar para mostrar progresso do plano)
- `app/src/main/AndroidManifest.xml` (definir Onboarding como launcher inicial se necessário)
- `app/src/main/res/values/strings.xml` (textos onboarding)

## Etapas

1. **Entidade `UserProfile`** (se não existe) para armazenar preferências de onboarding
2. **DAO `OnboardingDao`** para `UserProfile` com Flow observable
3. **ViewModel `OnboardingViewModel`** para coletar respostas e gerar plano
4. **Regras `ResetPlan`** para gerar ações a partir das áreas prioritárias
5. **Screen `OnboardingScreen`** com 7 perguntas (uma por tela ou stepper)
6. **Integração Hoje** — mostrar "Dia X de Y" + "Meta da semana" + "Próximas 3"
7. **Testes unitários** para geração de plano
8. **Teste manual no emulador** Pixel_8

## Validação

- `./gradlew :app:assembleDebug` passa
- `./gradlew :app:testDebugUnitTest` passa
- Onboarding concluído gera plano visível em Hoje
- Plano respeita limite de 3 prioridades diárias

## Fora do escopo

- IA generativa para ações
- Diagnóstico clínico/avaliação psicológica
- Sincronização ou login
- Gamificação, streaks, recompensas