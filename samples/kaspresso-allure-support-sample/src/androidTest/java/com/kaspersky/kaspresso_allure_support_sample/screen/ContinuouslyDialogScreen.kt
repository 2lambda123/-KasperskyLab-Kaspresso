package com.kaspersky.kaspresso_allure_support_sample.screen

import com.agoda.kakao.text.KTextView
import com.kaspersky.kaspresso_allure_support_sample.R
import com.kaspersky.kaspresso.screens.KScreen

object ContinuouslyDialogScreen : KScreen<ContinuouslyDialogScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val dialogTitle = KTextView {
        withText(R.string.continuously_dialog_title)
    }
}
