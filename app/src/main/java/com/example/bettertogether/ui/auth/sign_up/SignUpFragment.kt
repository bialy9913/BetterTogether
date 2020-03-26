package com.example.bettertogether.ui.auth.sign_up


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentSignUpBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.show
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
        val binding: FragmentSignUpBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_up, container, false
            )
        binding.signUpViewModel=signUpViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateFields()
    }

    override fun signingUpStarted() {
        clearFields()
        progressBar.show()
    }

    override fun signingUpSuccess() {
        progressBar.hide()
        showToast("Signing up success! Now you can log in")
        navController.navigateWithoutComingBack(R.id.signUpFragment,R.id.signInFragment)
    }

    override fun signingUpFailure(message: String) {
        progressBar.hide()
        showToast(message)
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
        if(email.text.toString().isEmpty()){
            email.error="Please enter an e-mail"
            email.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()){
            password.error="Please enter password"
            password.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()){
            password.error="Please enter confirm password"
            password.requestFocus()
            return
        }
    }
    private fun clearFields(){
        email.text.clear()
        password.text.clear()
        email.error=null
        password.error=null
        email.requestFocus()
    }
}
