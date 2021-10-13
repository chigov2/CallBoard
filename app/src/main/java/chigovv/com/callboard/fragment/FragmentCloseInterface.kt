package chigovv.com.callboard.fragment

import android.graphics.Bitmap

interface FragmentCloseInterface {
    fun onFragmentClose(list: ArrayList<Bitmap>){
        //in EnitAdsAct.kt дописываем этот интерфейс
    }
}