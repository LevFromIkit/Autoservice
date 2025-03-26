package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.adminsClass


class adminAdapter(private val adminList: ArrayList<adminsClass>): RecyclerView.Adapter<adminAdapter.adminViewHolder>(){

    var onItemClick : ((adminsClass) -> Unit)? = null

    class adminViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.textName)
        val lastname : TextView = itemView.findViewById(R.id.textLastname)
        val faired : TextView = itemView.findViewById(R.id.textView10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_adapter, parent, false)
        return adminViewHolder(view)
    }

    override fun onBindViewHolder(holder: adminViewHolder, position: Int) {
        val admin = adminList[position]
        holder.name.text = admin.name
        holder.lastname.text = admin.lastname
        holder.faired.text = ""

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(admin)
        }
    }

    override fun getItemCount(): Int {
        return adminList.size
    }
}