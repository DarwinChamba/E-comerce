package app.aplicacion.e_commerse.data.repositoryStore

import app.aplicacion.e_commerse.data.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val reference: DatabaseReference
):DataRepository {


    override suspend fun insertStore(uid: FirebaseUser?, user: User) {
        user.id=uid!!.uid
        reference.child(user.id).setValue(user)
    }
}