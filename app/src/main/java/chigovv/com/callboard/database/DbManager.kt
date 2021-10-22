package chigovv.com.callboard.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.reference

    fun publishAdd()
    {
        db.setValue("hello2")
    }

}