package com.example.wordme.ui

data class Word(
    val word: String,
    val translation: String,
    val pronunciation: String,
    val type: String,
    val definition: String,
    val examples: List<String>
)

val mockWordsList = listOf(
    Word(
        word = "HESITANT",
        translation = "متردد",
        pronunciation = "هِزِتَنت",
        type = "adjective",
        definition = "Not certain or confident about doing something.",
        examples = listOf(
            "I was hesitant to message her.",
            "She was hesitant about accepting the offer.",
            "Don't be hesitant to ask questions."
        )
    ),
    Word(
        word = "RELIABLE",
        translation = "موثوق",
        pronunciation = "رِلَايَبِل",
        type = "adjective",
        definition = "Consistently good in quality or performance; able to be trusted.",
        examples = listOf(
            "We need a reliable method of communication.",
            "She is a very reliable and hard-working person.",
            "The car is old but it is still reliable."
        )
    ),
    Word(
        word = "OVERWHELMED",
        translation = "مغمور",
        pronunciation = "أُوفَرهِلمد",
        type = "adjective",
        definition = "Having a strong emotional effect on; feeling like you have too much to deal with.",
        examples = listOf(
            "I was overwhelmed by the support I received.",
            "She felt overwhelmed with work.",
            "Don't get overwhelmed by the details."
        )
    ),
    Word(
        word = "REMARKABLE",
        translation = "ملحوظ",
        pronunciation = "رِمَاركَبِل",
        type = "adjective",
        definition = "Worthy of attention; striking or extraordinary.",
        examples = listOf(
            "It was a remarkable achievement.",
            "She has a remarkable talent for music.",
            "The view from the top is remarkable."
        )
    ),
    Word(
        word = "AWKWARD",
        translation = "محرج",
        pronunciation = "أُوكوَرْد",
        type = "adjective",
        definition = "Causing or feeling embarrassment or inconvenience; hard to deal with.",
        examples = listOf(
            "There was an awkward silence in the room.",
            "He felt awkward asking for money.",
            "It was an awkward situation for everyone."
        )
    )
)
