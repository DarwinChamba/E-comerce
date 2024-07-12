package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CurrenteUserViewModel @Inject constructor(
    val user:FirebaseAuth
):ViewModel() {
private val getCurrentUser= MutableStateFlow<StateFirebase<FirebaseUser>?>(StateFirebase.Loading())
    val currenteUser: StateFlow<StateFirebase<FirebaseUser>?> get() = getCurrentUser

    init {
        if(user.currentUser !=null){
            getCurrentUser.value=StateFirebase.Succes(user.currentUser)
        }else{
            getCurrentUser.value=StateFirebase.Error("usuario no registardo")
        }
    }



}