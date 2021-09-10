package chigovv.com.callboard.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.utils.CityHelper

class DialogSpinnerHelper{
    //необходим\передается контескт и список для создания диалога
    fun showSpinnerDialog(context: Context,list:ArrayList<String>){
         val builder = AlertDialog.Builder(context)
        //прежде чем показывать builder.show() - создадим диалог
        val dialog = builder.create()
        //создаем spinner_layout.xml
        //"надуваем"
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout,null)
        //create adapter
        val adapter = RcViewDialogSpinnerAdapter(context,dialog) //RcViewDialogSpinner_adapter
        // search rcView
        val rcView = rootView.findViewById<RecyclerView>(R.id.rcSpView)
        // search Searchview sv
        val sv = rootView.findViewById<SearchView>(R.id.svSpinner)
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        //in builder передаем rootView
        dialog.setView(rootView)
        adapter.updateAdapter(list)
        setSearchView(adapter,list,sv)
        dialog.show()
        //теперь диалог надо передать в RcView
    }

    private fun setSearchView(adapter: RcViewDialogSpinnerAdapter, list: ArrayList<String>, sv: SearchView?) {

        //добавить слушатель
        sv?.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list,newText)//получили список из совпадений и передаем в адартер
                adapter.updateAdapter(tempList)
                return true
            }
        }
        )
    }


}