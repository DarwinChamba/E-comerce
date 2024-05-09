package app.aplicacion.e_commerse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.aplicacion.e_commerse.databinding.ActivityMainBinding
import app.aplicacion.e_commerse.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var model:UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model=ViewModelProvider(this).get(UserViewModel::class.java)


    }

    override fun onStart() {
        super.onStart()
        navController=findNavController(R.id.fragmentContainer)
    }

}


