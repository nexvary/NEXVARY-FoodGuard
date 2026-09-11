# NEXVARY FoodGuard

Android-first, privacy-focused food freshness and spoilage guide with on-device visual analysis.

## Product goals

- Beautiful, premium UI with Arabic RTL and multilingual LTR support.
- Visual encyclopedia for fruits, vegetables, meat, poultry, fish, dairy, baked goods, prepared foods, drinks, canned foods and packaged foods.
- Each food entry documents normal appearance, ripeness stages, visible spoilage signs, storage guidance and when to discard.
- Camera/gallery analysis designed to run locally on the device.
- Results never claim that a food is "100% safe" from an image. The app distinguishes visual appearance from microbiological safety.
- Optional follow-up questions about smell, texture, temperature and storage time improve guidance without uploading the user's image.

## v0.1 Foundation

The first milestone establishes:

1. Android/Kotlin + Jetpack Compose project foundation.
2. Premium dark visual system with semantic fresh / caution / danger states.
3. Arabic, English, Turkish, French, Spanish, German and Italian resource structure.
4. Correct RTL/LTR behavior.
5. Home screen with food categories and scan entry point.
6. First reference entry: Mango / Egyptian Owaisi mango.
7. Architecture placeholders for local on-device food recognition and visible-spoilage analysis.
8. UI release gate checklist for small screens, large fonts, long translations, back navigation and dead-link prevention.

## Safety model

FoodGuard is a decision-support and education app, not a laboratory test. Some foodborne hazards cannot be seen, smelled or tasted. Visual analysis therefore reports states such as:

- Looks visually normal
- Very ripe / quality declining
- Visible spoilage signs detected
- Unable to determine from image

High-risk guidance must prefer safe discard advice when storage history or visible signs indicate risk.

## Planned architecture

- Kotlin
- Jetpack Compose + Material 3
- Local structured food knowledge base
- On-device inference layer (LiteRT-ready abstraction)
- CameraX integration
- Offline-first history and preferences
- No image upload required for core analysis

## UI release gate

Before each release:

- No overlapping text/icons at supported font scales.
- Arabic is true RTL; LTR languages remain LTR.
- All visible buttons and cards are actionable or clearly disabled.
- Back navigation works from every inner page.
- Small-phone and large-phone layouts are checked.
- Long German/French labels do not clip.
- Light/dark contrast is accessible.
- Camera and gallery permissions fail gracefully.
- Analysis results include uncertainty and safety disclaimer.

## Status

Foundation work started on 2026-09-11.
