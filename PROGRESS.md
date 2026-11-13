# TutorFlow — Progress and Continuity Record

Keep this file current. A new session starts by reading this file, `docs/05-implementation-checklist.md` and `docs/01-architecture.md`, then continues from "Next steps".

Last updated: 2026-11-13 · Current phase: **Phase 2 — code WRITTEN, NOT YET COMPILED** · Next: owner runs the GitHub build (docs/06), reports result; then Phase 3

## 1. Honest state of the project

| Item | State |
|---|---|
| Android project / Kotlin source | **Phase 2 skeleton written** (18 Kotlin files, ~2,000 lines): theme, glass components, splash, onboarding, nav shell, dashboard skeleton, More hub, Settings, About |
| Compiled | **No** — never compiled. First real compile happens on GitHub Actions |
| Tests executed | **None** |
| APK | **None. No APK has been built or signed** |
| Build environment used so far | JDK 21 only; no Gradle, no Android SDK, no emulator, no internet for shell commands |

Because of the last row, "compile and test each module" can only happen when the code is built somewhere with the Android SDK: the owner's Android Studio, or GitHub Actions (workflow added in Phase 2). The loop is: assistant writes a module → owner/CI runs `scripts/verify.sh` → errors or test reports come back → assistant fixes → module is ticked C/T only with that evidence.

## 2. Phase 1 deliverables (all in this folder)

| File | Purpose |
|---|---|
| `docs/01-architecture.md` | layers, decisions D1–D15, stack, offline/security/documents/backup/Drive/testing/release design |
| `docs/02-database-schema.md` | 18 required + supporting entities, constraints, ledger rules, immutability triggers, archive policy, migrations |
| `docs/er-diagram.png` / `.svg` / `.mmd` | rendered ER diagram (navy/antique-gold styling) and its source |
| `docs/03-navigation.md` | app flow, shell, full route map, quick-action map |
| `docs/04-design-system.md` | colour tokens, computed contrast audit, typography, Liquid Glass components, motion, bilingual rules |
| `docs/05-implementation-checklist.md` | per-task W/C/T tracking for Phases 1–8 + spec traceability |
| `assets/brand/tutorflow_logo_original.png` | owner's logo, unmodified (3264×3264 RGBA) |
| `assets/brand/developer_photo_original.png` | owner's photo for the About page, unmodified |
| `assets/reference/liquid_glass_tabbar_reference.jpg` | supplied tab-bar reference |
| `scripts/*.sh` | verify, keystore creation, release build (syntax-checked only, never executed against a real project) |

## 3. Decisions made on the owner's behalf — please confirm or change

1. **Attendance %** = (Present + Late) ÷ (Present + Late + Absent); Leave is excluded from the denominator. Configurable in Settings.
2. **Student ID format** `TF26-0001` (prefix + session year + 4-digit sequence), prefix editable.
3. **Merit ties:** rank by total, then by marks in subjects in exam order, then roll number; students equal on all keys share a position (1,2,2,4).
4. **Small colour deviation:** small secondary text on the light page background uses `#5B6B83` instead of spec `#64748B`, because the spec value measures 4.44:1 (AA needs 4.5). All other colours are exactly as specified.
5. **No SQLCipher in v1:** protection is PIN/biometric lock, app-private storage and encrypted backups. Say so if you want at-rest database encryption.
6. **Google Drive** uses the private app-data scope (minimum permission). It cannot work until *you* create the Google Cloud OAuth client (package `com.tutorflow.app` + your release-key SHA-1). I will provide a step-by-step guide in Phase 6.
7. **Signing key:** you generate it (`scripts/make-keystore.sh`) and keep a backup; I never hold it.
8. **Bengali font:** Noto Sans Bengali must be added to `app/src/main/res/font/` — it cannot be downloaded in this sandbox. (Free OFL font from Google Fonts.)
9. **WhatsApp limitation:** when sending a PDF/image, WhatsApp lets you choose the chat; it cannot be pre-targeted to one number. Text messages *can* open straight to the guardian's number.
10. **Developer photo:** the photo you uploaded is used for the About page; nothing is fetched from Facebook.

## 4. Spec rules that shape every later phase (do not regress)

No Firebase/online DB · no WebView shell · no ads/analytics · no demo data in release · payments append-only · derived values never stored · restore never overwrites without confirmation and safety backup · never claim delivery, integration success, test pass or APK existence without evidence · logo never redesigned or recoloured.

## 5. Test status (spec §30) — all NOT RUN

Student registration · student editing · duplicate prevention · multi-batch enrolment · attendance · attendance calculation · fee calculation · partial payments · previous-due calculation · payment history · exam creation · marks entry · grade calculation · merit list · PDF generation · JPG generation · Bengali font rendering · WhatsApp integration · SMS integration · local backup · local restore · Google Drive backup · Google Drive restore · database migration · dark mode · light mode · Bangla language · English language · application restart · large-database performance → **NOT RUN** (no code exists yet).

## 6. Change log

- 2026-11-13 — Phase 1: spec analysed; colours verified against logo pixels; architecture, schema, ER diagram, navigation, design system, checklist and this record created.

## 7. Next steps (Phase 2)

1. Look up and pin current stable dependency versions.
2. Create the Gradle project, theme, glass components, Bengali fonts, locale/theme persistence, splash, onboarding, navigation shell, dashboard skeleton, launcher icon.
3. Add `.github/workflows/android.yml` and `scripts/verify.sh` wiring so the owner can get real build and test results.
4. Update the checklist only with evidenced W/C/T states.

## 8. Phase 2 build notes (2026-11-13)

- Versions pinned after checking official release notes: AGP 9.1.1, Gradle 9.4.0 (CI installs it), Kotlin 2.3.10, KSP 2.3.11, Hilt 2.60 (plugin >= 2.59 needs AGP 9), Compose BOM 2026.08.00. Other AndroidX versions (activity 1.11.0, lifecycle 2.9.4, navigation 2.9.7, datastore 1.1.7, appcompat 1.7.1, core-ktx 1.17.0) were chosen from memory/partial evidence: **if the build reports a missing version or a compileSdk requirement, adjust that line.**
- compileSdk/targetSdk 36. No gradle wrapper jar exists (cannot be generated here); CI installs Gradle directly; Android Studio creates a wrapper when needed.
- No Room yet. Teacher/institution profile is in DataStore for now and moves to Room in task 3.3.
- Bengali font not bundled (file unavailable offline); the phone's system font is used. Dashboard figures show "—" because no database exists; modules not built open a "Not built yet" screen naming the phase.
- Launcher icon = the logo's symbol tile inside the adaptive-icon safe zone on flat navy; no monochrome (themed-icon) layer yet.
- Hilt ViewModel is created once in MainActivity (`AppViewModel`) and passed down; hilt-navigation-compose is deliberately not used (avoids an unverified artifact).
- Tab labels are 10 sp with ellipsis; check "Attendance"/"উপস্থিতি" fit on a small phone.
