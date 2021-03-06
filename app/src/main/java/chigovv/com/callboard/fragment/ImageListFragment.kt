package chigovv.com.callboard.fragment

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chigovv.com.callboard.R
import chigovv.com.callboard.databinding.ListImageFragmentBinding
import chigovv.com.callboard.dialog.ProgressDialog
import chigovv.com.callboard.utils.AdapterCallBack
import chigovv.com.callboard.utils.ImageManager
import chigovv.com.callboard.utils.ImagePicker
import chigovv.com.callboard.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


//показывает список с картинками

//и еще надо передавать список с фото
//если newList без val - не будет работать цикл временный
//необходимо создать интерфейс AdapterCallBack для появления.удаления кнопки добавить при удалении только одной фотки
//необходимо интерфейс запустить, передав его в адаптер val adapter = SelectImageRVAdapter(this)
//также в SelectImageRVAdapter в конструктор надо передать val adapterCallBack: AdapterCallBack
class ImageListFragment(private val fragmentCloseInterface: FragmentCloseInterface,
                        private val newList:ArrayList<String>?): BaseAdsFragment(), AdapterCallBack {
    //важно!!! newList:ArrayList<String>? -чтобы imagelistfragment мог получать null

    //создаем адаптер
    val adapter = SelectImageRVAdapter(this)///!!!!
    private var job: Job? = null
    val dragCallBack = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallBack)//класс перетаскиваюший элементы
    // - нужен callback - создаем его ItemTouchMoveCallback.kt
    //ндо создать данный класс
    // itemrecyclerview(встроенный)
    private var addImageItem: MenuItem? = null
    lateinit var binding : ListImageFragmentBinding
    //необходимо создать интерфейс AdapterCallBack для появления.удаления кнопки добавить при удалении только одной фотки

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = ListImageFragmentBinding.inflate(layoutInflater)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //подключение tb
        setUpToolBar()
        //подключение touchHelper к  RecyclerView
        binding.apply {
        touchHelper.attachToRecyclerView(rcViewSelectImage)

        //to recyclerview connect layout показываем как будет располагаться
        rcViewSelectImage.layoutManager = LinearLayoutManager(activity)

        rcViewSelectImage.adapter = adapter

        //т.к. imageResize стала suspend coroutine здесь
        //ImageManager.imageResize(newList)

        if (newList != null){resizeSelectedImages(newList,true)}
        //пока ничего не будет видно
        //нужно также отслеживать закрыт фрагмент или открыт - создаем fragmentCloseInterface
        }
    }

    override fun onItemDelete() {
        super.onItemDelete()
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>)
    {
        adapter.updateAdapter(bitmapList as ArrayList<Bitmap>,true)
    }

    override fun onDetach() {
        //сюда можно передать список adapter.mainArray и сделать апдейт адаптера
        super.onDetach()
        fragmentCloseInterface.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFragment)?.commit()


    }

    private fun resizeSelectedImages(newList: ArrayList<String>, needClear: Boolean)
    {
        job = CoroutineScope(Dispatchers.Main).launch{
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            //теперь прогресс бар не анонимный и его можно будет закрыть
            val bitmapList = ImageManager.imageResize(newList)
            //закончилось сжатие картинок
            dialog.dismiss()
            adapter.updateAdapter(bitmapList as ArrayList<Bitmap>,needClear)
            //проверяем сколько элементов в адаптере и нужно ли прятать кнопку Добавить
            if (adapter.mainArray.size > 2){
                addImageItem?.isVisible  = false
            }
        }
    }

    //настройка, инициализация tb
    private fun setUpToolBar()
    //запуск в onViewCreated
    {
        binding.apply {
        tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem  = tb.menu.findItem(R.id.id_delete_image)
        addImageItem  = tb.menu.findItem(R.id.id_add_image)

        deleteItem.setOnMenuItemClickListener {
            Log.d("MyLog","Delete Item")
            adapter.updateAdapter(ArrayList(),true)
            addImageItem?.isVisible = true
            true
        }
        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity,imageCount, ImagePicker.REQUEST_CODE_GET_IMAGES)
            Log.d("MyLog","Add Item")
            true
        }
        //кнопка назад
        tb.setNavigationOnClickListener(){
            Log.d("MyLog","Home button")
            showInterAd()//добавляем рекламу
        }
        }
    }

    fun updateAdapter(newList: ArrayList<String>){
         resizeSelectedImages(newList,false)
    }

    fun setSingleImage(uri: String, pos: Int){
         val pBar = binding.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch{
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            //Log.d("MyLog","result: ${bitmapList}")
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }
    }

}