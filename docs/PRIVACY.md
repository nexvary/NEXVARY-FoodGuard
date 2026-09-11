# Privacy model

NEXVARY FoodGuard is designed as a local-first Android application.

## Stage 550 behavior

- The manifest does not request Internet access.
- Camera capture uses the Android activity result contract and gallery selection uses the system document/content picker.
- Selected food images are analyzed in memory by the local image-quality helper; Stage 550 does not upload them.
- Assessment history is stored in app-private `SharedPreferences` and is capped at 100 records.
- History does not store a copy of the selected food photo.
- Android application backup is disabled and cleartext network traffic is disabled in the manifest.
- Users can clear assessment history from the History screen.

## Sharing

The Share Result action exports only a plain-text assessment summary through Android's chooser. The user chooses the receiving app. The food image itself is not attached by FoodGuard.

## Future ML datasets

Training images, private datasets, user photos and API credentials must not be committed to the public repository. Any future opt-in dataset contribution flow requires explicit consent, retention controls and a separate privacy review before implementation.
