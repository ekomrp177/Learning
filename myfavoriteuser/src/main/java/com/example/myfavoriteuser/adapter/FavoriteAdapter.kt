package com.example.myfavoriteuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavoriteuser.R
import com.example.myfavoriteuser.room.UserParcelable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var onItemClickCallBack : OnItemClickCallBack? = null
    private val mData = ArrayList<UserParcelable>()


    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack{
        fun onItemClicked(userParcelable: UserParcelable)
    }

    fun setData (items: ArrayList<UserParcelable>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class FavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(userParcelable: UserParcelable){
            with (itemView){
                user_name.text = userParcelable.username
                Picasso.get().load(userParcelable.image).into(user_image)
            }
            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(userParcelable) }
        }
    }
}