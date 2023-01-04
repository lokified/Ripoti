package com.loki.ripoti.presentation.add_report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.loki.ripoti.databinding.FragmentAddReportBinding
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.hideKeyboard
import com.loki.ripoti.util.extensions.navigateBack
import com.loki.ripoti.util.extensions.showSnackBar
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddReportFragment : Fragment() {

    private lateinit var binding: FragmentAddReportBinding
    private val addReportViewModel: AddReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReportBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val token = SharedPreferenceManager.getToken(requireContext())
        val jwt = JWT(token)

        val id = jwt.getClaim("id").asInt()!!
        val username = jwt.getClaim("userName").asString()!!
        val name = jwt.getClaim("name").asString()!!

        binding.apply {

            localUserInitialsTxt.text = GetUserInitials.initials(context = requireContext())
            usernameTxt.text = username
            nameTxt.text = "@$name"

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            addReportBtn.isEnabled = false
            etAddReport.doOnTextChanged { text, _, _, _ ->
                addReportBtn.isEnabled = true

                addReportBtn.setOnClickListener {

                    if (text?.isNotEmpty()!!) {
                        val description = etAddReport.text.toString()
                        val report = Report(
                            username, description
                        )
                        addReportViewModel.addReport(id, report)
                    }
                    root.hideKeyboard()
                }
            }
        }

        navigateBack()

        subScribeState()
    }

    private fun subScribeState() {

        lifecycleScope.launchWhenStarted {
            addReportViewModel.state.collectLatest { state ->

                binding.progressBar.isVisible = state.isLoading

                if (state.isLoading) {
                    binding.addReportBtn.isEnabled = false
                }

                if (state.message.isNotEmpty()) {

                    if (state.message == "report added") {
                        binding.root.showSnackBar(state.message)
                        findNavController().popBackStack()
                    }
                }

                if (state.error.isNotEmpty()) {
                    showToast(state.error)
                }
            }
        }
    }
}