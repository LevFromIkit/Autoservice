package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.engineersClass
import kotlinx.coroutines.NonDisposableHandle.parent


class engineerAdapter(private val engineerList: ArrayList<engineersClass>): RecyclerView.Adapter<engineerAdapter.engineerViewHolder>(){

    var onItemClick : ((engineersClass) -> Unit)? = null

    class engineerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.textName)
        val lastname : TextView = itemView.findViewById(R.id.textLastname)
        val faired : TextView = itemView.findViewById(R.id.textView10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): engineerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_adapter, parent, false)
        return engineerViewHolder(view)
    }

    override fun onBindViewHolder(holder: engineerViewHolder, position: Int) {
        val engineer = engineerList[position]
        holder.name.text = engineer.name
        holder.lastname.text = engineer.lastname
            if(engineer.faild)
                holder.faired.text = "заблокирован"
            else
                holder.faired.text = "активен"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(engineer)
        }
    }

    override fun getItemCount(): Int {
        return engineerList.size
    }
}