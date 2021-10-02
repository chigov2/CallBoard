package chigovv.com.callboard.act

import android.R.attr
import android.content.Intent
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
import com.fxn.pix.Pix
import android.R.attr.data
import android.app.Activity
import android.util.Log
import chigovv.com.callboard.adapters.ImageAdapter
import chigovv.com.callboard.fragment.FragmentCloseInterface
import chigovv.com.callboard.fragment.SelectImageItem
import chigovv.com.callboard.fragment.imageListFragment


class EditAdsAct : AppCompatActivity(),FragmentCloseInterface {
    lateinit var rootElement:ActivityEditAdsBinding
    private var dialog = DialogSpinnerHelper()//созается объект(инстанция) класса DialogSpinnerHelper
    private var isImagesPermitionGranted = false

    //5.1
    private lateinit var  imageAdapter : ImageAdapter


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == imagePicker.REQUEST_CODE_GET_IMAGES) {
            if (data != null)
            {
            val returnValues: ArrayList<String> = data.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>
            //val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            ///////////////////////
                if (returnValues?.size > 1)
                {
                    rootElement.scrollViewMain.visibility = View.GONE
                    //imagePicker.getImages(this)
                    //проверка фрагмента
                    val fm = supportFragmentManager.beginTransaction()
                    fm.replace(R.id.placeHolder,imageListFragment(this,returnValues))
                    fm.commit()
                }

                ///////////////////

            //Log.d("MyLog","Image : ${returnValues?.get(0)}")
            //Log.d("MyLog","Image : ${returnValues?.get(1)}")
            //Log.d("MyLog","Image : ${returnValues?.get(2)}")
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    imagePicker.getImages(this,3)
                    //Toast.makeText(this,"Permissions is OK",Toast.LENGTH_LONG).show()
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
        //5.1 12min
        //подключаем адаптер к viewpager
        //находим viewpager, инициализируем обязательно
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
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
        imagePicker.getImages(this,3)
    }

    override fun onFragmentClose(list: ArrayList<SelectImageItem>) {
        //при нахатии кнопки назад запускается данная функция
        //super.onFragmentClose()
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
    }
}
