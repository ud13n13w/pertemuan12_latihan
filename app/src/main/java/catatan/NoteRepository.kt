package catatan

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

//Repository merupakan langkah untuk memisahkan antara operasi data dengan arsitektur
//Repository memisahkan hal tersebut dengan menggunakan API
//Repository ditujukan untuk mengelola query dan mengelola banyak data backend
//Repository adalah algoritma untuk memutuskan mengambil data dari jaringan atau local database(DAO)

class NoteRepository(application: Application) {

    private var noteDao: NoteDao

    private var allNotes: LiveData<List<Note>>

    init {
        val database: NoteDatabase = NoteDatabase.getInstance(
            application.applicationContext
        )!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    //Penerapan Asyntask pada method wrapper sesuai dengan method yg ada pada DAO
    fun insert(note: Note) {
        val insertNoteAsyncTask = InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        val updateNoteAsyncTask = UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        val deleteNoteAsyncTask = DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        val deleteAllNotesAsyncTask = DeleteAllNotesAsyncTask(
            noteDao
        ).execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    //Asyntask ditujuan agar aplikasi dapat dijalankan pada masing-masing thread
    //Asyntask dibuat dengan menerapkan tugas apa yang ingin dijalankan pada thread yg berbeda
       //pada studi kasus ini digunakan untuk pengoperasian data pada database melalui DAO
    companion object {
        private class InsertNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao
            override fun doInBackground(vararg p0: Note?) {
                noteDao.insert(p0[0]!!)
            }
        }
        private class UpdateNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao
            override fun doInBackground(vararg p0: Note?) {
                noteDao.update(p0[0]!!)
            }
        }
        private class DeleteNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao
            override fun doInBackground(vararg p0: Note?) {
                noteDao.delete(p0[0]!!)
            }
        }
        private class DeleteAllNotesAsyncTask(noteDao: NoteDao) : AsyncTask<Unit, Unit, Unit>() {
            val noteDao = noteDao
            override fun doInBackground(vararg p0: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }
}