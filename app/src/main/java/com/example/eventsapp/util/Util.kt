package com.example.eventsapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Util {

    fun isEmailValid(email : String): Boolean {
        val emailSplit = email.split("@")

        return if(emailSplit.size >= 2) {
            email.isNotEmpty() && emailSplit[0].isNotEmpty() && emailSplit[1].isNotEmpty()
        } else {
            false
        }
    }

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.isNotEmpty()
    }

    fun hideKeyboard(context: Context, view : View){
        val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
}