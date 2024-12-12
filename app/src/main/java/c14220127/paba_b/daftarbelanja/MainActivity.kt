package paba.c14220151.cobaroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import c14220127.paba_b.daftarbelanja.R
import c14220127.paba_b.daftarbelanja.TambahDaftar
import c14220127.paba_b.daftarbelanja.adapterDaftar
import c14220127.paba_b.daftarbelanja.database.daftarBelanja
import c14220127.paba_b.daftarbelanja.database.daftarBelanjaDB
import c14220127.paba_b.daftarbelanja.database.historyBarang
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    // Initialize database
    private lateinit var DB: daftarBelanjaDB
    // Prepare RecyclerView and Adapter
    private lateinit var adapterDaftar: adapterDaftar
    private var arDaftar = mutableListOf<daftarBelanja>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DB = daftarBelanjaDB.getDatabase(this)
        val _btnFab = findViewById<FloatingActionButton>(R.id.fabAdd)

        // Set adapter
        adapterDaftar = adapterDaftar(arDaftar)
        // Identify RecyclerView view
        val _rvDaftar = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDaftar)
        // Add layout manager and adapter
        _rvDaftar.layoutManager = LinearLayoutManager(this)
        _rvDaftar.adapter = adapterDaftar

        // Delete item action
        adapterDaftar.setOnItemClickCallback(object : adapterDaftar.OnItemClickCallback {
            override fun delData(dtBelanja: daftarBelanja) {
                CoroutineScope(Dispatchers.IO).async {
                    // Delete item from database
                    DB.daftarBelanjaDAO().delete(dtBelanja)
                    // Fetch latest data
                    val daftar = DB.daftarBelanjaDAO().selectAll()
                    withContext(Dispatchers.Main) {
                        adapterDaftar.isiData(daftar)
                    }
                }
            }

            override fun selesaiData(dtBelanja: daftarBelanja) {
                CoroutineScope(Dispatchers.IO).async {
                    // Update status to completed
                    DB.historyBarangDAO().insert(
                        historyBarang(
                            tanggal = dtBelanja.tanggal,
                            item = dtBelanja.item,
                            jumlah = dtBelanja.jumlah
                        )
                    )
                    DB.daftarBelanjaDAO().delete(dtBelanja)
                    // Fetch latest data
                    val daftar = DB.daftarBelanjaDAO().selectAll()
                    withContext(Dispatchers.Main) {
                        adapterDaftar.isiData(daftar)
                    }
                }
            }
        })

        _btnFab.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }
    }

    // Load data when the app starts
    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            // Fetch all items from database
            val daftarBelanja = DB.daftarBelanjaDAO().selectAll()
            Log.d("data ROOM", daftarBelanja.toString())

            // Populate adapter with data
            adapterDaftar.isiData(daftarBelanja)
        }
    }
}