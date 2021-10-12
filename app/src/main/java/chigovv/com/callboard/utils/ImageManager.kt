package chigovv.com.callboard.utils

import android.graphics.BitmapFactory
import android.util.Log
import androidx.exifinterface.media.ExifInterface

import chigovv.com.callboard.R
import java.io.File

object ImageManager {
    const val MAX_IMAGE_SIZE = 1000
    const val WIDTH = 0
    const val HEIGHT = 1
    //uri - передать ссылку на картинку необходимо, а возвращать функция будет список List
    fun getImageSize(uri: String) : List<Int>
    {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true}
        BitmapFactory.decodeFile(uri, options)
        //необходимо записать длину и ширину в список и вернуть его
        //но сначала сделать проверку
        return if (imageRotation(uri) == 90)
            listOf(options.outHeight,options.outWidth)

        else listOf(options.outWidth,options.outHeight)
    }

    private fun imageRotation (uri: String) : Int {

        val rotation: Int
        val imageFile = File(uri)
        //знаем где лежит файл
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        rotation =
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                90
            }else
            {
                0
            }
            return rotation
    }

    //будем в нее передавать ссылки картинок uri
    fun imageResize(uris: List<String>)
    {
        val tempList = ArrayList<List<Int>>()//массив из массива из дввх элементов
        for (n in uris.indices)
        {
            val size = getImageSize( uris[n])
            Log.d("MyLog","width = ${size[WIDTH]} height = ${size[HEIGHT]}")

            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()
            if (imageRatio >1)//картинка горизоньальная
            {
                if (size[WIDTH] > MAX_IMAGE_SIZE)
                {
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
                }
                else{//если картинка маленькая
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }
            else// если картинка вертикальная
            {
                if (size[HEIGHT] > MAX_IMAGE_SIZE)
                {
                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                }
                else{//если картинка маленькая
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }
            Log.d("MyLog","width = ${tempList[n][WIDTH]} height = ${tempList[n][HEIGHT]}")
            //Log.d("MyLog","Ratio = ${imageRatio}")
        }
    }
}

