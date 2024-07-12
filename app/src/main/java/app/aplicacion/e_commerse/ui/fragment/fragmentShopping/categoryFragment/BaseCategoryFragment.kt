package app.aplicacion.e_commerse.ui.fragment.fragmentShopping.categoryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.databinding.FragmentBaseCategoryBinding
import app.aplicacion.e_commerse.ui.adapter.herenciaAdapter.HerenciaProductAdapter
import app.aplicacion.e_commerse.ui.viewmodel.CategorViewModel
import app.aplicacion.e_commerse.util.Category
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject


open class BaseCategoryFragment : Fragment() {


    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: HerenciaProductAdapter by lazy { HerenciaProductAdapter() }
    protected val bestProductAdapter: HerenciaProductAdapter by lazy { HerenciaProductAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        initBestProducts()
        initOfferproducts()
        sendOfferProducts()
        sendBestProducts()

        return binding.root
    }

    private fun sendBestProducts() {
        bestProductAdapter.setOnClickHereniaProducts {
            val bundle=Bundle().apply {
                putSerializable("products",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,bundle)
        }
    }

    private fun sendOfferProducts() {
        offerAdapter.setOnClickHereniaProducts {
            val bundle=Bundle().apply {
                putSerializable("products",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,bundle)
        }
    }


    fun showLoadingOfferProducts() {
        binding.pbOfferProducts.visibility = View.VISIBLE
    }

    fun showLoadingBestProducts() {
        binding.pbOfferProducts.visibility = View.VISIBLE
    }

    fun hideLoadingOfferProducts() {
        binding.pbOfferProducts.visibility = View.INVISIBLE
    }

    fun hideLoadingBestProducts() {
        binding.pbOfferProducts.visibility = View.INVISIBLE
    }

    private fun initOfferproducts() {

        binding.offerProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
            setHasFixedSize(true)

        }
    }

    private fun initBestProducts() {

        binding.bestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = bestProductAdapter
            setHasFixedSize(true)

        }
    }


}