# Safety Content Policy

FoodGuard content must be conservative, sourceable and explicit about uncertainty.

## Evidence hierarchy

Prefer food-safety guidance from national public-health and food-regulatory authorities, international public-health organizations and peer-reviewed literature. Community anecdotes may help identify usability questions but must not become safety rules without authoritative support.

## Visual-analysis boundary

A photo can help with visible quality defects such as mold, discoloration, physical damage and texture cues. It cannot reliably exclude pathogens, toxins or unsafe time-temperature exposure. Therefore:

- Never label an item "100% safe" from an image.
- Distinguish photo quality from spoilage classification.
- Keep storage history and user-reported smell/texture separate from image features.
- High-risk foods receive more conservative guidance.
- If a future ML model is added, publish per-class validation metrics and known failure modes.

## Content review checklist

Before adding or changing a food-specific rule:

1. Record the authoritative source and review date.
2. Separate quality guidance from safety guidance.
3. Avoid unsafe "taste to test" advice.
4. Check regional differences in storage practices and product types.
5. Translate the safety meaning, not only the literal wording.
6. Review Arabic RTL presentation and long LTR translations.
7. Add or update a regression test when the rule can affect a discard/caution verdict.

## Stage-200 content status

The current seed catalog is an engineering and UX foundation. It is not yet the final reviewed food-science corpus. The scanner's image engine evaluates image quality only and is intentionally not marketed as a trained spoilage classifier.
