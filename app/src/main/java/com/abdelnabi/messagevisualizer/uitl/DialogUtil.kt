package com.abdelnabi.messagevisualizer.uitl

import android.app.Dialog
import android.content.Context
import com.abdelnabi.messagevisualizer.R

class DialogUtil( context: Context) : Dialog(context){

    init {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun showDialog(){
        show()
    }

    fun hideDialog(){
        dismiss()
        cancel()
    }
}