package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.data.repository.UserRepositoryImpl
import app.aplicacion.e_commerse.data.repositoryStore.DataRepositoryImpl
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.RegisterFieldState
import app.aplicacion.e_commerse.util.RegisterValidation
import app.aplicacion.e_commerse.util.StateFirebase
import app.aplicacion.e_commerse.util.validateEmail
import app.aplicacion.e_commerse.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryImpl: UserRepositoryImpl,
    private val data: DataRepositoryImpl
) : ViewModel() {
    var datosFunciona: FirebaseUser? = null

    private val listaSignIn = MutableSharedFlow<FirebaseState<FirebaseUser>?>()
    val _listSignIn= listaSignIn.asSharedFlow()

    // lista flow
    private val flowCreate = MutableStateFlow<StateFirebase<FirebaseUser>?>(null)
    val _flowCreate: Flow<StateFirebase<FirebaseUser>?> = flowCreate

    //lista insert data in realtimeDatabase
    private val flowData = MutableStateFlow<StateFirebase<User>?>(null)
    val _flowData: Flow<StateFirebase<User>?> = flowData


    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    private val _sharedPassword= MutableSharedFlow<StateFirebase<String>>()
    val sharedPassword=_sharedPassword.asSharedFlow()


    fun createEmail(userCurrent: User) {
        if (validateCredentianls(userCurrent)) {
            viewModelScope.launch {
                flowCreate.value = StateFirebase.Loading()

                val user = repositoryImpl.createEmailAndPassword(userCurrent)

                data.insertStore(user.dataUser, userCurrent)
                //repositoryImpl.insertUserStore(userCurrent)

                flowCreate.value = user


            }
        } else {
            val registerState = RegisterFieldState(
                validateEmail(userCurrent.email),
                validatePassword(userCurrent.password)
            )


            runBlocking {
                _validation.send(registerState)
            }


        }

    }

    private fun validateCredentianls(user: User): Boolean {

        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val isCorrect = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
        return isCorrect
    }

    fun signInEmail(email: String, password: String) {
        viewModelScope.launch {
            listaSignIn.emit(FirebaseState.Loading)
            repositoryImpl.signInEmailAndPassword(email, password) {
                viewModelScope.launch {
                    listaSignIn.emit(it)
                }
            }
        }
    }

    fun resetPassword(email:String){
        viewModelScope.launch {
            repositoryImpl.resetPassword(email){
                viewModelScope.launch{
                    _sharedPassword.emit(it)
                }

            }
        }
    }




}