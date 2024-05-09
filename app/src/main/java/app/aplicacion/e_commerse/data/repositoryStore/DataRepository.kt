package app.aplicacion.e_commerse.data.repositoryStore

import app.aplicacion.e_commerse.data.model.User
import com.google.firebase.auth.FirebaseUser

interface DataRepository {
    suspend fun insertStore(uid: FirebaseUser?,user:User )
}