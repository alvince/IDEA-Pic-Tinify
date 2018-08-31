package com.alvincezy.tinypic2.ui

import javax.swing.text.JTextComponent

/*
 * Created by alvince on 18-8-31.
 *
 * @author alvince.zy@gmail.com
 */

fun JTextComponent.text(): String {
    val text = this.text
    if (text.isNullOrEmpty()) {
        return ""
    }
    return text.trim()
}
