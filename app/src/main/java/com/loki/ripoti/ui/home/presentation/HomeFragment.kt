package com.loki.ripoti.ui.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentHomeBinding
import com.loki.ripoti.ui.home.ReportsViewModel
import com.loki.ripoti.util.extensions.lightStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor
import com.loki.ripoti.util.extensions.showToast
import com.loki.ripoti.util.reportList
import dagger.hilt.android.AndroidEntryPoint

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
        binding.apply {

            reportAdapter = ReportAdapter { reports ->
                navigateToReportComments(reports.id)
            }
            reportRecycler.adapter = reportAdapter
            reportAdapter.setReportList(reportList)
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

    private fun navigateToReportComments(reportId: Int) {

    }
}