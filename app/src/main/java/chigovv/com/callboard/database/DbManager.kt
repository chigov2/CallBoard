package chigovv.com.callboard.database

import chigovv.com.callboard.data.Add
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth
    var wer = 5

    fun publishAdd(add: Add)
    {
        if (auth.uid != null){
        db.child(add.key ?: "empty").child(auth.uid!!).child("add").setValue(add)
        }
    }

}