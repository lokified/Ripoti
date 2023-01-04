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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

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
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }

            viewModel.apply {
                etEmail.doOnTextChanged { text, _, _, _ ->

                    if (text?.isNotEmpty()!!){
                        onEvent(LoginViewModel.LoginFormEvent.EmailChanged(text.toString()))
                        loginState.value.emailError = null
                    }
                    else {
                        loginState.value.email = ""
                    }
                }
                etPassword.doOnTextChanged { text, _, _, _ ->

                    if (text?.isNotEmpty()!!){
                        onEvent(LoginViewModel.LoginFormEvent.PasswordChanged(text.toString()))
                        loginState.value.passwordError = null
                    }
                    else{
                        loginState.value.password = ""
                    }
                }

                loginBtn.setOnClickListener {
                    onEvent(LoginViewModel.LoginFormEvent.Submit)
                    root.hideKeyboard()
                }
            }
        }


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.loginState.collectLatest { state ->
                binding.apply {
                    lEmail.helperText = state.emailError
                    lPassword.helperText = state.passwordError
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collectLatest { event ->

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
}