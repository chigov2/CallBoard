package chigovv.com.callboard.acoounthelper

import android.util.Log
import android.widget.Toast
import chigovv.com.callboard.MainActivity
import chigovv.com.callboard.R
import chigovv.com.callboard.constants.FireBaseConstants
import chigovv.com.callboard.dialoghelper.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

//import com.google.firebase.ktx.Firebase

//для регистрации ползователей
class AccountHelper(act: MainActivity) {
    private val act = act

    //val sigbInRequestCode = 132
    private lateinit var  signInClient: GoogleSignInClient //google client

    //регистрация по адресу email
    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)
                    } else {// не удалось зарегистрироваться
                        //Toast.makeText(act, act.resources.getString(R.string.sign_up_error),Toast.LENGTH_SHORT).show()
                        //проверка на ошибки - приходит  в task
                        Log.d("MyLog", "Exeption:: " + task.exception)


                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FireBaseConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                //здесь будем соединять gmail and e-mail
                                //Toast.makeText(act,FireBaseConstants.ERROR_EMAIL_ALREADY_IN_USE,Toast.LENGTH_SHORT).show()
                                //соединить два email google and usual
                                linkEmailToGmail(email,password)

                            }

                            //создаем новый пакет для констант constants и объект класс FireBaseConstants

                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FireBaseConstants.ERROR_INVALID_EMAIL) {
                                //здесь будем соединять gmail and e-mail
                                Toast.makeText(act,FireBaseConstants.ERROR_INVALID_EMAIL,Toast.LENGTH_SHORT).show()
                                //Log.d("MyLog","Exeption::  + ${exception.errorCode}")
                            }
                        }
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            //Log.d("MyLog", "Weak Password Exeption:: ${exception.errorCode}")
                            if (exception.errorCode == FireBaseConstants.ERROR_WEAK_PASSWORD) {
                                //здесь будем соединять gmail and e-mail
                                Toast.makeText(act,FireBaseConstants.ERROR_WEAK_PASSWORD,Toast.LENGTH_SHORT).show()
                                //Log.d("MyLog","Exeption::  + ${exception.errorCode}")
                            }
                        }
                    }
                }
            //также добавляем слушатель успешной\неуспешной регистрации

        }
    }

    private fun linkEmailToGmail(email: String,password: String){
        val credential = EmailAuthProvider.getCredential(email,password)
        if (act.mAuth.currentUser !== null) {
            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act,act.resources.getString(R.string.link_done),Toast.LENGTH_LONG).show()
                }
            }
        }
        else
        {
            Toast.makeText(act,act.resources.getString(R.string.link_not_done),Toast.LENGTH_SHORT).show()
        }
    }

    //вход по адресу email
    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    act.uiUpdate(task.result?.user) //показываем e-mail по которому зарегистрировались
                } else {
                    Log.d("MyLog", "sign In With Email Exeption3:: ${task.exception}")

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("MyLog", "sign In With Email Exeption:: " + task.exception)
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        //Log.d("MyLog", "sign In With Email Exeption2:: ${exception.errorCode}")
                        //Log.d("MyLog", "sign In With Email Exeption:: ${exception.errorCode}" )

                        if (exception.errorCode == FireBaseConstants.ERROR_INVALID_EMAIL) {
                            //здесь будем соединять gmail and e-mail
                            Toast.makeText(act,FireBaseConstants.ERROR_INVALID_EMAIL,Toast.LENGTH_SHORT).show()
                            //Log.d("MyLog","Exeption::  + ${exception.errorCode}")
                        }
                        else if(exception.errorCode == FireBaseConstants.ERROR_WRONG_PASSWORD){
                            Toast.makeText(act,FireBaseConstants.ERROR_WRONG_PASSWORD,Toast.LENGTH_SHORT).show()
                        }


                    }
                    else if (task.exception is FirebaseAuthInvalidUserException){
                         val exception = task.exception as FirebaseAuthInvalidUserException
                        Log.d("MyLog", "sign In With Email Exeption4:: ${exception.errorCode}")
                        if (exception.errorCode == FireBaseConstants.ERROR_USER_NOT_FOUND)
                        {
                            Toast.makeText(act,FireBaseConstants.ERROR_USER_NOT_FOUND,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }

    //функция отправки письма подтверждения
    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_done),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //gmail? создаем клиент при помощи этой функции
    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail()
            .build()   //requestEmail() - добавить емаил в бд и приложение
        return GoogleSignIn.getClient(act, gso)
    }

    //функция кнопки войти под google account. будет запускаться из MainActivity
    fun SignInWithGoogle() {
        signInClient = getSignInClient()
        val intent =
            signInClient.signInIntent //отпрапляем интент и ждем ответ под номером sigbInRequestCode .результат нам вернет наш аккаунт
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
        //след. шаг - получить наш аакаунт. для этого идем в mainactivity and create onActivityResult
    }

    fun SignOutGoogle() {
        getSignInClient().signOut()
    }

    //вход по адресу google
    //необходимо достать аккаунт из интента
    fun signInFirebaseWithGoogle(token: String) {//token нужен, чтобы получить credential
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(act, "Sign in is OK", Toast.LENGTH_SHORT).show()
                act.uiUpdate(task.result?.user)                           //показать gmail пользователя в приложении
            }else{
                Log.d("MyLog", "Googl Sign In Exeption:: " + task.exception)
            }
        }
    }

}


//Log.d("MyLog", "sign In With Email Exeption:: " + task.exception)
//Toast.makeText(act,act.resources.getString(R.string.sign_in_error),Toast.LENGTH_SHORT).show()
// Log.d("MyLog", "sign In With Email Exeption:: ${exception.errorCode}" )
//Toast.makeText(act,FireBaseConstants.ERROR_WRONG_PASSWORD,Toast.LENGTH_SHORT).show()
//Log.d("MyLog","Exeption::  + ${exception.errorCode}") //ERROR_EMAIL_ALREADY_IN_USE
//Log.d("MyLog", "Weak Paddword Exeption:: ${exception.errorCode}")