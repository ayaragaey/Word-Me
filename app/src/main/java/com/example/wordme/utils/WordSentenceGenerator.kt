package com.example.wordme.utils

import com.example.wordme.data.Word
import java.util.Locale

object WordSentenceGenerator {

    private data class SentenceTemplate(
        val english: String,
        val arabic: String
    )

    private val nounTemplates = listOf(
        SentenceTemplate("I always keep a [word] nearby.", "أحتفظ دائمًا بـ ([translation]) بالقرب مني."),
        SentenceTemplate("We need to replace the [word] as soon as possible.", "نحن بحاجة إلى استبدال ([translation]) في أقرب وقت ممكن."),
        SentenceTemplate("The new [word] works much better than the old one.", "يعمل ([translation]) الجديد بشكل أفضل بكثير من القديم."),
        SentenceTemplate("She described the [word] in detail.", "لقد وصفت ([translation]) بالتفصيل."),
        SentenceTemplate("He showed me how to use the [word] properly.", "لقد أراني كيفية استخدام ([translation]) بشكل صحيح."),
        SentenceTemplate("They gathered all the information about the [word].", "جمعوا كل المعلومات المتعلقة بـ ([translation]).")
    )

    private val verbTemplates = listOf(
        SentenceTemplate("It is important to [word] every single day.", "من المهم أن ([translation]) كل يوم."),
        SentenceTemplate("She tried to [word] the situation.", "حاولت أن ([translation]) الموقف."),
        SentenceTemplate("You should not [word] without a clear plan.", "لا ينبغي عليك أن ([translation]) بدون خطة واضحة."),
        SentenceTemplate("They decided to [word] after discussing it.", "قرروا أن ([translation]) بعد مناقشة الأمر."),
        SentenceTemplate("He helped me [word] the problem.", "ساعدني في أن ([translation]) المشكلة."),
        SentenceTemplate("We can [word] this together if we work hard.", "يمكننا أن ([translation]) هذا معًا إذا عملنا بجد.")
    )

    private val adjectiveTemplates = listOf(
        SentenceTemplate("This approach is very [word] for our project.", "هذا النهج ([translation]) للغاية لمشروعنا."),
        SentenceTemplate("She wants to make the room look more [word].", "تريد أن تجعل الغرفة تبدو أكثر ([translation])."),
        SentenceTemplate("He became [word] after hearing the news.", "أصبح ([translation]) بعد سماع الأخبار."),
        SentenceTemplate("It was a [word] moment for all of us.", "لقد كانت لحظة ([translation]) لنا جميعًا."),
        SentenceTemplate("They are looking for a [word] solution.", "إنهم يبحثون عن حل ([translation])."),
        SentenceTemplate("Nothing is more [word] than learning new skills.", "لا شيء أكثر ([translation]) من تعلم مهارات جديدة.")
    )

    private val adverbTemplates = listOf(
        SentenceTemplate("She spoke [word] during the presentation.", "تحدثت ([translation]) خلال العرض التقديمي."),
        SentenceTemplate("He walked [word] down the street.", "مشى ([translation]) في الشارع."),
        SentenceTemplate("The machine runs [word] without any issues.", "تعمل الآلة ([translation]) دون أي مشاكل."),
        SentenceTemplate("They completed the work [word] and on time.", "أكملوا العمل ([translation]) وفي الوقت المحدد."),
        SentenceTemplate("Please write [word] so I can read it.", "يرجى الكتابة ([translation]) حتى أتمكن من قراءتها."),
        SentenceTemplate("He always acts [word] under pressure.", "يتصرف دائمًا ([translation]) تحت الضغط.")
    )

    private val generalTemplates = listOf(
        SentenceTemplate("They talked about the concept of [word] yesterday.", "تحدثوا عن مفهوم ([translation]) بالأمس."),
        SentenceTemplate("We must understand [word] to succeed.", "يجب أن نفهم ([translation]) لننجح."),
        SentenceTemplate("She wrote a short paragraph about [word].", "كتبت فقرة قصيرة عن ([translation])."),
        SentenceTemplate("It is related to [word] in many ways.", "إنه مرتبط بـ ([translation]) بطرق عديدة."),
        SentenceTemplate("He explained the meaning of [word] clearly.", "شرح معنى ([translation]) بوضوح."),
        SentenceTemplate("We observed the effects of [word] in the study.", "لاحظنا تأثيرات ([translation]) في الدراسة.")
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

        // Shuffle templates and pick 3
        val chosen = templates.shuffled().take(3)
        val englishWord = word.word.lowercase(Locale.ROOT)
        val arabicTranslation = word.translation

        val examples = chosen.map { template ->
            template.english
                .replace("[word]", englishWord)
                .replace("[Word]", englishWord.replaceFirstChar { it.uppercase() })
        }

        val translations = chosen.map { template ->
            template.arabic.replace("[translation]", arabicTranslation)
        }

        return Pair(examples, translations)
    }
}
