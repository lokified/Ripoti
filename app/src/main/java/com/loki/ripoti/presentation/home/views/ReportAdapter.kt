package com.loki.ripoti.presentation.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.databinding.ReportItemLayoutBinding

class ReportAdapter(
    private val localUserName: String,
    private val onItemClick: (Reports) -> Unit = {}
): RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportList = mutableListOf<Reports>()

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
                userInitialsTxt.text = getInitials()
            }
        }

        private fun getInitials(): String {

            val nameArr = localUserName.split(" ")

            var firstName = ""
            var secondName = ""
            for (i in nameArr) {
                firstName = nameArr[0]
                secondName = nameArr[1]
            }

            val c1: Char = firstName[0]
            val c2: Char = secondName[0]
            return c1.toString() + c2.toString()
        }
    }
}