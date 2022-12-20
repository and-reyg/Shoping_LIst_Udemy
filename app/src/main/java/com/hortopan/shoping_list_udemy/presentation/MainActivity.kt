package com.hortopan.shoping_list_udemy.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //установка RecyclerView
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java] // или точно такое же .get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            //присвоить адаптеру новый list
            adapter.shopList = it
        }
    }

    private fun setupRecyclerView(){
        //получить RecyclerView
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        //создать адаптер
        adapter = ShopListAdapter()
        //установить этот адаптер к RecyclerView
        rvShopList.adapter = adapter
    }

}