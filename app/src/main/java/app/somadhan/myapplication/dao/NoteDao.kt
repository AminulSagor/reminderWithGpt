package app.somadhan.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.somadhan.myapplication.models.NoteSave

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteSave)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NoteSave>
}
