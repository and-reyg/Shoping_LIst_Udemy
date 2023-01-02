package com.hortopan.shoping_list_udemy.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hortopan.shoping_list_udemy.R
import com.hortopan.shoping_list_udemy.databinding.FragmentShopItemBinding
import com.hortopan.shoping_list_udemy.domain.ShopItem

class ShopItemFragment: Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFIND_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel(){
        //Если работа завершена то нужно закрыть экран
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            //Обращается к активити к которому прикреплен этот фрагмент
            //ранее был код - делает то же самое если бы юзер нажал кнопку назад activity?.onBackPressed()
            onEditingFinishListener.onEditingFinished()

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
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
            override fun afterTextChanged(p0: Editable?) { }

        })
        //при вводе текста скрывать ошибку
        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        binding.saveButton.setOnClickListener {
            //сохранение
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString())
        }
    }

    private fun launchAddMode(){
        binding.saveButton.setOnClickListener {
            //сохранение
            viewModel.addShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())
        }

    }

    //для опредиления в каком режиме работать (редактирование или добавление) проверка Intent
    private fun parseParams(){
        val args = requireArguments()
        // если аргумент не содержит параметр SCREEN_MODE то вівести исключения
        if(!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        //если mode редактирования, то нужно проверить есть ли id
        //если мод равен редактированию и у интента нет поля ID
        if(screenMode == MODE_EDIT){
            if (!args.containsKey(SHOP_ITEM_ID)){
                throw RuntimeException("Param shop item id is absent")
            }
            //обязательно вторым параметром передавать значения по умолчанию
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFIND_ID)
        }
    }


    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }

    companion object{

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        //Новый экземпляр фрагмента
        //для подключения в ShopItemActivity Вернет фрагмент в режиме добавления
        fun newInstanceAddItem(): ShopItemFragment{
            //Сохранить в переменную args Новые значения через метод putString
            //.apply пишется вместо args.putString просто putstring так как после apply{ } в скобках передается this.args
            //Будет создан обьект ShopItem. Вызывается метод set arguments в который устанавливаются новые значения
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }

        }

        //Новый экземпляр фрагмента
        //для подключения в ShopItemActivity Вернет фрагмент в режиме редактирования
        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment{

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}