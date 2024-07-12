package app.aplicacion.e_commerse.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.aplicacion.e_commerse.ui.viewmodel.CategorViewModel
import app.aplicacion.e_commerse.util.Category
import com.google.firebase.database.FirebaseDatabase

class BaseCategoryFactory (
    private val data:FirebaseDatabase,
    private val category:Category

        ):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategorViewModel(data,category) as T
    }

}
