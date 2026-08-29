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
        Word(2885, "ABUNDANT", "وفير", "أباندانت", "adjective", "Existing or available in large quantities; plentiful.", listOf("Rainfall is abundant in this fertile tropical region.", "The market offers an abundant supply of fresh fruit.", "She has abundant energy and enthusiasm for her work."), listOf("هطول الأمطار وفير في هذه المنطقة الاستوائية الخصبة.", "يوفر السوق إمداداً وفيراً من الفاكهة الطازجة.", "لديها طاقة وحماس وفيران لعملها.")),
        Word(6896, "BENEVOLENT", "كريم / خيّر", "بينيفولينت", "adjective", "Well meaning and kindly.", listOf("A benevolent donor contributed generously to support the hospital.", "The benevolent leader was deeply loved by all citizens.", "Her benevolent smile put everyone in the room at ease."), listOf("ساهم متبرع كريم بسخاء لدعم المستشفى.", "كان القائد الخيّر محبوباً للغاية من جميع المواطنين.", "أراحت ابتسامتها اللطيفة كل من كان في الغرفة.")),
        Word(15002, "CANDOR", "صراحة", "كاندور", "noun", "The quality of being open and honest in expression; frankness.", listOf("I appreciate your candor during our discussion.", "She spoke with refreshing candor about the challenges she faced.", "His honesty and candor earned him widespread respect."), listOf("أنا أقدر صراحتك أثناء مناقشتنا.", "تحدثت بصراحة مشجعة عن التحديات التي واجهتها.", "أكسبه صدقه وصراحته احتراماً واسع النطاق.")),
        Word(8866, "DILIGENT", "مجتهد", "ديلجينت", "adjective", "Having or showing care and conscientiousness in one's work.", listOf("She is a diligent student who always completes her assignments.", "His diligent efforts led to a well-deserved promotion.", "The team conducted a diligent review of the financial data."), listOf("إنها طالبة مجتهدة تكمل واجباتها دائماً.", "أدت جهوده الدؤوبة إلى ترقية مستحقة بجدارة.", "أجرى الفريق مراجعة دقيقة للبيانات المالية.")),
        Word(7542, "ELOQUENT", "فصيح", "إيلوكوانت", "adjective", "Fluent or persuasive in speaking or writing.", listOf("He made an eloquent speech that moved the entire audience.", "Her eloquent writing captures complex emotions beautifully.", "The lawyer presented an eloquent defense in court."), listOf("ألقى خطاباً فصيحاً أثر في الجمهور بأكمله.", "تجسد كتابتها الفصيحة المشاعر المعقدة بشكل جميل.", "قدم المحامي دفاعاً بليغاً في المحكمة.")),
        Word(15003, "FRUGAL", "اقتصادي / مقتصد", "فروغال", "adjective", "Sparing or economical with regard to money or food.", listOf("He led a frugal life and managed to save for a home.", "She made a frugal but delicious meal with simple ingredients.", "Being frugal allowed them to travel the world on a budget."), listOf("عاش حياة مقتصدة وتمكن من الادخار لشراء منزل.", "أعدت وجبة مقتصدة ولكنها لذيذة بمكونات بسيطة.", "أتاح لهم الاقتصاد في الإنفاق السفر حول العالم بميزانية محدودة.")),
        Word(15004, "GARRULOUS", "ثرثار", "غارولوس", "adjective", "Excessively talkative, especially on trivial matters.", listOf("The garrulous passenger talked non-stop during the whole flight.", "He became quite garrulous after having a warm cup of coffee.", "She politely listened to her garrulous neighbor for an hour."), listOf("تحدث الراكب الثرثار دون توقف طوال الرحلة بأكملها.", "أصبح ثرثاراً جداً بعد تناول فنجان قهوة دافئ.", "استمعت بأدب إلى جارها الثرثار لمدة ساعة.")),
        Word(15005, "HAUGHTY", "متكبر", "هوتي", "adjective", "Arrogantly superior and disdainful.", listOf("She gave a haughty look and refused to answer the question.", "His haughty behavior alienated many of his close colleagues.", "The haughty noble looked down upon ordinary townspeople."), listOf("ألقت نظرة متكبرة ورفضت الإجابة عن السؤال.", "أدى سلوكه المتكبر إلى نفور العديد من زملائه المقربين.", "نظر النبيل المتكبر بدونية إلى عامة أهل البلدة.")),
        Word(6377, "IMPARTIAL", "نزيه / محايد", "إمبارشال", "adjective", "Treating all rivals or disputants equally; fair and just.", listOf("An impartial judge must make decisions based solely on evidence.", "We need an impartial observer to oversee the election.", "She provided an impartial assessment of both proposals."), listOf("يجب على القاضي النزيه اتخاذ القرارات بناءً على الأدلة فقط.", "نحن بحاجة إلى مراقب محايد للإشراف على الانتخابات.", "قدمت تقييماً محايداً لكلا المقترحين.")),
        Word(15006, "JOVIAL", "مرح", "جوفيال", "adjective", "Cheerful and friendly.", listOf("He was in a jovial mood and greeted everyone warmly.", "The holiday dinner was filled with jovial laughter and stories.", "Her jovial personality made her popular among her peers."), listOf("كان في مزاج مرح ورحب بالجميع بحرارة.", "امتلأ عشاء العيد بالضحكات والقصص المرحة.", "جعلتها شخصيتها المرحة محبوبة بين أقرانها.")),
        Word(3790, "KEEN", "حريص / متحمس", "كين", "adjective", "Having or showing eagerness or enthusiasm.", listOf("He is keen on learning new languages and exploring cultures.", "She has a keen eye for detail in graphic design.", "The students showed a keen interest in the science project."), listOf("إنه حريص على تعلم لغات جديدة واستكشاف الثقافات.", "لديها عين ثاقبة للتفاصيل في التصميم الجرافيكي.", "أبدى الطلاب اهتماماً كبيراً بمشروع العلوم.")),
        Word(15007, "LETHARGIC", "خامل", "ليثارجيك", "adjective", "Affected by lethargy; sluggish and apathetic.", listOf("I felt tired and lethargic after staying up all night.", "The hot afternoon made all the animals in the zoo lethargic.", "A balanced diet helps prevent feeling lethargic during the day."), listOf("شعرت بالتعب والخمول بعد السهر طوال الليل.", "جعلت فترة ما بعد الظهر الحارة جميع الحيوانات في حديقة الحيوان خاملة.", "يساعد النظام الغذائي المتوازن على منع الشعور بالخمول خلال النهار.")),
        Word(7606, "MITIGATE", "تخفيف / يلطف", "ميتيجيت", "verb", "Make less severe, serious, or painful.", listOf("The government took immediate steps to mitigate the crisis.", "Wearing a helmet helps mitigate the risk of serious head injuries.", "Planting trees can help mitigate the effects of air pollution."), listOf("اتخذت الحكومة خطوات فورية لتخفيف وطأة الأزمة.", "يساعد ارتداء الخوذة في تقليل خطر إصابات الرأس الخطيرة.", "يمكن أن تساعد زراعة الأشجار في تخفيف آثار تلوث الهواء.")),
        Word(9524, "NOSTALGIA", "الحنين للماضي", "نوستالجيا", "noun", "A sentimental longing or wistful affection for the past.", listOf("Looking at childhood photographs brought a wave of nostalgia.", "The old songs filled the room with sweet nostalgia.", "He felt a deep sense of nostalgia when visiting his hometown."), listOf("أثارت رؤية صور الطفولة موجة من الحنين إلى الماضي.", "ملأت الأغاني القديمة الغرفة بحنين عذب إلى الماضي.", "شعر بشعور عميق بالحنين عندما زار مسقط رأسه.")),
        Word(5484, "OBSOLETE", "عفا عليه الزمن", "أوبسوليت", "adjective", "No longer produced or used; out of date.", listOf("Smartphones have made traditional pagers largely obsolete.", "The company replaced its obsolete machinery with automated tools.", "Many old computer systems are now completely obsolete."), listOf("جعلت الهواتف الذكية أجهزة النداء الآلي التقليدية ملغاة تقريباً.", "استبدلت الشركة آلاتها القديمة بأدوات آلية حديثة.", "أصبحت العديد من أنظمة الكمبيوتر القديمة الآن عفا عليها الزمن تماماً.")),
        Word(15008, "PENSIVE", "متأمل / غارق في التفكير", "بينسيف", "adjective", "Engaged in, involving, or reflecting deep or serious thought.", listOf("She sat by the window in a pensive mood, watching the rain.", "He gave a pensive sigh before answering the difficult question.", "Her pensive expression suggested she was thinking deeply."), listOf("جلست بجانب النافذة في حالة تأمل تراقب المطر.", "أطلق تنهيدة متأملة قبل الإجابة عن السؤال الصعب.", "أوحت ملامحها الغارقة في التفكير بأنها كانت تفكر بعمق.")),
        Word(9045, "QUAINT", "جاذبية قديمة / غريب", "كوانت", "adjective", "Attractively unusual or old-fashioned.", listOf("They spent their holiday in a quaint cottage by the seaside.", "The village has quaint narrow streets paved with cobblestones.", "We enjoyed having lunch in a quaint historic cafe."), listOf("قضوا عطلتهم في كوخ ريفي جذاب بجانب شاطئ البحر.", "تتميز القرية بشوارع ضيقة جذابة مرصوفة بالحصى.", "استمتعنا بتناول الغداء في مقهى تاريخي ساحر.")),
        Word(8573, "RESILIENT", "مرن / صامد", "ريزيليانت", "adjective", "Able to withstand or recover quickly from difficult conditions.", listOf("The resilient community rebuilt the town after the storm.", "Children are often remarkably resilient when facing changes.", "A resilient economy can absorb unexpected global shocks."), listOf("أعاد المجتمع الصامد بناء البلدة بعد العاصفة.", "غالباً ما يكون الأطفال مرنين بشكل ملحوظ عند مواجهة التغييرات.", "يمكن للاقتصاد المرن استيعاب الصدمات العالمية غير المتوقعة.")),
        Word(15009, "SCRUPULOUS", "دقيق / ضميري", "سكروبولوس", "adjective", "Diligent, thorough, and extremely attentive to details.", listOf("The accountant was scrupulous in maintaining accurate records.", "She conducted scrupulous research to verify all the facts.", "He is known for his scrupulous honesty in all business dealings."), listOf("كان المحاسب دقيقاً للغاية في الاحتفاظ بسجلات صحيحة.", "أجرت بحثاً دقيقاً للغاية للتحقق من جميع الحقائق.", "هو معروف بأمانته الشديدة في جميع تعاملاته التجارية.")),
        Word(15010, "TACITURN", "قليل الكلام", "تاسيتورن", "adjective", "Reserved or uncommunicative in speech; saying little.", listOf("He was a taciturn man who rarely expressed his opinions.", "Her taciturn nature made her seem mysterious to others.", "Despite being taciturn, he was a very caring and loyal friend."), listOf("كان رجلاً قليل الكلام نادراً ما يعبر عن آرائه.", "طبيعتها الكتومة جعلتها تبدو غامضة للآخرين.", "على الرغم من كونه قليل الكلام، كان صديقاً وفياً ومخلصاً للغاية.")),
        Word(8465, "UBIQUITOUS", "واسع الانتشار / كلي الوجود", "يوبيكويتوس", "adjective", "Present, appearing, or found everywhere.", listOf("Smartphones have become ubiquitous in modern daily life.", "Coffee shops are now ubiquitous in almost every major city.", "The ubiquitous presence of social media influences communication."), listOf("أصبحت الهواتف الذكية واسعة الانتشار في الحياة اليومية الحديثة.", "أصبحت المقاهي منتشرة في كل مكان تقريباً في كل مدينة كبرى.", "يؤثر الانتشار الواسع لوسائل التواصل الاجتماعي على التواصل.")),
        Word(15011, "VENERATE", "يوقر / يحترم", "فينيريت", "verb", "Regard with great respect; revere.", listOf("Many cultures venerate their elders for their wisdom.", "Students venerate the dedicated professor for his guidance.", "The ancient temple was built to venerate the sacred deities."), listOf("توقر العديد من الثقافات كبار السن لحكمتهم وخبرتهم.", "يوقر الطلاب الأستاذ المخلص لتوجيهه وإرشاده.", "تم بناء المعبد القديم لتبجيل وتوقير الآلهة المقدسة.")),
        Word(8191, "WARY", "حذر", "ويري", "adjective", "Feeling or showing caution about possible dangers or problems.", listOf("Be wary of strangers offering deals that sound too good to be true.", "The cat was wary of the energetic new puppy in the house.", "Investors remained wary amid rising geopolitical tensions."), listOf("كن حذراً من الغرباء الذين يقدمون صفقات تبدو أفضل من أن تكون حقيقية.", "كانت القطة حذرة من الجرو الجديد المفعم بالحيوية في المنزل.", "ظل المستثمرون حذرين وسط تصاعد التوترات الجيوسياسية.")),
        Word(9063, "ZENITH", "القمة / الأوج", "زينيث", "noun", "The time at which something is most powerful or successful.", listOf("He retired when he was at the zenith of his career.", "The Roman Empire reached its zenith during the second century.", "Her musical fame was at its zenith in the late nineties."), listOf("تقاعد عندما كان في أوج مسيرته المهنية.", "بلغت الإمبراطورية الرومانية أوجها خلال القرن الثاني.", "كانت شهرتها الموسيقية في ذروتها في أواخر التسعينيات.")),
        Word(15012, "APPREHENSIVE", "قلق / متخوف", "أبريهينسيف", "adjective", "Anxious or fearful that something bad or unpleasant will happen.", listOf("She felt apprehensive about moving to an unfamiliar country.", "Many applicants were apprehensive before the job interview.", "He gave an apprehensive glance at the dark storm clouds."), listOf("شعرت بالقلق والتردد بشأن الانتقال إلى بلد غير مألوف.", "كان العديد من المتقدمين متخوفين قبل مقابلة العمل.", "ألقى نظرة قلقة على سحب العاصفة المظلمة.")),
        Word(9681, "BELLIGERENT", "عدواني", "بيليجيرنت", "adjective", "Hostile and aggressive.", listOf("His belligerent attitude made it difficult to reach an agreement.", "The belligerent customer shouted loudly at the store clerk.", "Diplomats worked to ease tensions between the belligerent states."), listOf("موقفه العدواني جعل من الصعب التوصل إلى اتفاق.", "صرخ العميل العدواني بصوت عالٍ في وجه موظف المتجر.", "عمل الدبلوماسيون على تخفيف حدة التوتر بين الدول المتحاربة.")),
        Word(15013, "COMPLACENT", "راضٍ عن نفسه / مغرور", "كومبليسينت", "adjective", "Showing smug or uncritical satisfaction with oneself or one's achievements.", listOf("Do not become complacent after winning your first tournament.", "The leading company grew complacent and lost its market share.", "Successful leaders never allow their teams to become complacent."), listOf("لا تركن إلى الرضا عن النفس بعد الفوز ببطولتك الأولى.", "أصيبت الشركة الرائدة بالغرور والرضا الزائد ففقدت حصتها السوقية.", "لا يسمح القادة الناجحون لفرقهم أبداً بالوقوع في فخ الرضا عن النفس."))
    )
}
