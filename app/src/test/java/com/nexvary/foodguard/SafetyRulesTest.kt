package com.nexvary.foodguard

import com.nexvary.foodguard.data.FoodCatalog
import com.nexvary.foodguard.domain.SafetyRules
import com.nexvary.foodguard.model.ManualSafetyCheck
import com.nexvary.foodguard.model.SafetyVerdict
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SafetyRulesTest {
    @Test
    fun bulgingPackageForcesDiscardVerdict() {
        val result = SafetyRules.evaluate(
            FoodCatalog.byId("canned_food"),
            ManualSafetyCheck(leakingOrBulgingPackage = true)
        )
        assertEquals(SafetyVerdict.DISCARD, result.verdict)
        assertTrue(result.reasons.isNotEmpty())
    }

    @Test
    fun unsafeTemperatureHistoryForHighRiskFoodForcesDiscard() {
        val result = SafetyRules.evaluate(
            FoodCatalog.byId("cooked_chicken"),
            ManualSafetyCheck(unsafeTimeTemperatureHistory = true)
        )
        assertEquals(SafetyVerdict.DISCARD, result.verdict)
    }

    @Test
    fun unknownStorageOnHighRiskFoodReturnsCaution() {
        val result = SafetyRules.evaluate(
            FoodCatalog.byId("ground_beef"),
            ManualSafetyCheck(unknownStorageHistory = true)
        )
        assertEquals(SafetyVerdict.CAUTION, result.verdict)
    }

    @Test
    fun noReportedRedFlagsDoesNotClaimSafety() {
        val result = SafetyRules.evaluate(
            FoodCatalog.byId("owaisi_mango"),
            ManualSafetyCheck()
        )
        assertEquals(SafetyVerdict.NO_VISIBLE_RED_FLAGS, result.verdict)
        assertTrue(result.reasons.any { it.contains("does not prove", ignoreCase = true) })
    }
}
