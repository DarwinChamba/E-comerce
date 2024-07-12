package app.aplicacion.e_commerse.ui.fragment.fragmentLogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.core.dialogPersonalizado
import app.aplicacion.e_commerse.core.hide
import app.aplicacion.e_commerse.core.show
import app.aplicacion.e_commerse.databinding.FragmentLoginBinding
import app.aplicacion.e_commerse.ui.view.TiendaShoppig
import app.aplicacion.e_commerse.ui.viewmodel.UserViewModel
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var model: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.loginLogin.setOnClickListener {
            loginUser()
        }
        binding.forgotPassword.setOnClickListener {
            forgotPassword()
        }
        listSignIn()
        observeResetPassword()
        return binding.root
    }

    private fun observeResetPassword() {
        lifecycleScope.launchWhenStarted {
            model.sharedPassword.collect{
                when(it){
                    is StateFirebase.Error -> {
                        binding.progressBar.hide()
                        ToastM(it.message.toString())

                    }
                    is StateFirebase.Loading -> {
                        binding.progressBar.show()

                    }
                    is StateFirebase.Succes -> {
                        binding.progressBar.hide()
                        Snackbar.make(requireView(),"Ingrese al link del email ingresado",Snackbar.LENGTH_LONG).show()

                    }
                }
            }
        }
    }

    private fun forgotPassword() {
        dialogPersonalizado {
            model.resetPassword(it)
        }
    }

    private fun loginUser() {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString()
        model.signInEmail(email, password)
    }

    private fun listSignIn() {
        lifecycleScope.launchWhenStarted {
            model._listSignIn.collect {
                when (it) {
                    is FirebaseState.Failure -> {

                        ToastM(it.error)
                    }

                    FirebaseState.Loading -> {
                        binding.progressBar.show()

                    }

                    is FirebaseState.Success -> {
                        binding.progressBar.hide()

                        startActivity(Intent(requireContext(), TiendaShoppig::class.java))


                    }

                    null -> {

                    }
                }
            }
        }

    }


}