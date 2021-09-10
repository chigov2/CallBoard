package chigovv.com.callboard.utils

import android.content.Context
import chigovv.com.callboard.R
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

object CityHelper {
    //нет контекста - добавим в edit activity
    fun getAllCountries(context: Context):ArrayList<String>
    {
        var tempArray = ArrayList<String>()
        //попытка считать json fle from assets. чтение потока байтов
        try {
            //val inputStream:InputStream = context.assets.open("countriesToCities.json")
            val inputStream:InputStream = context.assets.open("countries.json")
            val size:Int = inputStream.available()
            val bytesArray = ByteArray(size)
            //узнали размер - теперь считывание
            inputStream.read(bytesArray)
            //теперь преобразовать в String
            val jsonFile = String(bytesArray)
            //превращаем всё в json
            val jsonObject = JSONObject(jsonFile)
            val countryNames = jsonObject.names()
            if (countryNames !== null) {
                for (n in 0 until countryNames.length()) {
                    tempArray.add(countryNames.getString(n))
                }
            }

        }catch (e:IOException){

        }
        return tempArray
    }
    //filtration
    fun filterListData(list: ArrayList<String>, searchText: String?):ArrayList<String>{
        //create temporary array
        val tempList = ArrayList<String>()
        tempList.clear()
        //filtration
        if (searchText ==null )
        {
            tempList.add("No result")
            return tempList
        }
        for (selection: String in list) {
            if ( selection.toLowerCase(Locale.ROOT).startsWith(searchText.toLowerCase(Locale.ROOT))){
                tempList.add(selection)
            }
        }
        //усли нет совпадений
        if(tempList.size == 0)
        {
            tempList.add("No result")
        }

        return tempList
    }

}