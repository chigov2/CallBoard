package chigovv.com.callboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.utils.ItemTouchMoveCallback

//показывает список с картинками

//и еще надо передавать список с фото
//если newList без val - не будет работать цикл временный
class imageListFragment(private val fragmentCloseInterface: FragmentCloseInterface,
                        private val newList:ArrayList<String>): Fragment() {
    //создаем адаптер
    val adapter = SelectImageRVAdapter()
    val dragCallBack = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallBack)//класс перетаскиваюший элементы
    // - нужен callback - создаем его ItemTouchMoveCallback.kt
    //ндо создать данный класс
    // itemrecyclerview(встроенный)


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.list_image_fragment,container,false)//это только отрисовка фрагмента
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //здесь получаем rcview
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage)
        //подключение touchHelper к  RecyclerView
        touchHelper.attachToRecyclerView(rcView)

        //здесь получаем view и из него достаем кнопку
        val bBack = view.findViewById<Button>(R.id.bBack)
        //to recyclerview connect layout показываем как будет располагаться
        rcView.layoutManager = LinearLayoutManager(activity)

        rcView.adapter = adapter

        //temporary
        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size)
        {
            //val selectImageItem = SelectImageItem("0","0")
            //selectImageItem.copy(title = "890")
            //updateList.add(SelectImageItem(n.toString(),newList[n]))
            updateList.add(SelectImageItem(n.toString(),newList[n]))
        }
        //in updateAdapter передаем updateList
        adapter.updateAdapter(updateList)

        bBack.setOnClickListener { activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() }
        //пока ничего не будет видно
        //нужно также отслеживать закрыт фрагмент или открыт - создаем fragmentCloseInterface
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose()
        Log.d("MyLog", "title 0: ${adapter.mainArray[0].title}")
        Log.d("MyLog", "title 1: ${adapter.mainArray[1].title}")
        Log.d("MyLog", "title 2: ${adapter.mainArray[2].title}")
    }

}