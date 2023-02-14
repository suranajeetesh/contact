package com.kishan.util.extensionFunction

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.TextView

/**
 * Created by Jeetesh surana.
 */


fun View.show() {
    visibility = View.VISIBLE
}

fun View.show(isShow: Boolean) {
    if (isShow)
        show()
    else
        hide()
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.invisible(isInvisible: Boolean = true) {
    visibility = if (isInvisible) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

fun View.hide() {
    visibility = View.GONE
}

fun View.isVisibleThenHide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}

fun View.disable(isAlpha: Boolean = true) {
    isEnabled = false
    if (isAlpha) alpha = 0.5f
    isClickable = false
}

fun View.enable(isAlpha: Boolean = true) {
    isEnabled = true
    if (isAlpha) alpha = 1f
    isClickable = true
}

fun show(vararg views: View) {
    for (view in views) {
        view.show()
    }
}

fun hide(vararg views: View) {
    for (view in views) {
        view.hide()
    }
}

fun invisible(vararg views: View) {
    for (view in views) {
        view.invisible()
    }
}

var TextView.setHtmlText: CharSequence
    get() = text
    set(value) {
        text = value.toString().stripHtml()?.trim()
    }

fun String.stripHtml(): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}