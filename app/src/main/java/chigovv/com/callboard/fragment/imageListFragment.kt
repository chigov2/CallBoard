package chigovv.com.callboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import chigovv.com.callboard.R

class imageListFragment(val fragmentCloseInterface: FragmentCloseInterface): Fragment() {
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.list_image_fragment,container,false)//это только отрисовка фрагмента
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //здесь получаем view и из него достаем кнопку
        val bBack = view.findViewById<Button>(R.id.bBack)
        bBack.setOnClickListener { activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() }
        //пока ничего не будет видно
        //нужно также отслеживать закрыт фрагмент или открыт - создаем fragmentCloseInterface
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose()
    }

}