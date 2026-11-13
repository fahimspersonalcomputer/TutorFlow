# TutorFlow — Technical Architecture (Phase 1)

Owner: Abdullah al Fahim · Tagline: *Manage Smarter. Teach Better.*
Status: design only. Nothing in this document has been compiled or run (see "Build environment" below).

---

## 0. Build environment (read first)

The sandbox used for Phase 1 has JDK 21 only. It has **no Gradle, no Android SDK, no emulator and no internet access for shell commands**. Consequences:

- Code written here **cannot be compiled, unit-tested or packaged into an APK in this environment.**
- Every checklist item therefore carries three separate states: **Written / Compiled / Tested**. Only "Written" can be ticked here. "Compiled" and "Tested" are ticked only after a real build log or test report exists.
- To get real compile feedback, the project ships with `scripts/verify.sh` and a GitHub Actions workflow (added in Phase 2). Running `./gradlew assembleDebug testDebugUnitTest` locally in Android Studio or in CI, then pasting any errors back, is the loop that makes "compile and test each module" real.

---

## 1. Guiding decisions

| # | Decision | Why |
|---|----------|-----|
| D1 | Single Gradle module `:app`, package-by-layer with feature sub-packages | Solo developer, fastest builds, fewest wiring errors. Layers stay separated by package rules, not by Gradle modules. |
| D2 | `domain` package is **pure Kotlin** (no `android.*`, no Room, no Compose imports) | Fee, attendance, grading and ranking logic can be JVM-unit-tested and cannot be broken by UI changes. A check in `scripts/verify.sh` greps for forbidden imports. |
| D3 | Money = `Long` minor units (1 BDT = 100 poisha). Marks = `Double` rounded to 2 dp at write time | No floating-point money. Marks may be fractional (e.g. 7.5). |
| D4 | Dates stored as ISO text `yyyy-MM-dd`; timestamps as UTC epoch-millis `Long`; times of day as minutes-since-midnight `Int` | Sortable, readable in a DB browser, no timezone surprises. |
| D5 | **Derived values are never stored**: total, grade, GPA, pass/fail, position, payment status, remaining due, attendance % | Prevents stale/contradictory data. Computed by domain use-cases from source rows. |
| D6 | Financial rows are **append-only**. Payments cannot be updated or deleted; corrections are reversal rows. Enforced by SQLite triggers, not only by app code | Data integrity is the top priority in the brief. |
| D7 | Soft-delete (archive) for students, batches, classes, subjects. Hard delete only when a record has no dependents | "Delete must not silently destroy financial records." |
| D8 | `android:allowBackup="false"` and data-extraction rules exclude everything | Stops Android auto-backup from uploading student data without explicit permission. |
| D9 | No SQLCipher in v1. DB lives in app-private storage; protection = app PIN/biometric lock + encrypted backups | Keeps APK small and avoids native-library build risk. Revisit in Phase 6 if the owner wants at-rest DB encryption. |
| D10 | Bengali font **bundled** in the APK (Noto Sans Bengali, OFL licence) | Offline requirement. Downloadable fonts need internet; some devices lack a good Bengali font. The TTF must be added to `res/font` — it cannot be downloaded in this sandbox. |
| D11 | Liquid Glass = translucent layered surfaces + gradient specular borders + soft shadows. True *backdrop* blur is optional | Compose `Modifier.blur` blurs a node's own content, not what is behind it. Real backdrop blur needs a render-node capture library; decision deferred to Phase 2 and gated by API level and a settings toggle. |
| D12 | Per-app language through `AppCompatDelegate.setApplicationLocales` (AndroidX back-port), mirrored in DataStore | Works API 26+, survives restart, no manual Context wrapping. Needs `AppCompatActivity` hosting Compose. |
| D13 | One draw routine per document, rendered to a `Canvas` that is backed by either `PdfDocument.Page` or a `Bitmap` | PDF, JPG and PNG are guaranteed identical. Bengali shaping comes from Android's text engine (`StaticLayout`). |
| D14 | Google Drive via Google Identity Services *Authorization* + Drive REST (`drive.appdata` scope) with a small OkHttp/HttpURLConnection client, not the heavy `google-api-client` | Minimum permission (hidden app folder), fewer dependencies. Needs an OAuth client created by the owner — see §9. |
| D15 | Exact dependency versions are pinned in Phase 2 after checking current stable releases | Avoids writing from stale memory. |

---

## 2. Layered architecture

```
┌──────────────────────────── UI (Jetpack Compose) ───────────────────────────┐
│ feature/<name>/ui     Screens, components, previews                          │
│ feature/<name>/vm     ViewModel (StateFlow<UiState>, one-shot Events)        │
└───────────────▲──────────────────────────────────────────────────────────────┘
                │ calls use-cases / observes Flow
┌───────────────┴──────────────────── DOMAIN (pure Kotlin) ───────────────────┐
│ model/        Student, Batch, Payment, Money, GradeScale …                   │
│ repository/   interfaces only                                                │
│ usecase/      RegisterStudent, RecordPayment, ReversePayment,                │
│               ComputeLedger, MarkAttendance, ComputeResult, RankMerit …      │
│ calc/         FeeCalculator, AttendanceCalculator, GradeCalculator           │
└───────────────▲──────────────────────────────────────────────────────────────┘
                │ implemented by
┌───────────────┴──────────────────── DATA ───────────────────────────────────┐
│ db/           Room: entities, DAOs, TypeConverters, migrations, triggers    │
│ repository/   *RepositoryImpl (maps entity ↔ domain, wraps transactions)    │
│ prefs/        DataStore (theme, language, security flags)                    │
│ files/        photo store, logo store, SAF helpers                           │
│ backup/       archive writer/reader, restore engine, Drive client            │
│ docs/         DocumentRenderer, templates, PDF/Bitmap/ZIP exporters          │
│ comm/         WhatsApp/SMS intent builders, variable resolver                │
│ work/         WorkManager workers (reminders, scheduled backup, bulk export) │
└──────────────────────────────────────────────────────────────────────────────┘
        core/  designsystem · navigation · i18n · util · di (Hilt modules)
```

**Dependency rule:** `ui → domain ← data`. `domain` imports nothing from `ui` or `data`. `data` implements `domain.repository` interfaces. Hilt wires implementations in `core/di`.

**State flow:** Room `Flow` → repository → use-case → ViewModel `StateFlow` → Compose `collectAsStateWithLifecycle()`. Dashboard and lists update automatically because they observe `Flow`s, never one-off reads.

**Transactions:** every multi-row write (register student + guardian + enrolment + opening charges; record payment + allocations; save a batch of marks; restore) runs in `db.withTransaction {}` inside a repository method. ViewModels never touch DAOs.

**Errors:** repositories return `Result`-style sealed outcomes (`Success`, `ValidationError(field, reason)`, `DuplicateWarning(candidates)`, `Failure(cause)`). UI maps them to bilingual messages. No raw exception text is shown to the user.

---

## 3. Package / project structure

```
TutorFlow/
├─ README.md   PROGRESS.md
├─ docs/                      01-architecture … 05-implementation-checklist
├─ assets/brand/              original logo + developer photo (unmodified)
├─ assets/reference/          Liquid Glass reference screenshot
├─ scripts/                   verify.sh, make-keystore.sh, build-release.sh
├─ .github/workflows/android.yml   (Phase 2)
├─ gradle/libs.versions.toml       (Phase 2)
├─ settings.gradle.kts  build.gradle.kts
└─ app/
   ├─ build.gradle.kts  proguard-rules.pro
   ├─ schemas/                     Room exported schemas (committed, used by migration tests)
   └─ src/
      ├─ main/
      │  ├─ AndroidManifest.xml
      │  ├─ res/ (values, values-bn, font, drawable, mipmap-*, xml/file_paths, xml/data_extraction_rules)
      │  └─ java/com/tutorflow/app/
      │     ├─ TutorFlowApp.kt  MainActivity.kt
      │     ├─ core/{designsystem,navigation,i18n,util,di}/
      │     ├─ domain/{model,repository,usecase,calc}/
      │     ├─ data/{db,repository,prefs,files,backup,docs,comm,work}/
      │     └─ feature/
      │        ├─ splash/ onboarding/ lock/ home/ students/ attendance/ payments/
      │        ├─ batches/ classes/ exams/ results/ documents/ idcard/
      │        ├─ messages/ calendar/ reports/ expenses/ backup/ settings/ about/ search/
      ├─ debug/   DevSeeder (1,000+ sample records; never in release)
      ├─ test/    JVM unit tests (domain, calculators, ranking, templates)
      └─ androidTest/   Room, migration, repository, Compose UI tests
```

Debug-only seeding lives in the `debug` source set, so the release build physically cannot contain sample data.

---

## 4. Technology stack and how each requirement is met

| Requirement | Approach |
|---|---|
| Kotlin, Compose, Material 3 | Compose BOM; Material 3 components restyled through `TutorFlowTheme` |
| MVVM + Clean | §2 |
| Room / SQLite | Versioned schema, `exportSchema = true`, explicit `Migration`s, foreign keys ON, triggers re-created in `onCreate`/`onOpen` |
| DataStore | Theme, language, onboarding-done, lock settings, last-used pickers |
| Hilt | `@HiltAndroidApp`, `@HiltViewModel`, modules in `core/di`; KSP for Hilt and Room |
| Navigation Compose | Type-safe routes; see `03-navigation.md` |
| Coroutines / Flow | `viewModelScope`, `Dispatchers.IO` inside repositories, `Flow` from DAOs |
| WorkManager | Reminder scheduling, scheduled local/Drive backup, bulk PDF/ZIP generation |
| PDF | `android.graphics.pdf.PdfDocument` (no extra dependency) |
| Images | `Bitmap` + `Canvas`; JPEG/PNG via `Bitmap.compress` |
| QR | `com.google.zxing:core` (pure Java). Payload = opaque verification token only, never personal data |
| Photo selection | Android Photo Picker (`PickVisualMedia`), then downscale to ≤ 600 px JPEG q85 into app-private storage |
| Storage | Storage Access Framework (`CreateDocument`, `OpenDocument`, `OpenDocumentTree` with persisted URI permission) |
| Encrypted backup | zip4j with AES-256 ZIP encryption; passphrase never stored |
| Google Drive | §9 |
| Biometric | `androidx.biometric` `BiometricPrompt`; PIN fallback |
| Min SDK | **26** (Android 8.0), as specified |

Not used (per spec): Firebase, any online database, WebView shell, ad SDKs, analytics/tracking.

---

## 5. Offline-first guarantee

- Every feature in spec §5 reads and writes only Room, app-private files, or SAF URIs. No repository in the core path takes a network dependency.
- The only classes allowed to open a network connection are `data/backup/drive/*`. The `INTERNET` permission is declared, but a unit check asserts no other package references `java.net`/OkHttp.
- Drive features are hidden behind a connected-account state; with Drive disconnected the app behaves identically.

---

## 6. Security design

- **PIN:** PBKDF2-HMAC-SHA256 with per-install salt and high iteration count; stored wrapped by an Android Keystore AES-GCM key. Lock-out delay after repeated failures.
- **Biometric:** `BIOMETRIC_STRONG`, with PIN as fallback. Re-lock after a configurable background timeout.
- **Screen privacy:** optional `FLAG_SECURE` setting (blocks screenshots and recents thumbnail).
- **Input validation:** domain validators (phone normalisation to +8801XXXXXXXXX, non-negative money, date sanity, required fields) run before any write; DB constraints are the second line of defence.
- **Deletion:** confirmation dialog stating what will and will not be removed; records with financial/academic history can only be archived.
- **Sharing:** documents leave the app only through `FileProvider` URIs and the system share sheet.
- **Backups:** encrypted ZIP, manifest with per-file SHA-256, restore never overwrites without a confirmed warning and an automatic safety backup.

---

## 7. Documents engine (spec §15–17)

```
DocumentSpec(type, student/exam/payment ids, templateId)
      │  DocumentDataResolver  (reads DB → DocumentData, no manual re-typing)
      ▼
DocumentRenderer.draw(canvas, DocumentData, TemplateConfig)   ← single drawing routine
      ├─ PdfDocument page canvas  → .pdf
      └─ Bitmap canvas            → .jpg / .png
```

- 15 document types, 5 design templates (Classic Academic, Modern Premium, Minimal White, Navy and Gold, Professional Institutional). Navy and Gold is the default.
- `TemplateConfig` (JSON in `document_templates`): colours, font choice, border, layout, logo/signature position, size (A4 or custom mm).
- Bulk generation runs in a WorkManager worker with progress; JPG bulk output is packed with `java.util.zip` into one ZIP. Large bitmaps are recycled per card to keep memory flat.
- Print through `PrintManager`; share through `FileProvider`.
- Branded documents embed the **original** logo asset, scaled with preserved aspect ratio — never recoloured.

---

## 8. Communication (spec §18)

- Variables (`{student_name}`, `{due_amount}`, …) are resolved by a pure-Kotlin `MessageVariableResolver` (unit-testable). Unknown variables are flagged, not silently dropped.
- **WhatsApp:** `https://wa.me/<number>?text=<encoded>` opens WhatsApp with a prefilled message the owner reviews before sending. `<queries>` entries for `com.whatsapp` and `com.whatsapp.w4b` are declared for Android 11+ package visibility.
  *Limitation:* when a file (PDF/image) is attached with `ACTION_SEND`, WhatsApp does not allow targeting a specific phone number — the owner picks the chat. The UI states this.
- **SMS:** default path opens the native SMS app with the text prefilled (no permission needed). Direct sending via `SmsManager` is an opt-in feature: runtime `SEND_SMS` permission, a confirmation dialog listing recipient count and a carrier-charge warning, never silent.
- The app records *prepared* / *opened* only. It never claims *delivered* (`message_log` has no delivered state).

---

## 9. Backup, restore, Google Drive (spec §19)

**Archive layout (versioned):**

```
tutorflow-<scope>-<yyyyMMdd-HHmm>.tfbak   (AES-256 encrypted ZIP)
├─ manifest.json   { format_version, app_version, db_schema_version, db_uuid, scope, created_at, entries:[{path, sha256}] }
├─ tutorflow.db    consistent snapshot (VACUUM INTO) — full scope
├─ tables/*.json   row export — scoped backups
├─ media/photos/*  media/logo.*  media/signature.*
├─ templates.json  settings.json
```

**Scopes:** Full, Student, Academic, Financial, Attendance, Document Templates, Settings. Full restore replaces the database. Scoped restore is allowed only when the archive's `db_uuid` equals the current database's lineage; rows are upserted by primary key inside one transaction and `PRAGMA foreign_key_check` must pass, otherwise the whole restore rolls back. (Merge-by-remap across unrelated databases is out of scope.)

**Restore pipeline (spec §19, 8 steps):** validate archive (zip + manifest + hashes) → check `db_schema_version` ≤ current (older is migrated, newer is refused) → create safety backup → show warning with counts → explicit confirmation → restore into a temporary DB, run `integrity_check` + `foreign_key_check` + migrations → atomically swap (close Room, replace file, reopen, re-create triggers) → show result. On any failure the safety backup is kept and the current data stays untouched.

**Google Drive:** Authorization API, scope `https://www.googleapis.com/auth/drive.appdata` (private app folder, minimum permission). Manual backup/restore, backup list as "cloud file selection", last-backup date and status from `backup_history`, scheduled upload by WorkManager with a network-connected constraint. No password handling. **Owner setup required** (cannot be done or tested by the assistant): a Google Cloud project, OAuth consent screen, an *Android* OAuth client for package `com.tutorflow.app` and the SHA-1 of the release keystore. Drive will be reported as untested until the owner completes this and runs it on a device.

---

## 10. Notifications and scheduling (spec §21)

- Events (class start, exam day, fee deadline, holiday) create rows in `notifications`; a WorkManager worker re-schedules them after reboot or app update.
- Inexact scheduling only (no exact-alarm permission), so a reminder may fire a few minutes late. `POST_NOTIFICATIONS` is requested on Android 13+ with an in-app explanation.

---

## 11. Testing strategy (what can and cannot run here)

| Layer | Tooling | Runnable in this sandbox? |
|---|---|---|
| Domain calculators, ranking, validators, variable resolver | JUnit on JVM | **No** (no Gradle/Kotlin compiler). Written here, executed by owner/CI |
| Room DAOs, triggers, constraints, migrations | `androidTest` + `MigrationTestHelper` | **No** (needs emulator/device) |
| Compose UI (themes, languages, navigation) | Compose UI test | **No** |
| Bengali rendering, Drive, WhatsApp/SMS intents, restart persistence, 1,000-record performance | Manual checklist on a real phone | **No** |

No test will be reported as passed unless a real report/log from an execution is available.

---

## 12. Release and signing

- `scripts/make-keystore.sh` guides the owner to create **their own** keystore. The assistant does not generate or hold the signing key: losing it blocks all future updates, and a key created in a shared sandbox should not be trusted.
- `keystore.properties` is git-ignored; `app/build.gradle.kts` reads it for the `release` signing config. R8 minification enabled with Room/Hilt/zip4j keep rules checked in Phase 8.
- Final step: `./gradlew assembleRelease`, then `apksigner verify`, then install on a device. An APK is declared "produced" only when that build log exists.
