package chigovv.com.callboard.acoounthelper

import android.widget.Toast
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.R
import chigovv.com.callboard.dialoghelper.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

//import com.google.firebase.ktx.Firebase

//для регистрации ползователей
class AccountHelper(act:MainActivity) {
    private val act = act
    //val sigbInRequestCode = 132
    private lateinit var signInClient: GoogleSignInClient //google client

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

        //функция отправки письма подтверждения
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

    //gmail? создаем клиент при помощи этой функции
    private fun getSignInClient():GoogleSignInClient
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(act.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(act,gso)
    }

    //функция кнопки войти под google account. будет запускаться из MainActivity
    fun SignInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent //отпрапляем интент и ждем ответ под номером sigbInRequestCode .результат нам вернет наш аккаунт
        act.startActivityForResult(intent,GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
        //след. шаг - получить наш аакаунт. для этого идем в mainactivity and create onActivityResult
    }

    //необходимо достать аккаунт из интента
    fun signInFirebaseWithGoogle(token:String)
    {//token нужен, чтобы получить credential
        val credential = GoogleAuthProvider.getCredential(token,null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(act,"Sign in is OK",Toast.LENGTH_SHORT).show()
            }
        }
    }

}