package com.hortopan.shoping_list_udemy.presentation

import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.databinding.ItemShopDisabledBinding

// класс который хранит View
class ShopItemViewHolder(
    val binding: ViewDataBinding //исползуется родительский класс для всех байдингов
    ): RecyclerView.ViewHolder(binding.root)