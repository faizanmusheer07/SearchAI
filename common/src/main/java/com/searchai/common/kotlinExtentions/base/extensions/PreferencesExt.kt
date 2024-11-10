package com.searchai.common.kotlinExtentions.base.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import com.searchai.common.R

fun Fragment.isInDarkTheme(): Boolean {
    return requireActivity().getSharedPreferences(
        getString(R.string.sharedPrefName),
        Context.MODE_PRIVATE
    ).getBoolean("darktheme", false)
}
