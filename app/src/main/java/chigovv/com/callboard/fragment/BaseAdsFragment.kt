package chigovv.com.callboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chigovv.com.callboard.R
import chigovv.com.callboard.databinding.ListImageFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

open class BaseAdsFragment: Fragment(), InterAdsClose {
    lateinit var adView: AdView
    var interAd: InterstitialAd? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAd()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds()
    {
        MobileAds.initialize(activity as Activity)      //инициализация
        val adRequest = AdRequest.Builder().build()     //загрузка рекламы
        adView.loadAd(adRequest)                //запрос на загрузку
    }

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity,getString(R.string.ad_inter_id),adRequest,object : InterstitialAdLoadCallback(){

            override fun onAdLoaded(ad: InterstitialAd) {
               interAd = ad
            }
        })
    }

    fun showInterAd()
    {
        if (interAd !== null)
        {
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

                override fun onAdDismissedFullScreenContent() {//необходимо создать интерфейс InterAdsClose
                onClose()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    onClose()
                }
            }
            interAd?.show(activity as Activity)
        }else
        {
           onClose()
        }
    }

    override fun onClose() {

    }
}