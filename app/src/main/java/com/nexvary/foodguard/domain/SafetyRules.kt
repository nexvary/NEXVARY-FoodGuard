package com.nexvary.foodguard.domain

import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.RiskTier
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyReasonCode
import com.nexvary.foodguard.model.SafetyVerdict

object SafetyRules {
    fun evaluate(food: FoodItem?, check: ManualSafetyCheck): SafetyAssessment {
        val reasons = mutableListOf<String>()
        val codes = linkedSetOf<SafetyReasonCode>()

        if (check.leakingOrBulgingPackage) {
            reasons += "Package is leaking, swollen, or otherwise compromised."
            codes += SafetyReasonCode.PACKAGE_COMPROMISED
        }
        if (check.visibleMold) {
            reasons += "Visible mold is present."
            codes += SafetyReasonCode.VISIBLE_MOLD
        }
        if (check.slimeOrStickyFilm) {
            reasons += "Unexpected slime or sticky film is present."
            codes += SafetyReasonCode.SLIME_OR_STICKY_FILM
        }
        if (check.fermentedOrRottenOdor) {
            reasons += "A fermented, rotten, or strongly abnormal odor is reported."
            codes += SafetyReasonCode.ABNORMAL_ODOR
        }
        if (check.unsafeTimeTemperatureHistory) {
            reasons += "The time-temperature history is unsafe."
            codes += SafetyReasonCode.UNSAFE_TIME_TEMPERATURE
        }
        if (check.unknownStorageHistory) {
            codes += SafetyReasonCode.UNKNOWN_STORAGE_HISTORY
        }

        val redFlags = check.redFlagCount()
        val uncertainty = check.uncertaintyCount()

        if (
            check.leakingOrBulgingPackage ||
            check.slimeOrStickyFilm ||
            check.fermentedOrRottenOdor ||
            check.unsafeTimeTemperatureHistory
        ) {
            return SafetyAssessment(
                verdict = SafetyVerdict.DISCARD,
                reasons = reasons,
                reasonCodes = codes,
                redFlagCount = redFlags,
                uncertaintyCount = uncertainty
            )
        }

        if (check.visibleMold) {
            val verdict = when (food?.category) {
                FoodCategory.BAKERY,
                FoodCategory.PREPARED,
                FoodCategory.MEAT,
                FoodCategory.POULTRY,
                FoodCategory.SEAFOOD,
                FoodCategory.DAIRY -> SafetyVerdict.DISCARD
                else -> SafetyVerdict.CAUTION
            }
            return SafetyAssessment(
                verdict = verdict,
                reasons = reasons,
                reasonCodes = codes,
                redFlagCount = redFlags,
                uncertaintyCount = uncertainty
            )
        }

        if (check.unknownStorageHistory) {
            reasons += "Storage history is unknown."
            return SafetyAssessment(
                verdict = if (food?.riskTier == RiskTier.HIGH) SafetyVerdict.CAUTION else SafetyVerdict.INSUFFICIENT_INFORMATION,
                reasons = reasons,
                reasonCodes = codes,
                redFlagCount = redFlags,
                uncertaintyCount = uncertainty
            )
        }

        reasons += "No red flags were reported in the manual checklist."
        reasons += "This does not prove microbiological safety."
        codes += SafetyReasonCode.NO_REPORTED_RED_FLAGS
        codes += SafetyReasonCode.NOT_PROOF_OF_MICROBIOLOGICAL_SAFETY
        return SafetyAssessment(
            verdict = SafetyVerdict.NO_VISIBLE_RED_FLAGS,
            reasons = reasons,
            reasonCodes = codes,
            redFlagCount = 0,
            uncertaintyCount = 0
        )
    }
}
