package app.aplicacion.e_commerse.core

import android.view.View
import androidx.fragment.app.Fragment
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.ui.view.TiendaShoppig
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation(){
    val bottomNavigation=(activity as TiendaShoppig).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigation.visibility= View.GONE
}

fun Fragment.showBottomNavigation(){
    val bottomNavigation=(activity as TiendaShoppig).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigation.visibility= View.VISIBLE
}