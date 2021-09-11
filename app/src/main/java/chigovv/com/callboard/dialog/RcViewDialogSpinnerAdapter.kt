package chigovv.com.callboard.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.act.EditAdsAct

class RcViewDialogSpinnerAdapter(var tvSelection: TextView, var dialog: AlertDialog)
    : RecyclerView.Adapter<RcViewDialogSpinnerAdapter.SpViewHolder>() {

    val mainList = ArrayList<String>()
    //содается переменная для передачи контекста в onCreateViewHolder
    //private var cont = context//теперь контекст доступен в любой функции


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder {
        //создается один элемент списка и передается
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item,parent,false)
        return SpViewHolder(view,tvSelection, dialog)
    }
    //когда всё нарисовано запускается onBindViewHolder
    override fun onBindViewHolder(holder: SpViewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    class SpViewHolder(itemView: View, var tvSelection: TextView,var dialog: AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var itemText = ""
        fun setData(text: String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem)
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //запуститься когда нажмется один из элементов списка
            //при нажатии надо взять текст из нажатого элемента и показать его в tvSpItem
            //context -это и есть EditActivity
            tvSelection.text = itemText
            //onClick - обязательно нужно присвоить setData item View
            dialog.dismiss()//1.3.3 20.25
        }
    }

    fun updateAdapter(list:ArrayList<String>)
    {
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}