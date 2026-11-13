# TutorFlow — UI Design System (Phase 1)

Reference identity: the owner's uploaded logo (`assets/brand/tutorflow_logo_original.png`, 3264 × 3264 RGBA, transparent margin). The logo is **never redrawn, recoloured or re-typeset**; it is only scaled with its aspect ratio preserved.

## 1. Colour tokens

The four brand colours were sampled from the actual logo pixels and match the spec exactly: Midnight Navy `#0B1B3F`, Golden Yellow `#F5B942`, White `#FFFFFF`, Antique Gold `#5D5340` (the thin inner ring).

All colours live in `core/designsystem/Color.kt` and are exposed only through `TutorFlowColors` (a `CompositionLocal`). Screens may not use hex literals.

| Token | Light | Dark |
|---|---|---|
| `background` | `#F5F7FC` | `#08152E` |
| `surface` (primary background) | `#FFFFFF` | `#101F3D` |
| `card` | `#FFFFFF` | `#142442` |
| `brand` (primary) | `#0B1B3F` | `#F5B942` (accent leads in dark) |
| `onBrand` | `#FFFFFF` | `#0B1B3F` |
| `accent` (secondary brand) | `#F5B942` | `#F5B942` |
| `onAccent` | `#0B1B3F` | `#0B1B3F` |
| `textPrimary` | `#0B1B3F` | `#FFFFFF` |
| `textSecondary` | `#64748B` (large text, icons) / `#5B6B83` (small text) | `#B8C4D8` |
| `border` | `#E3E7EF` | `#34435D` |
| `navBackground` | `#FFFFFF` @ glass | `#101F3D` @ glass |
| `ringAntiqueGold` | `#5D5340` | `#5D5340` |
| `success` | `#1B7F4B` | `#6FDC9B` |
| `error` | `#C62828` | `#FF8A80` |
| `warning` | `#8A5A00` (text) / `#F5B942` (fill) | `#F5B942` |

Gradients (selective use only): **Navy Hero** `#0B1B3F → #13295C` (dashboard header, splash, ID-card front); **Gold Shine** `#F5B942 → #FFD27A` (primary FAB highlight, active pill). No purple, pink or neon anywhere; charts use navy, gold, a lighter navy tint, and neutrals first, semantic green/red only for pass/fail and paid/due.

### Contrast audit (computed WCAG 2.x ratios, not estimated)

| Pair | Ratio | Verdict |
|---|---|---|
| Navy text on white | 16.90 | AAA |
| Navy text on `#F5F7FC` | 15.77 | AAA |
| White on navy | 16.90 | AAA |
| Navy text on gold | 9.58 | AAA |
| Gold on dark `#08152E` | 10.29 | AAA |
| Gold on dark card `#142442` | 8.75 | AAA |
| White on dark card | 15.43 | AAA |
| `#B8C4D8` on dark card | 8.76 | AAA |
| Spec secondary `#64748B` on white | 4.76 | AA |
| Spec secondary `#64748B` on `#F5F7FC` | **4.44** | **fails AA (4.5) for small text** |
| Proposed `#5B6B83` on `#F5F7FC` | 5.06 | AA |
| **Gold text on white** | **1.76** | **fails — forbidden** |
| Border `#E3E7EF` on white | 1.24 | decorative only |

Rules derived from the audit:
1. **Gold is a fill or an accent on navy/dark — never text or thin icons on white/light backgrounds.** On light surfaces gold appears as a filled pill/button with navy text.
2. Small secondary text on the light page background uses `#5B6B83`, a deliberate small deviation from the spec value `#64748B` (which misses AA by 0.06). The spec value stays for large text and icons. The owner can veto this.
3. Borders are never the only indicator of state; add an icon or text.

## 2. Typography

- Family: **Noto Sans Bengali** bundled in `res/font` (Regular 400, Medium 500, SemiBold 600, Bold 700) for *both* scripts, so line metrics stay consistent when Bangla and English are mixed on one line. (The font files must be added to the project; they cannot be downloaded in the Phase 1 sandbox.)
- Bangla needs more line height than Latin: body line height ≥ 1.5× font size in Bangla, 1.4× in English.
- Type scale (sp): Display 32 · Headline 24 · Title 20 · Subtitle 16 (SemiBold) · Body 14–16 · Label 12–13 · Caption 11 (never smaller).
- Numerals: a setting chooses Latin (0–9) or Bengali (০–৯) digits; amounts always show the ৳ symbol (currency configurable).

## 3. Shape, spacing, elevation

- Spacing scale (dp): 4 · 8 · 12 · 16 · 20 · 24 · 32. Screen horizontal padding 16.
- Radius: Small 12 · Card 20 · Sheet/Dialog 28 · Pill/Tab bar 999.
- Minimum touch target 48 × 48 dp.
- Elevation expressed as soft, low-opacity navy-tinted shadows (light) and as stronger border + faint glow (dark).

## 4. Liquid Glass components (original TutorFlow design)

Built as one composable family in `core/designsystem/glass/`:

| Component | Use | Recipe |
|---|---|---|
| `GlassSurface(level)` | base for everything | translucent fill, 1 dp gradient "specular" border (white 60 % → transparent, top-left to bottom-right), soft shadow, rounded corners |
| `GlassCard` | dashboard tiles, list items | level 1: ~72 % opaque in light, ~60 % in dark |
| `GlassBar` | floating tab bar, top app bar | level 2: ~82 % opaque + stronger border; capsule shape for the tab bar |
| `GlassSheet` / `GlassDialog` | bottom sheets, dialogs, menus | level 3: ~92 % opaque for readability of forms |
| `GlassButton` | primary = gold fill + navy text; secondary = glass + navy/white text | pressed scale 0.97 + slight brightness change |
| `GlassTextField` | forms | glass fill, border turns gold on focus, visible error text |
| `GlassChip`, `StatTile`, `SectionHeader`, `EmptyState`, `Avatar` | shared pieces | |

Performance and readability rules:
- **Opacity floors** guarantee the contrast numbers above are met on top of the worst-case background; text never sits on a surface below level 1.
- Real *backdrop* blur is **optional** (decision D11): off by default; evaluated in Phase 2; if enabled it applies only to the tab bar/top bar on API 31+, behind a Settings toggle ("Enhanced glass"), with the plain translucent look as the automatic fallback on API 26–30 and low-RAM devices.
- Background of screens: subtle navy-to-background gradient with 1–2 soft gold radial highlights, static (no continuously animating shaders).
- No more than 2 stacked translucent layers on any screen.

## 5. Motion

- Navigation: shared fade + 8 % slide, 220–280 ms, standard easing.
- Tab bar selection pill slides between tabs (spring, low bounce).
- Micro-interactions: button press scale, success check draw on save, count-up on dashboard numbers (once, 600 ms).
- Splash: navy field → logo fades/scales in (original asset) → gold ring sweep → wordmark "TutorFlow", "Smart Tuition Management System", tagline "Manage Smarter. Teach Better." (≈ 1.8 s total, skippable once loaded). Respect the system "remove animations" setting.

## 6. Charts (native Canvas, no chart library)

Monthly Income (bar, gold on navy tint), Student Growth (line), Attendance Overview (donut: present/late/leave/absent), Fee Collection (stacked bar collected vs due). Each has a text/number alternative for accessibility and an empty state.

## 7. Iconography

Material Symbols (rounded) icons, single-weight, 24 dp; selected state uses filled variant. Bottom bar icons: Home, Groups (Students), Fact-check (Attendance), Payments, Apps/Grid (More).

## 8. Bilingual UI rules

- All strings in `values/strings.xml` (English) and `values-bn/strings.xml` (Bangla); no hard-coded UI text. A debug lint check fails the build if a key is missing from either file.
- Layouts must tolerate Bangla strings being ~20–30 % longer; no fixed-width labels.
- Dates formatted by `DateFormatter` using the chosen date format and language; months/weekday names localised.
- Documents carry language independent of app language (e.g. English ID card while the app is in Bangla), chosen per template.

## 9. Branding rules for documents

- Logo placed with preserved aspect ratio and clear space ≥ 25 % of logo height; never recoloured; on dark backgrounds the original navy tile remains as-is (it already carries its own navy).
- Default design **Navy and Gold**; other four designs share the same data layout.
- Footer line on branded documents: "Generated by TutorFlow" (can be turned off for the institution's own template).

## 10. Launcher icon (Phase 8)

Adaptive icon (API 26+): foreground = the logo tile scaled into the 66 dp safe zone, background = flat `#0B1B3F`; monochrome layer for Android 13 themed icons derived from the tile shape. Legacy mipmaps generated from the original PNG without altering the symbol.
