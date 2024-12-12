package c14220127.paba_b.daftarbelanja

import android.content.ContentUris
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import c14220127.paba_b.daftarbelanja.database.daftarBelanja
import c14220127.paba_b.daftarbelanja.database.daftarBelanjaDB
import c14220127.paba_b.daftarbelanja.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TambahDaftar : AppCompatActivity() {
    lateinit var etItem: EditText
    lateinit var etJumlah: EditText
    var DB = daftarBelanjaDB.getDatabase(this)
    var tanggal = getCurrentDate()
    var iID: Int = 0
    var iAddEdit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etItem = findViewById(R.id.etItem)
        etJumlah = findViewById(R.id.etJumlah)
        val _btnTambah = findViewById<Button>(R.id.btnTambah)
        val _btnUpdate = findViewById<Button>(R.id.btnUpdate)
        iID = intent.getIntExtra("id", 0)
        iAddEdit = intent.getIntExtra("addEdit", 0)


        //apabila addEdit bernilai 0, maka button simpan akan menjadi visible dan button update menjadi
        //GONE. Selain itu, juga edittext judul menjadi enabled untuk dilakukan pengisian.
        if (iAddEdit == 0) { //terbaru
            // Mode Tambah
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            etItem.isEnabled = true
        } else {
            // Mode Edit
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            etItem.isEnabled = false

            // Ambil data item yang akan diedit
            CoroutineScope(Dispatchers.IO).async {
                val item = DB.daftarBelanjaDAO().getItem(iID)
                etItem.setText(item.item)
                etJumlah.setText(item.jumlah)
            }
        }

        //isikan CoroutineScope untuk menambahkan data ke databaseROOM
        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                try {
                    // Tambah item baru ke database
                    DB.daftarBelanjaDAO().insert(
                        daftarBelanja(
                            tanggal = tanggal,
                            item = etItem.text.toString(),
                            jumlah = etJumlah.text.toString()
                        )
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@TambahDaftar,
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@TambahDaftar,
                            " ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        //Proses Update Item
        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                try {
                    // Update item di database
                    DB.daftarBelanjaDAO().update(
                        isi_tanggal = tanggal,
                        isi_item = etItem.text.toString(),
                        isi_jumlah = etJumlah.text.toString(),
                        pilihid = iID
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@TambahDaftar,
                            "Data berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@TambahDaftar,
                            "Gagal mengupdate data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}