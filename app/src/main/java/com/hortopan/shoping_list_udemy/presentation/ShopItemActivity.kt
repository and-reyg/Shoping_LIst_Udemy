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

class ShopItemActivity: AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    //til - Text Input Layout
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    //режим (редактирование или добавление) присвоится в методе parseIntent()
    private var screenMode = MODE_UNKNOWN
    //присвоится в parseIntent()
    private var shopItemId = ShopItem.UNDEFIND_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel(){
        //если нужно отобразить сообщение об ошибке ввода Count
        viewModel.errorInputCount.observe(this){
            val message = if (it){
                getString(R.string.error_input_count)
            } else {
                null
            }
            //если в message будет лежать строка то сообщение отобразиться, если null то нет
            tilCount.error = message
        }

        //если нужно отобразить сообщение об ошибке ввода Name
        viewModel.errorInputName.observe(this){
            val message = if (it){
                getString(R.string.error_input_name)
            } else {
                null
            }
            //если в message будет лежать строка то сообщение отобразиться, если null то нет
            tilName.error = message
        }

        //Если работа завершена то нужно закрыть экран
        viewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }

    private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners(){
        //при вводе текста скрывать ошибку
        etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
            override fun afterTextChanged(p0: Editable?) { }

        })
        //при вводе текста скрывать ошибку
        etCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }
            override fun afterTextChanged(p0: Editable?) { }

        })
    }

    private fun launchEditMode(){
        //получение обьекта item (запись которую нужно редактировать)
        viewModel.getShopItem(shopItemId)
        //получение пнеобходимых полей
        viewModel.shopItem.observe(this){
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            //сохранение
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode(){
        buttonSave.setOnClickListener {
            //сохранение
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }

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

    private fun initViews(){
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
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