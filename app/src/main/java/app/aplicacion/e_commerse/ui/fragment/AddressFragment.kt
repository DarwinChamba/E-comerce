package app.aplicacion.e_commerse.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.aplicacion.e_commerse.data.model.Address
import app.aplicacion.e_commerse.databinding.FragmentAddressBinding
import app.aplicacion.e_commerse.ui.viewmodel.AddressViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    private val args by navArgs<AddressFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
           viewModel.addNewAddress.collectLatest {
               when(it){
                   is StateFirebase.Error -> {
                       binding.progressbarAddress.visibility=View.INVISIBLE
                       Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                   }
                   is StateFirebase.Loading -> {
                       binding.progressbarAddress.visibility=View.INVISIBLE
                   }
                   is StateFirebase.Succes -> {
                       binding.progressbarAddress.visibility=View.VISIBLE
                       Toast.makeText(requireContext(), "registro realizado con exito", Toast.LENGTH_SHORT).show()
                       findNavController().navigateUp()
                   }
                   null -> {

                   }
               }
           }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address=args.address
        if(address == null){
            binding.buttonDelelte.visibility=View.GONE
        }else{
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                    edStreet.setText(address.street)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                edState.setText(address.state)
            }
        }
        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle=edAddressTitle.text.toString()
                val fullname=edFullName.text.toString()
                val street=edStreet.text.toString()
                val phone=edPhone.text.toString()
                val city=edCity.text.toString()
                val state=edState.text.toString()
                val address=Address(addressTitle,fullname,street, phone, city, state)
                viewModel.addAddress(address)
            }
        }
    }
}