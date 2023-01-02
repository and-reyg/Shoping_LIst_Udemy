package com.hortopan.shoping_list_udemy.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopItemActivity: AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    //режим (редактирование или добавление) присвоится в методе parseIntent()
    private var screenMode = MODE_UNKNOWN
    //присвоится в parseIntent()
    private var shopItemId = ShopItem.UNDEFIND_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        //Сделает что бы при перевороте экрана onCreate вызывалс только один раз
        //если savedInstanceState равен null значит активити не пересоздавалось, то создать фрагмент
        if(savedInstanceState == null){
            launchRightMode()
        }

    }

    override fun onEditingFinished() {
        finish()
    }

    private fun launchRightMode(){
        val fragment = when(screenMode){
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        //чтобы в контейнере был фрагмент. add(id контейнера для фрагмента, сам фрагмент)
        //метод .replace() удаляет старый фрагмент и создает новый(перезаписывает), благодаря этомуму методу
        //в отличии от .add(), .replace() при поворотах єкрана не будет созвадвать новые обьекты
        //после всех транзакций в конце обязательо .commit - запустит транзакцию на выполнения
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    //для опредиления в каком режиме работать (редактирование или добавление) проверка Intent
    private fun parseIntent(){
        // если ИНтент не содержит параметр EXTRA_SCREEN_MODE то вівести исключения
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        //если mode редактирования, то нужно проверить есть ли id
        //если мод равен редактированию и у интента нет поля ID
        if(screenMode == MODE_EDIT){
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param shop item id is absent")
            }
            //обязательно вторым параметром передавать значения по умолчанию
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFIND_ID)
        }

    }

    companion object{

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        //создаст intent для запуска второго экрана в режиме добавлени
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        //создаст intent для запуска второго экрана в режиме редактирования
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }



    }

}