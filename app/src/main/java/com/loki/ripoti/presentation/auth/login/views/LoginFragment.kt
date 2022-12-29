package com.loki.ripoti.presentation.auth.login.views

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentLoginBinding
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.presentation.auth.login.LoginViewModel
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()
        binding.apply {

            progressBar.isVisible = false

            signUpBtn.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                findNavController().navigate(action)
            }

            etEmail.doOnTextChanged { text, _, _, _ ->
                email.value = text.toString()
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                password.value = text.toString()
            }
        }

        subscribeLoginEvent()

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.loginEvent.collect { event ->

                when(event) {
                    is LoginViewModel.LoginEvent.LoginLoading -> {
                        binding.progressBar.isVisible = true
                    }

                    is LoginViewModel.LoginEvent.Success -> {
                        binding.progressBar.isVisible = false

                        SharedPreferenceManager.saveAccessToken(requireContext(), event.token)
                        findNavController().navigateSafely(R.id.action_loginFragment_to_homeFragment)
                    }

                    is LoginViewModel.LoginEvent.LoginError -> {
                        binding.progressBar.isVisible = false

                        if (event.error.contains("HTTP 401")) {
                            showToast("Wrong password or email")
                        }
                        else {
                            showToast(event.error)
                        }
                    }
                }
            }
        }
    }

    private fun subscribeLoginEvent() {

        lifecycleScope.launch {
            formIsValid.collect { event ->
                binding.loginBtn.setOnClickListener {

                    binding.root.hideKeyboard()
                    val login = Login(email.value, password.value)

                    if (event) {
                        viewModel.loginUser(login)
                    }
                }
            }
        }
    }

    private val formIsValid = combine(
        email, password
    ) {
             email, password ->

        val emailNotEmpty = email.isNotEmpty()
        val emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordNotEmpty = password.isNotEmpty()

        binding.apply {

            lEmail.helperText = null
            lPassword.helperText = null

            when {

                emailNotEmpty.not() -> {
                    lEmail.helperText = "Please enter email"
                }

                emailValid.not() -> {
                    lEmail.helperText = "Please enter a valid email"
                }

                passwordNotEmpty.not() -> {
                    lPassword.helperText = "Please enter password"
                }

                else -> {
                    lEmail.helperText = null
                    lPassword.helperText = null
                }
            }
        }
        emailNotEmpty and passwordNotEmpty and emailValid
    }
}