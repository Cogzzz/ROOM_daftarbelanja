package c14220127.paba_b.daftarbelanja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import c14220127.paba_b.daftarbelanja.database.historyBarang

class adapterHistory(private val historyList: MutableList<historyBarang>) :
    RecyclerView.Adapter<adapterHistory.ListViewHolder>() {

    fun isiData(history: List<historyBarang>) {
        historyList.clear()
        historyList.addAll(history)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        var tvItemBarang: TextView = itemView.findViewById(R.id.tvItemBarang)
        var tvJumlahBarang: TextView = itemView.findViewById(R.id.tvJumlahBarang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = historyList[position]
        holder.tvTanggal.text = history.tanggal
        holder.tvItemBarang.text = history.item
        holder.tvJumlahBarang.text = history.jumlah
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}