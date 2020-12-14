package com.example.pertemuan12_praktikum

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import catatan.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ketika Action button di klick
        buttonAddNote.setOnClickListener {
            startActivityForResult(
                Intent(this, AddEditNoteActivity::class.java), ADD_NOTE_REQUEST
            )
        }

        //Settting RecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        val adapter = NoteAdapter()
        recycler_view.adapter = adapter

        //Setting ViewModel
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this, Observer<List<Note>> {
            adapter.submitList(it)
        })

        //Item touch helper untuk membantu melakukan trigger action ketika event terjadi pada item
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {

            //Ketika menambahkan data baru, maka memperbaharui instance recyclerview, viewholder, dan target
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //Ketika item di swiped, maka akan menjalankan function delete() dan data akan terhapus
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Catatan dihapus!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recycler_view)

        //Ketika item dipilih, maka akan mengirimkan data dari item yg dipilih dan intent berupa Edit Request
        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(baseContext, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddEditNoteActivity.EXTRA_JUDUL, note.title)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESKRIPSI, note.description)
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITAS, note.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //Ketika option menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {

            //Ketika menu option delete all menu dipilih, maka akan menjalankan function deleteAllNotes()
              // untuk menghapus semua data note yang tersimpan
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "Semua sudah dihapus!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    //Action ketika activity menerima result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Jika result intent berupa ADD_NOTE_REQUEST dan RESULT_OK, maka jalankan insert()
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newNote = Note(
                data!!.getStringExtra(AddEditNoteActivity.EXTRA_JUDUL).toString(),
                data.getStringExtra(AddEditNoteActivity.EXTRA_DESKRIPSI).toString(),
                data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITAS, 1),
                data.getStringExtra(AddEditNoteActivity.EXTRA_NOHP).toString()
            )
            noteViewModel.insert(newNote)
            Toast.makeText(this, "Catatan disimpan!", Toast.LENGTH_SHORT).show()

        //Jika result intent berupa EDIT_NOTE_REQUEST dan RESULT_OK, maka jalankan update()
        }else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Pembaharuan gagal!", Toast.LENGTH_SHORT).show()
            }
            val updateNote = Note(
                data!!.getStringExtra(AddEditNoteActivity.EXTRA_JUDUL).toString(),
                data.getStringExtra(AddEditNoteActivity.EXTRA_DESKRIPSI).toString(),
                data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITAS, 1),
                data.getStringExtra(AddEditNoteActivity.EXTRA_NOHP).toString()
            )
            updateNote.id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            noteViewModel.update(updateNote)
        } else {
            Toast.makeText(this, "Catatan tidak disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}