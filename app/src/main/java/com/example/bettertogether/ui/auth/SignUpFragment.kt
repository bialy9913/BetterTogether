package com.example.bettertogether.ui.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.bettertogether.R
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.showToast
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : AuthStateAbstractClass(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn.setOnClickListener(this)
        validateFields()

    }
    override fun onStarted() {
        progressBar.visibility=View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility=View.GONE
        navController.navigateWithoutComingBack(R.id.signUpFragment,R.id.signInFragment)
        showToast("Signing up success. Now you can Log in")
    }

    override fun onFailure(message: String) {
        progressBar.visibility=View.GONE
        clearFields()
        showToast(message)
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.signUpBtn -> {
                validateAndSignUp()
            }
        }
    }
    private fun validateFields(){
        emailText.addTextChangedListener {
            validateUsername(emailText)
        }
        passwordText.addTextChangedListener{
            validatePassword(passwordText)
        }
        passwordConfirmationText.addTextChangedListener {
            validatePassword(passwordConfirmationText)
        }
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
        viewModel.signUp(emailText.text.toString(),passwordText.text.toString())
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
