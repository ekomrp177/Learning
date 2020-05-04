package com.example.submission5.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission5.mainmodel.parcelable.UserParcelable.Companion.AUTHORITY
import com.example.submission5.mainmodel.parcelable.UserParcelable.Companion.TABLE_NAME
import com.example.submission5.room.UserDatabase
import com.example.submission5.room.UserDatabase.Companion.getInstance

class ContentProvider : ContentProvider() {


    companion object {
        private const val USER_ID = 1
        private const val USER_ITEM = 2
        private lateinit var userDatabase: UserDatabase

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_NAME, USER_ID)
            MATCHER.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ITEM)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO()
    }

    override fun onCreate(): Boolean {
        userDatabase = getInstance(context as Context)!!
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        userDatabase = getInstance(context as Context)!!
        when (MATCHER.match(uri)){
            USER_ID -> cursor = userDatabase.userDao().selectAllCursor()
            else -> cursor = null
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO()
    }
}
