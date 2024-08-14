package app.aplicacion.e_commerse.ui.fragment.fragmentShopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.Address
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.data.model.orden.Order
import app.aplicacion.e_commerse.data.model.orden.OrderStatus
import app.aplicacion.e_commerse.databinding.FragmentBillingBinding
import app.aplicacion.e_commerse.ui.adapter.AddressAdapter
import app.aplicacion.e_commerse.ui.adapter.BilingProductAdapter
import app.aplicacion.e_commerse.ui.viewmodel.BillingViewModel
import app.aplicacion.e_commerse.ui.viewmodel.OrderViewModel
import app.aplicacion.e_commerse.util.HorizontalItemDecoration
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingAdapter by lazy { BilingProductAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)

        products = args.mproducts.toList()
        totalPrice = args.totalPrice


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerAddress()
        initBillingCartProduct()
        observeBilling()
        if(args.payment){
            binding.apply {
                buttonPlaceOrder.visibility=View.INVISIBLE
                totalBoxContainer.visibility=View.INVISIBLE
                middleLine.visibility=View.INVISIBLE
                bottomLine.visibility=View.INVISIBLE
            }
        }
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        billingAdapter.diffBilling.submitList(products)
        binding.tvTotalPrice.text = "$ $totalPrice"

        addressAdapter.onClick = {
            selectedAddress = it
            if(!args.payment){
                val bundle=Bundle().apply { putParcelable("address",selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
            }

        }


        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "please seleceted addrees", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
            showInformacionDialog()
        }
        orderItems()

    }//llave de cierre del metodo onViewCreated

    private fun showInformacionDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Items")
            setMessage("Do you want to order your cart items")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun observeBilling() {
        lifecycleScope.launchWhenStarted {
            billingViewModel.adress.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    is StateFirebase.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is StateFirebase.Succes -> {
                        binding.progressbarAddress.visibility = View.GONE
                        addressAdapter.diff.submitList(it.dataUser)

                    }

                    null -> {


                    }
                }
            }
        }
    }

    private fun orderItems() {
        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    is StateFirebase.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is StateFirebase.Succes -> {
                        binding.progressbarAddress.visibility = View.GONE
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "your order was placed", Snackbar.LENGTH_SHORT)
                            .show()

                    }

                    null -> {


                    }
                }
            }
        }
    }


    private fun initBillingCartProduct() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun initRecyclerAddress() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

}