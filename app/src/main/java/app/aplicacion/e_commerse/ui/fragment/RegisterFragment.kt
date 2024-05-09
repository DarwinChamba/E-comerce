package app.aplicacion.e_commerse.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.core.hide
import app.aplicacion.e_commerse.core.show
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.databinding.FragmentRegisterBinding
import app.aplicacion.e_commerse.ui.viewmodel.UserViewModel
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.RegisterValidation
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var model: UserViewModel
    private var uid = ""
    var user = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.registrar.setOnClickListener {
            registerWithEmail()
        }

        readObserve()
        validarEntradaDatos()
        return binding.root
    }

    private fun validarEntradaDatos() {
        lifecycleScope.launchWhenStarted {
            model.validation.collect {
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.email.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if (it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.email.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }

    private fun readObserve() {
        lifecycleScope.launchWhenStarted {
            model._flowCreate.collect {
                when (it) {


                    is StateFirebase.Error -> {
                        binding.progressBar.hide()
                        ToastM(it.message!!)
                    }
                    is StateFirebase.Loading -> {

                        binding.progressBar.show()
                    }
                    is StateFirebase.Succes -> {

                        user.id = it.dataUser!!.uid
                        binding.progressBar.hide()
                        ToastM("registro realizado con exito")
                    }

                    null -> {

                    }
                }
            }
        }

    }

    private fun registerWithEmail() {
        user = User(
            binding.name.text.toString().trim(),
            binding.lastName.text.toString().trim(),
            binding.email.text.toString().trim(),
            binding.password.text.toString().trim(),

        )
        model.createEmail(user)

    }


}