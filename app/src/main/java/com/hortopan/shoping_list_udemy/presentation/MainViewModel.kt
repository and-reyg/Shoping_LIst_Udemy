package com.hortopan.shoping_list_udemy.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hortopan.shoping_list_udemy.data.ShopListRepositoryImpl
import com.hortopan.shoping_list_udemy.domain.*

class MainViewModel: ViewModel() {

    //здесь не правильная передача репозитория, правильная будет после изучения иньекций
    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    //LiveData
    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem){
        //будет создана копия shopItem со значением полем enabled противополжным оригиналу
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}