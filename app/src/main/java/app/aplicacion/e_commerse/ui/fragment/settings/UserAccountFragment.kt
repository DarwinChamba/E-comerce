package app.aplicacion.e_commerse.ui.fragment.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.databinding.FragmentUserAccountBinding
import app.aplicacion.e_commerse.ui.viewmodel.UserAccountViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserAccountFragment : Fragment() {
    private lateinit var binding: FragmentUserAccountBinding
    private val viewmodel by viewModels<UserAccountViewModel>()
    private var imageUri: Uri? = null
    private lateinit var imageActivityLauncher:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityLauncher=
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                imageUri=it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSettings()
        obserUpdateUser()
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val firstName=edFirstName.text.toString().trim()
                val lastName=edLastName.text.toString().trim()
                val email=edEmail.text.toString().trim()
                val user=User(firstName,lastName, email)
                viewmodel.updateUser(user, imageUri)
            }
        }
        binding.imageEdit.setOnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            imageActivityLauncher.launch(intent)
        }
        binding.tvUpdatePassword.setOnClickListener{
            
        }
    }

    private fun obserUpdateUser() {
        lifecycleScope.launchWhenStarted {
            viewmodel.updateInfo.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    is StateFirebase.Loading -> {

                    }

                    is StateFirebase.Succes -> {
                        findNavController().navigateUp()
                        Toast.makeText(requireContext(), "registro modificado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeSettings() {
        lifecycleScope.launchWhenStarted {
            viewmodel.user.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        hideUserLoading()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is StateFirebase.Loading -> {
                        showUserLoading()
                    }

                    is StateFirebase.Succes -> {
                        hideUserLoading()
                        showUserInformation(it.dataUser!!)
                        println("usuario actual "+it.dataUser)
                        Toast.makeText(requireContext(), "registro relaizado con exito", Toast.LENGTH_SHORT).show()
                    }

                    null -> {}
                }
            }
        }
    }

    private fun showUserInformation(dataUser: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment)
                .load(dataUser.imagePath)
                .error(ColorDrawable(Color.BLACK))
                .into(imageUser)
            edFirstName.setText(dataUser.firsName)
            edLastName.setText(dataUser.lastName)
            edEmail.setText(dataUser.email)
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }

}