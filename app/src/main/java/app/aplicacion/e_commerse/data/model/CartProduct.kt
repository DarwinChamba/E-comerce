package app.aplicacion.e_commerse.data.model

data class CartProduct(
    val products:Products,
    val quantity:Int,
    val selectedColor:Int?=null,
    val selectedSize:String?=null
){
    constructor() :this(Products(),1,null,null)

}