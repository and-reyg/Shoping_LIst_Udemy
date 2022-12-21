package com.hortopan.shoping_list_udemy.presentation

import androidx.recyclerview.widget.DiffUtil
import com.hortopan.shoping_list_udemy.domain.ShopItem

//сравнивает 2 списка новый и старый
class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //проеряет ли обьект тот же, те самые айди но просто значения другие
    //если изменится какой то параметр, но id тот же то адаптер будет знать что єто тот же обьект
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        //если одинаковые вернется true, иначе false
        return oldItem.id == newItem.id
    }

    //Проверяет не сами обьекты а их содержимое
    //вернет true если все поля остались прежними, а если что то изменилось то false
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        //так как ShopItem является дата классом, то его метод будет сравнивать все поля что в нем лежат
        //если одинаковые вернется true, иначе false
        return oldItem == newItem
    }
}