package edu.proyecto.expressfoodapp.ui.client.reports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.proyecto.expressfoodapp.data.model.DailyReport
import edu.proyecto.expressfoodapp.databinding.ItemReportBinding

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reports = emptyList<DailyReport>()

    fun submitList(newReports: List<DailyReport>) {
        reports = newReports
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(
        private val binding: ItemReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(report: DailyReport) {
            binding.tvReportDate.text = "Fecha: ${report.date}"
            binding.tvReportOrderCount.text = "Órdenes: ${report.orderCount}"
            binding.tvReportTotal.text = "Total diario: ₡${report.totalAmount}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size
}