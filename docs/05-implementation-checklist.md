# TutorFlow — Implementation Checklist

Legend — each task has three independent states:
**W** = written · **C** = compiled (a real Gradle build log exists) · **T** = tested (a real test report exists).
`[x]` done and evidenced · `[ ]` not done · `n/a` not applicable.
Only W can be ticked in the Phase 1 sandbox (no Gradle/SDK). C and T are never ticked from memory or assumption.

Spec § references are to `TutorFlow_Full_Master_Prompt`.

## PHASE 1 — Architecture  (spec §31)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 1.1 | Analyse full spec (all 34 sections read) | [x] | n/a | n/a |
| 1.2 | Verify logo palette against spec colours (sampled: #0B1B3F, #F5B942, #FFFFFF, #5D5340 ✔) | [x] | n/a | n/a |
| 1.3 | Architecture document (`01-architecture.md`) | [x] | n/a | n/a |
| 1.4 | Database schema + migration/trigger/archive policy (`02-database-schema.md`) | [x] | n/a | n/a |
| 1.5 | ER diagram, rendered to PNG/SVG (`er-diagram.*`) | [x] | n/a | n/a |
| 1.6 | Navigation structure (`03-navigation.md`) | [x] | n/a | n/a |
| 1.7 | UI design system + contrast audit (`04-design-system.md`) | [x] | n/a | n/a |
| 1.8 | Project folder, brand assets, this checklist, `PROGRESS.md` | [x] | n/a | n/a |
| 1.9 | Owner reviews decisions listed in `PROGRESS.md` §3 | [ ] | n/a | n/a |

## PHASE 2 — Design foundation  (spec §3, §6, §7, §8, §28)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 2.1 | Pin current stable versions (done 2026-11-13: AGP 9.1.1, Kotlin 2.3.10, KSP 2.3.11, Hilt 2.60, Compose BOM 2026.08.00; no version catalog, versions live in build files) (AGP, Kotlin, Compose BOM, Room, Hilt, Navigation, WorkManager, zip4j, ZXing, biometric) | [x] | [ ] | n/a |
| 2.2 | Gradle project: version catalog, `:app`, Hilt, KSP, Room schema export, signing config reading `keystore.properties` | [x] | [ ] | [ ] |
| 2.3 | CI workflow (`.github/workflows/android.yml` written; verify.sh not yet adapted) `.github/workflows/android.yml` + `scripts/verify.sh` (the real compile/test loop) | [x] | [ ] | [ ] |
| 2.4 | Colour/typography/shape tokens, `TutorFlowTheme`, light/dark/system | [x] | [ ] | [ ] |
| 2.5 | Bundle Noto Sans Bengali fonts (BLOCKED: font files cannot be downloaded here; Phase 2 uses the phone's system Bengali font); Bengali rendering check | [ ] | [ ] | [ ] |
| 2.6 | Glass component family (§4 of design system) | [x] | [ ] | [ ] |
| 2.7 | DataStore: theme, language, onboarding flag; locale switching via `AppCompatDelegate` | [x] | [ ] | [ ] |
| 2.8 | `strings.xml` en + bn, missing-key check | [x] | [ ] | [ ] |
| 2.9 | Launcher icon from original logo (adaptive + legacy + monochrome) | [x] | [ ] | [ ] |
| 2.10 | Splash screen (animated, original logo) | [x] | [ ] | [ ] |
| 2.11 | Onboarding wizard (writes `app_settings`) | [x] | [ ] | [ ] |
| 2.12 | Navigation shell: glass tab bar, search button, More hub grid, typed routes | [x] | [ ] | [ ] |
| 2.13 | Home dashboard layout with real-data hooks (tiles, charts, quick actions) | [x] | [ ] | [ ] |
| 2.14 | Theme/language switch + restart persistence tests | [ ] | [ ] | [ ] |

## PHASE 3 — Core modules  (spec §9–14, §20, §23, §27)

Order is dependency-driven; each module is compiled and tested before the next starts.

| ID | Task | W | C | T |
|---|---|---|---|---|
| 3.1 | Room DB v1: all entities, DAOs, converters, FKs, indexes, triggers, schema JSON | [ ] | [ ] | [ ] |
| 3.2 | `Money`, validators (phone normalisation, dates), id sequences | [ ] | [ ] | [ ] |
| 3.3 | Settings + sessions repository | [ ] | [ ] | [ ] |
| 3.4 | Classes, subjects, batches (CRUD, archive, capacity, schedule, counts, filters) | [ ] | [ ] | [ ] |
| 3.5 | Students: registration form, photo (Photo Picker → downscale), duplicate detection, multi-batch enrolment, profile, edit/archive/restore/delete rules | [ ] | [ ] | [ ] |
| 3.6 | Student search/filter/sort (indexed) + Global Search | [ ] | [ ] | [ ] |
| 3.7 | Attendance: take/edit/history/calendar, mark-all-present, unique constraint, % calculator | [ ] | [ ] | [ ] |
| 3.8 | Fees: structures (versioned), monthly charge generation (idempotent), ledger calculator | [ ] | [ ] | [ ] |
| 3.9 | Payments: full/partial/advance, methods, receipt numbers, reversal/correction, immutability triggers | [ ] | [ ] | [ ] |
| 3.10 | Exams + exam subjects; grade scales (versioned) | [ ] | [ ] | [ ] |
| 3.11 | Marks entry (absent/withheld/missing), result + GPA + pass/fail + merit ranking with ties | [ ] | [ ] | [ ] |
| 3.12 | Result analysis (highest/lowest/avg/pass%, top performers, exam comparison) | [ ] | [ ] | [ ] |
| 3.13 | Expenses (separate from payments) | [ ] | [ ] | [ ] |
| 3.14 | Calendar + notifications + WorkManager rescheduling | [ ] | [ ] | [ ] |
| 3.15 | Dashboard wired to real queries, live-updating | [ ] | [ ] | [ ] |
| 3.16 | Reports centre (screens + filters + CSV export) | [ ] | [ ] | [ ] |
| 3.17 | JVM unit tests: FeeCalculator, AttendanceCalculator, GradeCalculator, merit ranking, validators | [ ] | n/a | [ ] |
| 3.18 | Instrumented tests: constraints, triggers, transactions, multi-batch, no-duplicate attendance | [ ] | [ ] | [ ] |
| 3.19 | Debug seeder, 1,000+ records, measured query timings | [ ] | [ ] | [ ] |

## PHASE 4 — Document generator  (spec §15–17)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 4.1 | `DocumentRenderer` + PDF/JPG/PNG exporters (shared canvas drawing) | [ ] | [ ] | [ ] |
| 4.2 | Data resolver (no re-typing) + 15 document types | [ ] | [ ] | [ ] |
| 4.3 | 5 design templates + customisation config | [ ] | [ ] | [ ] |
| 4.4 | QR generation (opaque token only) + verification lookup | [ ] | [ ] | [ ] |
| 4.5 | ID-card designer (front/back, sizes, saved templates, preview) | [ ] | [ ] | [ ] |
| 4.6 | Admit card (multi-subject), registration card, marksheet, receipt, certificates | [ ] | [ ] | [ ] |
| 4.7 | Bulk generation worker + ZIP, print, share, save (SAF) | [ ] | [ ] | [ ] |
| 4.8 | Bengali/English rendering verification on device | [ ] | [ ] | [ ] |

## PHASE 5 — Communication  (spec §18)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 5.1 | Message templates (10 categories, en/bn) + variable resolver | [ ] | [ ] | [ ] |
| 5.2 | WhatsApp deep link + file share (with stated limitation) | [ ] | [ ] | [ ] |
| 5.3 | SMS: native app prefilled; opt-in direct send with permission + charge warning | [ ] | [ ] | [ ] |
| 5.4 | Parent Communication Center + bulk preview/confirmation, `message_log` | [ ] | [ ] | [ ] |

## PHASE 6 — Backup and security  (spec §19, §26)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 6.1 | Local backup: archive writer, manifest + SHA-256, AES-256, SAF destinations | [ ] | [ ] | [ ] |
| 6.2 | Scoped backups (student, academic, financial, attendance, templates, settings) | [ ] | [ ] | [ ] |
| 6.3 | Restore pipeline (8 steps), safety backup, temp-DB validation, atomic swap | [ ] | [ ] | [ ] |
| 6.4 | Google Drive backup/restore/schedule/history (owner OAuth setup required) | [ ] | [ ] | [ ] |
| 6.5 | PIN (PBKDF2 + Keystore), biometric, lock timeout, FLAG_SECURE option | [ ] | [ ] | [ ] |
| 6.6 | Integrity checks, input validation audit, `allowBackup=false` verification | [ ] | [ ] | [ ] |
| 6.7 | About page (original logo, developer photo saved locally, links, version) | [ ] | [ ] | [ ] |

## PHASE 7 — Testing  (spec §30)

The 30 test areas of spec §30 are tracked individually in `PROGRESS.md` §5 with one of: **PASSED (log attached)**, **FAILED**, **NOT RUN**. Anything needing a device or emulator stays NOT RUN until the owner or CI executes it.

## PHASE 8 — Release  (spec §31, §32)

| ID | Task | W | C | T |
|---|---|---|---|---|
| 8.1 | Release build config, R8 keep rules | [ ] | [ ] | [ ] |
| 8.2 | Owner creates keystore with `scripts/make-keystore.sh`; backs it up | [ ] | n/a | n/a |
| 8.3 | `assembleRelease`, `apksigner verify`, install and launch on a device | [ ] | [ ] | [ ] |
| 8.4 | User manual (en + bn), installation guide, APK build instructions | [ ] | n/a | n/a |
| 8.5 | Final source archive | [ ] | n/a | n/a |

---

## Spec traceability (every section maps to tasks)

| Spec § | Topic | Tasks | Spec § | Topic | Tasks |
|---|---|---|---|---|---|
| 2 | Logo usage | 2.9, 2.10, 4.x, 6.7 | 17 | Admit/registration card | 4.6 |
| 3 | Colours | 2.4 | 18 | WhatsApp/SMS | Phase 5 |
| 4 | Stack | 2.1–2.3 | 19 | Backup/restore/Drive | Phase 6 |
| 5 | Offline | all (rule) | 20 | Search/filter | 3.6 |
| 6 | Liquid Glass, theme, language | 2.4–2.8 | 21 | Calendar | 3.14 |
| 7 | Splash/onboarding | 2.10, 2.11 | 22 | Reports | 3.16 |
| 8 | Dashboard | 2.13, 3.15 | 23 | Expenses | 3.13 |
| 9 | Students | 3.5, 3.6 | 24 | Settings | 3.3, 2.7 |
| 10 | Classes/batches | 3.4 | 25 | About | 6.7 |
| 11 | Attendance | 3.7 | 26 | Security | 6.5, 6.6 |
| 12 | Fees/payments | 3.8, 3.9 | 27 | Database | 3.1 |
| 13 | Exams | 3.10 | 28 | Navigation | 2.12 |
| 14 | Marks/results | 3.11, 3.12 | 29 | Performance | 3.19 |
| 15 | Document generator | Phase 4 | 30 | QA | 3.17, 3.18, Phase 7 |
| 16 | ID card designer | 4.5 | 31–33 | Workflow/deliverables/rules | PROGRESS.md |
