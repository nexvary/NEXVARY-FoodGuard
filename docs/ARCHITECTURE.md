# NEXVARY FoodGuard Architecture

## Product boundary

FoodGuard is an offline-first Android decision-support application for food freshness, visible spoilage education and storage guidance. It is not a laboratory test and must never claim that an image proves microbiological safety.

## Layers

### UI

`ui/FoodGuardApp.kt`

- Compose application shell and navigation state.
- Premium Home, Catalog, Scanner, Safety, Settings, About and Food Detail screens.
- RTL/LTR layout comes from Android locale configuration.
- Theme preference is stored locally.

### Domain model

`model/FoodModels.kt`

- Food categories and risk tiers.
- Seven-language display labels.
- Food knowledge records.
- Manual safety-check input and verdict types.

### Local knowledge base

`data/FoodCatalog.kt`

- Stage-200 seed catalog with 50 foods.
- Ten categories.
- Search across seven localized names plus aliases.
- Shared category defaults plus food-specific overrides.

The catalog is intentionally code-backed in this foundation release so content changes are explicit and reviewable. Later stages can move reviewed content into versioned assets or a local database.

### Decision rules

`domain/SafetyRules.kt`

- Deterministic rules for reported red flags.
- High-risk foods receive stricter treatment when storage history is unknown.
- No-red-flag results retain an explicit statement that safety is not proven.

### Image analysis

`analysis/VisualHeuristicEngine.kt`

- Runs on the device.
- Samples image luminance, saturation and dark-area ratio.
- Produces image-quality observations only.
- Does not label food as safe/spoiled.

A future `FoodVisionModel` implementation should sit behind a narrow interface and expose calibrated, per-class model output only after validation.

## Privacy defaults

- Scanner uses the system camera/gallery contracts.
- No broad storage permission is required for the current scanner.
- No image-upload path exists in the Stage-200 scanner.
- Application backup is disabled.
- Cleartext network traffic is disabled.

## Release gate

A release candidate must pass:

1. Unit tests.
2. Android lint.
3. Debug/release assembly as applicable.
4. RTL and LTR smoke tests.
5. Small-screen and large-font visual checks.
6. Back-navigation checks.
7. Manual verification that every visible action is connected or clearly disabled.
8. Safety copy review for any new food class.
