package c14220127.paba_b.daftarbelanja

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import c14220127.paba_b.daftarbelanja.database.daftarBelanja

class adapterDaftar(private val daftarBelanja: MutableList<daftarBelanja>): RecyclerView.Adapter<adapterDaftar.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    // Membuat interface untuk aksi hapus
    interface OnItemClickCallback{
        fun delData(dtBelanja: daftarBelanja)
        fun selesaiData(dtBelanja: daftarBelanja)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    // Fungsi untuk mengisi data ke adapter
    fun isiData(daftar: List<daftarBelanja>) {
        daftarBelanja.clear()            // Bersihkan data lama
        daftarBelanja.addAll(daftar)     // Tambahkan data baru
        notifyDataSetChanged()           // Beritahu RecyclerView ada perubahan
    }

    inner class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val _tvItemBarang = itemView.findViewById<TextView>(R.id.tvItemBarang)
        val _tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        val _tvJumlahBarang = itemView.findViewById<TextView>(R.id.tvJumlahBarang)
        val _btnEdit = itemView.findViewById<ImageButton>(R.id.btnEdit)
        val _btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
        val _btnSelesai = itemView.findViewById<Button>(R.id.btnSelesai)

        init {
            _btnDelete.setOnClickListener{
                onItemClickCallback.delData(daftarBelanja[adapterPosition])
            }
            _btnSelesai.setOnClickListener{
                onItemClickCallback.selesaiData(daftarBelanja[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterDaftar.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
        R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterDaftar.ListViewHolder, position: Int) {
        var daftar = daftarBelanja[position]

        holder._tvTanggal.setText(daftar.tanggal)
        holder._tvItemBarang.setText(daftar.item)
        holder._tvJumlahBarang.setText(daftar.jumlah)

        holder._btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, TambahDaftar::class.java)
            intent.putExtra("id", daftar.id)
            intent.putExtra("addEdit", 1)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return daftarBelanja.size
    }
}