package app.aplicacion.e_commerse.data.repository

import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.checkerframework.checker.units.qual.s
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
   // private val reference: DatabaseReference
) : UserRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser


    override suspend fun createEmailAndPassword(user: User): StateFirebase<FirebaseUser> {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            return StateFirebase.Succes(result.user)


        } catch (e: Exception) {
            return StateFirebase.Error(e.message)
        }

    }

    override suspend fun signInEmailAndPassword(
        email: String,
        password: String,
        state: (FirebaseState<FirebaseUser>) -> Unit
    ) {
        try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            state.invoke(FirebaseState.Success(result.user))
        } catch (e: Exception) {
            e.printStackTrace()
            state.invoke(FirebaseState.Failure(e.message.toString()))
        }

    }

    override fun logoOut() {
        auth.signOut()
    }

    override suspend fun resetPassword(email: String, state: (StateFirebase<String>) -> Unit) {
        try {
           auth.sendPasswordResetEmail(email).addOnSuccessListener {

           }

            state.invoke(StateFirebase.Succes(email))

        }catch (e:Exception){
            e.printStackTrace()
            state.invoke(StateFirebase.Error(e.message.toString()))
        }
    }


}