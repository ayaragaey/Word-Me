package com.example.wordme.data

object WordData {

    val mockWordsList = listOf(
        Word(
            id = 6553,
            word = "MECHANICALLY",
            translation = "ميكانيكياً",
            pronunciation = "ميكانيكلي",
            type = "adverb",
            definition = "In a mechanical manner; by a mechanism.",
            examples = listOf(
                "This door opens mechanically.",
                "She responded mechanically during the conversation.",
                "He completed the task mechanically."
            ),
            exampleTranslations = listOf(
                "يفتح هذا الباب ميكانيكياً.",
                "أجابت بشكل آلي (ميكانيكي) خلال المحادثة.",
                "أنجز المهمة بطريقة آلية."
            ),
            level = "C1",
            category = "technical"
        ),
        Word(
            id = 10000,
            word = "HESITANT",
            translation = "متردد",
            pronunciation = "هِزِتَنت",
            type = "adjective",
            definition = "Unsure or slow to act because you are uncertain.",
            examples = listOf(
                "She was hesitant to accept the offer.",
                "He seemed hesitant about making the decision.",
                "Don't be hesitant to ask for help."
            ),
            exampleTranslations = listOf(
                "كانت مترددة في قبول العرض.",
                "بدا متردداً بشأن اتخاذ القرار.",
                "لا تكن متردداً في طلب المساعدة."
            ),
            level = "B1",
            category = "general"
        ),
        Word(
            id = 2225,
            word = "RELIABLE",
            translation = "موثوق",
            pronunciation = "رِلايَبُل",
            type = "adjective",
            definition = "Able to be trusted or depended on.",
            examples = listOf(
                "She is a reliable member of the team.",
                "We need a reliable internet connection.",
                "He has always been reliable when I need help."
            ),
            exampleTranslations = listOf(
                "إنها عضو موثوق في الفريق.",
                "نحن بحاجة إلى اتصال إنترنت موثوق.",
                "لقد كان دائماً موثوقاً عندما أحتاج إلى المساعدة."
            ),
            level = "B1",
            category = "general"
        ),
        Word(
            id = 15001,
            word = "OVERWHELMED",
            translation = "مُرهَق / غارق في المشاعر",
            pronunciation = "أُوفَرهِلمد",
            type = "adjective",
            definition = "Having a strong emotional effect on; feeling like you have too much to deal with.",
            examples = listOf(
                "I was overwhelmed by the support I received.",
                "She felt overwhelmed with work.",
                "Don't get overwhelmed by the details."
            ),
            exampleTranslations = listOf(
                "كنت غارقاً بمشاعر الامتنان للدعم الذي تلقيته.",
                "شعرت بالإرهاق (الغرق) من كثرة العمل.",
                "لا تدع التفاصيل ترهقك."
            ),
            level = "B2",
            category = "general"
        ),
        Word(
            id = 2041,
            word = "REMARKABLE",
            translation = "لافت للنظر",
            pronunciation = "رِماركَبُل",
            type = "adjective",
            definition = "Unusual or impressive enough to deserve attention.",
            examples = listOf(
                "She made remarkable progress in English.",
                "The team achieved a remarkable result.",
                "He has a remarkable ability to remember names."
            ),
            exampleTranslations = listOf(
                "حققت تقدماً لافتاً للنظر في اللغة الإنجليزية.",
                "حقق الفريق نتيجة رائعة.",
                "لديه قدرة لافتة للنظر على تذكر الأسماء."
            ),
            level = "B2",
            category = "general"
        ),
        Word(
            id = 5559,
            word = "AWKWARD",
            translation = "محرج / غير مريح",
            pronunciation = "أُوكوَرْد",
            type = "adjective",
            definition = "Causing or feeling embarrassment or inconvenience; hard to deal with.",
            examples = listOf(
                "There was an awkward silence in the room.",
                "He felt awkward asking for money.",
                "It was an awkward situation for everyone."
            ),
            exampleTranslations = listOf(
                "كانت هناك صمت محرج في الغرفة.",
                "شعر بالإحراج عند طلب المال.",
                "لقد كان موقفاً محرجاً للجميع."
            ),
            level = "B2",
            category = "general"
        )
    )

    // Initial 27 completed words for the user to satisfy Words Learned = 27
    val initialLearnedWords = listOf(
        Word(2885, "ABUNDANT", "وفير", "أباندانت", "adjective", "Existing or available in large quantities; plentiful.", listOf("Rainfall is abundant in this region."), listOf("هطول الأمطار وفير في هذه المنطقة.")),
        Word(6896, "BENEVOLENT", "كريم / خيّر", "بينيفولينت", "adjective", "Well meaning and kindly.", listOf("A benevolent donor helper."), listOf("متبرع خيّر ومساعد.")),
        Word(15002, "CANDOR", "صراحة", "كاندور", "noun", "The quality of being open and honest in expression; frankness.", listOf("I appreciate your candor."), listOf("أنا أقدر صراحتك.")),
        Word(8866, "DILIGENT", "مجتهد", "ديلجينت", "adjective", "Having or showing care and conscientiousness in one's work.", listOf("She is a diligent student."), listOf("إنها طالبة مجتهدة.")),
        Word(7542, "ELOQUENT", "فصيح", "إيلوكوانت", "adjective", "Fluent or persuasive in speaking or writing.", listOf("He made an eloquent speech."), listOf("ألقى خطاباً فصيحاً.")),
        Word(15003, "FRUGAL", "اقتصادي / مقتصد", "فروغال", "adjective", "Sparing or economical with regard to money or food.", listOf("He led a frugal life."), listOf("عاش حياة مقتصدة.")),
        Word(15004, "GARRULOUS", "ثرثار", "غارولوس", "adjective", "Excessively talkative, especially on trivial matters.", listOf("A garrulous neighbor."), listOf("جار ثرثار.")),
        Word(15005, "HAUGHTY", "متكبر", "هوتي", "adjective", "Arrogantly superior and disdainful.", listOf("She gave a haughty look."), listOf("ألقت نظرة متكبرة.")),
        Word(6377, "IMPARTIAL", "نزيه / محايد", "إمبارشال", "adjective", "Treating all rivals or disputants equally; fair and just.", listOf("An impartial judge."), listOf("قاضٍ نزيه.")),
        Word(15006, "JOVIAL", "مرح", "جوفيال", "adjective", "Cheerful and friendly.", listOf("He was in a jovial mood."), listOf("كان في مزاج مرح.")),
        Word(3790, "KEEN", "حريص / متحمس", "كين", "adjective", "Having or showing eagerness or enthusiasm.", listOf("He is keen on learning."), listOf("إنه حريص على التعلم.")),
        Word(15007, "LETHARGIC", "خامل", "ليثارجيك", "adjective", "Affected by lethargy; sluggish and apathetic.", listOf("I felt tired and lethargic."), listOf("شعرت بالتعب والخمول.")),
        Word(7606, "MITIGATE", "تخفيف / يلطف", "ميتيجيت", "verb", "Make less severe, serious, or painful.", listOf("To mitigate the effects of the crisis."), listOf("لتخفيف آثار الأزمة.")),
        Word(9524, "NOSTALGIA", "الحنين للماضي", "نوستالجيا", "noun", "A sentimental longing or wistful affection for the past.", listOf("A wave of nostalgia."), listOf("موجة من الحنين إلى الماضي.")),
        Word(5484, "OBSOLETE", "عفا عليه الزمن", "أوبسوليت", "adjective", "No longer produced or used; out of date.", listOf("Obsolete technology."), listOf("تكنولوجيا عفا عليها الزمن.")),
        Word(15008, "PENSIVE", "متأمل / غارق في التفكير", "بينسيف", "adjective", "Engaged in, involving, or reflecting deep or serious thought.", listOf("She looked pensive."), listOf("بدت غارقة في التفكير.")),
        Word(9045, "QUAINT", "جاذبية قديمة / غريب", "كوانت", "adjective", "Attractively unusual or old-fashioned.", listOf("A quaint cottage."), listOf("كوخ ريفي جذاب قديم.")),
        Word(8573, "RESILIENT", "مرن", "ريزيليانت", "adjective", "Able to withstand or recover quickly from difficult conditions.", listOf("A resilient economy."), listOf("اقتصاد مرن.")),
        Word(15009, "SCRUPULOUS", "دقيق / ضميري", "سكروبولوس", "adjective", "Diligent, thorough, and extremely attentive to details.", listOf("Scrupulous research."), listOf("بحث دقيق للغاية.")),
        Word(15010, "TACITURN", "قليل الكلام", "تاسيتورن", "adjective", "Reserved or uncommunicative in speech; saying little.", listOf("A taciturn man."), listOf("رجل قليل الكلام.")),
        Word(8465, "UBIQUITOUS", "واسع الانتشار / كلي الوجود", "يوبيكويتوس", "adjective", "Present, appearing, or found everywhere.", listOf("Mobile phones are ubiquitous."), listOf("الهواتف المحمولة واسعة الانتشار.")),
        Word(15011, "VENERATE", "يوقر / يحترم", "فينيريت", "verb", "Regard with great respect; revere.", listOf("We venerate our ancestors."), listOf("نحن نوقر أجدادنا.")),
        Word(8191, "WARY", "حذر", "ويري", "adjective", "Feeling or showing caution about possible dangers or problems.", listOf("Be wary of strangers."), listOf("كن حذراً من الغرباء.")),
        Word(9063, "ZENITH", "القمة / الأوج", "زينيث", "noun", "The time at which something is most powerful or successful.", listOf("At the zenith of his career."), listOf("في أوج مسيرته المهنية.")),
        Word(15012, "APPREHENSIVE", "قلق / متخوف", "أبريهينسيف", "adjective", "Anxious or fearful that something bad or unpleasant will happen.", listOf("Apprehensive about the exam."), listOf("قلق بشأن الامتحان.")),
        Word(9681, "BELLIGERENT", "عدواني", "بيليجيرنت", "adjective", "Hostile and aggressive.", listOf("A belligerent attitude."), listOf("موقف عدواني.")),
        Word(15013, "COMPLACENT", "راضٍ عن نفسه / مغرور", "كومبليسينت", "adjective", "Showing smug or uncritical satisfaction with oneself or one's achievements.", listOf("Don't become complacent."), listOf("لا تصبح راضياً عن نفسك بشكل أعمى."))
    )
}
