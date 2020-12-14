package com.example.pertemuan12_praktikum

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID= "com.piusanggoro.notesapp.EXTRA_ID"
        const val EXTRA_JUDUL = "com.piusanggoro.notesapp.EXTRA_JUDUL"
        const val EXTRA_DESKRIPSI = "com.piusanggoro.notesapp.EXTRA_DESKRIPSI"
        const val EXTRA_PRIORITAS = "com.piusanggoro.notesapp.EXTRA_PRIORITAS"
        const val EXTRA_NOHP = "com.piusanggoro.notesapp.EXTRA_NOHP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        //Menyetting min value dan max value pada Objek Number Picker
        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 5

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)

        //Jika intent memiliki ID (Ketika Item dipilih pada MainActivity)
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Catatan"
            edit_text_title.setText(intent.getStringExtra(EXTRA_JUDUL))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESKRIPSI))
            number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITAS, 1)
        }else {
            title = "Tambah Catatan"
        }
    }

    //Override menu pada activity agar sesuai dengan custom menu kita
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    //Trigger event, ketika layout save di tekan, maka akan menjalankan function saveNote()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {

        //Jika data yang dimasukkan masih kosong
        if (edit_text_title.text.toString().trim().isBlank() || edit_text_description.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Catatan kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        //Jika data tidak kosong, maka kirim semua data ke intent dan kirim sebagai RESULT OK
        val data = Intent().apply {
            putExtra(EXTRA_JUDUL, edit_text_title.text.toString())
            putExtra(EXTRA_DESKRIPSI, edit_text_description.text.toString())
            putExtra(EXTRA_PRIORITAS, number_picker_priority.value)
            putExtra(EXTRA_NOHP, edit_text_nohp.text)
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

}