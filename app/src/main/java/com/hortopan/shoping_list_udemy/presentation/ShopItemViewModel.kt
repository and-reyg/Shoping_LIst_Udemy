package com.hortopan.shoping_list_udemy.presentation

import androidx.lifecycle.ViewModel
import com.hortopan.shoping_list_udemy.data.ShopListRepositoryImpl
import com.hortopan.shoping_list_udemy.domain.AddShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.EditShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.GetShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopItemViewModel: ViewModel() {

    //здесь не правильная передача репозитория, правильная будет после изучения иньекций
    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    //в конструкторе нулабельные переменные, на случай если юзер не введет значение
    fun addShopItem(inputName: String?, inputCount: String?){
        //Обработка введеного текста, переобразования с null в не null
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        //Валидация введеного текста, вернет Boolean
        val fieldsValid = validateInput(name,count)
        //проверка на корректность, если корректно то добавить запись
        if(fieldsValid){
            //создать обьект shopItem
            val shopItem = ShopItem(name, count, true)
            //добавить shopItem
            addShopItemUseCase.addShopItem(shopItem)
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?){
        //Обработка введеного текста, переобразования с null в не null
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        //Валидация введеного текста, вернет Boolean
        val fieldsValid = validateInput(name,count)
        //проверка на корректность, если корректно то добавить запись
        if(fieldsValid){
            //создать обьект shopItem
            val shopItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(shopItem)
        }

    }

    //Для обработки введеного текста в поле Name. Принимает нулабельный String, возвращает НЕнулабельный String
    private fun parseName(inputName: String?): String{
        //если  inputName != null, то обрезать образать лишние пробелы и вернуть значение
        //елси inputNAme == null, то вернется просто пуская строка
        return inputName?.trim() ?: ""
    }

    //Для обработки введеного текста в поле Count. Принимает нулабельный String, возвращает НЕнулабельный Int
    private fun parseCount(inputCount: String?): Int{
        //Проверка если было введено не число а какой о текст
        return try{
            inputCount?.trim()?.toInt() ?: 0
        }catch (e: Exception){
            0
        }
    }

    //опредиляет что данные введены не коректно
    private fun validateInput(name: String, count: Int): Boolean{
        var result = true
        if(name.isBlank()){
            //TODO: show error input name
            result = false
        }
        if(count <= 0){
            //TODO: show error input count
            result  = false
        }

        return result

    }


}