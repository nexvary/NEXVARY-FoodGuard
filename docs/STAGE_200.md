# Stage 200 — Foundation Expansion

This document records the consolidated implementation pass from the initial foundation through Stage 200. Stage numbers are grouped into delivery blocks so progress is auditable without pretending that every number is a separate release.

## Stages 1–20 — Repository and Android foundation

- Android application module, Kotlin and Jetpack Compose foundation.
- API 26 minimum, API 36 target/compile level.
- Edge-to-edge layout and safe-drawing insets.
- Public-repository secret hygiene and local-data exclusions.
- Android CI workflow defined.

## Stages 21–40 — Premium design system

- Dark navy / gunmetal base with platinum text.
- Fresh green, gold, amber, danger red and electric-blue semantic accents.
- Rounded cards, border hierarchy, hero panels and status pills.
- Light and dark palettes.
- Theme choice persisted locally with SharedPreferences.

## Stages 41–60 — Localization and RTL

- Arabic, English, Turkish, French, Spanish, German and Italian resources.
- Arabic RTL supported at application level.
- Food names localized across all seven languages for the 50 seed entries.
- Android per-app language settings entry point.
- UI labels designed to tolerate long German/French text.

## Stages 61–80 — Food knowledge model

- FoodCategory, RiskTier and FoodItem domain models.
- LocalizedLabel with language-aware resolution.
- Ten food categories.
- Exactly 50 seed food entries.
- Search by multilingual name and aliases.
- Category-level normal, spoilage and storage defaults.

## Stages 81–100 — Reference content

- Owaisi mango is the flagship reference entry.
- Mango guidance distinguishes darker orange ripe flesh from visible spoilage signals.
- High-risk categories include meat, poultry, seafood, selected dairy, prepared foods and compromised canned foods.
- Special rules added for bread mold, cooked rice and damaged cans.

## Stages 101–120 — Navigation and catalog UX

- Bottom navigation for Home, Guide, Scan, Safety and Settings.
- Searchable catalog.
- Category filters.
- Food detail pages.
- Risk badges.
- Working back navigation for detail/about pages.
- Empty states and safe layout spacing.

## Stages 121–140 — Local image workflow

- Camera capture through the system camera contract.
- Gallery image selection without broad storage permission.
- No network upload required by the foundation scanner.
- Image displayed locally inside the scan flow.
- VisualHeuristicEngine computes local luminance, saturation, dark-pixel ratio and photo-quality observations.
- Image metrics are explicitly not presented as food-safety proof.

## Stages 141–160 — Safety decision support

- Manual checklist for mold, slime, odor, package condition, time-temperature history and unknown storage history.
- SafetyRules combines selected food risk with user-reported red flags.
- Verdicts: no reported red flags, caution, discard, insufficient information.
- The app never converts a clean photo into a claim of microbiological safety.

## Stages 161–180 — Safety handbook and privacy

- Practical safety guide for cans, meat/poultry, porous bread, leftovers and seafood.
- Clear distinction between visual quality and microbiological safety.
- Core scanner is local-first.
- Android backup disabled for this foundation build.
- Cleartext network traffic disabled.
- NEXVARY About page and official links included.

## Stages 181–200 — QA and release gate

- Unit tests for 50 unique catalog items and category coverage.
- Unit tests for critical safety-rule outcomes.
- CI configured for JDK 17 and Gradle 9.6.0, required by AGP 9.4.
- CI runs unit tests, Android lint and debug APK assembly.
- Debug APK is uploaded as a CI artifact only after a successful build.
- Release remains **unverified until CI returns green**.

## Stage 200 status

Implementation target: `0.2.0-stage200`.

Stage 200 is a functional foundation, not a trained spoilage classifier. A future model must be trained and validated per food class before model output can be shown as spoilage likelihood. Until then, image processing is limited to photo-quality signals and the decision-support checklist.
