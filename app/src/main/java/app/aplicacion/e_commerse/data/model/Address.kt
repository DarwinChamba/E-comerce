package app.aplicacion.e_commerse.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Address(
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phone: String,
    val city: String,
    val state: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "")
}
