package app.aplicacion.e_commerse.ui.fragment.fragmentShopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.FirebaseCommon
import app.aplicacion.e_commerse.databinding.FragmentCartBinding
import app.aplicacion.e_commerse.ui.adapter.CartProductAdapter
import app.aplicacion.e_commerse.ui.viewmodel.CartViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private val cartProductAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()
    var totalPrice = 0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()
        observePrice()
        observeCart()
        cartProductAdapter.onPlusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)
        }
        cartProductAdapter.onMinClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASE)
        }
        observeDeleteDialog()
        navigateDetails()
        binding.buttonCheckout.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(
                totalPrice,
                cartProductAdapter.lista.toTypedArray(),true
            )
            findNavController().navigate(action)
        }
    }

    private fun observeDeleteDialog() {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest { cartProduct ->
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item from your cart?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deleteCartProduct(cartProduct)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }
    }

    private fun navigateDetails() {
        cartProductAdapter.setOnClickBestProducts { cartProduct ->
            val b = Bundle().apply {
                putParcelable("products", cartProduct.products)
            }
            findNavController().navigate(R.id.action_cartFragment_to_detailsFragment, b)
        }

    }

    private fun observePrice() {
        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest {
                it?.let {
                    totalPrice = it
                    binding.tvTotalPrice.text = "$ $it"
                }
            }
        }
    }

    private fun observeCart() {
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {

                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    is StateFirebase.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE

                    }

                    is StateFirebase.Succes -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.dataUser!!.isEmpty()) {
                            showEmptyCart()
                            hideOtherViews()

                        } else {
                            hideEmptyCart()
                            showOtherViews()
                            // cartProductAdapter.diffCart.submitList(it.dataUser)
                            cartProductAdapter.setListaPrograma(it.dataUser.toMutableList())
                        }
                    }

                    null -> {

                    }
                }
            }
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartProductAdapter
        }
    }


}