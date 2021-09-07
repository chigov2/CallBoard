package chigovv.com.callboard.acoounthelper

import android.widget.Toast
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.R
import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.ktx.Firebase

//для регистрации ползователей
class AccountHelper(act:MainActivity) {
    private val act = act

    fun signUpWithEmail(email:String,password:String){
        if(email.isNotEmpty() && password.isNotEmpty())
        {
            act.mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result?.user)
                }
                else{
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error),Toast.LENGTH_SHORT).show()
                }
            }
            //также добавляем слушатель успешной\неуспешной регистрации

        }
    }

    fun signInWithEmail(email:String,password:String){
        if(email.isNotEmpty() && password.isNotEmpty())
        {
            act.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    act.uiUpdate(task.result?.user)
                }
                else{
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error),Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //функция отправки письмп подтверждения
    private fun sendEmailVerification(user:FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(act, act.resources.getString(R.string.send_verification_done),Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(act, act.resources.getString(R.string.send_verification_error),Toast.LENGTH_SHORT).show()
            }
        }
    }
}