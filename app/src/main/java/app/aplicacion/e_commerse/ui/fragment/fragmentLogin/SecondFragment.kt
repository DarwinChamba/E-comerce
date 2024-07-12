package app.aplicacion.e_commerse.ui.fragment.fragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {
private lateinit var binding:FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSecondBinding.inflate(inflater,container,false)
        binding.registerUser.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_registerFragment)

        }
        binding.buttonLoginAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_loginFragment)


        }

        return binding.root
    }




}