package com.nexvary.foodguard.model

import java.util.Locale

enum class FoodCategory {
    FRUIT,
    VEGETABLE,
    MEAT,
    POULTRY,
    SEAFOOD,
    DAIRY,
    BAKERY,
    PREPARED,
    DRINK,
    PACKAGED
}

enum class RiskTier {
    LOW,
    MEDIUM,
    HIGH
}

data class LocalizedLabel(
    val en: String,
    val ar: String,
    val tr: String,
    val fr: String,
    val es: String,
    val de: String,
    val it: String
) {
    fun resolve(language: String = Locale.getDefault().language): String = when (language.lowercase(Locale.ROOT)) {
        "ar" -> ar
        "tr" -> tr
        "fr" -> fr
        "es" -> es
        "de" -> de
        "it" -> it
        else -> en
    }
}

enum class FoodReferenceState {
    HEALTHY,
    RIPE,
    OVERRIPE,
    SPOILAGE
}

data class FoodReferenceImage(
    val assetKey: String,
    val state: FoodReferenceState,
    val caption: String
)

data class FoodItem(
    val id: String,
    val category: FoodCategory,
    val name: LocalizedLabel,
    val aliases: List<String> = emptyList(),
    val normalSigns: List<String>,
    val spoilageSigns: List<String>,
    val storageTips: List<String>,
    val riskTier: RiskTier,
    val featured: Boolean = false,
    val referenceImages: List<FoodReferenceImage> = emptyList()
) {
    fun localizedName(language: String = Locale.getDefault().language): String = name.resolve(language)
}

enum class SafetyVerdict {
    NO_VISIBLE_RED_FLAGS,
    CAUTION,
    DISCARD,
    INSUFFICIENT_INFORMATION
}

enum class SafetyReasonCode {
    PACKAGE_COMPROMISED,
    VISIBLE_MOLD,
    SLIME_OR_STICKY_FILM,
    ABNORMAL_ODOR,
    UNSAFE_TIME_TEMPERATURE,
    UNKNOWN_STORAGE_HISTORY,
    NO_REPORTED_RED_FLAGS,
    NOT_PROOF_OF_MICROBIOLOGICAL_SAFETY
}

data class ManualSafetyCheck(
    val visibleMold: Boolean = false,
    val slimeOrStickyFilm: Boolean = false,
    val fermentedOrRottenOdor: Boolean = false,
    val leakingOrBulgingPackage: Boolean = false,
    val unsafeTimeTemperatureHistory: Boolean = false,
    val unknownStorageHistory: Boolean = false
) {
    fun redFlagCount(): Int = listOf(
        visibleMold,
        slimeOrStickyFilm,
        fermentedOrRottenOdor,
        leakingOrBulgingPackage,
        unsafeTimeTemperatureHistory
    ).count { it }

    fun uncertaintyCount(): Int = if (unknownStorageHistory) 1 else 0
}

data class SafetyAssessment(
    val verdict: SafetyVerdict,
    val reasons: List<String>,
    val reasonCodes: Set<SafetyReasonCode> = emptySet(),
    val redFlagCount: Int = 0,
    val uncertaintyCount: Int = 0
)
