package com.example.submission5.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.submission5.R
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.example.submission5.room.UserDatabase
import com.example.submission5.room.UserDatabase.Companion.getInstance
import java.lang.Exception

internal class StackRemoteViewsFactory(private val context : Context) : RemoteViewsService.RemoteViewsFactory {
    private var widgetItems: ArrayList<UserParcelable> = ArrayList()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        var userDatabase: UserDatabase = getInstance(context)!!
        val userCheck = userDatabase.userDao().getAll()
        if (userCheck.size > 0) {
            val temp : ArrayList<UserParcelable> = ArrayList()
            for (i in 0 until userCheck.size) {
                temp.add(userCheck[i])
            }
            widgetItems.clear()
            widgetItems.addAll(temp)
        }
        else {
            widgetItems.addAll(userCheck)
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews{

        val data : UserParcelable = widgetItems.get(position)

        var bmp : Bitmap? = null
        try {
            bmp = Glide.with(context).asBitmap().load(data.image).submit().get()
        }catch (e : Exception){
            Log.d("Load Error", e.message.toString())
        }

        var rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, bmp)

        val extras = Bundle()
        extras.putString(ImageBannerWidget.EXTRA_ITEM, data.name)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }
    override fun getCount(): Int = widgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }
}