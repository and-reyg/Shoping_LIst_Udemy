package com.hortopan.shoping_list_udemy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hortopan.shoping_list_udemy.domain.ShopItem

@Entity(tableName = "shop_items")
data class ShopItemDbModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean

)