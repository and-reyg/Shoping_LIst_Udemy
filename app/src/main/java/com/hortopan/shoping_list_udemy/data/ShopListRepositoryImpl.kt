package com.hortopan.shoping_list_udemy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hortopan.shoping_list_udemy.domain.ShopItem
import com.hortopan.shoping_list_udemy.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {
    /**
     * При реализации записи в базу данных нужно будет в функции подставлять методы которые
     * к примеру в ф-ии addShopItem запишет в базу и так с каждым методом
     */

    //shopList LiveData
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10){
            val item = ShopItem(
                name = "Name $i",
                count = i,
                enabled = true
            )
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        //если id равен -1, то установить правильный id
        if(shopItem.id == ShopItem.UNDEFIND_ID){
            shopItem.id = autoIncrementId++ //присвоит id значение autoIncrementId и потом увеличит autoIncrementId на 1
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        //может вернутся null поєтому написали обработку
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        //правильно всегда возвращать не саму коллекцию, а ее копию
        //так с основной колекцией ничего сделать не смогут
        return shopListLD
    }

    private fun updateList(){
        //вернем копию листа
        shopListLD.value = shopList.toList()
    }
}