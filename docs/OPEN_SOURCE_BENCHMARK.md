# NEXVARY FoodGuard — Open-source benchmark

This redesign deliberately studies mature open-source food and Android projects before implementation. The goal is to reuse proven interaction patterns and architecture ideas without blindly copying UI or importing license-incompatible code.

## Benchmarked projects

### Open Food Facts `smooth-app`
- Repository: `openfoodfacts/smooth-app`
- License: Apache-2.0
- Why it matters: scan-first food UX, information-dense product pages, explicit uncertainty states, strong color semantics, search and category browsing.
- Applied to FoodGuard: scanner is treated as the primary operational workflow; food detail pages prioritize evidence, state, and decision support over decorative cards.

### Android `nowinandroid`
- Repository: `android/nowinandroid`
- License: Apache-2.0
- Why it matters: production-grade Compose structure, clean separation of UI state, adaptive navigation, restrained Material 3 usage, accessibility and testability.
- Applied to FoodGuard: compact top-level navigation, consistent spacing grid, reduced ornamental rounding, stronger information hierarchy, and components designed for both LTR and RTL.

### Grocy Android
- Repository: `patzly/grocy-android`
- License: GPL-3.0
- Why it matters: mature grocery/inventory workflows, dense professional information layout, barcode-oriented actions, batch-oriented UX.
- Applied to FoodGuard: concepts only. No Grocy source code is copied into FoodGuard because GPL code is intentionally kept outside this codebase.

### Open Food Facts data/API ecosystem
- Purpose: future barcode/product metadata enrichment and reference data.
- Rule: any future data/API integration must preserve source attribution and comply with the applicable data license and API terms.

## Enterprise redesign principles

1. **Command-center hierarchy** — the home screen should look like an operational console, not a collection of toy cards.
2. **Restrained effects** — royal gold, electric cyan/blue and glowing silver are accents, not full-surface fills.
3. **Information density with breathing room** — compact telemetry, risk summaries, evidence counts, and clear scan entry points.
4. **Professional iconography** — line icons remain small and purposeful; no oversized cartoon-like icon tiles.
5. **Evidence first** — healthy / ripe / overripe / visible spoilage references appear as evidence panels, not decorative gallery items.
6. **Uncertainty is explicit** — image inspection never claims that food is safe merely because it looks normal.
7. **RTL as a release gate** — navigation, chevrons, alignment and reading order must work in Arabic, not just translated strings.
8. **Offline-first** — local analysis and local history remain primary; cloud integrations are optional extensions.

## License policy

- Apache-2.0 / MIT ideas and code may be reused only with the required notices and attribution.
- GPL projects may be studied for interaction concepts, but their source is not copied into FoodGuard unless the entire licensing strategy is intentionally changed.
- Every imported asset, dataset, model, or source file must have a recorded origin and license before it enters a release build.
