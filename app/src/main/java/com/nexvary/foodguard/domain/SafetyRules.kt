package com.nexvary.foodguard.domain

import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.RiskTier
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyVerdict

object SafetyRules {
    fun evaluate(food: FoodItem?, check: ManualSafetyCheck): SafetyAssessment {
        val reasons = mutableListOf<String>()

        if (check.leakingOrBulgingPackage) reasons += "Package is leaking, swollen, or otherwise compromised."
        if (check.visibleMold) reasons += "Visible mold is present."
        if (check.slimeOrStickyFilm) reasons += "Unexpected slime or sticky film is present."
        if (check.fermentedOrRottenOdor) reasons += "A fermented, rotten, or strongly abnormal odor is reported."
        if (check.unsafeTimeTemperatureHistory) reasons += "The time-temperature history is unsafe."

        if (
            check.leakingOrBulgingPackage ||
            check.slimeOrStickyFilm ||
            check.fermentedOrRottenOdor ||
            check.unsafeTimeTemperatureHistory
        ) {
            return SafetyAssessment(SafetyVerdict.DISCARD, reasons)
        }

        if (check.visibleMold) {
            val verdict = when (food?.category) {
                com.nexvary.foodguard.model.FoodCategory.BAKERY,
                com.nexvary.foodguard.model.FoodCategory.PREPARED,
                com.nexvary.foodguard.model.FoodCategory.MEAT,
                com.nexvary.foodguard.model.FoodCategory.POULTRY,
                com.nexvary.foodguard.model.FoodCategory.SEAFOOD,
                com.nexvary.foodguard.model.FoodCategory.DAIRY -> SafetyVerdict.DISCARD
                else -> SafetyVerdict.CAUTION
            }
            return SafetyAssessment(verdict, reasons)
        }

        if (check.unknownStorageHistory) {
            reasons += "Storage history is unknown."
            return SafetyAssessment(
                if (food?.riskTier == RiskTier.HIGH) SafetyVerdict.CAUTION else SafetyVerdict.INSUFFICIENT_INFORMATION,
                reasons
            )
        }

        reasons += "No red flags were reported in the manual checklist."
        reasons += "This does not prove microbiological safety."
        return SafetyAssessment(SafetyVerdict.NO_VISIBLE_RED_FLAGS, reasons)
    }
}
