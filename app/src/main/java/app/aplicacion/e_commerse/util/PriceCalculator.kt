package app.aplicacion.e_commerse.util

fun Float?.getProductPrice(price: Float): Float {
    if (this == null) {
    return price
    }
    val remaingPricePercentage=1f-this
    val priceAffterOffer=remaingPricePercentage +price
    return priceAffterOffer
}