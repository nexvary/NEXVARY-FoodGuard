package com.nexvary.foodguard

import com.nexvary.foodguard.data.FoodCatalog
import com.nexvary.foodguard.model.FoodCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FoodCatalogTest {
    @Test
    fun catalogHasTwoHundredTwentyUniqueItems() {
        assertEquals(220, FoodCatalog.items.size)
        assertEquals(220, FoodCatalog.items.map { it.id }.toSet().size)
    }

    @Test
    fun allCategoriesHaveExpandedContent() {
        FoodCategory.entries.forEach { category ->
            assertTrue("Missing category: $category", FoodCatalog.items.any { it.category == category })
        }
    }

    @Test
    fun everyCatalogItemHasSevenLocalizedNames() {
        FoodCatalog.items.forEach { item ->
            val names = listOf(item.name.en, item.name.ar, item.name.tr, item.name.fr, item.name.es, item.name.de, item.name.it)
            assertTrue("Missing localized name for ${item.id}", names.all { it.isNotBlank() })
        }
    }

    @Test
    fun newestHundredItemsExposeHealthyAndSpoilageReferenceSlots() {
        FoodCatalog.items.takeLast(100).forEach { item ->
            assertEquals("Reference slots missing for ${item.id}", 2, item.referenceImages.size)
        }
    }

    @Test
    fun egyptianMangoVarietiesAreSearchable() {
        val ids = FoodCatalog.search("mango").map { it.id }.toSet()
        assertTrue(ids.contains("owaisi_mango"))
        assertTrue(ids.contains("naomi_mango"))
        assertTrue(ids.contains("keitt_mango"))
        assertTrue(ids.contains("zebda_mango"))
        assertTrue(ids.contains("timour_mango"))
        assertTrue(ids.contains("sukkari_mango"))
    }

    @Test
    fun arabicAliasesFindRegionalFoods() {
        assertTrue(FoodCatalog.search("ملوخية").any { it.id == "molokhia" })
        assertTrue(FoodCatalog.search("كابوريا").any { it.id == "crab" })
        assertTrue(FoodCatalog.search("بتلو").any { it.id == "veal" })
        assertTrue(FoodCatalog.search("قصب").any { it.id == "sugarcane_juice" })
        assertTrue(FoodCatalog.search("شاورما").any { it.id == "shawarma" })
        assertTrue(FoodCatalog.search("جندوفلي").any { it.id == "clams" })
    }

    @Test
    fun owaisiMangoIsFeaturedReferenceItem() {
        val mango = FoodCatalog.byId("owaisi_mango")
        assertNotNull(mango)
        assertTrue(mango!!.featured)
        assertTrue(mango.spoilageSigns.isNotEmpty())
        assertTrue(mango.normalSigns.isNotEmpty())
    }
}
