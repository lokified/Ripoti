package com.loki.ripoti.presentation.home.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.auth0.android.jwt.JWT
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.databinding.FragmentHomeBinding
import com.loki.ripoti.presentation.home.ReportsViewModel
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.lightStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val reportsViewModel: ReportsViewModel by viewModels()
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        setUpObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()

        val token = SharedPreferenceManager.getToken(context)
        val jwt = JWT(token)

        val id = jwt.getClaim("id").asString()
        val localUserName = jwt.getClaim("name").asString()
        val email = jwt.getClaim("email").asString()

        binding.apply {

            reportAdapter = ReportAdapter(localUserName!!) { reports ->
                navigateToReportComments(reports)
            }
            reportRecycler.adapter = reportAdapter
            reportRecycler.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).orientation
                )
            )
        }
    }

    private fun setUpObservers() {

        reportsViewModel.state.observe(viewLifecycleOwner) { result ->

            binding.progressBar.isVisible = result.isLoading

            if (result.reports.isNotEmpty()) {
                reportAdapter.setReportList(result.reports)
            }
            if (result.error.isNotBlank()) {
                showToast(result.error)
            }
        }
    }

    private fun navigateToReportComments(reports: Reports) {

        val action = HomeFragmentDirections.actionHomeFragmentToReportDetailFragment(
            report = reports
        )
        findNavController().navigate(action)
    }
}