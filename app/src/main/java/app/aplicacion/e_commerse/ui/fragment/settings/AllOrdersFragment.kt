package app.aplicacion.e_commerse.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.databinding.FragmentOrdersBinding
import app.aplicacion.e_commerse.ui.adapter.AllOrdersAdapter
import app.aplicacion.e_commerse.ui.viewmodel.AllOrdersViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersFragment:Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    val viewModel by viewModels<AllOrdersViewModel>()
    val ordersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOrdersBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRv()
        observeOrderAll()
        ordersAdapter.onClick={
            val action=AllOrdersFragmentDirections.actionAllOrdersFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeOrderAll() {
        lifecycleScope.launchWhenStarted {
            viewModel.allOrder.collectLatest {
                when(it){
                    is StateFirebase.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is StateFirebase.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE

                    }
                    is StateFirebase.Succes -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        ordersAdapter.orderDiff.submitList(it.dataUser)

                        if(it.dataUser.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                            binding.progressbarAllOrders.visibility = View.GONE
                        }

                    }
                    null -> {

                    }

                }
            }
        }
    }

    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter=ordersAdapter
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        }
    }
}