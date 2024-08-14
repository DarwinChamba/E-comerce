package app.aplicacion.e_commerse.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Products(
    val id: String,
    val name: String,
    val category: String,
    val price: Float?=null ,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>

):Parcelable{
    constructor():this("","","", images = emptyList())
}
