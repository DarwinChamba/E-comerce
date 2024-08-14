package app.aplicacion.e_commerse.data.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class  FirebaseCommon(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth

) {
    private val startCollection =
        database.getReference("userEcomerce").child(auth.uid!!).child("cart")


    fun addProductCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        val newProductRef = startCollection.push()  // Genera una clave única para cada producto
        newProductRef.setValue(cartProduct).addOnSuccessListener {
            onResult(cartProduct, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }


    fun increaseQuantity(productId: String, onResult: (String?, Exception?) -> Unit) {
        val cartRef = database.getReference("userEcomerce")
            .child(auth.uid!!).child("cart")

        cartRef.orderByChild("products/id").equalTo(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (cartSnapshot in snapshot.children) {
                            val cartProduct = cartSnapshot.getValue(CartProduct::class.java)
                            if (cartProduct != null) {
                                val newQuantity = cartProduct.quantity + 1
                                val updatedCartProduct = cartProduct.copy(quantity = newQuantity)

                                cartSnapshot.ref.setValue(updatedCartProduct)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            onResult(productId, null)
                                        } else {
                                            onResult(null, task.exception)
                                        }
                                    }
                            }
                        }
                    } else {
                        onResult(null, Exception("Product not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null, error.toException())
                }
            })
    }


    fun decraceQuantity(productId: String, onResult: (String?, Exception?) -> Unit) {
        val cartRef = database.getReference("userEcomerce")
            .child(auth.uid!!).child("cart")

        cartRef.orderByChild("products/id").equalTo(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (cartSnapshot in snapshot.children) {
                            val cartProduct = cartSnapshot.getValue(CartProduct::class.java)
                            if (cartProduct != null) {
                                val newQuantity = cartProduct.quantity - 1
                                val updatedCartProduct = cartProduct.copy(quantity = newQuantity)

                                cartSnapshot.ref.setValue(updatedCartProduct)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            onResult(productId, null)
                                        } else {
                                            onResult(null, task.exception)
                                        }
                                    }
                            }
                        }
                    } else {
                        onResult(null, Exception("Product not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null, error.toException())
                }
            })
    }


    enum class QuantityChanging {
        INCREASE, DECREASE
    }

}
