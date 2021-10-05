package chigovv.com.callboard.fragment

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.act.EditAdsAct
import chigovv.com.callboard.utils.ImagePicker
import chigovv.com.callboard.utils.ItemTouchMoveCallback
import kotlin.collections.ArrayList

class SelectImageRVAdapter: RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(),
    ItemTouchMoveCallback.itemTouchAdapter
//создаем ViewHolder - ImageHolder Придумали.
//необходимо создать новый layout - select_image_fragment_item - отдельный итем,
// который будет заполнять картитнку
{
    //mainArray - храниться список всех итемов
    val mainArray = ArrayList<String>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item,parent,false)
        return ImageHolder(view, parent.context,this)//вместо SelectImageRVAdapter - так не сработает
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

    class ImageHolder(itemView: View,val context: Context, val adapter: SelectImageRVAdapter) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView
        lateinit var imEditImage: ImageButton
        lateinit var imDeleteImage: ImageButton
        //создать класс, передавать ссылку и титл, т.е. создать элемент, который будет содеджать эти два элемента SelectImageItem.kt = data class
        fun setData(item: String)
        {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            imEditImage = itemView.findViewById(R.id.imEditImage)
            imDeleteImage = itemView.findViewById(R.id.imDelete)

            imEditImage.setOnClickListener {
                ImagePicker.getImages(context as EditAdsAct, 1,ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE)
                context.editImagePos = adapterPosition
                //Log.d("MyLog","${context.editImagePos}")
            }

            imDeleteImage.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)

                for (n in 0 until adapter.mainArray.size)
                {
                    adapter.notifyItemChanged(n)
                }
            }

            //нашли - теперь можем заполнять
            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(Uri.parse(item))
        }

    }

    //необходимо создать фенкцию для обновления
    fun updateAdapter(newList: ArrayList<String>, needClear : Boolean){
        if (needClear == true)
        {//очищаем main array
            mainArray. clear()
        }
        mainArray.addAll(newList)
        notifyDataSetChanged()
        //готовы принять все и показать в recycler view
    }

}