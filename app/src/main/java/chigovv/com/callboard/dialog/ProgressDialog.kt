package chigovv.com.callboard.dialog

import android.app.Activity
import android.app.AlertDialog
import chigovv.com.callboard.databinding.ProgressDialogLayoutBinding
import chigovv.com.callboard.databinding.SignDialogBinding

object ProgressDialog {

    fun createProgressDialog(act: Activity): AlertDialog{
        //dialogBuilder - создаем спец класс
        val builder = AlertDialog.Builder(act)
        //теперь с помощью билдера создаем диалог - это обычный экран - при помощи binding
        //act - важно.
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(act.layoutInflater)// содержит всю разметку
        //содержит разметку progress dialog нухно достать
        val view = rootDialogElement.root  //содержит всю инфо  progress dialog

        //создали диалог, но он еще не рисуется на экране
        builder.setView(view)

        val dialog = builder.create()
        dialog.setCancelable(false)// диалог нельзя будет остановить

        //рисуем на экране
        dialog.show()
        return dialog
    }

}