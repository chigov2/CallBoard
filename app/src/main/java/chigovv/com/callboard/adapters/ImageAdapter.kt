package chigovv.com.callboard.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.adapters.ImageAdapter.*

//ImageHolder придуман - надо создавать вручную
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageHolder>(){
   //создается массив, куда будут передаваться
    val mainArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item,parent,false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    //itemView должен приходить из конструктора - добавляем (itemView : View)
    class ImageHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        lateinit var imItem: ImageView
        fun setData(uri: String )
        {
           imItem = itemView.findViewById(R.id.imItem)
            //заполняем
           imItem.setImageURI(Uri.parse(uri))
        }
    }
    //функция обновления
    fun update(newList : ArrayList<String>){
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()

    }
}