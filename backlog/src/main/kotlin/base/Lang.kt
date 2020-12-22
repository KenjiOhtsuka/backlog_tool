package com.improvefuture.blt.backlog.base

interface Lang {
    val enText: String
    val jaText: String

    fun invoke(lang: String, vararg v: Any): String {
        return text(lang, *v)
    }

    fun text(lang: String = "en", vararg v: Any): String {
        if (lang == "ja")
            return String.format(jaText, *v)
        return String.format(enText, *v)
    }
}