# F-Droid publication readiness — NEXVARY FoodGuard

Status: **NOT READY TO PUBLISH**. This checklist is a release gate, not a claim of F-Droid approval.

## Verified from the repository (2026-11-20)

- Android Kotlin / Jetpack Compose project; application ID `com.nexvary.foodguard`.
- Version in `app/build.gradle.kts`: `0.7.0-rc2`, versionCode `7`; minSdk 26, targetSdk 36.
- Latest checked GitHub Actions Android CI run `36147413992` completed successfully for `main` at commit `0d4012ed`; this is **not** an F-Droid build verification.
- Main branch has no tracked root LICENSE; Apache-2.0 license is proposed in PR #6.
- Repository is **public** (verified on November 20, 2026) and has no published GitHub release.
- The tracked tree contains Gradle/Kotlin sources, XML/vector resources, tests and docs. No obvious signing-key files, private datasets, image binaries or bundled ML models appear in the current tree listing; this does **not** establish that the entire Git history or GitHub artifacts are safe to publish.
- Android manifest does not request INTERNET permission, disables app backup and cleartext traffic.
- Current visual analysis is image-quality guidance, **not** a validated spoilage-detection model; preserve conservative safety language.

## Remaining release and F-Droid gates

- [ ] Review **all Git history**, tags, branches, pull-request attachments, Actions artifacts and secrets exposure. Rotate/revoke any exposed credentials; deleting a file in a later commit is insufficient.
- [ ] Verify that the company owns or has redistribution rights for **every** source file, icon, font, translation, dataset and other asset; record provenance and required notices.
- [ ] Review transitive dependencies for F-Droid compatibility, nonfree SDKs, tracking and network behavior.
- [ ] Confirm the owner accepts the legal effect of Apache-2.0 for this codebase; add any third-party license notices required by upstream sources.
- [ ] Review Android release versioning and source/build reproducibility, including Gradle distribution, plugin and dependency resolution.
- [ ] Build the **release APK from clean source** and run tests/lint and emulator RTL/English checks. A debug APK or Google Play AAB alone does not establish F-Droid readiness.
- [ ] Install and test on a real Android device; validate scanning, camera/gallery, offline operation, navigation and safety messaging.
- [ ] Prepare F-Droid metadata: app description, icon, screenshots, changelog, source URL and reproducible build recipe.
- [ ] Submit an F-Droid Request for Packaging after the release gates and F-Droid inclusion requirements are satisfied.

GitHub Actions run `44682012101` failed before starting the build job. GitHub reports a billing or spending-limit issue. F-Droid also generally requires three months of active development, subject to its published exceptions. The earliest available commit is dated September 11, 2026.

## CI evidence

- Android CI: https://github.com/nexvary/NEXVARY-FoodGuard/actions/runs/36147413992
- Licensing draft: https://github.com/nexvary/NEXVARY-FoodGuard/pull/6

## Privacy and safety

Never upload private food photographs, training datasets, signing keys or personal information to public Git. Do not claim that a photo proves food is safe. Preserve local-first behavior and the documented uncertainty boundaries.
