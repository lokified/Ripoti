package com.loki.ripoti.presentation.account.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loki.ripoti.databinding.FragmentChangePasswordDialogBinding
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.presentation.account.UserUpdateViewModel
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordDialog(
    private val userId: Int,
    private val email: String
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChangePasswordDialogBinding
    private val userUpdateViewModel: UserUpdateViewModel by viewModels()

    private val oldPassword = MutableStateFlow("")
    private val newPassword = MutableStateFlow("")
    private val conPassword = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object{
        const val TAG = "Change pin Bottom Sheet"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {

            etOldPassword.doOnTextChanged { text, _, _, _ ->
                oldPassword.value = text.toString()
            }

            etNewPassword.doOnTextChanged { text, _, _, _ ->
                newPassword.value = text.toString()
            }

            etNewConPassword.doOnTextChanged { text, _, _, _ ->
                conPassword.value = text.toString()
            }


            lifecycleScope.launch {
                isFormValid.collect { event ->

                    updateBtn.setOnClickListener {
                        if (event) {
                            val password = Password(
                                oldPassword = oldPassword.value,
                                newPassword = newPassword.value
                            )
                            userUpdateViewModel.updateUserPassword(userId, email, password)
                        }
                    }
                }
            }
        }

        subscribeEvents()
    }



    private fun subscribeEvents() {

        CoroutineScope(Dispatchers.Main).launch {

            userUpdateViewModel.changePassEvent.collect { event ->

                when(event) {

                    is UserUpdateViewModel.UpdateEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.updateBtn.text = "Update"
                        showToast(event.message)
                        dismiss()
                    }

                    is UserUpdateViewModel.UpdateEvent.UpdateLoading -> {
                        binding.progressBar.isVisible = true
                        binding.updateBtn.text = ""
                    }

                    is UserUpdateViewModel.UpdateEvent.UpdateError -> {
                        binding.progressBar.isVisible = false
                        binding.updateBtn.text = "Update"
                        showToast(event.error)
                    }
                }
            }
        }
    }


    private val isFormValid = combine(
        oldPassword, newPassword, conPassword
    ) {
            oldPassword, newPassword, conPassword ->

        val oldPasswordNotEmpty = oldPassword.isNotEmpty()
        val newPasswordNotEmpty = newPassword.isNotEmpty()
        val conPasswordNotEmpty = conPassword.isNotEmpty()
        val passwordMatches = newPassword == conPassword

        binding.apply {

            lOldPassword.helperText = null
            lNewPassword.helperText = null
            lNewConPassword.helperText = null

            when {

                oldPasswordNotEmpty.not() -> {
                    lOldPassword.helperText = "Please enter your old password"
                }

                newPasswordNotEmpty.not() -> {
                    lNewPassword.helperText = "Please enter a password"
                }

                conPasswordNotEmpty.not() -> {
                    lNewConPassword.helperText = "Please confirm password"
                }

                passwordMatches.not() -> {
                    lNewConPassword.helperText = "Password does not match"
                }

                else -> {
                    lOldPassword.helperText = null
                    lNewPassword.helperText = null
                    lNewConPassword.helperText = null
                }
            }
        }
        oldPasswordNotEmpty and newPasswordNotEmpty and conPasswordNotEmpty and passwordMatches
    }

}