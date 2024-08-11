package app.aplicacion.e_commerse.data.modelcart

import app.aplicacion.e_commerse.data.model.Products

data class CartProduct(
    val product:Products,
    val quantity:Int,
    val color:Int?=null,
    val size:String?=null
){
    constructor() :this(Products(),1,null,null)

}
