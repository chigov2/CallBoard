package chigovv.com.callboard.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

//этот класс должен наследоваться от itemTouchHelperCallback
class ItemTouchMoveCallback(val adapter: itemTouchAdapter) : ItemTouchHelper.Callback()
{//надо подключить интерфейс itemTouchAdapter

    override fun getMovementFlags(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder): Int {
       //указываем направление движения
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlag,0)
    }

    override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean
    { //список храниться в SelectImageRVAdapter ->
        // main Array и нужно указать этому массиву, что его элементы поменяны местами
        // для этого необходимо создать интерфейс
        adapter.onMove(viewHolder.adapterPosition,target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
    //меняем прозрачность выбранного элемента
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
    {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
        {
            viewHolder?.itemView?.alpha = 0.5f
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
        adapter.onClear()
        super.clearView(recyclerView, viewHolder)
    }

    interface itemTouchAdapter{
        //создаем функцию, которая будет передавать позицию элемента, который хотим перетащить
        fun onMove(startPos:Int, targetPos: Int)
        //добавляем данный интерфейс  к SelectImageRVAdapter
        fun onClear()//чтобы при перетаскивании обновлялся адаптер

    }
}