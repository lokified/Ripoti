package com.loki.ripoti.presentation.auth.registration.views

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
import com.loki.ripoti.databinding.FragmentRegistrationBinding
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.presentation.auth.registration.RegistrationViewModel
import com.loki.ripoti.util.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel : RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()
        navigateBack()
        binding.apply {

            progressBar.isVisible = false

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            viewModel.apply {
                etFirstName.doOnTextChanged { text, _, _, _ ->

                    if (text?.isNotEmpty()!!) {
                        onEvent(RegistrationViewModel.RegistrationFormEvent.FirstNameChanged(text.toString()))
                        registerState.value.firstnameError = null
                    }
                    else {
                        registerState.value.firstname = ""
                    }
                }
                etLastName.doOnTextChanged { text, _, _, _ ->
                    if (text?.isNotEmpty()!!) {
                        onEvent(RegistrationViewModel.RegistrationFormEvent.LastNameChanged(text.toString()))
                        registerState.value.lastnameError = null
                    }
                    else {
                        registerState.value.lastname = ""
                    }
                }
                etEmail.doOnTextChanged { text, _, _, _ ->
                    if (text?.isNotEmpty()!!) {
                        onEvent(RegistrationViewModel.RegistrationFormEvent.EmailChanged(text.toString()))
                        registerState.value.emailError = null
                    }
                    else {
                        registerState.value.email = ""
                    }
                }
                etPassword.doOnTextChanged { text, _, _, _ ->
                    if (text?.isNotEmpty()!!) {
                        onEvent(RegistrationViewModel.RegistrationFormEvent.PasswordChanged(text.toString()))
                        registerState.value.passwordError = null
                    }
                    else {
                        registerState.value.password = ""
                    }
                }
                etConPassword.doOnTextChanged { text, _, _, _ ->
                    if (text?.isNotEmpty()!!) {
                        onEvent(RegistrationViewModel.RegistrationFormEvent.ConPasswordChanged(text.toString()))
                        registerState.value.conPasswordError = null
                    }
                    else {
                        registerState.value.conPassword = ""
                    }
                }

                registerBtn.setOnClickListener {
                    onEvent(RegistrationViewModel.RegistrationFormEvent.Submit)
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.registerState.collect { state ->

                binding.apply {
                    lFirstName.helperText = state.firstnameError
                    lLastName.helperText = state.lastnameError
                    lEmail.helperText = state.emailError
                    lPassword.helperText = state.passwordError
                    lConPassword.helperText = state.conPasswordError
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.registerEvent.collectLatest { event ->

                when(event) {

                    is RegistrationViewModel.RegisterEvent.RegisterLoading-> {
                        binding.progressBar.isVisible = true
                    }

                    is RegistrationViewModel.RegisterEvent.RegisterError -> {
                        binding.progressBar.isVisible = false

                        if(event.error.contains("HTTP 400")){
                            showToast("you are already registered")
                        }
                    }

                    is RegistrationViewModel.RegisterEvent.Success -> {
                        binding.progressBar.isVisible = false
                        if (event.message == "registration successful") {
                            showToast(event.message)
                            findNavController().navigateSafely(R.id.action_registrationFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }
}