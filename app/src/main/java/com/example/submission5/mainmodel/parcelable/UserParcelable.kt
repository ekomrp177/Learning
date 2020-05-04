package com.example.submission5.mainmodel.parcelable

import android.net.Uri
import android.os.Parcelable
import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "USER_FAVORITE_TABLE")
data class UserParcelable (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(index = true, name = BaseColumns._ID) var id : Long? = null,
    @ColumnInfo(name = "username")var username : String? = null,
    @ColumnInfo(name = "name")var name : String? = null,
    @ColumnInfo(name = "image")var image : String? = null,
    @ColumnInfo(name = "company")var company : String? = null,
    @ColumnInfo(name = "location")var location : String? = null,
    @ColumnInfo(name = "blog")var blog : String? = null,
    @ColumnInfo(name = "repositori")var repositori : String? = null
) : Parcelable {
    companion object{
        const val AUTHORITY = "com.example.submission5.provider"
        const val SCHEME = "content"
        val TABLE_NAME = "USER_FAVORITE_TABLE"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }
}
