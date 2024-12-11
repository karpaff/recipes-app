package com.example.recipesapp.fragment
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.recipesapp.R

class LoginDialogFragment(
    private val onLogin: (String, String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_login, null)

        val usernameEditText: EditText = view.findViewById(R.id.login_input)
        val passwordEditText: EditText = view.findViewById(R.id.password_input)
        val loginButton: Button = view.findViewById(R.id.login_button)

        builder.setView(view)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                onLogin(username, password)
                dismiss()
            }
        }

        return builder.create()
    }
}