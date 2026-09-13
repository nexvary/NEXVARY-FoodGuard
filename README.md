# NEXVARY FoodGuard

Android-first, multilingual and privacy-focused food freshness and visible-spoilage guide with local image assistance.

## Current milestone

**Stage 550 — `0.5.0-stage550`**

Stage 550 extends the Stage-200 foundation with structured safety reason codes, local assessment history, risk-tier catalog filtering, a rebuilt navigation shell, plain-text result sharing, multilingual History UX, additional tests and release documentation.

See [`docs/STAGE_550.md`](docs/STAGE_550.md), [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md), [`docs/PRIVACY.md`](docs/PRIVACY.md) and [`docs/DATASET_READINESS.md`](docs/DATASET_READINESS.md).

## Implemented

- Kotlin + Jetpack Compose + Material 3.
- Premium navy/gunmetal visual system with gold, fresh-green, amber, danger-red and electric-blue semantic accents.
- Light, dark and system appearance modes.
- Arabic RTL plus English, Turkish, French, Spanish, German and Italian resources.
- 120 catalog foods across 10 categories, including multiple Egyptian mango varieties, regional vegetables, meat, poultry, seafood, dairy, bakery, prepared foods, drinks and packaged foods.
- Searchable catalog with category and risk-tier filters.
- Food detail screens with normal signs, spoilage signs and storage guidance.
- Camera and gallery image selection through Android system contracts.
- Local image-quality analysis for luminance, saturation and dark-area ratio.
- Manual checklist for mold, slime, odor, package condition and storage history.
- Structured safety reason codes with red-flag and uncertainty counts.
- Local-only assessment history capped at 100 records; food images are not copied into history.
- Plain-text result sharing through the Android chooser.
- Practical safety handbook and About page with NEXVARY links.
- Unit tests, strict Android lint and debug APK CI assembly.

## Important safety boundary

FoodGuard is decision support and education, not a laboratory test. Some foodborne hazards cannot be seen, smelled or tasted. The app must not tell users that food is "100% safe" because a photo looks normal.

Current visual analysis assesses **photo quality** and supports visual comparison. A trained spoilage model is intentionally not represented as complete until per-food datasets, validation, calibration and false-negative analysis are available.

## Privacy boundary

Stage 550 remains local-first. The Android manifest does not request Internet access, app backup is disabled and cleartext traffic is disabled. Assessment history is app-private and can be cleared by the user. Sharing exports only a text summary chosen by the user; FoodGuard does not attach the source food photo.

## Build stack

- Android Gradle Plugin 9.4.0
- Gradle 9.6.0 in CI
- Kotlin 2.3.21
- JDK 17
- compileSdk / targetSdk 36
- minSdk 26

## Local build

The repository relies on CI-installed Gradle rather than committing a binary Gradle wrapper JAR. With JDK 17 and Gradle 9.6 installed:

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

Every push to `main` and pull request targeting `main` runs unit tests, Android lint and debug assembly. The debug APK is uploaded as `NEXVARY-FoodGuard-stage550-debug` when the build reaches the artifact step successfully.

**Do not call the build verified until GitHub Actions is green.**

## Public repository note

Do not commit signing keys, API keys, private datasets, unreleased training images, personal user photos or licensed assets that cannot be redistributed.