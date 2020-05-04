package com.example.myfavoriteuser.room

import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log

object CursorHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserParcelable> {
        val list = ArrayList<UserParcelable>()

        if (cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val company = cursor.getString(cursor.getColumnIndex("company"))
                val location = cursor.getString(cursor.getColumnIndex("location"))
                val blog = cursor.getString(cursor.getColumnIndex("blog"))
                val repository = cursor.getString(cursor.getColumnIndex("repositori"))
                list.add(UserParcelable(id, username, name, image, company, location, blog, repository))
            }while (cursor.moveToNext())
        }
        else {
            Log.d("GAGAL : ", "X")
        }
        return list
    }
}