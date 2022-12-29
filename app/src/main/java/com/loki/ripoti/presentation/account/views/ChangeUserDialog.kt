package com.loki.ripoti.presentation.account.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loki.ripoti.databinding.FragmentChangeUserDialogBinding
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.UserUpdate
import com.loki.ripoti.presentation.account.UserUpdateViewModel
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeUserDialog(
    private val username: String,
    private val name: String,
    private val email: String,
    private val userId: Int
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChangeUserDialogBinding
    private val userUpdateViewModel: UserUpdateViewModel by viewModels()
    private lateinit var userDialogListener: UserDialogListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeUserDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        const val TAG = "Change User Dialog"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            etName.setText(name)
            etUsername.setText(username)

            updateBtn.setOnClickListener {
                if (etName.text?.isNotEmpty()!! && etUsername.text?.isNotEmpty()!!) {

                    val user = UserUpdate(
                        etName.text.toString(), etUsername.text.toString()
                    )
                    userUpdateViewModel.updateUser(
                        userId = userId, email = email, user = user
                    )
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
                        userDialogListener.onDismiss()
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

    fun setListener(dialogListener: UserDialogListener) {
        this.userDialogListener = dialogListener
    }

    interface UserDialogListener {
        fun onDismiss()
    }
}