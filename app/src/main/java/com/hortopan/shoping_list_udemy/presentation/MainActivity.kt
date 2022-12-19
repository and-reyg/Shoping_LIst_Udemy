package com.hortopan.shoping_list_udemy.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.hortopan.shoping_list_udemy.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java] // или точно такое же .get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            Log.d("MainActivityTest", it.toString())
            //проверка на удаление єлемента
            if(count == 0){
                count++
                val item = it[0]
                viewModel.changeEnableState(item)
            }
        }
    }
}