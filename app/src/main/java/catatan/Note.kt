package catatan

import androidx.room.Entity
import androidx.room.PrimaryKey

//Merepresentasikan tabel SQLite bernama note_table
@Entity(tableName = "note_table")

//Menunjukkan bahwa tabel note_table berisi kolom id dengan tipe data int dengan auto increment
data class Note(
    var title: String,
    var description: String,
    var priority: Int,
    var nohp: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}