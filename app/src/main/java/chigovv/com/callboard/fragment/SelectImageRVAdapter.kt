package chigovv.com.callboard.fragment

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.act.EditAdsAct
import chigovv.com.callboard.databinding.SelectImageFragmentItemBinding
import chigovv.com.callboard.utils.AdapterCallBack
import chigovv.com.callboard.utils.ImageManager
import chigovv.com.callboard.utils.ImagePicker
import chigovv.com.callboard.utils.ItemTouchMoveCallback
import kotlin.collections.ArrayList

class SelectImageRVAdapter(val adapterCallBack: AdapterCallBack): RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(),
    ItemTouchMoveCallback.itemTouchAdapter
//создаем ViewHolder - ImageHolder Придумали.
//необходимо создать новый layout - select_image_fragment_item - отдельный итем,
// который будет заполнять картитнку
{
    //mainArray - храниться список всех итемов
    val mainArray = ArrayList<Bitmap>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewBinding = SelectImageFragmentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageHolder(viewBinding, parent.context,this)//вместо SelectImageRVAdapter - так не сработает
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        // mainArray передается сюда
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {

        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos,targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()//не запустится, пока в ItemTouchCallback fun clearView() не прописать adapter.onClear()
    }

    class ImageHolder(private val viewBinding:SelectImageFragmentItemBinding, val context: Context, val adapter: SelectImageRVAdapter) : RecyclerView.ViewHolder(viewBinding.root) {

        lateinit var pBar: ProgressBar
        //создать класс, передавать ссылку и титл, т.е. создать элемент, который будет содеджать эти два элемента SelectImageItem.kt = data class
        fun setData(bitMap: Bitmap)
        {

            pBar = itemView.findViewById(R.id.pBar)

            viewBinding.imEditImage.setOnClickListener {
               // pBar.visibility = View.VISIBLE
                ImagePicker.getImages(context as EditAdsAct, 1,ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE)
                context.editImagePos = adapterPosition
                //Log.d("MyLog","${context.editImagePos}")
            }

            viewBinding.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size){adapter.notifyItemChanged(n)}
                adapter.adapterCallBack.onItemDelete()//появится кнопка добавить при удалении третьей фотки
            }
            //нашли - теперь можем заполнять
            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(viewBinding.imageContent,bitMap)
            viewBinding.imageContent.setImageBitmap(bitMap)
        }
    }

    //необходимо создать фенкцию для обновления
    fun updateAdapter(newList: ArrayList<Bitmap>, needClear : Boolean){
        if (needClear == true)
        {//очищаем main array
            mainArray. clear()
        }
        mainArray.addAll(newList)
        notifyDataSetChanged()
        //готовы принять все и показать в recycler view
    }

}