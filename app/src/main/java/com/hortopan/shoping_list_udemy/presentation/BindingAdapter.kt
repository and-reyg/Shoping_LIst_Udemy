package com.hortopan.shoping_list_udemy.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.hortopan.shoping_list_udemy.R

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean){
    val message = if (isError){
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    //если в message будет лежать строка то сообщение отобразиться, если null то нет
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean){
    val message = if (isError){
        textInputLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    //если в message будет лежать строка то сообщение отобразиться, если null то нет
    textInputLayout.error = message
}