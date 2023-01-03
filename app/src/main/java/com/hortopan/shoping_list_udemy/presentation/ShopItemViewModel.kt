package com.hortopan.shoping_list_udemy.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hortopan.shoping_list_udemy.data.ShopListRepositoryImpl
import com.hortopan.shoping_list_udemy.domain.AddShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.EditShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.GetShopItemUseCase
import com.hortopan.shoping_list_udemy.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application): AndroidViewModel(application) {

    //здесь не правильная передача репозитория, правильная будет после изучения иньекций
    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    //livedata для передачи в активити true or false об ошибке
    //в активти подпишемся на эту livedata и если значение true то будет показана ошибка
    //С этой переменной работем только с ViewModel, так как переменная привтаная и ее можно менять
    private val _errorInputName = MutableLiveData<Boolean>()
    //С этой переменной работать с Активити
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    //тоже что и _errorInputName
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    //С этой переменной работем только с ViewModel, так как переменная привтаная и ее можно менять
    private val _shopItem = MutableLiveData<ShopItem>()
    //С этой переменной работать с Активити
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    //в _shouldCloseScreen всегда будет установлено true,
    // нужно активити сообщить только то что нужно закрыть экран, поэтому тип будет не Boolean а Unit
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int){
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.value = item
        }
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
            viewModelScope.launch {
                //создать обьект shopItem
                val shopItem = ShopItem(name, count, true)
                //добавить shopItem
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
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
            Log.d("EditShopItem", "Opened Valid")
            //получить обьект shopitem из liveData
            //.let будет редактировать если _shopItem не равен null
            _shopItem.value?.let {
                viewModelScope.launch {
                    //создать копию обьекта, и присвоить новые значения
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }
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
            //запись в livedata что есть ошибка
            _errorInputName.value = true
            result = false
        }
        if(count <= 0){
            //запись в livedata что есть ошибка
            _errorInputCount.value = true
            result  = false
        }

        return result

    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputName.value = false
    }

    //закрыть скрин после добавления иил редактирования
    private fun finishWork(){
        _shouldCloseScreen.value = Unit
    }

}