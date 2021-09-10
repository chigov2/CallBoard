package chigovv.com.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import chigovv.com.callboard.R
import chigovv.com.callboard.databinding.ActivityEditAdsBinding
import chigovv.com.callboard.dialog.DialogSpinnerHelper
import chigovv.com.callboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {
    lateinit var rootElement:ActivityEditAdsBinding
    private var dialog = DialogSpinnerHelper()//созается объект(инстанция) класса DialogSpinnerHelper

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
   }

    private fun init() {
      }

    //создали кнопку на activity edit ads- создаем слушатель
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        //создаем новый диалог - для этого создается новый пакет dialog
        //запуск диалога
        dialog.showSpinnerDialog(this, listCountry)
    }
}