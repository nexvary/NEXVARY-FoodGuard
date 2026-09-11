# NEXVARY FoodGuard

Android-first, multilingual and privacy-focused food freshness and visible-spoilage guide with local image assistance.

## Current milestone

**Stage 200 foundation — `0.2.0-stage200`**

The Stage-200 pass expands the original prototype into a functional application foundation with a premium Compose UI, searchable food catalog, local scanner workflow, manual safety decision support, safety handbook, theme/language settings and CI release gates.

See [`docs/STAGE_200.md`](docs/STAGE_200.md) for the grouped stage record and [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the technical structure.

## What is implemented

- Kotlin + Jetpack Compose + Material 3.
- Premium navy/gunmetal visual system with gold, fresh-green, amber, danger-red and electric-blue semantic accents.
- Light and dark appearance modes.
- Arabic RTL plus English, Turkish, French, Spanish, German and Italian resources.
- Seven-language names for all 50 seed foods.
- 10 categories: fruits, vegetables, meat, poultry, seafood, dairy, bakery, prepared foods, drinks and packaged foods.
- Searchable local catalog and food detail screens.
- Owaisi mango reference guide with normal ripeness vs visible spoilage guidance.
- Camera and gallery image selection using Android system contracts.
- Local image-quality analysis for luminance, saturation and dark-area ratio.
- Manual checklist for mold, slime, odor, package condition and storage history.
- Deterministic safety-rule layer with cautious verdicts.
- Practical safety handbook.
- About page with NEXVARY links.
- Unit tests, lint gate and debug APK CI assembly.

## Important safety boundary

FoodGuard is decision support and education, not a laboratory test. Some foodborne hazards cannot be seen, smelled or tasted. The app must not tell users that food is "100% safe" because a photo looks normal.

Current visual analysis only assesses **photo quality** and supports visual comparison. A trained spoilage model is intentionally not represented as complete until per-food datasets, validation and calibration are available.

## Build stack

- Android Gradle Plugin 9.4.0
- Gradle 9.6.0 in CI
- Kotlin 2.3.21
- JDK 17
- compileSdk / targetSdk 36
- minSdk 26

## Local build

The repository currently relies on CI-installed Gradle rather than committing a binary Gradle wrapper JAR. With JDK 17 and Gradle 9.6 installed:

```bash
gradle :app:testDebugUnitTest
gradle :app:lintDebug
gradle :app:assembleDebug
```

The APK is produced at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## CI

Every push to `main` runs unit tests, Android lint and debug assembly. The debug APK is uploaded as a workflow artifact only when the build job reaches that step successfully.

**Do not call the build verified until GitHub Actions is green.**

## Public repository note

Do not commit signing keys, API keys, private datasets, unreleased training images, personal user photos or licensed assets that cannot be redistributed. The repository `.gitignore` is configured to reduce accidental exposure, but contributors remain responsible for reviewing every commit.
