package app.aplicacion.e_commerse.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.ui.application.MyApplication
import app.aplicacion.e_commerse.util.RegisterValidation
import app.aplicacion.e_commerse.util.StateFirebase
import app.aplicacion.e_commerse.util.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<StateFirebase<User>?>(null)
    val user = _user.asStateFlow()

    private val _updateInfo = MutableSharedFlow<StateFirebase<User>>()
    val updateInfo = _updateInfo.asSharedFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch { _user.emit(StateFirebase.Loading()) }
        database.getReference("userEcomerce").child(auth.uid!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val user = it.getValue(User::class.java)
                    user?.let {
                        viewModelScope.launch { _user.emit(StateFirebase.Succes(it)) }
                        println("este es el usuario $it")
                    }
                } else {
                    viewModelScope.launch { _user.emit(StateFirebase.Error("El usuario no existe")) }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { _user.emit(StateFirebase.Error("Error ruta incorrecta")) }
            }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        val areInputValid = validateEmail(user.email) is RegisterValidation.Success &&
                user.firsName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()

        if (!areInputValid) {
            viewModelScope.launch { _updateInfo.emit(StateFirebase.Error("Check your input")) }
            return
        }
        viewModelScope.launch { _updateInfo.emit(StateFirebase.Loading()) }

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }
    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<MyApplication>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)
            } catch (e: Exception) {
                viewModelScope.launch { _user.emit(StateFirebase.Error(e.message)) }
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrievedOldImage: Boolean) {
        val userRef = database.getReference("userEcomerce").child(auth.uid!!)

        userRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                // Obtén el usuario actual desde los datos mutables
                val currentUser = currentData.getValue(User::class.java)

                if (currentUser == null) {
                    // Si el usuario no existe, establece los nuevos datos y finaliza la transacción
                    currentData.value = user
                    return Transaction.success(currentData)
                }
                // Si debe recuperarse la imagen anterior, copia el path de la imagen
                if (shouldRetrievedOldImage) {
                    val newUser = user.copy(imagePath = currentUser.imagePath ?: "")
                    currentData.value = newUser
                } else {
                    // Si no, simplemente actualiza con los datos nuevos
                    currentData.value = user
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    // Manejo del error si la transacción falla
                    Log.e("FirebaseTransaction", "Transaction failed.", error.toException())
                } else if (committed) {
                    // La transacción se completó con éxito
                    Log.d("FirebaseTransaction", "Transaction completed.")
                }
            }

        })
    }
}
