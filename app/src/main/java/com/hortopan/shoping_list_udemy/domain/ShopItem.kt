package com.hortopan.shoping_list_udemy.domain

data class ShopItem (
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: Int = UNDEFIND_ID //костыль! так как базы данных не будет, и даные будут хранится в переменных
){
    //
    companion object{

        const val UNDEFIND_ID = -1
    }
}