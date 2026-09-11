package com.nexvary.foodguard.storage

import android.content.Context
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.SafetyAssessment
import com.nexvary.foodguard.model.SafetyVerdict
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS_NAME = "foodguard_history"
private const val HISTORY_KEY = "records"
private const val MAX_RECORDS = 100

data class ScanRecord(
    val id: String,
    val timestampEpochMs: Long,
    val foodId: String,
    val foodNameSnapshot: String,
    val verdict: SafetyVerdict,
    val redFlagCount: Int,
    val uncertaintyCount: Int,
    val imageNeedsRetake: Boolean?
)

class ScanHistoryStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<ScanRecord> {
        val raw = prefs.getString(HISTORY_KEY, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val verdict = runCatching {
                        SafetyVerdict.valueOf(item.optString("verdict"))
                    }.getOrNull() ?: continue
                    add(
                        ScanRecord(
                            id = item.optString("id"),
                            timestampEpochMs = item.optLong("time"),
                            foodId = item.optString("foodId"),
                            foodNameSnapshot = item.optString("foodName"),
                            verdict = verdict,
                            redFlagCount = item.optInt("redFlags"),
                            uncertaintyCount = item.optInt("uncertainty"),
                            imageNeedsRetake = if (item.has("retake") && !item.isNull("retake")) item.optBoolean("retake") else null
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    fun append(record: ScanRecord): List<ScanRecord> {
        val updated = (listOf(record) + load())
            .distinctBy { it.id }
            .take(MAX_RECORDS)
        persist(updated)
        return updated
    }

    fun clear() {
        prefs.edit().remove(HISTORY_KEY).apply()
    }

    private fun persist(records: List<ScanRecord>) {
        val array = JSONArray()
        records.forEach { record ->
            val json = JSONObject()
                .put("id", record.id)
                .put("time", record.timestampEpochMs)
                .put("foodId", record.foodId)
                .put("foodName", record.foodNameSnapshot)
                .put("verdict", record.verdict.name)
                .put("redFlags", record.redFlagCount)
                .put("uncertainty", record.uncertaintyCount)
            if (record.imageNeedsRetake == null) json.put("retake", JSONObject.NULL)
            else json.put("retake", record.imageNeedsRetake)
            array.put(json)
        }
        prefs.edit().putString(HISTORY_KEY, array.toString()).apply()
    }

    companion object {
        fun createRecord(
            food: FoodItem?,
            assessment: SafetyAssessment,
            imageNeedsRetake: Boolean?,
            timestampEpochMs: Long = System.currentTimeMillis()
        ): ScanRecord {
            val foodId = food?.id.orEmpty()
            return ScanRecord(
                id = "$timestampEpochMs:$foodId:${assessment.verdict.name}:${assessment.redFlagCount}:${assessment.uncertaintyCount}",
                timestampEpochMs = timestampEpochMs,
                foodId = foodId,
                foodNameSnapshot = food?.localizedName() ?: "Unknown",
                verdict = assessment.verdict,
                redFlagCount = assessment.redFlagCount,
                uncertaintyCount = assessment.uncertaintyCount,
                imageNeedsRetake = imageNeedsRetake
            )
        }
    }
}
