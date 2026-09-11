package com.nexvary.foodguard

import com.nexvary.foodguard.data.FoodCatalog
import com.nexvary.foodguard.domain.AssessmentSummary
import com.nexvary.foodguard.domain.SafetyRules
import com.nexvary.foodguard.model.ManualSafetyCheck
import org.junit.Assert.assertTrue
import org.junit.Test

class AssessmentSummaryTest {
    @Test
    fun summaryKeepsSafetyBoundary() {
        val food = FoodCatalog.byId("owaisi_mango")
        val assessment = SafetyRules.evaluate(food, ManualSafetyCheck())
        val text = AssessmentSummary.plainText(food, assessment, false)
        assertTrue(text.contains("does not prove microbiological safety", ignoreCase = true))
    }

    @Test
    fun structuredCountsAreReported() {
        val food = FoodCatalog.byId("canned_food")
        val assessment = SafetyRules.evaluate(food, ManualSafetyCheck(leakingOrBulgingPackage = true))
        val text = AssessmentSummary.plainText(food, assessment, true)
        assertTrue(text.contains("Red flags: 1"))
        assertTrue(text.contains("retake recommended"))
    }
}
