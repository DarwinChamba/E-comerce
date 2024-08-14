package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
):ViewModel(){

    private val _user= MutableStateFlow<StateFirebase<User>?>(null)
    val user=_user.asStateFlow()
    init {
        getUser()
    }
    fun getUser(){
        viewModelScope.launch { _user.emit(StateFirebase.Loading()) }
        database.getReference("userEcomerce").child(auth.uid!!).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val userObject=it.getValue(User::class.java)
                    userObject?.let {
                        viewModelScope.launch { _user.emit(StateFirebase.Succes(it)) }
                    }

                }else{
                    viewModelScope.launch { _user.emit(StateFirebase.Error("la ruta para encontrar el usuario esta equiocada")) }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _user.emit(StateFirebase.Error(it.message)) }
            }
    }

    fun logout() {
        auth.signOut()
    }
}