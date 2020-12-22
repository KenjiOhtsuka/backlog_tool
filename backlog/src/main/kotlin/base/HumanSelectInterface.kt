package com.improvefuture.blt.backlog.base

interface HumanSelectInterface {
    val humanSelectId: Long?

    fun humanSelectName(lang: String? = null): String
}