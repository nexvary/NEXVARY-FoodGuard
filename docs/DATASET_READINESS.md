# Dataset and ML readiness gate

FoodGuard must not present a machine-learning model as a food-spoilage detector until the following evidence exists for each supported food group.

## Required dataset controls

1. Provenance and redistribution rights are recorded for every dataset.
2. Labels distinguish normal ripening/aging from visible spoilage and from image-quality defects.
3. Training, validation and test sets are separated by source/lot/session where possible to reduce leakage.
4. Class imbalance is documented.
5. Lighting, camera, background and packaging variation are represented.
6. Personally identifying imagery is excluded or handled under an approved privacy process.

## Validation requirements

- Per-class precision, recall and confusion matrices.
- False-negative analysis, with special attention to higher-risk foods.
- Calibration analysis for any confidence value shown to users.
- Out-of-distribution and low-quality image rejection tests.
- Versioned model cards and reproducible evaluation scripts.
- Human food-safety review before user-facing safety wording changes.

## Product boundary

Even a validated visual model cannot detect every microbiological or chemical hazard. A future model may support visual comparison and triage; it must not replace storage history, recall information, packaging integrity checks or professional/public-health guidance.

Stage 550 therefore keeps the current image engine limited to capture-quality signals and does not label it as a spoilage classifier.
