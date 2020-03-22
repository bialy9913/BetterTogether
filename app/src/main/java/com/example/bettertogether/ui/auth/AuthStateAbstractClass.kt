package com.example.bettertogether.ui.auth

import android.util.Patterns
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import com.example.bettertogether.R
import java.util.regex.Pattern

abstract class AuthStateAbstractClass: Fragment(), AuthListener {
    protected val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph)
    protected val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onStart() {
        super.onStart()
        viewModel.initAuthListener(this)
    }

    protected fun validateUsername(editText: EditText) {
        val pattern = Patterns.EMAIL_ADDRESS
        editText.error=
            if (!pattern.matcher(editText.text.toString()).matches()){
                "Please enter valid e-mail address"
            }
            else
                null
    }
    protected  fun validatePassword(passwordText: EditText){
        val pattern= Pattern.compile(".{4,}")
        passwordText.error=
            if (!pattern.matcher(passwordText.text.toString()).matches()){
                "Password doesn't match our policy"
            }
            else
                null
    }
}