package com.example.recipesapp.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.recipesapp.R

class RegisterDialogFragment(
    private val onRegister: (String, String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_register, null)

        val usernameEditText: EditText = view.findViewById(R.id.username_edit_text)
        val passwordEditText: EditText = view.findViewById(R.id.password_edit_text)
        val registerButton: Button = view.findViewById(R.id.register_button)

        builder.setView(view)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                onRegister(username, password)
                dismiss()
            }
        }

        return builder.create()
    }
}
