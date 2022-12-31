package com.loki.ripoti.presentation.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.databinding.ReportItemLayoutBinding
import com.loki.ripoti.util.GetUserInitials

class ReportAdapter(
    private val onItemClick: (Reports) -> Unit = {}
): RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportList = mutableListOf<Reports>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ReportItemLayoutBinding.inflate(inflater, parent, false)

        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reportList[position])
        holder.itemView.setOnClickListener {
            onItemClick(reportList[position])
        }
    }

    override fun getItemCount() = reportList.size

    fun setReportList(reports: List<Reports>) {
        this.reportList = reports.toMutableList()
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(
         val binding: ReportItemLayoutBinding
        ): RecyclerView.ViewHolder(binding.root) {

        fun bind(reports: Reports) {
            binding.apply {
                reportTitleTxt.text = reports.username
                reportDescriptionTxt.text = reports.description
                reportTimeTxt.text = reports.created_at
                userInitialsTxt.text = GetUserInitials.initials(username = reports.name)
                reportDateTxt.text = reports.created_on
            }
        }
    }
}