package chigovv.com.callboard.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface

import chigovv.com.callboard.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager {
    private const val MAX_IMAGE_SIZE = 1000
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
            { 90 }
            else { 0 }
            return rotation
    }
    //вертикально или горизонтально расположены картинки
    fun chooseScaleType(im: ImageView, bitMap: Bitmap) {
        if (bitMap.width > bitMap.height) {
            im.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            im.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }
    //будем в нее передавать ссылки картинок uri
    suspend fun imageResize(uris: List<String>): List<Bitmap> = withContext(Dispatchers.IO)
    {
        val tempList = ArrayList<List<Int>>()//массив из массива из дввх элементов
        val bitmapList = ArrayList<Bitmap>()
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

        }
        for (i in uris.indices)
        {
            val e = kotlin.runCatching {
                bitmapList.add(Picasso.get().load(File(uris[i]))
                .resize(tempList[i][WIDTH],tempList[i][HEIGHT]).get())
            }
            Log.d("MyLog","status : ${e.isSuccess}")

        }

        //delay(5000)
        return@withContext bitmapList
    }
}

