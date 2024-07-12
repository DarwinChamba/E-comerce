package app.aplicacion.e_commerse.data.model


import java.io.Serializable

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

):Serializable{
    constructor():this("","","", images = emptyList())
}
