package app.aplicacion.e_commerse.ui.fragment.fragmentShopping.categoryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.core.hide
import app.aplicacion.e_commerse.core.show
import app.aplicacion.e_commerse.core.showBottomNavigation
import app.aplicacion.e_commerse.databinding.FragmentMainCategoryBinding
import app.aplicacion.e_commerse.ui.adapter.bestdeals.BestDealtsAdapter
import app.aplicacion.e_commerse.ui.adapter.productsAdapter.ProductAdapter
import app.aplicacion.e_commerse.ui.adapter.specialproductsAdapter.SpecialProductsAdapter
import app.aplicacion.e_commerse.ui.viewmodel.CategorViewModel
import app.aplicacion.e_commerse.ui.viewmodel.SpecialProductsViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var cadapter: SpecialProductsAdapter
    private lateinit var model: SpecialProductsViewModel


    private lateinit var adapterBestDeals: BestDealtsAdapter
    private lateinit var adapterProduct:ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        initRecycler()
        initRecyclerBestDeals()
        initRecyclerProduct()
        model = ViewModelProvider(requireActivity()).get(SpecialProductsViewModel::class.java)

        observeList()
        observeListBestDeals()
        observeListBestProducts()
        paggingNested()
        sendProducts()
        sendBestProduct()
        sentSpecialProducts()
        return binding.root
    }

    private fun sentSpecialProducts() {
       cadapter.setOnClickSpecialProducts {
            val bundle=Bundle().apply {
                putParcelable("products",it)

            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,bundle)

        }
    }

    private fun sendBestProduct() {
        adapterProduct.setOnClickBestProducts {
            val bundle=Bundle().apply {
                putParcelable("products",it)

            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,bundle)

        }
    }

    private fun sendProducts() {
        adapterBestDeals.setOnClickDealsProducts{
            val bundle=Bundle().apply {
                putParcelable("products",it)

            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,bundle)

        }
    }

    private fun paggingNested() {
        binding.nextedCategoryMainFragment.setOnScrollChangeListener(NestedScrollView
            .OnScrollChangeListener{v,_,scrollY,_,_->
                if(v.getChildAt(0).bottom <= v.height +scrollY){
                    model.getBestProductsFromDatbase()
                }

            })
    }

    private fun observeListBestProducts() {
        lifecycleScope.launchWhenStarted {
            model.bestProducts.collect{
                when(it){
                    is StateFirebase.Error -> {
                        binding.pbPagging.hide()
                        ToastM(it.message.toString())
                    }
                    is StateFirebase.Loading -> {
                        binding.pbPagging.show()
                    }
                    is StateFirebase.Succes -> {
                        val d=it.dataUser
                        binding.pbPagging.hide()
                        adapterProduct.setListBestProducts(it.dataUser!!)
                    }
                    null -> {


                    }
                }

            }
        }
    }

    private fun initRecyclerProduct() {
        adapterProduct= ProductAdapter()
        binding.recyclerBestProducts.apply {
            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=adapterProduct
            setHasFixedSize(true)

        }
    }



    //observe list best deals
    private fun observeListBestDeals() {

        lifecycleScope.launchWhenStarted {
            model.bestDealsList.collect {
                when (it) {
                    is StateFirebase.Error -> {
                        ToastM(it.message.toString())
                        binding.pbBestDeals.hide()
                    }

                    is StateFirebase.Loading -> {
                        binding.pbBestDeals.show()
                    }

                    is StateFirebase.Succes -> {
                        binding.pbBestDeals.hide()
                        adapterBestDeals.diffBestDeals.submitList(it.dataUser)
                        it.dataUser?.forEach {
                            println("best deals productos que trae $it")
                        }
                    }

                    null -> {

                    }
                }
            }
        }



    }

    private fun initRecyclerBestDeals() {
        adapterBestDeals = BestDealtsAdapter()
        binding.recyclerBestDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter = adapterBestDeals
        }
    }

    private fun observeList() {
        lifecycleScope.launchWhenStarted {
            model.especialProducts.collectLatest {
                when (it) {
                    is StateFirebase.Error -> {
                        binding.pbSpecialProducts.hide()
                        Log.d("error special product",it.message.toString())
                        ToastM(it.message.toString())
                    }

                    is StateFirebase.Loading -> {

                        binding.pbSpecialProducts.show()
                    }

                    is StateFirebase.Succes -> {
                        binding.pbSpecialProducts.hide()
                        cadapter.diff.submitList(it.dataUser)

                    }

                    null -> {

                    }
                }
            }
        }
    }

    private fun initRecycler() {
        cadapter = SpecialProductsAdapter()
        binding.recyclerSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = cadapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

}