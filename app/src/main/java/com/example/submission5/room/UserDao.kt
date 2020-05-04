package com.example.submission5.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.submission5.mainmodel.parcelable.UserParcelable

@Dao
interface UserDao {
    @Query("SELECT * FROM USER_FAVORITE_TABLE ORDER BY _id ASC")
    fun getAll(): List<UserParcelable>

    @Insert(onConflict = REPLACE)
    fun insert(userParcelable: UserParcelable)

    @Query("DELETE FROM USER_FAVORITE_TABLE WHERE username = :username")
    fun delete(username : String?)

    @Query("DELETE FROM USER_FAVORITE_TABLE")
    fun delete_all()

    @Query( "SELECT * FROM USER_FAVORITE_TABLE WHERE username = :username")
    fun getById(username: String?) : List<UserParcelable>

    @Query("SELECT * FROM USER_FAVORITE_TABLE ORDER BY _id ASC")
    fun selectAllCursor(): Cursor
}