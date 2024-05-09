package app.aplicacion.e_commerse.core

import android.app.Dialog
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import app.aplicacion.e_commerse.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.dialogPersonalizado(
    onSetDialog:(String)->Unit
){
    val dialog=BottomSheetDialog(requireContext(),R.style.DialogStyle)
    dialog.setContentView(R.layout.dialog_forot_password)
    dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
    val emailEditText=dialog.findViewById<EditText>(R.id.resetEmail)
    val cancel=dialog.findViewById<Button>(R.id.cancelButton)
    val send=dialog.findViewById<Button>(R.id.sendButton)
    send?.setOnClickListener {
        val email=emailEditText?.text.toString().trim()
        onSetDialog(email)
        dialog.dismiss()
    }
    cancel?.setOnClickListener {
        dialog.dismiss()
    }
    dialog.create()
    dialog.show()

}