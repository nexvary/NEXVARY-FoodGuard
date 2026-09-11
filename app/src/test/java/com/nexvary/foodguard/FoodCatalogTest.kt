package com.nexvary.foodguard

import com.nexvary.foodguard.data.FoodCatalog
import com.nexvary.foodguard.model.FoodCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FoodCatalogTest {
    @Test
    fun seedCatalogHasFiftyUniqueItems() {
        assertEquals(50, FoodCatalog.items.size)
        assertEquals(50, FoodCatalog.items.map { it.id }.toSet().size)
    }

    @Test
    fun allCategoriesHaveSeedContent() {
        FoodCategory.entries.forEach { category ->
            assertTrue("Missing category: $category", FoodCatalog.items.any { it.category == category })
        }
    }

    @Test
    fun everySeedItemHasSevenLocalizedNames() {
        FoodCatalog.items.forEach { item ->
            val names = listOf(item.name.en, item.name.ar, item.name.tr, item.name.fr, item.name.es, item.name.de, item.name.it)
            assertTrue("Missing localized name for ${item.id}", names.all { it.isNotBlank() })
        }
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
