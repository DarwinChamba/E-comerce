package app.aplicacion.e_commerse.ui.fragment.fragmentShopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.core.showBottomNavigation
import app.aplicacion.e_commerse.databinding.FragmentProfileBinding
import app.aplicacion.e_commerse.ui.fragment.fragmentLogin.LoginFragment
import app.aplicacion.e_commerse.ui.viewmodel.ProfileViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import com.bumptech.glide.Glide
import com.google.firebase.components.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }
        binding.linearOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }
        binding.linearOrders.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                0f,
                emptyArray(),false
            )
            findNavController().navigate(action)
        }
        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), LoginFragment::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.tvVersion.text = "version ${BuildConfig.VERSION_NAME}"

        lifecycleScope.launch {
            viewModel.user.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is StateFirebase.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }

                    is StateFirebase.Succes -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.dataUser!!.imagePath)
                            .error(ColorDrawable(Color.BLACK)).into(binding.imageUser)
                        binding.tvUserName.text=it.dataUser.firsName
                    }

                    null -> {


                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

}