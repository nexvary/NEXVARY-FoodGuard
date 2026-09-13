package com.nexvary.foodguard.data

import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.LocalizedLabel
import com.nexvary.foodguard.model.RiskTier

internal object FoodCatalogExpansion {
    private fun label(en: String, ar: String, tr: String, fr: String, es: String, de: String, it: String) =
        LocalizedLabel(en, ar, tr, fr, es, de, it)

    private fun normal(category: FoodCategory): List<String> = when (category) {
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

    private fun spoilage(category: FoodCategory): List<String> = when (category) {
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

    private fun storage(category: FoodCategory): List<String> = when (category) {
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
        normalSigns: List<String> = normal(category),
        spoilageSigns: List<String> = spoilage(category),
        storageTips: List<String> = storage(category)
    ) = FoodItem(id, category, name, aliases, normalSigns, spoilageSigns, storageTips, risk)

    val items: List<FoodItem> = listOf(
        item("naomi_mango", FoodCategory.FRUIT, label("Naomi mango", "مانجو ناعومي", "Naomi mango", "Mangue Naomi", "Mango Naomi", "Naomi-Mango", "Mango Naomi"), RiskTier.LOW, listOf("mango", "ناعومي")),
        item("keitt_mango", FoodCategory.FRUIT, label("Keitt mango", "مانجو كيت", "Keitt mango", "Mangue Keitt", "Mango Keitt", "Keitt-Mango", "Mango Keitt"), RiskTier.LOW, listOf("mango", "كيت")),
        item("zebda_mango", FoodCategory.FRUIT, label("Zebda mango", "مانجو زبدية", "Zebda mango", "Mangue Zebda", "Mango Zebda", "Zebda-Mango", "Mango Zebda"), RiskTier.LOW, listOf("mango", "زبدية", "zebdia")),
        item("timour_mango", FoodCategory.FRUIT, label("Timour mango", "مانجو تيمور", "Timour mango", "Mangue Timour", "Mango Timour", "Timour-Mango", "Mango Timour"), RiskTier.LOW, listOf("mango", "تيمور")),
        item("sukkari_mango", FoodCategory.FRUIT, label("Sukkari mango", "مانجو سكري", "Sukkari mango", "Mangue Sukkari", "Mango Sukkari", "Sukkari-Mango", "Mango Sukkari"), RiskTier.LOW, listOf("mango", "سكري")),
        item("pomegranate", FoodCategory.FRUIT, label("Pomegranate", "رمان", "Nar", "Grenade", "Granada", "Granatapfel", "Melograno"), RiskTier.LOW, listOf("رمان")),
        item("pear", FoodCategory.FRUIT, label("Pear", "كمثرى", "Armut", "Poire", "Pera", "Birne", "Pera"), RiskTier.LOW, listOf("كمثرى")),
        item("kiwi", FoodCategory.FRUIT, label("Kiwi", "كيوي", "Kivi", "Kiwi", "Kiwi", "Kiwi", "Kiwi"), RiskTier.LOW, listOf("كيوي")),
        item("pineapple", FoodCategory.FRUIT, label("Pineapple", "أناناس", "Ananas", "Ananas", "Piña", "Ananas", "Ananas"), RiskTier.LOW, listOf("اناناس", "أناناس")),
        item("papaya", FoodCategory.FRUIT, label("Papaya", "بابايا", "Papaya", "Papaye", "Papaya", "Papaya", "Papaya"), RiskTier.LOW, listOf("بابايا")),
        item("figs", FoodCategory.FRUIT, label("Figs", "تين", "İncir", "Figues", "Higos", "Feigen", "Fichi"), RiskTier.LOW, listOf("تين")),
        item("dates", FoodCategory.FRUIT, label("Dates", "تمر", "Hurma", "Dattes", "Dátiles", "Datteln", "Datteri"), RiskTier.LOW, listOf("تمر", "بلح")),
        item("plum", FoodCategory.FRUIT, label("Plum", "برقوق", "Erik", "Prune", "Ciruela", "Pflaume", "Prugna"), RiskTier.LOW, listOf("برقوق")),
        item("cherries", FoodCategory.FRUIT, label("Cherries", "كرز", "Kiraz", "Cerises", "Cerezas", "Kirschen", "Ciliegie"), RiskTier.LOW, listOf("كرز")),
        item("cantaloupe", FoodCategory.FRUIT, label("Cantaloupe", "كنتالوب", "Kavun", "Melon cantaloup", "Melón cantalupo", "Cantaloupe-Melone", "Melone cantalupo"), RiskTier.LOW, listOf("كنتالوب", "شمام")),

        item("garlic", FoodCategory.VEGETABLE, label("Garlic", "ثوم", "Sarımsak", "Ail", "Ajo", "Knoblauch", "Aglio"), RiskTier.LOW, listOf("ثوم")),
        item("cabbage", FoodCategory.VEGETABLE, label("Cabbage", "كرنب", "Lahana", "Chou", "Repollo", "Kohl", "Cavolo"), RiskTier.LOW, listOf("كرنب", "ملفوف")),
        item("cauliflower", FoodCategory.VEGETABLE, label("Cauliflower", "قرنبيط", "Karnabahar", "Chou-fleur", "Coliflor", "Blumenkohl", "Cavolfiore"), RiskTier.LOW, listOf("قرنبيط")),
        item("broccoli", FoodCategory.VEGETABLE, label("Broccoli", "بروكلي", "Brokoli", "Brocoli", "Brócoli", "Brokkoli", "Broccoli"), RiskTier.LOW, listOf("بروكلي")),
        item("peas", FoodCategory.VEGETABLE, label("Peas", "بسلة", "Bezelye", "Petits pois", "Guisantes", "Erbsen", "Piselli"), RiskTier.LOW, listOf("بسلة", "بازلاء")),
        item("green_beans", FoodCategory.VEGETABLE, label("Green beans", "فاصوليا خضراء", "Taze fasulye", "Haricots verts", "Judías verdes", "Grüne Bohnen", "Fagiolini"), RiskTier.LOW, listOf("فاصوليا")),
        item("okra", FoodCategory.VEGETABLE, label("Okra", "بامية", "Bamya", "Gombo", "Okra", "Okra", "Gombo"), RiskTier.LOW, listOf("بامية")),
        item("molokhia", FoodCategory.VEGETABLE, label("Molokhia", "ملوخية", "Molokhia", "Molokhia", "Molokhia", "Molokhia", "Molokhia"), RiskTier.LOW, listOf("ملوخية", "jute mallow")),
        item("parsley", FoodCategory.VEGETABLE, label("Parsley", "بقدونس", "Maydanoz", "Persil", "Perejil", "Petersilie", "Prezzemolo"), RiskTier.LOW, listOf("بقدونس")),
        item("cilantro", FoodCategory.VEGETABLE, label("Cilantro", "كزبرة خضراء", "Kişniş", "Coriandre", "Cilantro", "Koriander", "Coriandolo"), RiskTier.LOW, listOf("كزبرة")),
        item("mint", FoodCategory.VEGETABLE, label("Mint", "نعناع", "Nane", "Menthe", "Menta", "Minze", "Menta"), RiskTier.LOW, listOf("نعناع")),
        item("beetroot", FoodCategory.VEGETABLE, label("Beetroot", "بنجر", "Pancar", "Betterave", "Remolacha", "Rote Bete", "Barbabietola"), RiskTier.LOW, listOf("بنجر", "شمندر")),
        item("sweet_potato", FoodCategory.VEGETABLE, label("Sweet potato", "بطاطا حلوة", "Tatlı patates", "Patate douce", "Batata", "Süßkartoffel", "Patata dolce"), RiskTier.LOW, listOf("بطاطا")),
        item("mushrooms", FoodCategory.VEGETABLE, label("Mushrooms", "مشروم", "Mantar", "Champignons", "Champiñones", "Pilze", "Funghi"), RiskTier.MEDIUM, listOf("مشروم", "فطر")),
        item("corn", FoodCategory.VEGETABLE, label("Corn", "ذرة", "Mısır", "Maïs", "Maíz", "Mais", "Mais"), RiskTier.LOW, listOf("ذرة")),

        item("veal", FoodCategory.MEAT, label("Veal", "لحم بتلو", "Dana eti", "Veau", "Ternera", "Kalbfleisch", "Vitello"), RiskTier.HIGH, listOf("بتلو")),
        item("goat_meat", FoodCategory.MEAT, label("Goat meat", "لحم ماعز", "Keçi eti", "Viande de chèvre", "Carne de cabra", "Ziegenfleisch", "Carne di capra"), RiskTier.HIGH, listOf("ماعز")),
        item("camel_meat", FoodCategory.MEAT, label("Camel meat", "لحم جمل", "Deve eti", "Viande de chameau", "Carne de camello", "Kamelfleisch", "Carne di cammello"), RiskTier.HIGH, listOf("جمل")),
        item("beef_steak", FoodCategory.MEAT, label("Beef steak", "ستيك بقري", "Dana biftek", "Steak de bœuf", "Bistec de res", "Rindersteak", "Bistecca di manzo"), RiskTier.HIGH, listOf("steak", "ستيك")),
        item("raw_meatballs", FoodCategory.MEAT, label("Raw meatballs", "كفتة نيئة", "Çiğ köfte eti", "Boulettes crues", "Albóndigas crudas", "Rohe Fleischbällchen", "Polpette crude"), RiskTier.HIGH, listOf("كفتة")),
        item("raw_burger_patty", FoodCategory.MEAT, label("Raw burger patty", "برجر نيئ", "Çiğ burger köftesi", "Steak haché cru", "Hamburguesa cruda", "Rohes Burgerpatty", "Hamburger crudo"), RiskTier.HIGH, listOf("برجر", "burger")),
        item("kidney", FoodCategory.MEAT, label("Kidney", "كلاوي", "Böbrek", "Rognon", "Riñón", "Niere", "Rene"), RiskTier.HIGH, listOf("كلاوي")),
        item("beef_heart", FoodCategory.MEAT, label("Beef heart", "قلب بقري", "Dana yüreği", "Cœur de bœuf", "Corazón de res", "Rinderherz", "Cuore di manzo"), RiskTier.HIGH, listOf("قلب")),

        item("chicken_thigh", FoodCategory.POULTRY, label("Chicken thigh", "وراك دجاج", "Tavuk but", "Cuisse de poulet", "Muslo de pollo", "Hähnchenschenkel", "Coscia di pollo"), RiskTier.HIGH, listOf("وراك", "فخذ دجاج")),
        item("chicken_wings", FoodCategory.POULTRY, label("Chicken wings", "أجنحة دجاج", "Tavuk kanat", "Ailes de poulet", "Alitas de pollo", "Hähnchenflügel", "Ali di pollo"), RiskTier.HIGH, listOf("اجنحة", "أجنحة")),
        item("chicken_liver", FoodCategory.POULTRY, label("Chicken liver", "كبدة دجاج", "Tavuk ciğeri", "Foie de poulet", "Hígado de pollo", "Hähnchenleber", "Fegato di pollo"), RiskTier.HIGH, listOf("كبدة فراخ")),
        item("quail", FoodCategory.POULTRY, label("Quail", "سمان", "Bıldırcın", "Caille", "Codorniz", "Wachtel", "Quaglia"), RiskTier.HIGH, listOf("سمان")),
        item("goose", FoodCategory.POULTRY, label("Goose", "أوز", "Kaz", "Oie", "Ganso", "Gans", "Oca"), RiskTier.HIGH, listOf("اوز", "أوز")),
        item("turkey_breast", FoodCategory.POULTRY, label("Turkey breast", "صدر ديك رومي", "Hindi göğsü", "Blanc de dinde", "Pechuga de pavo", "Putenbrust", "Petto di tacchino"), RiskTier.HIGH, listOf("صدر رومي")),

        item("sardines", FoodCategory.SEAFOOD, label("Sardines", "سردين", "Sardalya", "Sardines", "Sardinas", "Sardinen", "Sardine"), RiskTier.HIGH, listOf("سردين")),
        item("fresh_tuna", FoodCategory.SEAFOOD, label("Fresh tuna", "تونة طازجة", "Taze ton balığı", "Thon frais", "Atún fresco", "Frischer Thunfisch", "Tonno fresco"), RiskTier.HIGH, listOf("تونة")),
        item("mackerel", FoodCategory.SEAFOOD, label("Mackerel", "ماكريل", "Uskumru", "Maquereau", "Caballa", "Makrele", "Sgombro"), RiskTier.HIGH, listOf("ماكريل")),
        item("sea_bass", FoodCategory.SEAFOOD, label("Sea bass", "قاروص", "Levrek", "Bar", "Lubina", "Wolfsbarsch", "Branzino"), RiskTier.HIGH, listOf("قاروص")),
        item("sea_bream", FoodCategory.SEAFOOD, label("Sea bream", "دنيس", "Çipura", "Dorade", "Dorada", "Dorade", "Orata"), RiskTier.HIGH, listOf("دنيس")),
        item("crab", FoodCategory.SEAFOOD, label("Crab", "كابوريا", "Yengeç", "Crabe", "Cangrejo", "Krabbe", "Granchio"), RiskTier.HIGH, listOf("كابوريا", "سلطعون")),
        item("mussels", FoodCategory.SEAFOOD, label("Mussels", "بلح البحر", "Midye", "Moules", "Mejillones", "Muscheln", "Cozze"), RiskTier.HIGH, listOf("بلح البحر")),
        item("clams", FoodCategory.SEAFOOD, label("Clams", "محار", "Kum midyesi", "Palourdes", "Almejas", "Venusmuscheln", "Vongole"), RiskTier.HIGH, listOf("محار")),
        item("octopus", FoodCategory.SEAFOOD, label("Octopus", "أخطبوط", "Ahtapot", "Poulpe", "Pulpo", "Oktopus", "Polpo"), RiskTier.HIGH, listOf("اخطبوط", "أخطبوط")),
        item("fish_fillets", FoodCategory.SEAFOOD, label("Fish fillets", "فيليه سمك", "Balık fileto", "Filets de poisson", "Filetes de pescado", "Fischfilets", "Filetti di pesce"), RiskTier.HIGH, listOf("فيليه")),

        item("cream", FoodCategory.DAIRY, label("Cream", "قشطة", "Krema", "Crème", "Crema", "Sahne", "Panna"), RiskTier.HIGH, listOf("قشطة", "كريمة")),
        item("butter", FoodCategory.DAIRY, label("Butter", "زبدة", "Tereyağı", "Beurre", "Mantequilla", "Butter", "Burro"), RiskTier.MEDIUM, listOf("زبدة")),
        item("cottage_cheese", FoodCategory.DAIRY, label("Cottage cheese", "جبنة قريش", "Lor peyniri", "Fromage cottage", "Queso cottage", "Hüttenkäse", "Fiocchi di latte"), RiskTier.HIGH, listOf("قريش")),
        item("feta_cheese", FoodCategory.DAIRY, label("Feta cheese", "جبنة فيتا", "Beyaz peynir", "Feta", "Queso feta", "Feta", "Feta"), RiskTier.HIGH, listOf("فيتا")),
        item("mozzarella", FoodCategory.DAIRY, label("Mozzarella", "موزاريلا", "Mozzarella", "Mozzarella", "Mozzarella", "Mozzarella", "Mozzarella"), RiskTier.HIGH, listOf("موزاريلا")),
        item("ice_cream", FoodCategory.DAIRY, label("Ice cream", "آيس كريم", "Dondurma", "Glace", "Helado", "Eiscreme", "Gelato"), RiskTier.MEDIUM, listOf("ايس كريم", "آيس كريم"), storageTips = listOf("Keep continuously frozen", "Discard if storage history suggests prolonged thawing or repeated thaw-refreeze cycles")),

        item("toast_bread", FoodCategory.BAKERY, label("Toast bread", "توست", "Tost ekmeği", "Pain de mie", "Pan de molde", "Toastbrot", "Pane in cassetta"), RiskTier.LOW, listOf("توست")),
        item("baguette", FoodCategory.BAKERY, label("Baguette", "باجيت", "Baget ekmek", "Baguette", "Baguette", "Baguette", "Baguette"), RiskTier.LOW, listOf("باجيت")),
        item("filled_pastry", FoodCategory.BAKERY, label("Filled pastry", "مخبوزات محشية", "Dolgulu hamur işi", "Viennoiserie fourrée", "Bollería rellena", "Gefülltes Gebäck", "Pasta ripiena"), RiskTier.MEDIUM, listOf("فطائر", "مخبوزات")),
        item("doughnut", FoodCategory.BAKERY, label("Doughnut", "دونات", "Donut", "Beignet", "Dona", "Donut", "Ciambella"), RiskTier.MEDIUM, listOf("دونات")),

        item("cooked_beef", FoodCategory.PREPARED, label("Cooked beef", "لحم بقري مطبوخ", "Pişmiş dana eti", "Bœuf cuit", "Carne de res cocida", "Gekochtes Rindfleisch", "Manzo cotto"), RiskTier.HIGH, listOf("لحم مطبوخ")),
        item("cooked_fish", FoodCategory.PREPARED, label("Cooked fish", "سمك مطبوخ", "Pişmiş balık", "Poisson cuit", "Pescado cocido", "Gekochter Fisch", "Pesce cotto"), RiskTier.HIGH, listOf("سمك مطبوخ")),
        item("pizza", FoodCategory.PREPARED, label("Pizza", "بيتزا", "Pizza", "Pizza", "Pizza", "Pizza", "Pizza"), RiskTier.MEDIUM, listOf("بيتزا")),
        item("prepared_sandwich", FoodCategory.PREPARED, label("Prepared sandwich", "ساندويتش جاهز", "Hazır sandviç", "Sandwich préparé", "Sándwich preparado", "Belegtes Sandwich", "Panino preparato"), RiskTier.HIGH, listOf("ساندويتش", "سندوتش")),

        item("smoothie", FoodCategory.DRINK, label("Smoothie", "سموثي", "Smoothie", "Smoothie", "Batido", "Smoothie", "Frullato"), RiskTier.MEDIUM, listOf("سموثي", "batido")),

        item("frozen_meal", FoodCategory.PACKAGED, label("Frozen meal", "وجبة مجمدة", "Dondurulmuş yemek", "Plat surgelé", "Comida congelada", "Tiefkühlgericht", "Pasto surgelato"), RiskTier.MEDIUM, listOf("وجبة مجمدة", "frozen food"), storageTips = listOf("Keep continuously frozen until preparation", "Follow the package cooking and storage directions and avoid repeated thaw-refreeze cycles"))
    )
}