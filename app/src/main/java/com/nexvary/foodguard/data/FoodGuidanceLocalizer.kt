package com.nexvary.foodguard.data

import com.nexvary.foodguard.model.FoodCategory
import com.nexvary.foodguard.model.FoodItem
import com.nexvary.foodguard.model.FoodReferenceState
import java.util.Locale

/**
 * Locale-aware presentation layer for food guidance.
 *
 * Catalog content remains reviewable in one place, while this class guarantees
 * that Arabic UI never falls back to English guidance text.
 */
object FoodGuidanceLocalizer {
    private fun isArabic(): Boolean = Locale.getDefault().language.equals("ar", ignoreCase = true)

    fun normal(food: FoodItem): List<String> {
        if (!isArabic()) return food.normalSigns
        return when (food.id) {
            "owaisi_mango" -> listOf(
                "يكون لحم المانجو السليم عادةً أصفر إلى برتقالي داكن.",
                "الرائحة الطبيعية المتوقعة حلوة ومميزة للمانجو.",
                "يمكن أن يكون اللب طريًا لكنه متماسك عند اكتمال النضج؛ وجود مركز برتقالي أغمق وحده لا يثبت فساد الثمرة."
            )
            "cooked_rice" -> listOf(
                "معرفة تاريخ الحفظ والوقت ودرجة الحرارة أهم من المظهر وحده.",
                "لا توجد رائحة حامضة أو لزوجة أو رطوبة غير معتادة."
            )
            "canned_food" -> listOf(
                "العلبة غير منتفخة ومحكمة الغلق ولا يوجد بها تسرب.",
                "لا يوجد تلف شديد في الحواف أو ضغط غير معتاد داخل العبوة."
            )
            else -> defaultNormal(food.category)
        }
    }

    fun spoilage(food: FoodItem): List<String> {
        if (!isArabic()) return food.spoilageSigns
        return when (food.id) {
            "owaisi_mango" -> listOf(
                "نسيج أسود أو بني مهترئ وطري ينتشر داخل اللب.",
                "لزوجة أو تسرب سوائل أو عفن ظاهر أو مناطق مجوفة متعفنة.",
                "رائحة تخمر حادة أو كحولية أو رائحة تعفن."
            )
            "potato" -> listOf(
                "تعفن رطب أو لزوجة أو عفن أو رائحة تعفن قوية.",
                "الاخضرار الواسع أو التبرعم الشديد يستلزم الحذر والتخلص من الحبة عند الشك."
            )
            "bread" -> listOf(
                "أي عفن ظاهر على الخبز المسامي.",
                "رائحة عفنة أو بقع رطبة ولزجة."
            )
            "cooked_rice" -> listOf(
                "ترك الأرز في درجة حرارة الغرفة لمدة غير آمنة.",
                "رائحة حامضة أو غير طبيعية أو لزوجة أو عفن ظاهر."
            )
            "canned_food" -> listOf(
                "علبة منتفخة أو متسربة أو يندفع منها السائل أو متضررة بشدة.",
                "تلف في خط اللحام أو صدأ شديد عند الحواف أو رائحة غير طبيعية بعد الفتح."
            )
            "ice_cream" -> listOf(
                "علامات ذوبان طويل أو إعادة تجميد متكرر.",
                "تغير غير طبيعي في القوام أو الرائحة أو ظهور عفن."
            )
            "lemon", "lime", "tangerine", "grapefruit" -> listOf(
                "مناطق رخوة مائية أو انهيار في القشرة مع تسرب العصير.",
                "عفن زغبي أزرق أو أخضر أو أبيض.",
                "رائحة تخمر أو تعفن قوية."
            )
            "raspberry", "blueberry", "blackberry" -> listOf(
                "عفن زغبي بين الحبات.",
                "حبات منهارة أو لزجة أو متسربة.",
                "رائحة حامضة أو متخمرة بوضوح."
            )
            "coconut" -> listOf(
                "رائحة زنخة أو حامضة بعد الفتح.",
                "لب وردي أو رمادي أو أسود أو عليه عفن.",
                "سائل عكر كريه الرائحة أو لب لزج."
            )
            "arugula", "dill", "green_onion", "celery", "leek" -> listOf(
                "أوراق أو سيقان لزجة.",
                "تعفن رطب داكن ينتشر في الأنسجة.",
                "رائحة تعفن قوية أو عفن نشط."
            )
            "pumpkin" -> listOf(
                "مناطق غائرة رخوة ومتعفنة رطبة.",
                "عفن حول المناطق المصابة أو الساق.",
                "تسرب سوائل أو رائحة تخمر."
            )
            "mushrooms" -> listOf(
                "لزوجة شديدة.",
                "أنسجة داكنة منهارة.",
                "رائحة حامضة أو متعفنة قوية."
            )
            "sour_cream", "cream_cheese", "ricotta", "labneh" -> listOf(
                "عفن زغبي غير متوقع.",
                "رائحة خميرية أو متعفنة قوية أو تكون غازات.",
                "انتفاخ العبوة أو انفصال مائي غير طبيعي مصحوب برائحة سيئة."
            )
            "evaporated_milk", "condensed_milk", "flavored_milk", "milkshake" -> listOf(
                "انتفاخ العبوة أو تسربها.",
                "تكتل غير طبيعي مصحوب برائحة غير مقبولة.",
                "غازات أو عفن أو رائحة حامضة/متعفنة قوية."
            )
            "kefir" -> listOf(
                "عفن غير متوقع أو تغير غير طبيعي في اللون.",
                "انتفاخ العبوة بشكل يتجاوز طبيعة التخمير المعتادة.",
                "رائحة تعفن بدل الرائحة المتخمرة النظيفة المعتادة."
            )
            "muffin", "cupcake", "biscuit", "cookies", "brownies", "bagel", "brioche", "tortilla_wrap" -> listOf(
                "أي عفن ظاهر على المخبوزات المسامية.",
                "رائحة عفنة.",
                "بقع رطبة لزجة أو تخمر غير طبيعي."
            )
            "pancakes", "waffles" -> listOf(
                "عفن ظاهر.",
                "رائحة حامضة أو متخمرة غير متوقعة.",
                "سطح رطب ولزج بعد التخزين."
            )
            "lentil_soup", "koshari", "stuffed_vine_leaves", "macaroni_bechamel", "lasagna", "shawarma", "fried_chicken", "cooked_vegetables", "mashed_potatoes", "falafel" -> listOf(
                "تعرض غير آمن لدرجة حرارة الغرفة أو عدم معرفة طريقة التبريد.",
                "رائحة حامضة/متعفنة أو لزوجة أو غازات أو عفن ظاهر.",
                "انتفاخ أو تسرب العبوة بعد التخزين."
            )
            "sugarcane_juice", "tamarind_drink", "hibiscus_drink", "lemonade", "iced_tea", "coconut_water", "protein_shake" -> listOf(
                "غازات أو رغوة غير متوقعة.",
                "رائحة حامضة أو متخمرة عندما لا يكون ذلك طبيعيًا للمنتج.",
                "عفن أو لزوجة خيطية أو تسرب أو انتفاخ العبوة."
            )
            "mayonnaise", "packaged_hummus", "tomato_sauce_jar", "jam" -> listOf(
                "كسر الختم أو وجود تسرب أو غطاء منتفخ.",
                "عفن ظاهر أو تكون غازات.",
                "رائحة غير طبيعية قوية أو تغير غير معتاد في القوام بعد الفتح."
            )
            "canned_tuna", "canned_beans", "canned_corn" -> listOf(
                "علبة منتفخة أو متسربة أو يندفع منها السائل أو متضررة بشدة.",
                "تلف في خط اللحام أو صدأ شديد عند الحواف.",
                "رائحة غير طبيعية قوية بعد الفتح؛ لا تتذوق محتوى العلب المشكوك فيها."
            )
            else -> defaultSpoilage(food.category)
        }
    }

    fun storage(food: FoodItem): List<String> {
        if (!isArabic()) return food.storageTips
        return when (food.id) {
            "owaisi_mango" -> listOf(
                "اترك الثمرة الكاملة لتنضج في درجة حرارة الغرفة.",
                "ضعها في الثلاجة بعد اكتمال النضج.",
                "احفظ المانجو المقطعة مبردة داخل وعاء نظيف ومحكم."
            )
            "bread" -> listOf(
                "احفظ الخبز جافًا وبعيدًا عن الرطوبة.",
                "عند ظهور العفن على الخبز المسامي تخلص من الرغيف المتأثر كاملًا بدل إزالة الجزء الظاهر فقط."
            )
            "cooked_rice" -> listOf(
                "برّد الأرز سريعًا بعد الطهي.",
                "احفظه مبردًا في وعاء مغطى وتجنب تركه مدة طويلة في درجة حرارة الغرفة."
            )
            "canned_food" -> listOf(
                "خزّن العلب غير المفتوحة حسب تعليمات المنتج.",
                "تخلص من أي علبة منتفخة أو متسربة دون تذوق محتواها."
            )
            "ice_cream" -> listOf(
                "احفظه مجمدًا باستمرار.",
                "تخلص منه إذا كان تاريخ التخزين يشير إلى ذوبان طويل أو دورات متكررة من الذوبان وإعادة التجميد."
            )
            else -> defaultStorage(food.category)
        }
    }

    fun referenceCaption(state: FoodReferenceState, fallback: String): String {
        if (!isArabic()) return fallback
        return when (state) {
            FoodReferenceState.HEALTHY -> "صورة مرجعية للحالة السليمة"
            FoodReferenceState.RIPE -> "صورة مرجعية للحالة الناضجة"
            FoodReferenceState.OVERRIPE -> "صورة مرجعية لفرط النضج"
            FoodReferenceState.SPOILAGE -> "صورة مرجعية لعلامات التلف الظاهرة"
        }
    }

    private fun defaultNormal(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("رائحة طبيعية مناسبة لنوع الفاكهة.", "القوام يتوافق مع مرحلة النضج المتوقعة.", "لا يوجد عفن نشط أو تسرب ناتج عن التعفن.")
        FoodCategory.VEGETABLE -> listOf("قوام متماسك أو مقرمش طبيعيًا.", "رائحة نباتية طازجة.", "لا توجد لزوجة أو عفن نشط أو تعفن رطب.")
        FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("العبوة سليمة وتاريخ الحفظ البارد معروف.", "السطح رطب طبيعيًا لكنه غير لزج.", "لا توجد رائحة تعفن قوية أو رائحة كبريتية غير طبيعية.")
        FoodCategory.SEAFOOD -> listOf("رائحة بحرية نظيفة وليست رائحة أمونيا قوية.", "اللحم متماسك ومرن وليس مهترئًا.", "لا توجد لزوجة شديدة أو تغيرات لونية متقدمة وغير طبيعية.")
        FoodCategory.DAIRY -> listOf("العبوة سليمة ومحفوظة بالتبريد عند الحاجة.", "الرائحة والقوام طبيعيان للمنتج.", "لا يوجد عفن أو غازات أو انفصال غير متوقع مرتبط بالتلف.")
        FoodCategory.BAKERY -> listOf("القوام طبيعي لنوع المخبوز.", "لا يوجد عفن ظاهر.", "لا توجد رائحة عفنة أو بقع رطبة ولزجة.")
        FoodCategory.PREPARED -> listOf("تاريخ الوقت ودرجة الحرارة أثناء الحفظ معروف وآمن.", "لا توجد رائحة حامضة/متعفنة أو لزوجة أو غازات غير متوقعة.", "تم التبريد والحفظ بما يناسب نوع الوجبة.")
        FoodCategory.DRINK -> listOf("الختم سليم.", "اللون والرائحة طبيعيان للمنتج.", "لا توجد غازات أو انتفاخ أو عفن أو تخمر غير متوقع.")
        FoodCategory.PACKAGED -> listOf("العبوة والختم سليمان.", "لا يوجد انتفاخ أو تسرب أو تلف شديد في الحواف.", "ظروف التخزين متوافقة مع تعليمات المنتج.")
    }

    private fun defaultSpoilage(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("عفن ظاهر.", "أنسجة بنية أو سوداء طرية ومتهتكة على نطاق واسع.", "لزوجة أو تسرب سوائل أو رائحة كحولية/متعفنة.")
        FoodCategory.VEGETABLE -> listOf("لزوجة أو تعفن رطب.", "عفن زغبي ظاهر.", "رائحة تعفن قوية أو انهيار واسع في الأنسجة.")
        FoodCategory.MEAT, FoodCategory.POULTRY -> listOf("لزوجة مستمرة أو طبقة سطحية لاصقة.", "رائحة تعفن قوية أو رائحة كبريتية غير طبيعية.", "انتفاخ العبوة أو تاريخ تخزين غير آمن.")
        FoodCategory.SEAFOOD -> listOf("رائحة أمونيا قوية أو رائحة تعفن.", "لزوجة شديدة.", "لحم مهترئ أو تغير لوني كبير وغير طبيعي.")
        FoodCategory.DAIRY -> listOf("عفن غير متوقع للمنتج.", "انتفاخ العبوة أو تكون غازات.", "رائحة حامضة/متعفنة قوية أو تكتل غير طبيعي.")
        FoodCategory.BAKERY -> listOf("أي عفن ظاهر على المخبوزات المسامية.", "رائحة عفنة.", "بقع رطبة أو لزجة أو تخمر غير طبيعي.")
        FoodCategory.PREPARED -> listOf("تاريخ وقت/حرارة غير آمن.", "رائحة حامضة أو متعفنة غير متوقعة.", "لزوجة أو غازات أو عفن أو انتفاخ العبوة.")
        FoodCategory.DRINK -> listOf("تخمر أو غازات غير متوقعة.", "انتفاخ أو تسرب العبوة.", "عفن أو رواسب غير معتادة أو رائحة غير طبيعية قوية.")
        FoodCategory.PACKAGED -> listOf("انتفاخ أو تسرب العبوة.", "كسر الختم أو تلف الحواف.", "عفن أو غازات أو رائحة غير طبيعية قوية بعد الفتح.")
    }

    private fun defaultStorage(category: FoodCategory): List<String> = when (category) {
        FoodCategory.FRUIT -> listOf("اتبع احتياجات النضج الخاصة بالفاكهة.", "برّد الفاكهة المقطعة سريعًا في وعاء نظيف ومغطى.")
        FoodCategory.VEGETABLE -> listOf("احفظ الخضروات جافة ومبردة عند الحاجة.", "برّد الخضروات المقطعة سريعًا.")
        FoodCategory.MEAT, FoodCategory.POULTRY, FoodCategory.SEAFOOD -> listOf("احفظها مبردة أو مجمدة دون كسر سلسلة التبريد.", "استخدم وعاءً نظيفًا ومحكمًا وتجنب التلوث المتبادل.")
        FoodCategory.DAIRY -> listOf("احفظ المنتج مبردًا ما لم تنص العبوة على غير ذلك.", "أغلق العبوة وتجنب تركها طويلًا في درجة حرارة الغرفة.")
        FoodCategory.BAKERY -> listOf("احفظ المنتج جافًا وبعيدًا عن الرطوبة.", "يمكن تجميد الحصص عند الحاجة إلى تخزين أطول.")
        FoodCategory.PREPARED -> listOf("برّد بقايا الطعام سريعًا واحفظها في الثلاجة.", "تجنب تكرار دورات التسخين والتبريد.")
        FoodCategory.DRINK -> listOf("اتبع تعليمات التبريد بعد الفتح.", "حافظ على نظافة الأغطية وأسطح الصب.")
        FoodCategory.PACKAGED -> listOf("خزّن المنتج حسب تعليمات الملصق.", "بعد الفتح اتبع تعليمات التبريد وفترة الاستخدام.")
    }
}
