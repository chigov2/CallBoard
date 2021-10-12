package chigovv.com.callboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chigovv.com.callboard.R
import chigovv.com.callboard.R.id
import chigovv.com.callboard.databinding.ListImageFragmentBinding
import chigovv.com.callboard.utils.ImageManager
import chigovv.com.callboard.utils.ImagePicker
import chigovv.com.callboard.utils.ItemTouchMoveCallback
import chigovv.com.callboard.utils.ImagePicker.getImages


//показывает список с картинками

//и еще надо передавать список с фото
//если newList без val - не будет работать цикл временный
class ImageListFragment(private val fragmentCloseInterface: FragmentCloseInterface,
                        private val newList:ArrayList<String>): Fragment() {

    lateinit var rootElement : ListImageFragmentBinding                      //от list_image_fragment
    //создаем адаптер
    val adapter = SelectImageRVAdapter()
    val dragCallBack = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallBack)//класс перетаскиваюший элементы
    // - нужен callback - создаем его ItemTouchMoveCallback.kt
    //ндо создать данный класс
    // itemrecyclerview(встроенный)


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        //return inflater.inflate(R.layout.list_image_fragment,container,false)//это только отрисовка фрагмента
        rootElement = ListImageFragmentBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //подключение tb
        setUpToolBar()

        //подключение touchHelper к  RecyclerView
        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage)

        //to recyclerview connect layout показываем как будет располагаться
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)

        rootElement.rcViewSelectImage.adapter = adapter

        ImageManager.imageResize(newList)
        //in updateAdapter передаем updateList
        //adapter.updateAdapter(newList,true)

        //
        //пока ничего не будет видно
        //нужно также отслеживать закрыт фрагмент или открыт - создаем fragmentCloseInterface
    }

    override fun onDetach() {
        //сюда можно передать список adapter.mainArray и сделать апдейт адаптера
        super.onDetach()
        fragmentCloseInterface.onFragmentClose(adapter.mainArray)

    }

    //настройка, инициализация tb
    private fun setUpToolBar()
    //запуск в onViewCreated
    {
        rootElement.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem  = rootElement.tb.menu.findItem(R.id.id_delete_image)
        val addImageItem  = rootElement.tb.menu.findItem(R.id.id_add_image)

        deleteItem.setOnMenuItemClickListener {
            Log.d("MyLog","Delete Item")
            adapter.updateAdapter(ArrayList(),true)
            true
        }
        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity,imageCount, ImagePicker.REQUEST_CODE_GET_IMAGES)
            Log.d("MyLog","Add Item")
            true
        }

        rootElement.tb.setNavigationOnClickListener(){
            Log.d("MyLog","Home button")
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    fun updateAdapter(newList: ArrayList<String>){
         //in updateAdapter передаем updateList
        adapter.updateAdapter(newList,false)
    }

    fun setSingleImage(uri: String, pos: Int){
        adapter.mainArray[pos] = uri
        adapter.notifyDataSetChanged()
    }
}