package c14220127.paba_b.daftarbelanja

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class TambahDaftar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etItem = findViewById<EditText>(R.id.etItem)
        val etJumlah = findViewById<EditText>(R.id.etItem)
        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        var DB = daftarBelanjaDB.getDatabase(this)
        var tanggal = getCurrentDate()
        btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftarBelanjaDAO().insert(
                    daftarBelanja(
                        tanggal = tanggal,
                        item = etItem.text.toString(),
                        jumlah = etJumlah.text.toString()
                    )
                )
            }
        }

    }
}