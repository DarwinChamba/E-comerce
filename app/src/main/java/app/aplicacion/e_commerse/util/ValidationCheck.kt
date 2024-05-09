package app.aplicacion.e_commerse.util

import android.util.Patterns

fun validateEmail(email:String):RegisterValidation{
    if(email.isEmpty()){
        return RegisterValidation.Failed("email required")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("error al ingresar el email")
    }
  return RegisterValidation.Success

}

fun validatePassword(password:String):RegisterValidation{
    if(password.length <6){
        return RegisterValidation.Failed("la contrase;a no puede ser menor de 6 carcaterees")
    }
    if(password.isEmpty()){
        return RegisterValidation.Failed("password required")
    }
    return RegisterValidation.Success

}