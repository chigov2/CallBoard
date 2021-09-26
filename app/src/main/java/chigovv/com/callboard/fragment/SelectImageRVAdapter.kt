package chigovv.com.callboard.fragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import kotlin.collections.ArrayList

class SelectImageRVAdapter: RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>()
//создаем ViewHolder - ImageHolder Придумали.
//необходимо создать новый layout - select_image_fragment_item - отдельный итем, который будет заполнять картитнку
{
    private val mainArray = ArrayList<SelectImageItem>()

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView
        //создать класс, передавать ссылку и титл, т.е. создать элемент, который будет содеджать эти два элемента SelectImageItem.kt = data class
        fun setData(item: SelectImageItem)
        {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            //нашли - теперь можем заполнять
            tvTitle.text = item.title
            image.setImageURI(Uri.parse(item.imageUri))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item,parent,false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        // mainArray передается сюда
        return mainArray.size
    }
    //необходимо создать фенкцию для обновления
    fun updateAdapter(newList: ArrayList<SelectImageItem>){
        //очищаем main array
        mainArray.clear()
        mainArray.addAll(newList)
        //чтобы обнулился
        notifyDataSetChanged()
        //готовы принять все и показать в recycler view
    }
}