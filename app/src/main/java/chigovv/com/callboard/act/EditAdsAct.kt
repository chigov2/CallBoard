package chigovv.com.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import chigovv.com.callboard.R
import chigovv.com.callboard.databinding.ActivityEditAdsBinding

class EditAdsAct : AppCompatActivity() {
    private lateinit var rootElement:ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
    }
}