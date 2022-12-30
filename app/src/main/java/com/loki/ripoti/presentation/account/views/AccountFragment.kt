package com.loki.ripoti.presentation.account.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentAccountBinding
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.presentation.account.UserProfileViewModel
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.navigateSafely
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val userProfileViewModel : UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpAccount()
        implicitIntents()
    }


    private fun setUpAccount() {

        val token = SharedPreferenceManager.getToken(requireContext())
        val jwt = JWT(token)
        val id = jwt.getClaim("id").asInt()!!
        val email = jwt.getClaim("email").asString()!!

        binding.apply {
            val profile = Profile(email)
            userProfileViewModel.getUserProfile(profile)

            userInitialsTxt.text = GetUserInitials.initials(context = requireContext())

            changePinLayout.setOnClickListener {
                val changePasswordDialog = ChangePasswordDialog(id, email)
                changePasswordDialog.show(parentFragmentManager, ChangePasswordDialog.TAG)
            }
        }

        subscribeState(id)
    }

    private fun subscribeState(userId: Int) {

        binding.apply {
            lifecycleScope.launchWhenStarted {

                userProfileViewModel.userProfileState.collect { state ->
                    if (state.isLoading) {
                        progressBar.isVisible = state.isLoading
                    }

                    state.userProfile?.let { profile ->
                        progressBar.isVisible = false
                        userInitialNameTxt.text = profile.name
                        userEmailTxt.text = profile.email
                        userUsernameTxt.text = profile.username


                        editBtn.setOnClickListener {
                            val changeUserDialog = ChangeUserDialog(
                                username = profile.username,
                                name = profile.name,
                                email = profile.email,
                                userId = userId
                            )
                            changeUserDialog.setListener(userDialogListener)
                            changeUserDialog.show(parentFragmentManager, ChangeUserDialog.TAG)
                        }
                    }

                    if (state.error.isNotEmpty()) {
                        progressBar.isVisible = false
                        showToast(state.error)
                    }
                }
            }
        }
    }

    private val userDialogListener = object: ChangeUserDialog.UserDialogListener {
        override fun onDismiss() {

            val token = SharedPreferenceManager.getToken(requireContext())
            val jwt = JWT(token)
            val email = jwt.getClaim("email").asString()!!
            val profile = Profile(email)
            userProfileViewModel.getUserProfile(profile)
        }
    }


    private fun implicitIntents() {
        val from = arrayOf("lsheldon645@gmail.com")

        binding.makeRequestLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, from)
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        val phoneNumber = "254725992494"

        binding.callLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
    }
}