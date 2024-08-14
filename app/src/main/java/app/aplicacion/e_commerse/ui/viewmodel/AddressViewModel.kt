package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.Address
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val data: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<StateFirebase<Address>?>(null)
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()


    fun addAddress(address: Address) {
        val validateInput = validateInput(address)
        if (validateInput) {
            viewModelScope.launch { _addNewAddress.emit(StateFirebase.Loading()) }
            data.getReference("userEcomerce").child(auth.uid!!)
                .child("address").child(UUID.randomUUID().toString()).setValue(address).addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(StateFirebase.Succes(address)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(StateFirebase.Error(it.message)) }
                }
        } else {
            viewModelScope.launch { _error.emit("Todos los campos son obligatorios") }
        }


    }

    private fun validateInput(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.state.trim().isNotEmpty()
    }


}