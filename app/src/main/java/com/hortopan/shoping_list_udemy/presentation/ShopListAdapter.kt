package com.hortopan.shoping_list_udemy.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.ListAdapter
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.databinding.ItemShopDisabledBinding
import com.hortopan.shoping_list_udemy.databinding.ItemShopEnabledBinding
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopListAdapter() : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()){

    //лямбда интерфейс, принимает ShopItem, ничего не возвращает, имеет тип null
    var onShopItemLongClickListener: ((ShopItem) -> Unit) ? = null

    var onShopItemClickListener: ((ShopItem) -> Unit) ? = null

    //реализует как создать View
    //будет вызван всего условно 20 раз (вместо всех условных 10 000 раз по условно 10 000 елементов)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //обработка нужного ViewType
        var layout = when(viewType){
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown viewType: $viewType")
        }
        //создаем view из макета
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        //возвращаем созданый обьект ShopItemViewHolder
        return ShopItemViewHolder(binding)
    }

    //реализует как вставить значения внутри этого View
    //будет вызван для абсолютно всех элементов списка
    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        //получаем элемент из коллекции (в новой версии из ListAdapter)
        // getItem() - метод из класса ListAdapter
        val shopItem = getItem(position)
        val binding = viewHolder.binding
        //устанавливаем слушатель нажатия (клика)
        binding.root.setOnClickListener{
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener{
            onShopItemClickListener?.invoke(shopItem)
        }
        when(binding) {
            is ItemShopDisabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
        }
    }


    //отвечает за определения типа view
    //вернет позицию элемента (у 0 элемента ViewType = 0, у 1 элемента ViewType = 1 и тд)
    override fun getItemViewType(position: Int): Int {
        //получение элемента
        // getItem() - метод из класса ListAdapter
        val item = getItem(position)
        return if(item.enabled){
            VIEW_TYPE_ENABLED
        }else{
            VIEW_TYPE_DISABLED
        }

    }


    companion object{

        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101

        //количество ViewHolder
        const val MAX_POOL_SIZE = 15
    }
}