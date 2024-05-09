package app.aplicacion.e_commerse.util

sealed class FirebaseState<out T>{

    object Loading:FirebaseState<Nothing>()
    data class Success<out T>(val data:T?=null):FirebaseState<T> ()
    data class Failure <out T>(val error:String):FirebaseState<T>()
}

sealed class StateFirebase<T>(
    val dataUser: T?=null,
    val message:String?=null
){
    class Succes<T>(data:T?):StateFirebase<T>(data)
    class Error<T>(meesage:String?):StateFirebase<T>(null,meesage)
    class Loading<T>():StateFirebase<T>()
}

