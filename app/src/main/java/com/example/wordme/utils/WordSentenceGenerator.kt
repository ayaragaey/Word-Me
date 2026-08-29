package com.example.wordme.utils

import com.example.wordme.data.Word
import java.util.Locale

object WordSentenceGenerator {

    private data class SentenceTemplate(
        val english: String,
        val arabic: String
    )

    private val nounTemplates = listOf(
        SentenceTemplate(
            "Understanding the value of [word] can help in many situations.",
            "إن فهم قيمة [translation] يمكن أن يساعد في العديد من المواقف."
        ),
        SentenceTemplate(
            "They spent time discussing the importance of [word].",
            "أمضوا وقتاً في مناقشة أهمية [translation]."
        ),
        SentenceTemplate(
            "The concept of [word] plays an important role in everyday life.",
            "يلعب مفهوم [translation] دوراً مهماً في الحياة اليومية."
        ),
        SentenceTemplate(
            "She shared some interesting thoughts about [word].",
            "شاركت بعض الأفكار المثيرة للاهتمام حول [translation]."
        ),
        SentenceTemplate(
            "He wanted to learn more about [word] before making a decision.",
            "أراد معرفة المزيد عن [translation] قبل اتخاذ القرار."
        ),
        SentenceTemplate(
            "Recent discussions focused on the impact of [word].",
            "ركزت المناقشات الأخيرة على تأثير [translation]."
        ),
        SentenceTemplate(
            "Everyone agreed that [word] deserves careful attention.",
            "اتفق الجميع على أن [translation] يستحق اهتماماً دقيقاً."
        ),
        SentenceTemplate(
            "We need to consider how [word] affects our overall plans.",
            "نحن بحاجة إلى النظر في كيفية تأثير [translation] على خططنا العامة."
        )
    )

    private val verbTemplates = listOf(
        SentenceTemplate(
            "You should [word] the instructions carefully before starting.",
            "يجب عليك [translation] التعليمات بعناية قبل البدء."
        ),
        SentenceTemplate(
            "She decided to [word] a detailed plan for the upcoming project.",
            "قررت [translation] خطة مفصلة للمشروع القادم."
        ),
        SentenceTemplate(
            "They worked together to [word] the overall quality of the service.",
            "عملوا معاً لـ [translation] الجودة العامة للخدمة."
        ),
        SentenceTemplate(
            "We need to [word] the problem as soon as possible.",
            "نحن بحاجة إلى [translation] المشكلة في أقرب وقت ممكن."
        ),
        SentenceTemplate(
            "He tried to [word] new methods to improve team efficiency.",
            "حاول [translation] أساليب جديدة لتحسين كفاءة الفريق."
        ),
        SentenceTemplate(
            "The team was able to [word] the task ahead of schedule.",
            "تمكن الفريق من [translation] المهمة قبل الموعد المحدد."
        ),
        SentenceTemplate(
            "He always makes sure to [word] on time for important meetings.",
            "يحرص دائماً على أن [translation] في الوقت المحدد للمواعيد المهمة."
        ),
        SentenceTemplate(
            "It takes patience and practice to [word] with confidence.",
            "يتطلب الأمر صبراً وممارسة لـ [translation] بثقة."
        ),
        SentenceTemplate(
            "She made an effort to [word] regularly throughout the week.",
            "بذلت جهداً لـ [translation] بانتظام طوال الأسبوع."
        ),
        SentenceTemplate(
            "They gathered all the resources needed to [word] successfully.",
            "جمعوا جميع الموارد اللازمة لـ [translation] بنجاح."
        )
    )

    private val adjectiveTemplates = listOf(
        SentenceTemplate(
            "Finding a [word] approach was essential to solving the problem.",
            "كان إيجاد نهج [translation] أمراً ضرورياً لحل المشكلة."
        ),
        SentenceTemplate(
            "They are actively searching for a more [word] solution to this challenge.",
            "إنهم يبحثون بنشاط عن حل أكثر [translation] لهذا التحدي."
        ),
        SentenceTemplate(
            "The final report provided a [word] overview of the results.",
            "قدم التقرير النهائي نظرة عامة [translation] على النتائج."
        ),
        SentenceTemplate(
            "She offered a [word] perspective that helped clarify the matter.",
            "قدمت وجهة نظر [translation] ساعدت في توضيح الأمر."
        ),
        SentenceTemplate(
            "It was clear that maintaining a [word] standard would benefit everyone.",
            "كان من الواضح أن الحفاظ على مستوى [translation] سيفيد الجميع."
        ),
        SentenceTemplate(
            "Their decision led to a remarkably [word] outcome in the long run.",
            "أدى قرارهم إلى نتيجة [translation] بشكل ملحوظ على المدى الطويل."
        ),
        SentenceTemplate(
            "He is known for his [word] attitude during critical moments.",
            "هو معروف بموقفه الـ[translation] في اللحظات الحرجة."
        ),
        SentenceTemplate(
            "The presentation was very [word] and well received by the audience.",
            "كان العرض التقديمي [translation] للغاية ولاقى استحسان الجمهور."
        )
    )

    private val adverbTemplates = listOf(
        SentenceTemplate(
            "She managed to handle the situation [word].",
            "تمكنت من التعامل مع الموقف بـ [translation]."
        ),
        SentenceTemplate(
            "The work was completed [word], meeting all requirements.",
            "تم إنجاز العمل بـ [translation] مستوفياً جميع المتطلبات."
        ),
        SentenceTemplate(
            "He responded [word] when asked about the plan.",
            "أجاب بـ [translation] عندما سُئل عن الخطة."
        ),
        SentenceTemplate(
            "The system operates [word] even during peak hours.",
            "يعمل النظام بـ [translation] حتى في ساعات الذروة."
        ),
        SentenceTemplate(
            "They performed [word] throughout the entire project.",
            "أدوا عملهم بـ [translation] طوال المشروع بأكمله."
        ),
        SentenceTemplate(
            "She explained the details [word] so everyone could understand.",
            "شرحت التفاصيل بـ [translation] حتى يتمكن الجميع من الفهم."
        ),
        SentenceTemplate(
            "He acted [word] to resolve the misunderstanding.",
            "تصرف بـ [translation] لحل سوء التفاهم."
        )
    )

    private val generalTemplates = listOf(
        SentenceTemplate(
            "They discussed how [word] influences the final outcome.",
            "ناقشوا كيف يؤثر [translation] على النتيجة النهائية."
        ),
        SentenceTemplate(
            "Understanding the role of [word] is essential for progress.",
            "إن فهم دور [translation] أمر أساسي للتقدم."
        ),
        SentenceTemplate(
            "She wrote a thoughtful piece about [word].",
            "كتبت مقالاً عميقاً حول [translation]."
        ),
        SentenceTemplate(
            "Many factors, including [word], contributed to the result.",
            "ساهمت العديد من العوامل، بما في ذلك [translation]، في النتيجة."
        ),
        SentenceTemplate(
            "He shared a clear perspective regarding [word].",
            "شارك وجهة نظر واضحة بخصوص [translation]."
        )
    )

    fun generateNewSentences(word: Word): Pair<List<String>, List<String>> {
        val type = word.type.lowercase(Locale.ROOT).trim()
        val templates = when {
            type.contains("noun") -> nounTemplates
            type.contains("verb") -> verbTemplates
            type.contains("adject") -> adjectiveTemplates
            type.contains("adverb") -> adverbTemplates
            else -> generalTemplates
        }

        val chosen = templates.shuffled().take(3)
        val englishWord = word.word.lowercase(Locale.ROOT)
        val cleanAr = word.translation
            .split("/", "،", ",")
            .firstOrNull()
            ?.trim()
            ?.removePrefix("الـ")
            ?.removePrefix("ال")
            ?: word.translation.trim()

        val examples = chosen.map { template ->
            template.english
                .replace("[word]", englishWord)
                .replace("[Word]", englishWord.replaceFirstChar { it.uppercase() })
        }

        val translations = chosen.map { template ->
            template.arabic.replace("[translation]", cleanAr)
        }

        return Pair(examples, translations)
    }
}
