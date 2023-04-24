package com.example.pixabayseachimage.utils

import java.util.*
import kotlin.math.ln
import kotlin.math.pow


fun toKFormat(number: Long): String {
    if (number < 1000) return "$number"
    val exp = (ln(number.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f %c", number / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
}


fun toTagFormat(tagName: String): String {
    return "${AppConstants.HASH_TAG}${
        tagName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }"
}
