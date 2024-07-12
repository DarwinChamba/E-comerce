package app.aplicacion.e_commerse.util

sealed class Category (val cateory:String) {

    object Chair:Category("chair")
    object Cupboard:Category("cuoboard")
    object Table:Category("table")
    object Acceory:Category("Accesory")
    object Furniture:Category("furniture")
}