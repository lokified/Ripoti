package com.loki.ripoti.presentation.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserReports
import com.loki.ripoti.databinding.ReportItemLayoutBinding

class ReportAdapter(
    private val onItemClick: (UserReports) -> Unit = {}
): RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportList = mutableListOf<UserReports>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ReportItemLayoutBinding.inflate(inflater, parent, false)

        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.binding.userIv.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.test))
        holder.bind(reportList[position])
        holder.itemView.setOnClickListener {
            onItemClick(reportList[position])
        }
    }

    override fun getItemCount() = reportList.size

    fun setReportList(reports: List<UserReports>) {
        this.reportList = reports.toMutableList()
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(
         val binding: ReportItemLayoutBinding
        ): RecyclerView.ViewHolder(binding.root) {

        fun bind(reports: UserReports) {
            binding.apply {
                reportTitleTxt.text = reports.report.username
                reportDescriptionTxt.text = reports.report.description
                reportTimeTxt.text = reports.report.created_at
                userInitialsTxt.text = reports.report.username[0].toString()
            }
        }
    }
}