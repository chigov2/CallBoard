package chigovv.com.callboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chigovv.com.callboard.databinding.ListImageFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

open class BaseSelectImageFragment: Fragment() {//надо подключить ViewBinding
    lateinit var binding : ListImageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = ListImageFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy()
    }

    private fun initAds()
    {
        MobileAds.initialize(activity as Activity)      //инициализация
        val adRequest = AdRequest.Builder().build()     //загрузка рекламы
        binding.adView.loadAd(adRequest)                //запрос на загрузку
    }
}