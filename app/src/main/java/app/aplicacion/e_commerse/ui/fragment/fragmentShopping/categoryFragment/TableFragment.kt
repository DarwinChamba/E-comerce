package app.aplicacion.e_commerse.ui.fragment.fragmentShopping.categoryFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.ui.viewmodel.CategorViewModel
import app.aplicacion.e_commerse.ui.viewmodel.factory.BaseCategoryFactory
import app.aplicacion.e_commerse.util.Category
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class TableFragment: BaseCategoryFragment() {

    @Inject
    lateinit var data: FirebaseDatabase

    private val viewmodel by viewModels<CategorViewModel> {
        BaseCategoryFactory(data, Category.Table)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOfferProducts()
        collectBestProducts()


    }





    private fun collectBestProducts() {
        lifecycleScope.launchWhenStarted {
            viewmodel.bestProducts.collect { state ->
                when (state) {
                    is StateFirebase.Error -> {
                        hideLoadingBestProducts()
                        ToastM(state.message.toString())
                    }

                    is StateFirebase.Loading -> {
                        showLoadingBestProducts()
                    }

                    is StateFirebase.Succes -> {
                        hideLoadingBestProducts()
                        bestProductAdapter.productHerenciafDiff.submitList(state.dataUser)
                        bestProductAdapter.notifyDataSetChanged()
                    }

                    null -> {


                    }
                }
            }
        }
    }

    private fun collectOfferProducts() {
        lifecycleScope.launchWhenStarted {
            viewmodel.offerProducts.collect { state ->
                when (state) {
                    is StateFirebase.Error -> {
                        hideLoadingOfferProducts()
                        ToastM(state.message.toString())
                        Log.d("erorChair", state.message.toString())
                    }

                    is StateFirebase.Loading -> {
                        showLoadingOfferProducts()
                    }

                    is StateFirebase.Succes -> {
                        hideLoadingOfferProducts()

                        offerAdapter.productHerenciafDiff.submitList(state.dataUser)
                        offerAdapter.notifyDataSetChanged()

                    }

                    null -> {

                    }
                }
            }
        }
    }

}
