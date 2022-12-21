package com.hortopan.shoping_list_udemy.presentation

import androidx.recyclerview.widget.DiffUtil
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopItemDiffCallback: DiffUtil.ItemCallback<ShopItem>() {

    //проеряет ли обьект тот же, те самые айди но просто значения другие
    //если изменится какой то параметр, но id тот же то адаптер будет знать что єто тот же обьект
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        //если одинаковые вернется true, иначе false
        return oldItem.id == newItem.id
    }

    //Проверяет не сами обьекты а их содержимое
    //вернет true если все поля остались прежними, а если что то изменилось то false
    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}