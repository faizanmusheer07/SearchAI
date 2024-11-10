package com.searchai.common.kotlinExtentions.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarMessage {

    fun makeSnackbar(view: View,msg:String){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()
    }

}