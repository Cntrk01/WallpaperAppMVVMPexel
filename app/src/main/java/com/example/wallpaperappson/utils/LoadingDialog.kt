package com.example.wallpaperappson.utils

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.wallpaperappson.R

class LoadingDialog(val fragment: Fragment) {

    private lateinit var isDialog:AlertDialog

    fun startLoading(){
        val inflater=fragment.layoutInflater
        val dialogView=inflater.inflate(R.layout.loading_dialog,null)
        val builder=AlertDialog.Builder(fragment.activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog=builder.create()
        isDialog.show()
    }

    fun stopLoading(){
        isDialog.dismiss()
    }
}