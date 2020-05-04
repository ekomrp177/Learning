package com.example.submission5.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submission5.mainmodel.parcelable.UserParcelable


@Database(entities = arrayOf(UserParcelable::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE : UserDatabase? = null

        fun getInstance(context: Context): UserDatabase? {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "userdata.db").build()
                }
            }
            return INSTANCE
        }
    }
}