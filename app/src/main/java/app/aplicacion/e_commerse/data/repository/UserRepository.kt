package app.aplicacion.e_commerse.data.repository

import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    val currentUser:FirebaseUser?
    suspend fun createEmailAndPassword(user:User):StateFirebase<FirebaseUser>
    suspend fun signInEmailAndPassword(email:String,password:String,state:(FirebaseState<FirebaseUser>)->Unit)
    fun logoOut()

   suspend fun resetPassword(email:String,state:(StateFirebase<String>)->Unit)
}