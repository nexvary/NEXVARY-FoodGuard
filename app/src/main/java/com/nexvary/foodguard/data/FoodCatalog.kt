package com.nexvary.foodguard.data

import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.LocalizedLabel
import com.nexvary.foodguard.model.RiskTier

object FoodCatalog {
    private fun label(en: String, ar: String, tr: String, fr: String, es: String, de: String, it: String) =
        LocalizedLabel(en, ar, tr, fr, es, de, it)

    private fun defaultNormal(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("Fresh aroma appropriate to the fruit", "Texture matches the expected ripeness stage", "No active mold or leaking decay")
        FoodCategory.VEGETABLE -> listOf("Firm or naturally crisp texture", "Fresh plant aroma", "No slime, active mold, or wet rot")
        FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("Package intact and within safe storage history", "Surface is moist but not slimy", "No strong rotten or sulfur-like odor")
        FoodCategory.SEAFOOD -> listOf("Clean sea-like smell rather than strong ammonia", "Flesh is resilient rather than mushy", "No heavy slime or advanced discoloration")
        FoodCategory.DAIRY -> listOf("Packaging is intact", "Smell and texture are typical for the product", "No unexpected mold, gas, or separation associated with spoilage")
        FoodCategory.BAKERY -> listOf("Dry or soft texture appropriate to the product", "No visible mold growth", "No musty or fermented odor unless expected by the recipe")
        FoodCategory.PREPARED -> listOf("Known safe time and temperature history", "No unexpected slime, gas, or sour odor", "Reheated and chilled according to the dish")
        FoodCategory.DRINK -> listOf("Seal is intact", "Color and aroma match the product", "No unexpected gas, swelling, or fermentation")
        FoodCategory.PACKAGED -> listOf("Container and seal are undamaged", "No swelling, leaking, or severe seam damage", "Product remains within safe storage conditions")
    }

    private fun defaultSpoilage(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("Visible mold", "Widespread brown or black mushy tissue", "Slime, leaking fluid, or alcoholic/rotten odor")
        FoodCategory.VEGETABLE -> listOf("Slime or wet rot", "Visible fuzzy mold", "Strong rotten odor or widespread collapse")
        FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("Persistent slime or sticky film", "Strong rotten or sulfur-like odor", "Swollen package or unsafe storage history")
        FoodCategory.SEAFOOD -> listOf("Strong ammonia or rotten odor", "Heavy slime", "Mushy flesh or major abnormal discoloration")
        FoodCategory.DAIRY -> listOf("Unexpected mold for the product", "Bulging packaging or gas", "Strong sour/rotten odor or abnormal curdling")
        FoodCategory.BAKERY -> listOf("Any visible mold on porous bread or cake", "Musty odor", "Wet, sticky, or unusually fermented patches")
        FoodCategory.PREPARED -> listOf("Unsafe time-temperature history", "Unexpected sour/rotten odor", "Slime, gas, mold, or package swelling")
        FoodCategory.DRINK -> listOf("Unexpected fermentation or gas", "Bulging/leaking container", "Mold, unusual sediment, or strong off-odor")
        FoodCategory.PACKAGED -> listOf("Bulging or leaking package", "Broken seal or damaged seam", "Mold, gas, or strong off-odor after opening")
    }

    private fun defaultStorage(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("Follow the fruit's ripening needs", "Refrigerate cut fruit promptly in a clean covered container")
        FoodCategory.VEGETABLE -> listOf("Keep dry produce dry and chilled when appropriate", "Refrigerate cut vegetables promptly")
        FoodCategory.MEAT, FoodCategory.POULTRY, FoodCategory.SEAFOOD -> listOf("Keep refrigerated or frozen without breaking the cold chain", "Use a clean sealed container and avoid cross-contamination")
        FoodCategory.DAIRY -> listOf("Keep refrigerated unless the label explicitly says otherwise", "Close the package and avoid prolonged room-temperature exposure")
        FoodCategory.BAKERY -> listOf("Keep dry and protected from humidity", "Freeze portions if longer storage is needed")
        FoodCategory.PREPARED -> listOf("Cool leftovers promptly and refrigerate", "Reheat only the portion needed and avoid repeated warm-cool cycles")
        FoodCategory.DRINK -> listOf("Follow label refrigeration instructions after opening", "Keep caps and pouring surfaces clean")
        FoodCategory.PACKAGED -> listOf("Store as directed on the label", "After opening, follow refrigeration and use-by guidance")
    }

    private fun item(
        id: String,
        category: FoodCategory,
        name: LocalizedLabel,
        risk: RiskTier,
        aliases: List<String> = emptyList(),
        normal: List<String> = defaultNormal(category),
        spoilage: List<String> = defaultSpoilage(category),
        storage: List<String> = defaultStorage(category),
        featured: Boolean = false
    ) = FoodItem(id, category, name, aliases, normal, spoilage, storage, risk, featured)

    private val seedItems: List<FoodItem> = listOf(
        item(
            id = "owaisi_mango",
            category = FoodCategory.FRUIT,
            name = label("Owaisi mango", "مانجو عويسي", "Owaisi mango", "Mangue Owaisi", "Mango Owaisi", "Owaisi-Mango", "Mango Owaisi"),
            risk = RiskTier.LOW,
            aliases = listOf("mango", "عويسي", "mangue"),
            normal = listOf("Healthy flesh is commonly yellow to deep orange", "A sweet mango aroma is expected", "Soft but coherent flesh can be fully ripe; a darker orange center alone does not prove spoilage"),
            spoilage = listOf("Black or brown mushy tissue spreading through the flesh", "Slime, leaking fluid, visible mold, or hollow rotten areas", "Sharp fermented/alcoholic or rotten odor"),
            storage = listOf("Ripen whole fruit at room temperature", "Refrigerate once ripe", "Keep cut mango chilled in a clean sealed container"),
            featured = true
        ),
        item("banana", FoodCategory.FRUIT, label("Banana", "موز", "Muz", "Banane", "Plátano", "Banane", "Banana"), RiskTier.LOW),
        item("apple", FoodCategory.FRUIT, label("Apple", "تفاح", "Elma", "Pomme", "Manzana", "Apfel", "Mela"), RiskTier.LOW),
        item("orange", FoodCategory.FRUIT, label("Orange", "برتقال", "Portakal", "Orange", "Naranja", "Orange", "Arancia"), RiskTier.LOW),
        item("strawberry", FoodCategory.FRUIT, label("Strawberry", "فراولة", "Çilek", "Fraise", "Fresa", "Erdbeere", "Fragola"), RiskTier.LOW),
        item("grapes", FoodCategory.FRUIT, label("Grapes", "عنب", "Üzüm", "Raisin", "Uvas", "Trauben", "Uva"), RiskTier.LOW),
        item("watermelon", FoodCategory.FRUIT, label("Watermelon", "بطيخ", "Karpuz", "Pastèque", "Sandía", "Wassermelone", "Anguria"), RiskTier.LOW),
        item("peach", FoodCategory.FRUIT, label("Peach", "خوخ", "Şeftali", "Pêche", "Melocotón", "Pfirsich", "Pesca"), RiskTier.LOW),
        item("avocado", FoodCategory.FRUIT, label("Avocado", "أفوكادو", "Avokado", "Avocat", "Aguacate", "Avocado", "Avocado"), RiskTier.LOW),
        item("guava", FoodCategory.FRUIT, label("Guava", "جوافة", "Guava", "Goyave", "Guayaba", "Guave", "Guava"), RiskTier.LOW),

        item("tomato", FoodCategory.VEGETABLE, label("Tomato", "طماطم", "Domates", "Tomate", "Tomate", "Tomate", "Pomodoro"), RiskTier.LOW),
        item("potato", FoodCategory.VEGETABLE, label("Potato", "بطاطس", "Patates", "Pomme de terre", "Patata", "Kartoffel", "Patata"), RiskTier.LOW,
            spoilage = listOf("Wet rot, slime, mold, or strong rotten odor", "Extensive green skin or heavy sprouting indicates quality/safety concerns and should be assessed cautiously")),
        item("onion", FoodCategory.VEGETABLE, label("Onion", "بصل", "Soğan", "Oignon", "Cebolla", "Zwiebel", "Cipolla"), RiskTier.LOW),
        item("cucumber", FoodCategory.VEGETABLE, label("Cucumber", "خيار", "Salatalık", "Concombre", "Pepino", "Gurke", "Cetriolo"), RiskTier.LOW),
        item("bell_pepper", FoodCategory.VEGETABLE, label("Bell pepper", "فلفل ألوان", "Dolmalık biber", "Poivron", "Pimiento", "Paprika", "Peperone"), RiskTier.LOW),
        item("lettuce", FoodCategory.VEGETABLE, label("Lettuce", "خس", "Marul", "Laitue", "Lechuga", "Salat", "Lattuga"), RiskTier.LOW),
        item("spinach", FoodCategory.VEGETABLE, label("Spinach", "سبانخ", "Ispanak", "Épinards", "Espinaca", "Spinat", "Spinaci"), RiskTier.LOW),
        item("carrot", FoodCategory.VEGETABLE, label("Carrot", "جزر", "Havuç", "Carotte", "Zanahoria", "Karotte", "Carota"), RiskTier.LOW),
        item("zucchini", FoodCategory.VEGETABLE, label("Zucchini", "كوسة", "Kabak", "Courgette", "Calabacín", "Zucchini", "Zucchina"), RiskTier.LOW),
        item("eggplant", FoodCategory.VEGETABLE, label("Eggplant", "باذنجان", "Patlıcan", "Aubergine", "Berenjena", "Aubergine", "Melanzana"), RiskTier.LOW),

        item("beef", FoodCategory.MEAT, label("Beef", "لحم بقري", "Sığır eti", "Bœuf", "Carne de res", "Rindfleisch", "Manzo"), RiskTier.HIGH),
        item("ground_beef", FoodCategory.MEAT, label("Ground beef", "لحم بقري مفروم", "Kıyma", "Bœuf haché", "Carne molida", "Hackfleisch", "Manzo macinato"), RiskTier.HIGH),
        item("lamb", FoodCategory.MEAT, label("Lamb", "لحم ضأن", "Kuzu eti", "Agneau", "Cordero", "Lamm", "Agnello"), RiskTier.HIGH),
        item("liver", FoodCategory.MEAT, label("Liver", "كبدة", "Karaciğer", "Foie", "Hígado", "Leber", "Fegato"), RiskTier.HIGH),
        item("sausage", FoodCategory.MEAT, label("Sausage", "سجق", "Sosis", "Saucisse", "Salchicha", "Wurst", "Salsiccia"), RiskTier.HIGH),

        item("chicken", FoodCategory.POULTRY, label("Whole chicken", "دجاج كامل", "Bütün tavuk", "Poulet entier", "Pollo entero", "Ganzes Hähnchen", "Pollo intero"), RiskTier.HIGH),
        item("chicken_breast", FoodCategory.POULTRY, label("Chicken breast", "صدور دجاج", "Tavuk göğsü", "Blanc de poulet", "Pechuga de pollo", "Hähnchenbrust", "Petto di pollo"), RiskTier.HIGH),
        item("turkey", FoodCategory.POULTRY, label("Turkey", "ديك رومي", "Hindi", "Dinde", "Pavo", "Pute", "Tacchino"), RiskTier.HIGH),
        item("duck", FoodCategory.POULTRY, label("Duck", "بط", "Ördek", "Canard", "Pato", "Ente", "Anatra"), RiskTier.HIGH),

        item("tilapia", FoodCategory.SEAFOOD, label("Tilapia", "بلطي", "Tilapya", "Tilapia", "Tilapia", "Tilapia", "Tilapia"), RiskTier.HIGH),
        item("mullet", FoodCategory.SEAFOOD, label("Mullet", "بوري", "Kefal", "Mulet", "Lisa", "Meeräsche", "Cefalo"), RiskTier.HIGH),
        item("salmon", FoodCategory.SEAFOOD, label("Salmon", "سلمون", "Somon", "Saumon", "Salmón", "Lachs", "Salmone"), RiskTier.HIGH),
        item("shrimp", FoodCategory.SEAFOOD, label("Shrimp", "جمبري", "Karides", "Crevette", "Camarón", "Garnele", "Gambero"), RiskTier.HIGH),
        item("squid", FoodCategory.SEAFOOD, label("Squid", "حبار", "Kalamar", "Calmar", "Calamar", "Tintenfisch", "Calamaro"), RiskTier.HIGH),

        item("milk", FoodCategory.DAIRY, label("Milk", "لبن", "Süt", "Lait", "Leche", "Milch", "Latte"), RiskTier.HIGH),
        item("yogurt", FoodCategory.DAIRY, label("Yogurt", "زبادي", "Yoğurt", "Yaourt", "Yogur", "Joghurt", "Yogurt"), RiskTier.MEDIUM),
        item("soft_cheese", FoodCategory.DAIRY, label("Soft cheese", "جبنة طرية", "Yumuşak peynir", "Fromage à pâte molle", "Queso blando", "Weichkäse", "Formaggio morbido"), RiskTier.HIGH),
        item("hard_cheese", FoodCategory.DAIRY, label("Hard cheese", "جبنة صلبة", "Sert peynir", "Fromage à pâte dure", "Queso duro", "Hartkäse", "Formaggio duro"), RiskTier.MEDIUM),

        item("bread", FoodCategory.BAKERY, label("Bread", "خبز", "Ekmek", "Pain", "Pan", "Brot", "Pane"), RiskTier.LOW,
            spoilage = listOf("Any visible mold on porous bread", "Musty smell or damp sticky patches"),
            storage = listOf("Keep dry and protected from humidity", "If mold appears on porous bread, discard the whole affected loaf rather than trimming the spot")),
        item("pita", FoodCategory.BAKERY, label("Pita / baladi bread", "عيش بلدي", "Pide ekmeği", "Pain pita", "Pan pita", "Fladenbrot", "Pane pita"), RiskTier.LOW),
        item("cake", FoodCategory.BAKERY, label("Cake", "كيك", "Kek", "Gâteau", "Pastel", "Kuchen", "Torta"), RiskTier.MEDIUM),
        item("croissant", FoodCategory.BAKERY, label("Croissant", "كرواسون", "Kruvasan", "Croissant", "Croissant", "Croissant", "Cornetto"), RiskTier.LOW),

        item("cooked_rice", FoodCategory.PREPARED, label("Cooked rice", "أرز مطبوخ", "Pişmiş pirinç", "Riz cuit", "Arroz cocido", "Gekochter Reis", "Riso cotto"), RiskTier.HIGH,
            normal = listOf("Known safe time-temperature history is more important than appearance", "No sour odor, slime, or unexpected moisture"),
            spoilage = listOf("Unsafe room-temperature exposure", "Sour or unusual odor, slime, or visible mold"),
            storage = listOf("Cool promptly after cooking", "Refrigerate in a covered container and avoid prolonged room-temperature holding")),
        item("cooked_pasta", FoodCategory.PREPARED, label("Cooked pasta", "مكرونة مطبوخة", "Pişmiş makarna", "Pâtes cuites", "Pasta cocida", "Gekochte Nudeln", "Pasta cotta"), RiskTier.MEDIUM),
        item("soup", FoodCategory.PREPARED, label("Soup", "شوربة", "Çorba", "Soupe", "Sopa", "Suppe", "Zuppa"), RiskTier.HIGH),
        item("cooked_chicken", FoodCategory.PREPARED, label("Cooked chicken", "دجاج مطبوخ", "Pişmiş tavuk", "Poulet cuit", "Pollo cocido", "Gekochtes Hähnchen", "Pollo cotto"), RiskTier.HIGH),

        item("fresh_juice", FoodCategory.DRINK, label("Fresh juice", "عصير طازج", "Taze meyve suyu", "Jus frais", "Jugo fresco", "Frischer Saft", "Succo fresco"), RiskTier.MEDIUM),
        item("bottled_juice", FoodCategory.DRINK, label("Bottled juice", "عصير معبأ", "Şişelenmiş meyve suyu", "Jus en bouteille", "Jugo embotellado", "Saft in Flasche", "Succo confezionato"), RiskTier.MEDIUM),

        item("canned_food", FoodCategory.PACKAGED, label("Canned food", "معلبات", "Konserve gıda", "Conserve", "Alimento enlatado", "Konserve", "Cibo in scatola"), RiskTier.HIGH,
            normal = listOf("Can is flat, sealed, and free of leaks", "No severe seam damage or unusual pressure"),
            spoilage = listOf("Bulging, leaking, spurting, or badly damaged can", "Broken seam, severe rust at seams, or strong off-odor after opening"),
            storage = listOf("Store unopened cans as directed", "Discard a bulging or leaking can without tasting the contents")),
        item("deli_meat", FoodCategory.PACKAGED, label("Deli meat", "لحوم باردة", "Şarküteri eti", "Charcuterie", "Fiambre", "Aufschnitt", "Affettato"), RiskTier.HIGH)
    )

    val items: List<FoodItem> = seedItems + FoodCatalogExpansion.items + FoodCatalogExpansion2.items

    init {
        check(seedItems.size == 50) { "FoodGuard base catalog must contain exactly 50 seed items" }
        check(FoodCatalogExpansion.items.size == 70) { "FoodGuard first catalog expansion must contain exactly 70 items" }
        check(FoodCatalogExpansion2.items.size == 100) { "FoodGuard second catalog expansion must contain exactly 100 items" }
        check(items.size == 220) { "FoodGuard catalog must contain exactly 220 items" }
        check(items.map { it.id }.toSet().size == items.size) { "Food catalog IDs must be unique" }
    }

    fun byId(id: String): FoodItem? = items.firstOrNull { it.id == id }

    fun search(query: String): List<FoodItem> {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) return items
        return items.filter { item ->
            item.name.en.lowercase().contains(normalized) ||
                item.name.ar.contains(query.trim()) ||
                item.name.tr.lowercase().contains(normalized) ||
                item.name.fr.lowercase().contains(normalized) ||
                item.name.es.lowercase().contains(normalized) ||
                item.name.de.lowercase().contains(normalized) ||
                item.name.it.lowercase().contains(normalized) ||
                item.aliases.any { it.lowercase().contains(normalized) }
        }
    }
}
