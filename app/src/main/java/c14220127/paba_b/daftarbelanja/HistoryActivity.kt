package c14220127.paba_b.daftarbelanja

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import c14220127.paba_b.daftarbelanja.database.daftarBelanjaDB
import c14220127.paba_b.daftarbelanja.database.historyBarang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var DB: daftarBelanjaDB
    private lateinit var adapterHistory: adapterHistory
    private var arHistory = mutableListOf<historyBarang>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DB = daftarBelanjaDB.getDatabase(this)

        adapterHistory = adapterHistory(arHistory)
        val _rvHistory = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvHistory)
        _rvHistory.layoutManager = LinearLayoutManager(this)
        _rvHistory.adapter = adapterHistory
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val historyList = DB.historyBarangDAO().selectAll()
            withContext(Dispatchers.Main) {
                adapterHistory.isiData(historyList)
            }
        }
    }
}