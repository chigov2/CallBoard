package chigovv.com.callboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import chigovv.com.callboard.act.EditAdsAct
import com.fxn.pix.Options
import com.fxn.pix.Pix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//в этом классе будем получать картинки
object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val  REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3

    fun getImages(context:AppCompatActivity, imageCounter: Int, rCode: Int ){
        val options = Options.init()
            .setRequestCode(rCode)                                          //Request code for activity results
            .setCount(imageCounter)                                         //Number of images to restict selection count
            .setFrontfacing(false)                                          //Front Facing camera on start
            //.setSpanCount(3)                                              //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture)                                  //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)      //Orientation
            .setPath("/pix/images");                                        //Custom Path For media Storage

        Pix.start(context, options)

    }

    fun showSelectedImages(resultCode: Int,requestCode: Int, data: Intent?, edAct: EditAdsAct){
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {

            if (data != null)
            {
                val returnValues: ArrayList<String> = data.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>

                if (returnValues?.size > 1 && edAct.chooseImageFragment == null)//если размер больше 1 и не открыт новый фрагмент
                {
                    edAct.openChooseImageFragment(returnValues)//запускам фрагмент
                }
                else if (returnValues.size == 1 && edAct.chooseImageFragment == null){
                    //imageAdapter.update(returnValues)
                    //функция возвращает список с шириной и высотой   картинки.
                    // На одной позиции - ширина, на другой - высота
                    val tempList = ImageManager.getImageSize(returnValues[0])
                    Log.d("MyLog","Image width = ${tempList[0]}")
                    Log.d("MyLog","Image height = ${tempList[1]}")
                    CoroutineScope(Dispatchers.Main).launch{
                        edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                        val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                        //выдает массив с битмап, но только одна картинка - надо передать в imageAdapter
                        edAct.rootElement.pBarLoad.visibility = View.GONE
                        edAct.imageAdapter.update(bitMapArray)
                    }
                }
                else if (edAct.chooseImageFragment != null){
                    edAct.chooseImageFragment?.updateAdapter(returnValues)
                }
            }

        }
        else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGE)
        {
            if (data != null)
            {
                val uris: ArrayList<String> = data.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>
                //val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)
                Log.d("MyLog","${edAct.editImagePos}")
            }

        }
    }
}