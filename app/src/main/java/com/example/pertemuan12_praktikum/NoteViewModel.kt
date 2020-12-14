package com.example.pertemuan12_praktikum

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import catatan.Note
import catatan.NoteRepository

//ViewModel digunakan untuk mengelola data yang dibutuhkan untuk ditampilkan kedalam Layout maupun Fragment
class NoteViewModel(application: Application) : AndroidViewModel(application)
{
    //Repository
    private var repository: NoteRepository = NoteRepository(application)

    //LiveData digunakan untuk kondisi jika terdapat data yang dapat berubah-ubah pada layout (dinamis)
    //Sehingga jika terjadi perubahan data pada database, maka LiveData akan mengubah tampilan pada layout
       // sesuai dengan data yang telah diubah
    private var allNotes: LiveData<List<Note>> = repository.getAllNotes()

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}
