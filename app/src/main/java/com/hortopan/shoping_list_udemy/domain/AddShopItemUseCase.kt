package com.hortopan.shoping_list_udemy.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun addShopItem(shopItem: ShopItem){

        shopListRepository.addShopItem(shopItem)
    }
}