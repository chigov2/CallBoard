package chigovv.com.callboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import chigovv.com.callboard.act.EditAdsAct
import chigovv.com.callboard.databinding.ActivityMainBinding
import chigovv.com.callboard.dialoghelper.DialogConst
import chigovv.com.callboard.dialoghelper.DialogHelper
import chigovv.com.callboard.dialoghelper.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    //binding
    //private var rootElement:ActivityMainBinding? = null
    private lateinit var rootElement:ActivityMainBinding
    private lateinit var tvAccount:TextView

    //Dialog helper
    private val dialogHelper = DialogHelper(this)

    //когда запускается MainActivity - берем оттуда Authentication
    val mAuth = FirebaseAuth.getInstance()

    //добавляем слушатель нажатий для меню
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialization of var
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }
    //прослушиваем меню main_menu new ads from the right и открываем EditAdsAct
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_ads){
            //Запуск нового activity
            val i = Intent(this,EditAdsAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    //создание \ подключение main menu (new button)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE) {
            //Log.d("MyLog", "Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                //есть аккаунт. теперь из него надо достать токен
                if (account !=null)
                {
                    Log.d("MyLog", "API Google sign in: <-")
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken)
                    Log.d("MyLog", "API Google sign in: ->")
                }

            }catch (e:ApiException){
                Log.d("MyLog", "API error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart(){
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init(){
        setSupportActionBar(rootElement.mainContent.toolbar)

        //кнопка - необходимо создать два ресурса в string.xml и обратиться к ним яерез R.string
        val toggle = ActionBarDrawerToggle(this,rootElement.drawerLayout,rootElement.mainContent.toolbar,R.string.open,R.string.close)

        //указать что DraderLayout будет открываться через эту кнопку
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // связь navView and item selected listener
        rootElement.navView.setNavigationItemSelectedListener(this)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
        //поиск редера и текствью


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.id_my_ads -> {Toast.makeText(this,"Pressed id_my_ads",Toast.LENGTH_SHORT).show()}
            R.id.id_car -> {Toast.makeText(this,"Pressed id_car",Toast.LENGTH_SHORT).show()}
            R.id.id_pc -> {Toast.makeText(this,"Pressed id_pc",Toast.LENGTH_SHORT).show()}
            R.id.id_smartphones -> {Toast.makeText(this,"Pressed id_smartphones",Toast.LENGTH_SHORT).show()}
            R.id.id_home_tech -> {Toast.makeText(this,"Pressed id_home_tech",Toast.LENGTH_SHORT).show()}
            R.id.ac_sign_up -> {
                //Toast.makeText(this,"Pressed ac_sign_up",Toast.LENGTH_SHORT).show()
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }
            R.id.ac_sign_in -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.ac_sign_out -> {
                uiUpdate(null)
                mAuth.signOut()
                //чтобы добраться до accounthelper нужет dialoghelper
                dialogHelper.accHelper.SignOutGoogle()
            }
        }
        //чтобы меню закрывалось после нажатия
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user:FirebaseUser?){
            tvAccount.text = if (user == null){
                resources.getString(R.string.not_reg)
            }
        else{
            user.email
        }
    }
}