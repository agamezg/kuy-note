package org.kuykuy.kuynote.repository.dao

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import org.kuykuy.kuynote.domain.Note

class DbManager(private val dbHelper: KuyNoteDbHelper) {

    fun insert(values: ContentValues) : Long?{
        val db = dbHelper.writableDatabase
        return db?.insert(Notes.NoteEntry.TABLE_NAME, null, values)
    }

    fun findAll(projection:Array<String>, selection:String, selectionArgs:Array<String>) : ArrayList<Note>{
        val db = dbHelper.readableDatabase
        val cursor = db?.query(Notes.NoteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
        return toNoteArray(cursor)
    }

    fun findAll() : ArrayList<Note>{
        val db = dbHelper.readableDatabase
        val cursor = db?.query(Notes.NoteEntry.TABLE_NAME, null, null, null, null, null, null)
        return toNoteArray(cursor)
    }

    fun delete(note:Note?) : Int?{
        val db = dbHelper.writableDatabase
        val id = note?.id
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$id")
        return db?.delete(Notes.NoteEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun update(id:Long?, values:ContentValues) : Int?{
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$id")
        return db?.update(Notes.NoteEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    fun close(){
        dbHelper.close()
    }

    private fun toNoteArray(cursor: Cursor?): ArrayList<Note> {
        val notes = ArrayList<Note>()
        with(cursor) {
            while (this?.moveToNext()!!) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(Notes.NoteEntry.COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(Notes.NoteEntry.COLUMN_DESCRIPTION))
                notes.add(Note(id, title, description))
            }
        }
        return notes
    }
}