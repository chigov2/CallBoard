 package chigovv.com.callboard.dialoghelper

import android.annotation.SuppressLint
import android.app.AlertDialog
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.R
import chigovv.com.callboard.acoounthelper.AccountHelper
import chigovv.com.callboard.databinding.SignDialogBinding

//создаем конструктор в круглых кавычках (их не было) но добавляем их и act:MainActivity
class DialogHelper(act:MainActivity) {
    private val act = act

    private val accHelper = AccountHelper(act)

    //передаем индекс для выбора - вход/регистрация
    @SuppressLint("StringFormatInvalid")

    public fun createSignDialog(index:Int){
        //dialogBuilder - создаем спец класс
        val builder = AlertDialog.Builder(act)
        //теперь с помощью билдера создаем диалог - это обычный экран - при помощи binding
        //act - важно.
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)// содержит всю разметку
        //содержит разметку sign dialog нухно достать
        val view = rootDialogElement.root  //содержит всю инфо  sign dialog

        //создали диалог, но он еще не рисуется на экране
        builder.setView(view)

        // выбор окна
        if (index == DialogConst.SIGN_UP_STATE)
        {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        }
        else if (index  == DialogConst.SIGN_IN_STATE){
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            rootDialogElement.btSignUpIn.text =  act.resources.getString(R.string.sign_in_action)
        }
        val dialog = builder.create()
        rootDialogElement.btSignUpIn.setOnClickListener{
            dialog.dismiss()
            if (index == DialogConst.SIGN_UP_STATE){
                accHelper.signUpWithEmail(rootDialogElement.etSignEmail.text.toString(),
                    rootDialogElement.etSignPassword.text.toString())
            }
            else if ((index == DialogConst.SIGN_IN_STATE)){
                accHelper.signInWithEmail(rootDialogElement.etSignEmail.text.toString(),
                    rootDialogElement.etSignPassword.text.toString())
            }
        }

        //рисуем на экране
        dialog.show()
    }
}