package app.aplicacion.e_commerse.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.ActivityTiendaShoppigBinding

class TiendaShoppig : AppCompatActivity() {
    private lateinit var binding:ActivityTiendaShoppigBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTiendaShoppigBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        navController=findNavController(R.id.fragmentContainer)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}