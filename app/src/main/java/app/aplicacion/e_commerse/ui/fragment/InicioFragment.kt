package app.aplicacion.e_commerse.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.FragmentInicioBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InicioFragment : Fragment() {
    private  lateinit var binding:FragmentInicioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentInicioBinding.inflate(inflater,container,false)
        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_inicioFragment_to_secondFragment)


        }
        return binding.root
    }

}