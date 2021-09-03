package chigovv.com.callboard.dialoghelper

import android.app.AlertDialog
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.databinding.SignDialogBinding

//создаем конструктор в круглых кавычках (их не было) но добавляем их и act:MainActivity
class DialogHelper(act:MainActivity) {
    private val act = act

    public fun createSignDialog(){
        //dialogBuilder - создаем спец класс
        val builder = AlertDialog.Builder(act)
        //теперь с помощью билдера создаем диалог - это обычный экран - при помощи binding
        //act - важно.
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        //содержит разметку sign dialog нухно достать
        val view = rootDialogElement.root
        //создали диалог, но он еще не рисуется на экране
        builder.setView(view)
        //рисуем на экране
        builder.show()
    }
}