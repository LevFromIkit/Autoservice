package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class zapisAdapter(private val zapisList: ArrayList<String>): RecyclerView.Adapter<zapisAdapter.zapisViewHolder>(){

    var onItemClick : ((String) -> Unit)? = null

    class zapisViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date : TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): zapisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_zapis_adapter, parent, false)
        return zapisViewHolder(view)
    }

    override fun onBindViewHolder(holder: zapisViewHolder, position: Int) {
        val zapis = zapisList[position]
        holder.date.text = zapis.split(".")[0] + "." + zapis.split(".")[1]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(zapis)
        }
    }

    override fun getItemCount(): Int {
        return zapisList.size
    }
}