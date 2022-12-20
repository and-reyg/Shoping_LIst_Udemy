package com.hortopan.shoping_list_udemy.presentation


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopListAdapter() : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>(){

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //реализует как создать View
    //будет вызван всего условно 20 раз (вместо всех условных 10 000 раз по условно 10 000 елементов)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //создаем view из макета
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_disabled,
            parent,
            false
        )
        //возвращаем созданый обьект ShopItemViewHolder
        return ShopItemViewHolder(view)
    }

    //реализует как вставить значения внутри этого View
    //будет вызван для абсолютно всех элементов списка
    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        //получаем элемент из коллекции
        val shopItem = shopList[position]
        val status = if(shopItem.enabled){
            "Active"
        }else{
            "Not active"
        }

        //устанавливаем слушатель нажатия (клика)
        viewHolder.view.setOnClickListener{
            true
        }
        if(shopItem.enabled){
            //у полученых TextView устанавливаем значения
            viewHolder.tvName.text = "${shopItem.name} $status"
            viewHolder.tvCount.text = shopItem.count.toString()
            viewHolder.tvName.setTextColor(ContextCompat.getColor(viewHolder.view.context, android.R.color.holo_red_light))
        }
    }

    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
        super.onViewRecycled(viewHolder)
        viewHolder.tvName.text = ""
        viewHolder.tvCount.text = ""
        viewHolder.tvName.setTextColor(ContextCompat.getColor(viewHolder.view.context, android.R.color.white))

    }

    //возвращает количество елементов в колекции
    override fun getItemCount(): Int {
        return shopList.size
    }

    // класс который хранит View
    class ShopItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }
}