package com.loki.ripoti.presentation.account.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentAccountBinding
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.navigateSafely
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

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
    }


    private fun setUpAccount() {

        binding.apply {
            val token = SharedPreferenceManager.getToken(requireContext())
            val jwt = JWT(token)
            val id = jwt.getClaim("id").asInt()!!
            val name = jwt.getClaim("name").asString()!!
            val email = jwt.getClaim("email").asString()!!
            val username = jwt.getClaim("userName").asString()!!

            userInitialsTxt.text = GetUserInitials.initials(context = requireContext())
            userInitialNameTxt.text = name
            userEmailTxt.text = email

            changePinLayout.setOnClickListener {
                val changePasswordDialog = ChangePasswordDialog(id, email)
                changePasswordDialog.show(parentFragmentManager, ChangePasswordDialog.TAG)
            }

            editBtn.setOnClickListener {
                val changeUserDialog = ChangeUserDialog(
                    username = username,
                    name = name,
                    email = email,
                    userId = id
                )
                changeUserDialog.show(parentFragmentManager, ChangeUserDialog.TAG)
            }
        }
    }
}