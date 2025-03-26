package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.usersClass

class userAdapter(private val userList: ArrayList<usersClass>): RecyclerView.Adapter<userAdapter.userViewHolder>(){

    var onItemClick : ((usersClass) -> Unit)? = null

    class userViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.textName)
        val lastname : TextView = itemView.findViewById(R.id.textLastname)
        val faired : TextView = itemView.findViewById(R.id.textView10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_adapter, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val user = userList[position]
        holder.name.text = user.name
        holder.lastname.text = user.lastname
        if (user.faild)
            holder.faired.text = "заблокирован"
        else
            holder.faired.text = "активен"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}