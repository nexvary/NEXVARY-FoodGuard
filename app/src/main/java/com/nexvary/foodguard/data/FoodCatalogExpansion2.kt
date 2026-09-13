package com.nexvary.foodguard.data

import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.FoodReferenceImage
import com.nexvary.foodguard.model.FoodReferenceState
import com.nexvary.foodguard.model.LocalizedLabel
import com.nexvary.foodguard.model.RiskTier

internal object FoodCatalogExpansion2 {
    private fun label(en: String, ar: String, tr: String, fr: String, es: String, de: String, it: String) =
        LocalizedLabel(en, ar, tr, fr, es, de, it)

    private fun normal(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("Aroma and color are typical for the fruit and ripeness stage", "Flesh is coherent rather than slimy or collapsing", "No active mold or leaking decay")
        FoodCategory.VEGETABLE -> listOf("Texture is firm, crisp, or naturally tender for the item", "Plant aroma is fresh rather than rotten", "No slime, wet rot, or active mold")
        FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("Cold-chain history is known", "Surface is moist but not sticky or slimy", "No strong rotten or sulfur-like odor")
        FoodCategory.SEAFOOD -> listOf("Clean sea-like smell rather than ammonia", "Flesh is resilient rather than mushy", "No heavy slime or advanced abnormal discoloration")
        FoodCategory.DAIRY -> listOf("Package is intact and product stayed chilled when required", "Texture and aroma match the product", "No unexpected gas, mold, or separation")
        FoodCategory.BAKERY -> listOf("Texture matches the baked product", "No visible mold", "No musty odor or wet sticky patches")
        FoodCategory.PREPARED -> listOf("Safe time-temperature history is known", "No sour/rotten odor, slime, or gas", "Container is intact and food was cooled promptly when required")
        FoodCategory.DRINK -> listOf("Seal is intact", "Color and aroma are typical", "No unexpected gas, swelling, mold, or fermentation")
        FoodCategory.PACKAGED -> listOf("Seal and container are intact", "No swelling, leaking, or seam damage", "Storage conditions match the label")
    }

    private fun spoilage(id: String, category: FoodCategory): List<String> = when (id) {
        "lemon", "lime", "tangerine", "grapefruit" -> listOf("Soft water-soaked collapse or leaking juice", "Blue/green/white fuzzy mold", "Strong fermented or rotten odor")
        "raspberry", "blueberry", "blackberry" -> listOf("Fuzzy mold between berries", "Leaking, slimy or collapsed fruit", "Sharp sour or fermented odor")
        "coconut" -> listOf("Rancid or sour smell after opening", "Pink, gray, black, or moldy flesh", "Cloudy foul-smelling liquid or slimy flesh")
        "arugula", "dill", "green_onion", "celery", "leek" -> listOf("Slimy leaves or stalks", "Dark wet rot spreading through tissue", "Strong rotten odor or active mold")
        "pumpkin" -> listOf("Soft sunken wet rot", "Mold around damaged areas or stem", "Leaking fluid or fermented odor")
        "mushrooms" -> listOf("Heavy slime", "Dark collapsing tissue", "Strong sour or rotten odor")
        "sour_cream", "cream_cheese", "ricotta", "labneh" -> listOf("Unexpected fuzzy mold", "Strong rotten/yeasty odor or gas", "Bulging package or abnormal watery separation with off-odor")
        "evaporated_milk", "condensed_milk", "flavored_milk", "milkshake" -> listOf("Swollen or leaking package", "Unexpected curdling with off-odor", "Gas, mold, or strong sour/rotten smell")
        "kefir" -> listOf("Unexpected mold or abnormal coloration", "Package swelling beyond normal fermentation behavior", "Rotten rather than clean fermented aroma")
        "muffin", "cupcake", "biscuit", "cookies", "brownies", "bagel", "brioche", "tortilla_wrap" -> listOf("Any visible mold on porous baked food", "Musty odor", "Wet sticky patches or abnormal fermentation")
        "pancakes", "waffles" -> listOf("Visible mold", "Sour/fermented odor when not expected", "Wet slimy surface after storage")
        "lentil_soup", "koshari", "stuffed_vine_leaves", "macaroni_bechamel", "lasagna", "shawarma", "fried_chicken", "cooked_vegetables", "mashed_potatoes", "falafel" -> listOf("Unsafe room-temperature exposure or unknown cooling history", "Sour/rotten odor, slime, gas, or visible mold", "Container swelling or leaking after storage")
        "sugarcane_juice", "tamarind_drink", "hibiscus_drink", "lemonade", "iced_tea", "coconut_water", "protein_shake" -> listOf("Unexpected gas or foaming", "Sour/fermented odor when not intended", "Mold, stringiness, leaking or swollen container")
        "mayonnaise", "packaged_hummus", "tomato_sauce_jar", "jam" -> listOf("Broken seal, leaking, or bulging lid", "Visible mold or gas", "Strong off-odor or abnormal texture after opening")
        "canned_tuna", "canned_beans", "canned_corn" -> listOf("Bulging, leaking, spurting, or severely damaged can", "Broken seam or severe seam rust", "Strong off-odor after opening; never taste suspicious cans")
        else -> when (category) {
            FoodCategory.FRUIT -> listOf("Visible mold", "Widespread brown/black mushy tissue", "Slime, leaking fluid, or alcoholic/rotten odor")
            FoodCategory.VEGETABLE -> listOf("Slime or wet rot", "Visible mold", "Strong rotten odor or widespread tissue collapse")
            FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("Persistent slime or sticky film", "Strong rotten or sulfur-like odor", "Unsafe storage history or swollen package")
            FoodCategory.SEAFOOD -> listOf("Strong ammonia or rotten odor", "Heavy slime", "Mushy flesh or major abnormal discoloration")
            FoodCategory.DAIRY -> listOf("Unexpected mold", "Bulging packaging or gas", "Strong sour/rotten odor or abnormal curdling")
            FoodCategory.BAKERY -> listOf("Visible mold", "Musty odor", "Wet sticky or unusually fermented patches")
            FoodCategory.PREPARED -> listOf("Unsafe time-temperature history", "Unexpected sour/rotten odor", "Slime, gas, mold, or package swelling")
            FoodCategory.DRINK -> listOf("Unexpected fermentation or gas", "Bulging/leaking container", "Mold, unusual sediment, or strong off-odor")
            FoodCategory.PACKAGED -> listOf("Bulging or leaking package", "Broken seal", "Mold, gas, or strong off-odor after opening")
        }
    }

    private fun storage(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("Store according to ripening needs", "Refrigerate cut fruit promptly in a clean covered container")
        FoodCategory.VEGETABLE -> listOf("Keep produce clean and dry where appropriate", "Refrigerate cut vegetables promptly")
        FoodCategory.MEAT, FoodCategory.POULTRY, FoodCategory.SEAFOOD -> listOf("Keep refrigerated or frozen without breaking the cold chain", "Prevent raw-food cross-contamination")
        FoodCategory.DAIRY -> listOf("Keep refrigerated unless the label says otherwise", "Close the package and limit room-temperature exposure")
        FoodCategory.BAKERY -> listOf("Protect from humidity", "Freeze portions if longer storage is required")
        FoodCategory.PREPARED -> listOf("Cool leftovers promptly and refrigerate", "Avoid repeated warm-cool cycles")
        FoodCategory.DRINK -> listOf("Follow refrigeration instructions after opening", "Keep caps and pouring surfaces clean")
        FoodCategory.PACKAGED -> listOf("Follow label storage directions", "After opening, follow refrigeration and use-by guidance")
    }

    private fun refs(id: String) = listOf(
        FoodReferenceImage("ref_${id}_healthy", FoodReferenceState.HEALTHY, "Healthy reference image slot"),
        FoodReferenceImage("ref_${id}_spoilage", FoodReferenceState.SPOILAGE, "Visible-spoilage reference image slot")
    )

    private fun item(
        id: String,
        category: FoodCategory,
        name: LocalizedLabel,
        risk: RiskTier,
        aliases: List<String> = emptyList()
    ) = FoodItem(
        id = id,
        category = category,
        name = name,
        aliases = aliases,
        normalSigns = normal(category),
        spoilageSigns = spoilage(id, category),
        storageTips = storage(category),
        riskTier = risk,
        referenceImages = refs(id)
    )

    val items: List<FoodItem> = listOf(
        item("lemon", FoodCategory.FRUIT, label("Lemon", "ليمون", "Limon", "Citron", "Limón", "Zitrone", "Limone"), RiskTier.LOW, listOf("ليمون")),
        item("lime", FoodCategory.FRUIT, label("Lime", "ليمون أخضر", "Misket limonu", "Citron vert", "Lima", "Limette", "Lime"), RiskTier.LOW, listOf("lime")),
        item("tangerine", FoodCategory.FRUIT, label("Tangerine", "يوسفي", "Mandalina", "Mandarine", "Mandarina", "Mandarine", "Mandarino"), RiskTier.LOW, listOf("يوسفي", "mandarin")),
        item("grapefruit", FoodCategory.FRUIT, label("Grapefruit", "جريب فروت", "Greyfurt", "Pamplemousse", "Pomelo", "Grapefruit", "Pompelmo"), RiskTier.LOW),
        item("apricot", FoodCategory.FRUIT, label("Apricot", "مشمش", "Kayısı", "Abricot", "Albaricoque", "Aprikose", "Albicocca"), RiskTier.LOW, listOf("مشمش")),
        item("nectarine", FoodCategory.FRUIT, label("Nectarine", "نكتارين", "Nektarin", "Nectarine", "Nectarina", "Nektarine", "Nettarina"), RiskTier.LOW),
        item("raspberry", FoodCategory.FRUIT, label("Raspberry", "توت العليق", "Ahududu", "Framboise", "Frambuesa", "Himbeere", "Lampone"), RiskTier.LOW, listOf("توت")),
        item("blueberry", FoodCategory.FRUIT, label("Blueberry", "توت أزرق", "Yaban mersini", "Myrtille", "Arándano", "Blaubeere", "Mirtillo"), RiskTier.LOW),
        item("blackberry", FoodCategory.FRUIT, label("Blackberry", "توت أسود", "Böğürtlen", "Mûre", "Mora", "Brombeere", "Mora"), RiskTier.LOW),
        item("coconut", FoodCategory.FRUIT, label("Coconut", "جوز هند", "Hindistan cevizi", "Noix de coco", "Coco", "Kokosnuss", "Cocco"), RiskTier.LOW, listOf("جوز الهند")),

        item("radish", FoodCategory.VEGETABLE, label("Radish", "فجل", "Turp", "Radis", "Rábano", "Radieschen", "Ravanello"), RiskTier.LOW),
        item("turnip", FoodCategory.VEGETABLE, label("Turnip", "لفت", "Şalgam", "Navet", "Nabo", "Rübe", "Rapa"), RiskTier.LOW),
        item("leek", FoodCategory.VEGETABLE, label("Leek", "كراث", "Pırasa", "Poireau", "Puerro", "Lauch", "Porro"), RiskTier.LOW),
        item("celery", FoodCategory.VEGETABLE, label("Celery", "كرفس", "Kereviz", "Céleri", "Apio", "Sellerie", "Sedano"), RiskTier.LOW),
        item("artichoke", FoodCategory.VEGETABLE, label("Artichoke", "خرشوف", "Enginar", "Artichaut", "Alcachofa", "Artischocke", "Carciofo"), RiskTier.LOW, listOf("خرشوف")),
        item("asparagus", FoodCategory.VEGETABLE, label("Asparagus", "هليون", "Kuşkonmaz", "Asperge", "Espárrago", "Spargel", "Asparago"), RiskTier.LOW),
        item("pumpkin", FoodCategory.VEGETABLE, label("Pumpkin", "قرع", "Balkabağı", "Citrouille", "Calabaza", "Kürbis", "Zucca"), RiskTier.LOW, listOf("يقطين")),
        item("green_onion", FoodCategory.VEGETABLE, label("Green onion", "بصل أخضر", "Taze soğan", "Oignon vert", "Cebolla verde", "Frühlingszwiebel", "Cipollotto"), RiskTier.LOW),
        item("arugula", FoodCategory.VEGETABLE, label("Arugula", "جرجير", "Roka", "Roquette", "Rúcula", "Rucola", "Rucola"), RiskTier.LOW, listOf("جرجير")),
        item("dill", FoodCategory.VEGETABLE, label("Dill", "شبت", "Dereotu", "Aneth", "Eneldo", "Dill", "Aneto"), RiskTier.LOW, listOf("شبت")),

        item("beef_ribs", FoodCategory.MEAT, label("Beef ribs", "ضلوع بقري", "Dana kaburga", "Côtes de bœuf", "Costillas de res", "Rinderrippen", "Costine di manzo"), RiskTier.HIGH),
        item("beef_tenderloin", FoodCategory.MEAT, label("Beef tenderloin", "فيليه بقري", "Dana bonfile", "Filet de bœuf", "Solomillo de res", "Rinderfilet", "Filetto di manzo"), RiskTier.HIGH),
        item("lamb_chops", FoodCategory.MEAT, label("Lamb chops", "ريش ضاني", "Kuzu pirzola", "Côtelettes d'agneau", "Chuletas de cordero", "Lammkoteletts", "Costolette d'agnello"), RiskTier.HIGH, listOf("ريش")),
        item("lamb_shank", FoodCategory.MEAT, label("Lamb shank", "موزة ضاني", "Kuzu incik", "Jarret d'agneau", "Jarrete de cordero", "Lammhaxe", "Stinco d'agnello"), RiskTier.HIGH),
        item("oxtail", FoodCategory.MEAT, label("Oxtail", "ذيل بقري", "Dana kuyruğu", "Queue de bœuf", "Rabo de res", "Ochsenschwanz", "Coda di manzo"), RiskTier.HIGH),
        item("tripe", FoodCategory.MEAT, label("Tripe", "كرشة", "İşkembe", "Tripes", "Callos", "Kutteln", "Trippa"), RiskTier.HIGH, listOf("كرشة")),
        item("beef_tongue", FoodCategory.MEAT, label("Beef tongue", "لسان بقري", "Dana dili", "Langue de bœuf", "Lengua de res", "Rinderzunge", "Lingua di manzo"), RiskTier.HIGH),
        item("beef_brisket", FoodCategory.MEAT, label("Beef brisket", "صدر بقري", "Dana döş", "Poitrine de bœuf", "Pecho de res", "Rinderbrust", "Punta di petto"), RiskTier.HIGH),
        item("beef_shank", FoodCategory.MEAT, label("Beef shank", "موزة بقري", "Dana incik", "Jarret de bœuf", "Jarrete de res", "Rinderhaxe", "Stinco di manzo"), RiskTier.HIGH),
        item("meat_skewers", FoodCategory.MEAT, label("Raw meat skewers", "أسياخ لحم نيئة", "Çiğ et şiş", "Brochettes de viande crues", "Brochetas crudas", "Rohe Fleischspieße", "Spiedini crudi"), RiskTier.HIGH),

        item("chicken_drumstick", FoodCategory.POULTRY, label("Chicken drumstick", "دبوس دجاج", "Tavuk baget", "Pilon de poulet", "Muslo de pollo", "Hähnchenkeule", "Fuso di pollo"), RiskTier.HIGH),
        item("chicken_gizzard", FoodCategory.POULTRY, label("Chicken gizzard", "قوانص دجاج", "Tavuk taşlığı", "Gésier de poulet", "Molleja de pollo", "Hähnchenmagen", "Ventriglio di pollo"), RiskTier.HIGH, listOf("قوانص")),
        item("chicken_neck", FoodCategory.POULTRY, label("Chicken neck", "رقبة دجاج", "Tavuk boynu", "Cou de poulet", "Cuello de pollo", "Hähnchenhals", "Collo di pollo"), RiskTier.HIGH),
        item("chicken_heart", FoodCategory.POULTRY, label("Chicken heart", "قلب دجاج", "Tavuk yüreği", "Cœur de poulet", "Corazón de pollo", "Hähnchenherz", "Cuore di pollo"), RiskTier.HIGH),
        item("chicken_feet", FoodCategory.POULTRY, label("Chicken feet", "أرجل دجاج", "Tavuk ayağı", "Pattes de poulet", "Patas de pollo", "Hühnerfüße", "Zampe di pollo"), RiskTier.HIGH),
        item("turkey_leg", FoodCategory.POULTRY, label("Turkey leg", "ورك ديك رومي", "Hindi budu", "Cuisse de dinde", "Pierna de pavo", "Putenkeule", "Coscia di tacchino"), RiskTier.HIGH),
        item("duck_breast", FoodCategory.POULTRY, label("Duck breast", "صدر بط", "Ördek göğsü", "Magret de canard", "Pechuga de pato", "Entenbrust", "Petto d'anatra"), RiskTier.HIGH),
        item("duck_leg", FoodCategory.POULTRY, label("Duck leg", "ورك بط", "Ördek budu", "Cuisse de canard", "Pierna de pato", "Entenkeule", "Coscia d'anatra"), RiskTier.HIGH),
        item("quail_breast", FoodCategory.POULTRY, label("Quail breast", "صدر سمان", "Bıldırcın göğsü", "Poitrine de caille", "Pechuga de codorniz", "Wachtelbrust", "Petto di quaglia"), RiskTier.HIGH),
        item("goose_breast", FoodCategory.POULTRY, label("Goose breast", "صدر أوز", "Kaz göğsü", "Poitrine d'oie", "Pechuga de ganso", "Gänsebrust", "Petto d'oca"), RiskTier.HIGH),

        item("herring", FoodCategory.SEAFOOD, label("Herring", "رنجة", "Ringa", "Hareng", "Arenque", "Hering", "Aringa"), RiskTier.HIGH, listOf("رنجة")),
        item("anchovies", FoodCategory.SEAFOOD, label("Anchovies", "أنشوجة", "Hamsi", "Anchois", "Anchoas", "Sardellen", "Acciughe"), RiskTier.HIGH),
        item("grouper", FoodCategory.SEAFOOD, label("Grouper", "هامور", "Lahoz", "Mérou", "Mero", "Zackenbarsch", "Cernia"), RiskTier.HIGH),
        item("sole", FoodCategory.SEAFOOD, label("Sole", "سمك موسى", "Dil balığı", "Sole", "Lenguado", "Seezunge", "Sogliola"), RiskTier.HIGH),
        item("eel", FoodCategory.SEAFOOD, label("Eel", "ثعبان البحر", "Yılan balığı", "Anguille", "Anguila", "Aal", "Anguilla"), RiskTier.HIGH),
        item("lobster", FoodCategory.SEAFOOD, label("Lobster", "استاكوزا", "Istakoz", "Homard", "Langosta", "Hummer", "Aragosta"), RiskTier.HIGH, listOf("استاكوزا")),
        item("scallops", FoodCategory.SEAFOOD, label("Scallops", "اسكالوب بحري", "Deniz tarağı", "Coquilles Saint-Jacques", "Vieiras", "Jakobsmuscheln", "Capesante"), RiskTier.HIGH),
        item("clams", FoodCategory.SEAFOOD, label("Clams", "جندوفلي", "Midye tarak", "Palourdes", "Almejas", "Venusmuscheln", "Vongole"), RiskTier.HIGH, listOf("جندوفلي")),
        item("swordfish", FoodCategory.SEAFOOD, label("Swordfish", "سمك أبو سيف", "Kılıç balığı", "Espadon", "Pez espada", "Schwertfisch", "Pesce spada"), RiskTier.HIGH),
        item("catfish", FoodCategory.SEAFOOD, label("Catfish", "قرموط", "Yayın balığı", "Poisson-chat", "Bagre", "Wels", "Pesce gatto"), RiskTier.HIGH, listOf("قرموط")),

        item("sour_cream", FoodCategory.DAIRY, label("Sour cream", "كريمة حامضة", "Ekşi krema", "Crème aigre", "Crema agria", "Saure Sahne", "Panna acida"), RiskTier.MEDIUM),
        item("evaporated_milk", FoodCategory.DAIRY, label("Evaporated milk", "حليب مبخر", "Buharlaştırılmış süt", "Lait évaporé", "Leche evaporada", "Kondensmilch", "Latte evaporato"), RiskTier.MEDIUM),
        item("condensed_milk", FoodCategory.DAIRY, label("Condensed milk", "حليب مكثف", "Yoğunlaştırılmış süt", "Lait concentré", "Leche condensada", "Gezuckerte Kondensmilch", "Latte condensato"), RiskTier.MEDIUM),
        item("ricotta", FoodCategory.DAIRY, label("Ricotta", "ريكوتا", "Ricotta", "Ricotta", "Ricotta", "Ricotta", "Ricotta"), RiskTier.HIGH),
        item("cheddar", FoodCategory.DAIRY, label("Cheddar", "جبنة شيدر", "Çedar", "Cheddar", "Cheddar", "Cheddar", "Cheddar"), RiskTier.MEDIUM),
        item("gouda", FoodCategory.DAIRY, label("Gouda", "جبنة جودة", "Gouda", "Gouda", "Gouda", "Gouda", "Gouda"), RiskTier.MEDIUM),
        item("parmesan", FoodCategory.DAIRY, label("Parmesan", "بارميزان", "Parmesan", "Parmesan", "Parmesano", "Parmesan", "Parmigiano"), RiskTier.MEDIUM),
        item("cream_cheese", FoodCategory.DAIRY, label("Cream cheese", "جبنة كريمي", "Krem peynir", "Fromage à la crème", "Queso crema", "Frischkäse", "Formaggio cremoso"), RiskTier.HIGH),
        item("kefir", FoodCategory.DAIRY, label("Kefir", "كفير", "Kefir", "Kéfir", "Kéfir", "Kefir", "Kefir"), RiskTier.MEDIUM),
        item("labneh", FoodCategory.DAIRY, label("Labneh", "لبنة", "Labne", "Labneh", "Labneh", "Labneh", "Labneh"), RiskTier.HIGH, listOf("لبنة")),

        item("muffin", FoodCategory.BAKERY, label("Muffin", "مافن", "Muffin", "Muffin", "Muffin", "Muffin", "Muffin"), RiskTier.LOW),
        item("cupcake", FoodCategory.BAKERY, label("Cupcake", "كب كيك", "Cupcake", "Cupcake", "Cupcake", "Cupcake", "Cupcake"), RiskTier.MEDIUM),
        item("biscuit", FoodCategory.BAKERY, label("Biscuit", "بسكويت", "Bisküvi", "Biscuit", "Galleta", "Keks", "Biscotto"), RiskTier.LOW),
        item("cookies", FoodCategory.BAKERY, label("Cookies", "كوكيز", "Kurabiye", "Cookies", "Galletas", "Cookies", "Biscotti"), RiskTier.LOW),
        item("brownies", FoodCategory.BAKERY, label("Brownies", "براونيز", "Brownie", "Brownies", "Brownies", "Brownies", "Brownies"), RiskTier.MEDIUM),
        item("pancakes", FoodCategory.BAKERY, label("Pancakes", "بان كيك", "Pankek", "Pancakes", "Panqueques", "Pfannkuchen", "Pancake"), RiskTier.MEDIUM),
        item("waffles", FoodCategory.BAKERY, label("Waffles", "وافل", "Waffle", "Gaufres", "Gofres", "Waffeln", "Waffle"), RiskTier.MEDIUM),
        item("bagel", FoodCategory.BAKERY, label("Bagel", "بيجل", "Bagel", "Bagel", "Bagel", "Bagel", "Bagel"), RiskTier.LOW),
        item("brioche", FoodCategory.BAKERY, label("Brioche", "بريوش", "Briyoş", "Brioche", "Brioche", "Brioche", "Brioche"), RiskTier.LOW),
        item("tortilla_wrap", FoodCategory.BAKERY, label("Tortilla wrap", "خبز تورتيلا", "Tortilla", "Tortilla", "Tortilla", "Tortilla", "Tortilla"), RiskTier.LOW),

        item("lentil_soup", FoodCategory.PREPARED, label("Lentil soup", "شوربة عدس", "Mercimek çorbası", "Soupe de lentilles", "Sopa de lentejas", "Linsensuppe", "Zuppa di lenticchie"), RiskTier.HIGH),
        item("falafel", FoodCategory.PREPARED, label("Falafel", "طعمية / فلافل", "Falafel", "Falafel", "Falafel", "Falafel", "Falafel"), RiskTier.MEDIUM, listOf("طعمية", "فلافل")),
        item("koshari", FoodCategory.PREPARED, label("Koshari", "كشري", "Koshari", "Koshari", "Koshari", "Koshari", "Koshari"), RiskTier.HIGH, listOf("كشري")),
        item("stuffed_vine_leaves", FoodCategory.PREPARED, label("Stuffed vine leaves", "ورق عنب", "Yaprak sarma", "Feuilles de vigne farcies", "Hojas de parra rellenas", "Gefüllte Weinblätter", "Foglie di vite ripiene"), RiskTier.HIGH, listOf("ورق عنب")),
        item("macaroni_bechamel", FoodCategory.PREPARED, label("Macaroni béchamel", "مكرونة بشاميل", "Beşamel soslu makarna", "Macaroni béchamel", "Macarrones con bechamel", "Makkaroni mit Béchamel", "Maccheroni besciamella"), RiskTier.HIGH, listOf("بشاميل")),
        item("lasagna", FoodCategory.PREPARED, label("Lasagna", "لازانيا", "Lazanya", "Lasagnes", "Lasaña", "Lasagne", "Lasagna"), RiskTier.HIGH),
        item("shawarma", FoodCategory.PREPARED, label("Shawarma", "شاورما", "Şavurma", "Chawarma", "Shawarma", "Schawarma", "Shawarma"), RiskTier.HIGH, listOf("شاورما")),
        item("fried_chicken", FoodCategory.PREPARED, label("Fried chicken", "دجاج مقلي", "Kızarmış tavuk", "Poulet frit", "Pollo frito", "Brathähnchen", "Pollo fritto"), RiskTier.HIGH),
        item("cooked_vegetables", FoodCategory.PREPARED, label("Cooked vegetables", "خضار مطبوخ", "Pişmiş sebze", "Légumes cuits", "Verduras cocidas", "Gekochtes Gemüse", "Verdure cotte"), RiskTier.MEDIUM),
        item("mashed_potatoes", FoodCategory.PREPARED, label("Mashed potatoes", "بطاطس مهروسة", "Patates püresi", "Purée de pommes de terre", "Puré de patatas", "Kartoffelpüree", "Purè di patate"), RiskTier.MEDIUM),

        item("bottled_water", FoodCategory.DRINK, label("Bottled water", "مياه معبأة", "Şişe su", "Eau en bouteille", "Agua embotellada", "Flaschenwasser", "Acqua in bottiglia"), RiskTier.LOW),
        item("flavored_milk", FoodCategory.DRINK, label("Flavored milk", "لبن منكّه", "Aromalı süt", "Lait aromatisé", "Leche saborizada", "Aromatisierte Milch", "Latte aromatizzato"), RiskTier.HIGH),
        item("iced_tea", FoodCategory.DRINK, label("Iced tea", "شاي مثلج", "Buzlu çay", "Thé glacé", "Té helado", "Eistee", "Tè freddo"), RiskTier.MEDIUM),
        item("lemonade", FoodCategory.DRINK, label("Lemonade", "ليمونادة", "Limonata", "Limonade", "Limonada", "Limonade", "Limonata"), RiskTier.MEDIUM),
        item("sugarcane_juice", FoodCategory.DRINK, label("Sugarcane juice", "عصير قصب", "Şeker kamışı suyu", "Jus de canne à sucre", "Jugo de caña", "Zuckerrohrsaft", "Succo di canna"), RiskTier.HIGH, listOf("قصب")),
        item("tamarind_drink", FoodCategory.DRINK, label("Tamarind drink", "تمر هندي", "Demirhindi içeceği", "Boisson au tamarin", "Bebida de tamarindo", "Tamarindengetränk", "Bevanda al tamarindo"), RiskTier.MEDIUM, listOf("تمر هندي")),
        item("hibiscus_drink", FoodCategory.DRINK, label("Hibiscus drink", "كركديه", "Hibiskus içeceği", "Boisson d'hibiscus", "Bebida de hibisco", "Hibiskusgetränk", "Bevanda all'ibisco"), RiskTier.MEDIUM, listOf("كركديه")),
        item("coconut_water", FoodCategory.DRINK, label("Coconut water", "ماء جوز الهند", "Hindistan cevizi suyu", "Eau de coco", "Agua de coco", "Kokoswasser", "Acqua di cocco"), RiskTier.MEDIUM),
        item("protein_shake", FoodCategory.DRINK, label("Protein shake", "مشروب بروتين", "Protein içeceği", "Shake protéiné", "Batido de proteína", "Proteinshake", "Frullato proteico"), RiskTier.HIGH),
        item("milkshake", FoodCategory.DRINK, label("Milkshake", "ميلك شيك", "Milkshake", "Milk-shake", "Batido", "Milchshake", "Frappè"), RiskTier.HIGH),

        item("canned_tuna", FoodCategory.PACKAGED, label("Canned tuna", "تونة معلبة", "Konserve ton balığı", "Thon en conserve", "Atún enlatado", "Thunfischkonserve", "Tonno in scatola"), RiskTier.HIGH),
        item("canned_beans", FoodCategory.PACKAGED, label("Canned beans", "فاصوليا معلبة", "Konserve fasulye", "Haricots en conserve", "Frijoles enlatados", "Bohnenkonserve", "Fagioli in scatola"), RiskTier.MEDIUM),
        item("canned_corn", FoodCategory.PACKAGED, label("Canned corn", "ذرة معلبة", "Konserve mısır", "Maïs en conserve", "Maíz enlatado", "Maiskonserve", "Mais in scatola"), RiskTier.MEDIUM),
        item("tomato_sauce_jar", FoodCategory.PACKAGED, label("Tomato sauce jar", "صلصة طماطم معبأة", "Domates sosu", "Sauce tomate en bocal", "Salsa de tomate", "Tomatensoße im Glas", "Salsa di pomodoro"), RiskTier.MEDIUM),
        item("mayonnaise", FoodCategory.PACKAGED, label("Mayonnaise", "مايونيز", "Mayonez", "Mayonnaise", "Mayonesa", "Mayonnaise", "Maionese"), RiskTier.HIGH),
        item("ketchup", FoodCategory.PACKAGED, label("Ketchup", "كاتشب", "Ketçap", "Ketchup", "Kétchup", "Ketchup", "Ketchup"), RiskTier.MEDIUM),
        item("peanut_butter", FoodCategory.PACKAGED, label("Peanut butter", "زبدة فول سوداني", "Fıstık ezmesi", "Beurre de cacahuète", "Mantequilla de maní", "Erdnussbutter", "Burro d'arachidi"), RiskTier.MEDIUM),
        item("jam", FoodCategory.PACKAGED, label("Jam", "مربى", "Reçel", "Confiture", "Mermelada", "Marmelade", "Marmellata"), RiskTier.MEDIUM),
        item("packaged_hummus", FoodCategory.PACKAGED, label("Packaged hummus", "حمص معبأ", "Paket humus", "Houmous emballé", "Hummus envasado", "Verpackter Hummus", "Hummus confezionato"), RiskTier.HIGH, listOf("حمص")),
        item("frozen_vegetables", FoodCategory.PACKAGED, label("Frozen vegetables", "خضار مجمد", "Dondurulmuş sebze", "Légumes surgelés", "Verduras congeladas", "Tiefkühlgemüse", "Verdure surgelate"), RiskTier.MEDIUM)
    )
}
