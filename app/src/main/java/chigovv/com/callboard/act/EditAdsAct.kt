package chigovv.com.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import chigovv.com.callboard.R
import chigovv.com.callboard.databinding.ActivityEditAdsBinding
import chigovv.com.callboard.dialog.DialogSpinnerHelper
import chigovv.com.callboard.utils.CityHelper
import chigovv.com.callboard.MainActivity
import com.fxn.utility.PermUtil
import android.content.pm.PackageManager
import chigovv.com.callboard.utils.imagePicker


class EditAdsAct : AppCompatActivity() {
    lateinit var rootElement:ActivityEditAdsBinding
    private var dialog = DialogSpinnerHelper()//созается объект(инстанция) класса DialogSpinnerHelper
    private var isImagesPermitionGranted = false

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
   }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permissions is OK",Toast.LENGTH_LONG).show()
                } else {
                    isImagesPermitionGranted = false
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }


    private fun init() {
      }

    //создали кнопку на activity edit ads- создаем слушатель
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        //создаем новый диалог - для этого создается новый пакет dialog
        //запуск диалога
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        //проверка выбран ли город перед выбором страны(чтобы не было Украина - Москва)
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)){
            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvCountry.text
        if (selectedCountry !== getString(R.string.select_country))
        {
            val listCities = CityHelper.getAllCities(selectedCountry as String,this)
            //создаем новый диалог - для этого создается новый пакет dialog
            //запуск диалога
            dialog.showSpinnerDialog(this, listCities, rootElement.tvCity)
        }else
        {
            Toast.makeText(this,this.resources.getString(R.string.no_country_selected),Toast.LENGTH_LONG).show()
        }

    }

    fun onClickGetImages(view: View){
        imagePicker.getImages(this)

    }
}