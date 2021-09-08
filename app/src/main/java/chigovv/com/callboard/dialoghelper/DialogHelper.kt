 package chigovv.com.callboard.dialoghelper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.widget.Toast
//import android.view.View.VISIBLE
//import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
//import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.R
import chigovv.com.callboard.acoounthelper.AccountHelper
import chigovv.com.callboard.databinding.SignDialogBinding
//import com.google.firebase.database.core.view.View

 //создаем конструктор в круглых кавычках (их не было) но добавляем их и act:MainActivity
class DialogHelper(act:MainActivity) {
    private val act = act

    val accHelper = AccountHelper(act)

    //передаем индекс для выбора - вход/регистрация
    @SuppressLint("StringFormatInvalid")

    fun createSignDialog(index:Int){
        //dialogBuilder - создаем спец класс
        val builder = AlertDialog.Builder(act)
        //теперь с помощью билдера создаем диалог - это обычный экран - при помощи binding
        //act - важно.
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)// содержит всю разметку
        //содержит разметку sign dialog нухно достать
        val view = rootDialogElement.root  //содержит всю инфо  sign dialog

        //создали диалог, но он еще не рисуется на экране
        builder.setView(view)

        setDialogState(index,rootDialogElement)

        val dialog = builder.create()
        //слушатели для кнопок
        rootDialogElement.btSignUpIn.setOnClickListener{
            setOnClickSignUpIn(index,rootDialogElement,dialog)
        }

        rootDialogElement.btForgetPass.setOnClickListener{
            setOnClickResetPassword(rootDialogElement,dialog)
        }
        rootDialogElement.btGoogleSignIn.setOnClickListener{
            accHelper.SignInWithGoogle()
        }
        //рисуем на экране
        dialog.show()
    }

     private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {
         // выбор окна
         if (index == DialogConst.SIGN_UP_STATE)
         {
             rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
             rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
         }
         else if (index  == DialogConst.SIGN_IN_STATE){
             rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
             rootDialogElement.btSignUpIn.text =  act.resources.getString(R.string.sign_in_action)
             rootDialogElement.btForgetPass.visibility =android.view.View.VISIBLE
         }
     }

     private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
         dialog?.dismiss()
         if (index == DialogConst.SIGN_UP_STATE){
             accHelper.signUpWithEmail(rootDialogElement.etSignEmail.text.toString(),
                 rootDialogElement.etSignPassword.text.toString())
         }
         else if ((index == DialogConst.SIGN_IN_STATE)){
             accHelper.signInWithEmail(rootDialogElement.etSignEmail.text.toString(),
                 rootDialogElement.etSignPassword.text.toString())
         }
     }

     private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
            if (rootDialogElement.etSignEmail.text.isNotEmpty()){
                act.mAuth.sendPasswordResetEmail(rootDialogElement.etSignEmail.text.toString()).addOnCompleteListener { task ->
                    //addOnCompleteListener выдет выдавать была ли успешная передача email
                    if (task.isSuccessful){
                        Toast.makeText(act,R.string.email_reset_pass_was_sent,Toast.LENGTH_LONG).show()
                    }
                }
                dialog?.dismiss()
            }

         //если поле пароля пусто
         else{
             rootDialogElement.tvDialogMaessage.visibility = View.VISIBLE
         }
     }
 }