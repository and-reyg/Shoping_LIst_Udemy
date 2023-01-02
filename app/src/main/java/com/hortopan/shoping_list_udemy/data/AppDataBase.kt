package com.hortopan.shoping_list_udemy.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        //переменная єкземпляра баз данных
        private var INSTANCE: AppDataBase? = null
        //обьект синхронизации
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDataBase {
            INSTANCE?.let {
                return it
            }
            //если вдруг 2 потока вызвали одновременно базу, и она показала что пустая, то благодаря
            // этому методу, сначала зайдет первый поток, и потом еще раз будет проверка,
            // и уже для второго потока база не будет пустой
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                //экземпляр базы данных
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}