package com.example.bettertogether.ui.auth.sign_up


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.NavHostFragment
import com.example.bettertogether.R
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.show
import com.example.bettertogether.utils.showToast
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : BaseFragment(),SignUpNavigator {

    private val signUpViewModel: SignUpViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateFields()
        setOnClickListeners()
    }

    override fun signingUpStarted() {
        progressBar.visibility=View.VISIBLE
    }

    override fun signingUpSuccess() {
        progressBar.visibility=View.GONE
        showToast("Signing up success! Now you can log in")
        navController.navigateWithoutComingBack(R.id.signUpFragment,R.id.signInFragment)
    }

    override fun signingUpFailure(message: String) {
        progressBar.visibility=View.GONE
        showToast(message)
    }
    private fun setOnClickListeners(){
        signUpBtn.setOnClickListener{
            validateAndSignUp()
        }
    }
    private fun validateFields(){
        /*emailText.addTextChangedListener {
            validateUsername(emailText)
        }
        passwordText.addTextChangedListener{
            validatePassword(passwordText)
        }
        passwordConfirmationText.addTextChangedListener {
            validatePassword(passwordConfirmationText)
        }*/
    }
    private fun validateAndSignUp(){
        if(emailText.text.toString().isEmpty()){
            emailText.error="Please enter an e-mail"
            emailText.requestFocus()
            return
        }
        if(passwordText.text.toString().isEmpty()){
            passwordText.error="Please enter password"
            passwordText.requestFocus()
            return
        }
        if(passwordText.text.toString().isEmpty()){
            passwordText.error="Please enter confirm password"
            passwordText.requestFocus()
            return
        }
        if(passwordText.text.toString() != passwordConfirmationText.text.toString()){
            passwordConfirmationText.error="Passwords don't match each other"
            return
        }
        signUpViewModel.signUp(emailText.text.toString(),passwordText.text.toString())
        clearFields()
    }
    private fun clearFields(){
        emailText.text.clear()
        passwordText.text.clear()
        passwordConfirmationText.text.clear()
        emailText.error=null
        passwordText.error=null
        passwordConfirmationText.error=null
    }
}
