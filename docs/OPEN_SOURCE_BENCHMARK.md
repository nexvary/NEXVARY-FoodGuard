# NEXVARY FoodGuard — Open-source benchmark

This redesign deliberately studies mature open-source food and Android projects before implementation. The goal is to reuse proven interaction patterns, architecture ideas and validated workflows without blindly copying UI or importing license-incompatible code.

## Benchmarked projects

### Open Food Facts `smooth-app`
- Repository: `openfoodfacts/smooth-app`
- License: Apache-2.0
- Why it matters: scan-first food UX, information-dense product pages, explicit uncertainty states, strong color semantics, search and category browsing.
- Applied to FoodGuard: scanner is treated as a primary operational workflow; food detail pages prioritize evidence, state and decision support over decorative cards.

### Android `nowinandroid`
- Repository: `android/nowinandroid`
- License: Apache-2.0
- Why it matters: production-grade Compose architecture, adaptive navigation, restrained Material 3 usage, accessibility, state separation and testability.
- Applied to FoodGuard: compact top-level navigation, consistent spacing grid, reduced ornamental rounding, stronger information hierarchy and components designed for both LTR and RTL.

### FoodExpirationDates
- Repository: `lorenzovngl/FoodExpirationDates`
- License: MIT
- Why it matters: Kotlin/Compose food-expiry workflows, MVVM, local persistence and modern Android UI patterns.
- Applied to FoodGuard: concept reference for expiry/storage records and local-first state organization. No visual design is copied.

### Grocy Android
- Repository: `patzly/grocy-android`
- License: GPL-3.0
- Why it matters: mature grocery/inventory workflows, dense professional information layout, barcode-oriented actions and batch-oriented UX.
- Applied to FoodGuard: concepts only. No Grocy source code is copied into FoodGuard because GPL code is intentionally kept outside this codebase.

### Friskr
- Repository: `vardirhq/friskr`
- Why it matters: refrigerator/freezer/pantry workflows, expiry tracking, barcode use and OCR-oriented product entry.
- Applied to FoodGuard: operational concepts for storage context, expiry metadata and fast item capture. Any future direct code reuse requires a fresh license check at the exact revision used.

### OpenPantry / EverShelf / Prepper Log
- Repositories studied: `chromagic-development/OpenPantry`, `dadaloop82/EverShelf`, `BEKO2210/Prepper_Log`
- Why they matter: inventory dashboards, offline-first pantry management, barcode flows, expiry alerts and reporting.
- Applied to FoodGuard: concepts for later inventory/storage modules and information-density patterns. No source code is imported by this redesign.

### TensorFlow Lite / LiteRT Android image-classification examples
- Sources studied: official TensorFlow/LiteRT Android examples and MIT food-classification demos such as `SilviaSantano/Recognize-Food-With-TensorFlow-Lite`.
- Why they matter: CameraX-to-model inference plumbing, preprocessing, model lifecycle and on-device classification patterns.
- Applied to FoodGuard: future ML infrastructure only. A generic food classifier must never be presented as a validated spoilage detector. FoodGuard requires a labeled freshness/spoilage dataset and per-food validation before confidence scores can be safety-facing.

### Open Food Facts data/API ecosystem
- Purpose: future barcode/product metadata enrichment and reference data.
- Rule: any future data/API integration must preserve source attribution and comply with the applicable data license and API terms.

## Enterprise redesign principles

1. **Command-center hierarchy** — the home screen should look like an operational console, not a collection of toy cards.
2. **Restrained effects** — royal gold, electric cyan/blue and glowing silver are accents, not full-surface fills.
3. **Information density with breathing room** — compact telemetry, risk summaries, evidence counts and clear scan entry points.
4. **Professional iconography** — line icons remain small and purposeful; no oversized cartoon-like icon tiles.
5. **Evidence first** — healthy / ripe / overripe / visible spoilage references appear as evidence panels, not decorative gallery items.
6. **Uncertainty is explicit** — image inspection never claims that food is safe merely because it looks normal.
7. **RTL as a release gate** — navigation, chevrons, alignment and reading order must work in Arabic, not just translated strings.
8. **Offline-first** — local analysis and local history remain primary; cloud integrations are optional extensions.
9. **Operational language** — telemetry, risk, evidence and status labels are preferred over playful copy and oversized decorative UI.
10. **Release evidence** — a redesign is not accepted until unit tests, lint, APK assembly and English/Arabic emulator screenshots all pass.

## License policy

- Apache-2.0 / MIT ideas and code may be reused only with the required notices and attribution.
- GPL projects may be studied for interaction concepts, but their source is not copied into FoodGuard unless the entire licensing strategy is intentionally changed.
- Projects without a confirmed compatible license are concept references only until the exact revision and license are verified.
- Every imported asset, dataset, model or source file must have a recorded origin, revision and license before it enters a release build.
- Reference food images must be traceable to a license/source; synthetic imagery is not treated as authoritative food-safety evidence.
