package com.hortopan.shoping_list_udemy.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hortopan.shoping_list_udemy.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //установка RecyclerView
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java] // или точно такое же .get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            //присвоить адаптеру новый list
            shopListAdapter.submitList(it)
        }

        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener{
            //переход на второй экран
            val intent = ShopItemActivity.newIntentAddItem(this)
            //запуск активити
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(){
        //получить RecyclerView
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList){
            //создать адаптер
            shopListAdapter = ShopListAdapter()
            //установить этот адаптер к RecyclerView
            adapter = shopListAdapter
            //Установка размера ViewHolderPool
            // setMaxRecycledViews принимает 2 параметра View  и размер пула, так как в проекте 2 типа View нужно написать дважды
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE)
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)

    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
            //переход на второй экран
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            //запуск активити
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView){
        //создание анонимного обьекта
        //SimpleCallback(направление перемещения и свайпа
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //этот метод не нужен поэтому просто так написали false
                return false
            }
            //удвление элемента их списка, ведь при свайпе удалится жлемент
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        //передача анонимного обьекта и создание itemTouchHelper
        val itemTouchHelper = ItemTouchHelper(callback)
        //присваивание к ресайклеру
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }


}