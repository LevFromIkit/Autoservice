package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.chatClass

class chatAdapter(private val chatList: ArrayList<chatClass>): RecyclerView.Adapter<chatAdapter.chatViewHolder>(){

    var onItemClick : ((chatClass) -> Unit)? = null

    class chatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.textName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_adapter, parent, false)
        return chatViewHolder(view)
    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.name.text = chat.id

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(chat)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}