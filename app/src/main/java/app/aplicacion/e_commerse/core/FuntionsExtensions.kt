package app.aplicacion.e_commerse.core

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.ToastM (message:String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}

fun View.hide(){
    visibility=View.INVISIBLE
}

fun View.show(){
    visibility=View.VISIBLE
}