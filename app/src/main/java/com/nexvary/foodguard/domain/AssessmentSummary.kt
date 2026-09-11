package com.nexvary.foodguard.domain

import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.SafetyAssessment

object AssessmentSummary {
    fun plainText(
        food: FoodItem?,
        assessment: SafetyAssessment,
        imageNeedsRetake: Boolean? = null
    ): String = buildString {
        appendLine("NEXVARY FoodGuard")
        appendLine("Food: ${food?.localizedName() ?: "Unknown"}")
        appendLine("Verdict: ${assessment.verdict.name.replace('_', ' ')}")
        appendLine("Red flags: ${assessment.redFlagCount}")
        appendLine("Uncertainty: ${assessment.uncertaintyCount}")
        if (imageNeedsRetake != null) {
            appendLine("Photo quality: ${if (imageNeedsRetake) "retake recommended" else "usable for visual comparison"}")
        }
        if (assessment.reasons.isNotEmpty()) {
            appendLine("Reasons:")
            assessment.reasons.forEach { appendLine("- $it") }
        }
        append("Decision support only; this does not prove microbiological safety.")
    }
}
