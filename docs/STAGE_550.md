# Stage 550 milestone

`0.5.0-stage550`

The project stages are grouped delivery bands rather than 350 separate releases. This milestone advances the Stage-200 foundation through the Stage-550 release gate with implemented code, tests and documentation.

## 201–250 — safer domain model

- Added structured `SafetyReasonCode` values beside human-readable reasons.
- Added red-flag and uncertainty counts to assessments.
- Kept the safety boundary that visual or manual checks cannot prove microbiological safety.

## 251–300 — local history foundation

- Added a private on-device assessment history using `SharedPreferences` + JSON.
- History is capped at 100 records.
- Records store food ID/name snapshot, verdict, timestamp, red-flag count, uncertainty count and whether the photo should be retaken.
- No food photo is copied into history.

## 301–350 — Stage-550 application shell

- New Compose application shell with Home, Guide, Scanner, History and Settings destinations.
- Detail, Safety and About pages use explicit back navigation.
- Bottom navigation stays limited to five primary destinations.

## 351–400 — scanner workflow

- Camera and gallery input remain local-first.
- Image quality assistance remains explicitly non-diagnostic.
- Manual checklist evaluation writes a local history record.
- Current assessment can be shared as plain text without attaching the source image.

## 401–450 — catalog and decision support

- Catalog supports text search, food-category filtering and risk-tier filtering.
- Catalog expanded from 50 seed foods to 120 foods while retaining all 10 top-level categories.
- Expansion includes Owaisi, Naomi, Keitt, Zebda, Timour and Sukkari mangoes plus additional regional produce, meats, poultry, seafood, dairy, bakery items, prepared foods, drinks and packaged foods.
- Every added item includes names in Arabic, English, Turkish, French, Spanish, German and Italian.
- Food details retain normal signs, spoilage signs and storage guidance.
- Higher-risk foods are surfaced on the dashboard without claiming that other foods are risk-free.

## 451–500 — multilingual and privacy UX

- Stage-550 History/assessment UI strings added for Arabic, English, Turkish, French, Spanish, German and Italian.
- Arabic continues to use Android RTL support.
- History can be reviewed or cleared from the device.

## 501–525 — release hardening

- Launcher switched to the Stage-550 shell.
- App version advanced to `0.5.0-stage550` / versionCode 5.
- Existing backup and cleartext restrictions remain enabled in the manifest.

## 526–550 — release gate

- Added assessment-summary and catalog-expansion unit tests.
- CI still requires unit tests, Android lint and debug APK assembly.
- CI artifact renamed to `NEXVARY-FoodGuard-stage550-debug`.
- A build is considered verified only after the GitHub Actions workflow is green.

## Not claimed at Stage 550

FoodGuard still does **not** contain a validated food-spoilage ML classifier. Image analysis assesses capture quality and supports comparison only. Training data, per-food validation, calibration, false-negative analysis and external review are prerequisites before a model can be described as a spoilage detector.