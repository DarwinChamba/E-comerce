package app.aplicacion.e_commerse.ui.fragment.fragmentLogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.core.hide
import app.aplicacion.e_commerse.core.show
import app.aplicacion.e_commerse.databinding.FragmentInicioBinding
import app.aplicacion.e_commerse.ui.view.TiendaShoppig
import app.aplicacion.e_commerse.ui.viewmodel.CurrenteUserViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class InicioFragment : Fragment() {
    private  lateinit var binding:FragmentInicioBinding
    private lateinit var model:CurrenteUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentInicioBinding.inflate(inflater,container,false)
        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_inicioFragment_to_secondFragment)


        }
        model=ViewModelProvider(requireActivity()).get(CurrenteUserViewModel::class.java)
        observeCurrentUser()
        return binding.root
    }
    private  fun observeCurrentUser(){
        lifecycleScope.launchWhenStarted {
            model.currenteUser.collect{
                when(it){
                    is StateFirebase.Error -> {
                        binding.progressBar.hide()
                        ToastM(it.message.toString())
                    }
                    is StateFirebase.Loading ->{
                        binding.progressBar.show()
                    }
                    is StateFirebase.Succes -> {
                        binding.progressBar.hide()
                        startActivity(Intent(requireContext(),TiendaShoppig::class.java))

                    }

                    null -> {


                    }
                }
            }
        }
    }

}