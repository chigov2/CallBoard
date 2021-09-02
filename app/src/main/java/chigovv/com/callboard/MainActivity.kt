package chigovv.com.callboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import chigovv.com.callboard.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    //binding
    //private var rootElement:ActivityMainBinding? = null
    private lateinit var rootElement:ActivityMainBinding

    //добавляем слушатель нажатий для меню
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialization of var
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        //val view = rootElement.root
        setContentView(rootElement.root)
        init()
    }

    private fun init(){

        //кнопка - необходимо создать два ресурса в string.xml и обратиться к ним яерез R.string
        val toggle = ActionBarDrawerToggle(this,rootElement.drawerLayout,rootElement.mainContent.toolbar,R.string.open,R.string.close)

        //указать что DraderLayout будет открываться через эту кнопку
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // связь navView and item selected listener
        rootElement.navView.setNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.id_my_ads -> {Toast.makeText(this,"Pressed id_my_ads",Toast.LENGTH_SHORT).show()}
            R.id.id_car -> {Toast.makeText(this,"Pressed id_car",Toast.LENGTH_SHORT).show()}
            R.id.id_pc -> {Toast.makeText(this,"Pressed id_pc",Toast.LENGTH_SHORT).show()}
            R.id.id_smartphones -> {Toast.makeText(this,"Pressed id_smartphones",Toast.LENGTH_SHORT).show()}
            R.id.id_home_tech -> {Toast.makeText(this,"Pressed id_home_tech",Toast.LENGTH_SHORT).show()}
            R.id.ac_sign_up -> {Toast.makeText(this,"Pressed ac_sign_up",Toast.LENGTH_SHORT).show()}
            R.id.ac_sign_in -> {Toast.makeText(this,"Pressed ac_sign_in",Toast.LENGTH_SHORT).show()}
            R.id.ac_sign_out -> {Toast.makeText(this,"Pressed ac_sign_out",Toast.LENGTH_SHORT).show()}
        }
        //чтобы меню закрывалось после нажатия
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}