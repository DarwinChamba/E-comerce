package app.aplicacion.e_commerse.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.ActivityTiendaShoppigBinding
import app.aplicacion.e_commerse.ui.viewmodel.CartViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TiendaShoppig : AppCompatActivity() {
    private lateinit var binding: ActivityTiendaShoppigBinding
    private lateinit var navController: NavController
    //private lateinit var modelCart: CartViewModel
    private val modelCart by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiendaShoppigBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // modelCart = ViewModelProvider(this).get(CartViewModel::class.java)
        oberveCart()
    }

    private fun oberveCart() {
        lifecycleScope.launch {
            modelCart.cartProducts.collect {
                when (it) {
                    is StateFirebase.Error -> {

                    }

                    is StateFirebase.Loading -> {


                    }

                    is StateFirebase.Succes -> {

                        val count = it.dataUser?.size ?: 0
                        binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }

                    null -> {

                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}